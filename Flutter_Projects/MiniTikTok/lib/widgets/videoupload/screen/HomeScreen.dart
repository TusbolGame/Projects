import 'dart:async';
import 'dart:io';
import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:MiniTikTok/widgets/videoupload/constant/Constant.dart';
import 'package:MiniTikTok/widgets/videoupload/screen/VideoPlayerScreen.dart';
import 'package:video_player/video_player.dart';
import 'package:MiniTikTok/widgets/videoupload/screen/CameraHomeScreen.dart';
import 'package:http/http.dart' as http;
import 'package:async/async.dart';
import 'package:path/path.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:progress_dialog/progress_dialog.dart';
import 'package:MiniTikTok/resources/assets.dart';
import 'package:MiniTikTok/challenge_page.dart';

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
        Navigator.pop(context);
        Navigator.push(
          context,
          MaterialPageRoute(
              builder: (context) => new ChallengePage(url, null, null)),
        );
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
                              builder: (context) => new ChallengePage(url, null, null)),
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
                        pr = new ProgressDialog(context, isDismissible: true, showLogs: true);
                        pr.show();
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
                        Navigator.pop(context);
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => new ChallengePage(url, null, null)),
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
  _changemessage(){
    pr.update(
      message: "Video Uploading...",
    );
  }
  _uploadVideo() async {

    File video = new File(_videoPath);
    var stream = http.ByteStream(DelegatingStream.typed(video.openRead()));
    var length = await video.length();
    var response;
    try{
      var uri = Uri.parse('http://test.qlipqlop.com:3333/videoupload.php');
      var resquest = http.MultipartRequest('POST', uri);
      var multipartfile = new http.MultipartFile('video', stream, length,
          filename: basename(video.path));
      resquest.files.add(multipartfile);
      response = await resquest.send();
    }
    catch(e){
      return Fluttertoast.showToast(
          msg: "Image Upload Failed! Maybe You are using Proxy.",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIos: 3,
          backgroundColor: Colors.red,
          textColor: Colors.white);
    }


    if (response.statusCode == 200) { // UPload Success
      pr.hide().then((isHidden) {
        print(isHidden);
      });
      return Fluttertoast.showToast(
          msg: "Video Upload Success!",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIos: 3,
          backgroundColor: Colors.green,
          textColor: Colors.white);
    } else { // Upload Failed
      pr.hide().then((isHidden) {
        print(isHidden);
      });
      return Fluttertoast.showToast(
          msg: "Video Upload Failed!",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIos: 3,
          backgroundColor: Colors.red,
          textColor: Colors.white);
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
