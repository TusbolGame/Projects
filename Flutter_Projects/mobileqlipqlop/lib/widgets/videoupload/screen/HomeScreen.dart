import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:QlipQlop/resources/Constants.dart';
import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:QlipQlop/widgets/videoupload/constant/Constant.dart';
import 'package:QlipQlop/widgets/videoupload/screen/VideoPlayerScreen.dart';
import 'package:video_player/video_player.dart';
import 'package:QlipQlop/widgets/videoupload/screen/CameraHomeScreen.dart';
import 'package:http/http.dart' as http;
import 'package:async/async.dart';
import 'package:path/path.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:progress_dialog/progress_dialog.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/challenge_page.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:QlipQlop/Auth/session.dart' as session;
import 'package:http_parser/http_parser.dart';
import 'package:QlipQlop/widgets/buzzfeed/JsonParser.dart';
import 'package:share/share.dart';

class VideoHomeScreen extends StatefulWidget {
  String url;
  VideoHomeScreen(this.url);
  @override
  State<StatefulWidget> createState() {
    return _VideoHomeScreenState(this.url);
  }
}

class _VideoHomeScreenState extends State<VideoHomeScreen> {
  String _videoPath = null;
  double _headerHeight = 500.0;
  BuildContext context;
  VideoPlayerScreen videoScreen;
  final String _assetPlayImagePath = 'assets/images/ic_play.png';
  final String _assetImagePath = 'assets/images/ic_no_video.png';
  var _thumbPath;
  var _videoName;
  String url;
  _VideoHomeScreenState(this.url);

  ProgressDialog pr;


  @override
  Widget build(BuildContext context) {
    this.context = context;
    return new WillPopScope(
      onWillPop: (){
        session.audioPlayer.stop();
        Navigator.pop(context);
        return;
      },
      child: new Scaffold(
          body: Stack(
            children: <Widget>[
              _videoPath != null ?
              Stack(
                children: <Widget>[
                  _getVideoContainer() ,
                  _getButtonFab()
                ],
              )
                  :
              Stack(
                children: <Widget>[
                  _getImageFromAsset(),
                  _getCameraFab(),
                ],
              ),
            ],
          ))
    );
  }

  Widget _getImageFromAsset() {
    return ClipPath(
      child: Padding(
        padding: EdgeInsets.only(bottom: 0.0),
        child: Container(
            width: MediaQuery.of(context).size.width,
            height: MediaQuery.of(context).size.height,
            color: Colors.grey,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                    Image.asset(
                      _assetImagePath,
                      fit: BoxFit.fill,
                      width: 48.0,
                      height: 32.0,
                    ),
                    Container(
                      margin: EdgeInsets.only(top: 8.0),
                      child: Text(
                        'Please Record a Video.',
                        style: TextStyle(
                          color: Colors.grey[350],
                          //fontWeight: FontWeight.bold,
                          fontSize: 16.0,
                        ),
                      ),
                    ),
                  ],
        )
      ),
    ),
    );
  }

  Widget _getVideoContainer() {
    videoScreen = new VideoPlayerScreen(key: UniqueKey(), path: _videoPath);
    return Container(
      padding: EdgeInsets.only(bottom: 0.0),
      child: new Container(
          width: MediaQuery.of(context).size.width,
          height: MediaQuery.of(context).size.height,
          color: Colors.grey,
          child: Stack(
            children: <Widget>[
              _thumbPath != null
                  ? new Opacity(
                      opacity: 0.5,
                      child: new Image.file(
                        File(
                          _thumbPath,
                        ),
                        fit: BoxFit.cover,
                        width: MediaQuery.of(context).size.width,
                        height: MediaQuery.of(context).size.height,
                      ),
                    )
                  : new Container(),
              videoScreen != null? videoScreen : new Container(),
              _buildPathWidget(),
            ],
          )),
    );
  }

  Widget _buildPathWidget() {
    return _videoPath != null
        ? new Align(
            alignment: Alignment.bottomCenter,
            child: Container(
              width: double.infinity,
              height: 100.0,
              padding: EdgeInsets.only(
                  left: 10.0, right: 10.0, top: 5.0, bottom: 5.0),
              color: Color.fromRGBO(00, 00, 00, 0.7),
            ),
          )
        : new Container();
  }
  Widget _getCameraFab() {
    return Column(
      mainAxisSize: MainAxisSize.max,
      mainAxisAlignment: MainAxisAlignment.end,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Container(
          color: Colors.transparent,
          child: Padding(
              padding: EdgeInsets.only(bottom: 20),
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisSize: MainAxisSize.max,
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Expanded(
                    flex: 4,
                    child: Text(''),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "_Recordbutton",
                      onPressed: _recordVideo,
                      child: Icon(
                        Icons.videocam,
                        color: Colors.white,
                        size: 20,
                      ),
                      backgroundColor: Colors.red,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Backbutton1",
                      onPressed: (){
                        Navigator.pop(context);
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => new ChallengePage(url)),
                        );
                      },
                      child: Icon(
                        AppIcons.backbutton,
                        color: Colors.white,
                        size: 20,
                      ),
                      backgroundColor: Colors.lightGreen,
                    ),
                  ),
                ],
              )),
        )
      ],
    );
  }
  Widget _getButtonFab() {
    return Column(
      mainAxisSize: MainAxisSize.max,
      mainAxisAlignment: MainAxisAlignment.end,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Container(
          color: Colors.transparent,
          child: Padding(
              padding: EdgeInsets.only(bottom: 20),
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisSize: MainAxisSize.max,
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Playbutton",
                      onPressed: (){
                        videoScreen.screenState.videoPlay();
                      },
                      child: Icon(
                        Icons.play_arrow,
                        color: Colors.white,
                      ),
                      backgroundColor: Colors.red,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Pausebutton",
                      onPressed: (){
                        videoScreen.videoPause();
                      },
                      child: Icon(
                        Icons.pause,
                        color: Colors.white,
                      ),
                      backgroundColor: Colors.green,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Stopbutton",
                      onPressed: (){
                        videoScreen.videoStop();
                      },
                      child: Icon(
                        Icons.stop,
                        color: Colors.white,
                      ),
                      backgroundColor: Colors.yellow,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Uploadbutton",
                      onPressed: (){
                        /*pr = new ProgressDialog(context, isDismissible: true, showLogs: true);
                        pr.show();*/
                        session.showProgress(context, "Uploading Video...");
                        _uploadVideo();
                      },
                      child: Icon(
                        Icons.cloud_upload,
                        color: Colors.white,
                      ),
                      backgroundColor: Colors.blue,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Recordbutton",
                      onPressed: _recordVideo,
                      child: Icon(
                        Icons.videocam,
                        color: Colors.white,
                        size: 20,
                      ),
                      backgroundColor: Colors.red,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Backbutton",
                      onPressed: (){
                        session.audioPlayer.stop();
                        Navigator.pushNamed(context, "/mainfeed");
                        /*Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => new ChallengePage(url)),
                        );*/
                      },
                      child: Icon(
                        AppIcons.backbutton,
                        color: Colors.white,
                        size: 20,
                      ),
                      backgroundColor: Colors.lightGreen,
                    ),
                  ),
                ],
              )),
        )
      ],
    );
  }

  showAlertDialog(BuildContext context, String id) async{
    // show the dialog
    await showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Challenge"),
          content: Text("Would you like to share your video?"),
          actions: [
            FlatButton(
              child: Text("Back to Feed"),
              onPressed: (){
                Navigator.pushReplacementNamed(context, "/mainfeed");
              },
            ),
            FlatButton(
              child: Text("Share"),
              onPressed: () async{
                final body = {
                  "id": id,
                };
                final data = await session.post(domain_url + share_answer, body);
                try{
                  String share_url = data["share_url"];
                  Navigator.pop(context);
                  //showToast(share_url, Colors.green);
                  Share.share(share_prefix + share_url, subject: "Share");
                }
                catch(e){
                  Navigator.pop(context);
                  showToast("Share error", Colors.red);
                }

              },
            ),
          ],
        );
      },
    );
  }
  showToast(String message, Color color){
    return Fluttertoast.showToast(
        msg: message,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.CENTER,
        timeInSecForIos: 3,
        backgroundColor: color,
        textColor: Colors.white);
  }

  _uploadVideo() async {

    File video = new File(_videoPath);
    var stream = http.ByteStream(DelegatingStream.typed(video.openRead()));
    var length = await video.length();
    var response;
    var respStr;
    try{
      var uri = Uri.parse(domain_url + challengeUpload_url);
      var request = http.MultipartRequest('POST', uri);
      request.headers.addAll(session.headers);

      request.fields["prefix"] = "challenge";
      request.files.add(await http.MultipartFile.fromPath('file', video.path,
          contentType: new MediaType('video', extension(video.path))));
      response = await request.send();
      respStr = await response.stream.bytesToString();
    }
    catch(e){
      session.dismissProgress();
      showToast("Video Upload Failed! Maybe You are using Proxy", Colors.red);
    }

    if (response.statusCode == 200) { // UPload Success
      session.dismissProgress();
      var response_json = jsonDecode(respStr);
      String local_url = response_json["local_url"];

      final body = {
        "content_id": session.feed_id,
        "local_url": local_url,
      };
      var data;
      try{
        data = await session.post(domain_url + upsert_answer, body);
      }
      catch(e){
        print(e.toString());
      }
      if(data["ok"] == true){
        //showToast("Video Upload Success", Colors.green);
        showAlertDialog(context, data["id"].toString());
      }
      else{
        showToast("Video Upload Failed!", Colors.red);
      }

    }
    else {
      session.dismissProgress();
      showToast("Video Upload Failed!", Colors.red);
    }
  }




  List<CameraDescription> cameras;
  Future _recordVideo() async {
    videoScreen = null;
    WidgetsFlutterBinding.ensureInitialized();
    try {
      cameras = await availableCameras();
    } on CameraException catch (e) {

    }
    final videoPath = await Navigator.push(
      context,
      MaterialPageRoute(
          builder: (context) => CameraHomeScreen(cameras)),
    );
    setState(() {
      _videoPath = videoPath;
    });
  }
}
