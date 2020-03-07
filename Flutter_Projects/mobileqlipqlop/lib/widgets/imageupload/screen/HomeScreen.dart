import 'dart:async';
import 'dart:io';
import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:QlipQlop/widgets/imageupload/constant/Constant.dart';
import 'package:QlipQlop/widgets/imageupload/screen/CameraHomeScreen.dart';
import 'package:QlipQlop/widgets/imageupload/utility/DiagonalClipper.dart';
import 'package:http/http.dart' as http;
import 'package:async/async.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path/path.dart';
import 'dart:math';
import 'package:path_provider/path_provider.dart';
import 'dart:convert';
import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:flutter/rendering.dart';
import 'package:image/image.dart' as imgLib;
import 'dart:io';
import 'dart:typed_data';
import 'package:screenshot/screenshot.dart';
import 'package:intl/intl.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:progress_dialog/progress_dialog.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/challenge_page.dart';
import 'package:QlipQlop/Auth/session.dart' as session;
import 'package:http_parser/http_parser.dart';
import 'package:path/path.dart';
import 'package:QlipQlop/resources/Constants.dart';
import 'package:share/share.dart';


class ImageHomeScreen extends StatefulWidget {
  String url;
  ImageHomeScreen(this.url);

  @override
  State<StatefulWidget> createState() {
    return _ImageHomeScreenState(this.url);
  }
}

class _ImageHomeScreenState extends State<ImageHomeScreen> {
  String _imagePath;
  Widget image;
  double _headerHeight = 320.0;
  final String _assetImagePath = 'assets/images/ic_no_image.png';
  BuildContext context;

  TextOverlay overText = null;
  bool textFlag = false;
  ProgressDialog pr;
  String url;

  _ImageHomeScreenState(this.url);

  ScreenshotController screenshotController = ScreenshotController();
  ScreenshotController screenshotController1 = ScreenshotController();

  @override
  void initState() {
    super.initState();
  }

  FocusNode myFocusNode;
  @override
  Widget build(BuildContext context) {
    this.context = context;


    return new WillPopScope(
        onWillPop: (){
          Navigator.pop(context);
          /*Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => new ChallengePage(url)),
          );*/
          return;
        },
        child: new Scaffold(
            body: Stack(
              children: <Widget>[
                _imagePath != null
                    ? Stack(
                  children: <Widget>[
                    Screenshot(
                      controller: screenshotController,
                      child: Stack(
                        children: <Widget>[
                          Screenshot(
                            controller: screenshotController1,
                          ),
                          _getImageFromFile(_imagePath),
                          overText != null
                              ? overText
                              : new Container(
                            height: 0,
                          ),
                        ],
                      ),
                    ),
                    _getButtonFab()
                  ],
                )
                    : Stack(
                  children: <Widget>[
                    _getImageFromAsset(),
                    _getCameraFab(),
                  ],
                ),
              ],
            ))
    );

    /*return Scaffold(
        body: Stack(
      children: <Widget>[
        _imagePath != null
            ? Stack(
                children: <Widget>[
                  Screenshot(
                    controller: screenshotController,
                    child: Stack(
                      children: <Widget>[
                        Screenshot(
                          controller: screenshotController1,
                        ),
                        _getImageFromFile(_imagePath),
                        overText != null
                            ? overText
                            : new Container(
                                height: 0,
                              ),
                      ],
                    ),
                  ),
                  _getButtonFab()
                ],
              )
            : Stack(
                children: <Widget>[
                  _getImageFromAsset(),
                  _getCameraFab(),
                ],
              ),
      ],
    ));*/
  }

  Widget _getImageFromAsset() {
    return Padding(
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
                width: 60.0,
                height: 60.0,
              ),
              Container(
                margin: EdgeInsets.only(top: 8.0),
                child: Text(
                  'No Image Available',
                  style: TextStyle(
                    color: Colors.grey[350],
                    fontSize: 16.0,
                  ),
                ),
              ),
            ],
          )),
    );
  }

  Widget _getImageFromFile(String imagePath) {
    return Padding(
      padding: EdgeInsets.only(bottom: 0.0),
      child: Image.file(
        File(
          imagePath,
        ),
        fit: BoxFit.cover,
        width: double.infinity,
        height: double.infinity,
      ),
    );
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
                      heroTag: "_Camerabutton",
                      onPressed: _openCamera,
                      child: Icon(
                        Icons.photo_camera,
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
                  Expanded(flex: 1, child: Text("")),
                  Expanded(
                    flex: 1,
                    child: Text(""),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Editbutton",
                      onPressed: () {
                        textFlag = !textFlag;
                        setState(() {
                          if (textFlag) {
                            overText = new TextOverlay();
                          } else {
                            overText = null;
                          }
                        });
                      },
                      child: Icon(
                        Icons.text_fields,
                        color: Colors.white,
                      ),
                      backgroundColor: Colors.yellow,
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FloatingActionButton(
                      heroTag: "Uploadbutton",
                      focusNode: myFocusNode,
                      onPressed: (){
                        session.showProgress(context, "Uploading Image...");
                        _uploadImage();
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
                      onPressed: _openCamera,
                      child: Icon(
                        Icons.photo_camera,
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
          content: Text("Would you like to share image?"),
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
                  String shareurl = data["share_url"];
                  Navigator.pop(context);
                  Share.share(share_prefix + shareurl, subject: "Share");
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
  _uploadImage() async {
    //if(textFlag){
    await FocusScope.of(context).requestFocus(myFocusNode);
    await FocusScope.of(context).unfocus();
    await new Future.delayed(const Duration(milliseconds: 500));

    File _imageFile;
    final directory = (await getApplicationDocumentsDirectory())
        .path; //from path_provide package
    //String fileName = DateTime.now().toIso8601String();
    DateTime now = DateTime.now();
    String fileName = DateFormat("yyyy_MM_dd_kk_mm").format(now);
    String path = '$directory/$fileName.png';
    screenshotController.capture(path: path).then((File img) async {
      setState(() {
        _imageFile = img;
      });
      var stream =
          http.ByteStream(DelegatingStream.typed(_imageFile.openRead()));
      var length = await _imageFile.length();
      var response;
      var respStr;
      try{
        var uri = Uri.parse('http://test.qlipqlop.com/a/netizen/upload_challenge');
        var request = http.MultipartRequest('POST', uri);
        request.headers.addAll(session.headers);

        request.fields["prefix"] = "image";
        //request.files.add(multipartfile);
        request.files.add(await http.MultipartFile.fromPath('file', path,
            contentType: new MediaType('image', 'png')));
        response = await request.send();
        respStr = await response.stream.bytesToString();
      }
      catch(e){
        /*pr.hide().then((isHidden) {
          print(isHidden);
        });*/
        session.dismissProgress();
        showToast("Image Upload Failed! Maybe You are using Proxy.", Colors.red);
      }

      if (response.statusCode == 200) {
        /*pr.hide().then((isHidden) {
          print(isHidden);
        });*/
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
          showAlertDialog(context, data["id"].toString());
          return true;
        }
        else{
          showToast("Image Upload Failed!", Colors.red);
          return true;
        }

        setState(() {
          /*_imageFile.delete();
          _imageFile = null;*/
        });
      } else {
        /*pr.hide().then((isHidden) {
          print(isHidden);
        });*/
        session.dismissProgress();
        return Fluttertoast.showToast(
            msg: "Image Upload Failed!",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.CENTER,
            timeInSecForIos: 3,
            backgroundColor: Colors.red,
            textColor: Colors.white);
        // Upload Failed
        /*_imageFile.delete();
        _imageFile = null;*/
      }
    }).catchError((onError) {
      print(onError);
    });
    //}
    /*else{
      File image = new File(_imagePath);
      var stream = http.ByteStream(DelegatingStream.typed(image.openRead()));
      var length = await image.length();
      var uri = Uri.parse('http://173.212.232.157:3333/imageupload.php');
      var resquest = http.MultipartRequest('POST', uri);
      var multipartfile = new http.MultipartFile('image', stream, length,
          filename: basename(image.path));
      resquest.files.add(multipartfile);
      var response = await resquest.send();
      if (response.statusCode == 200) {
        // UPload Success
        setState(() {
          image = null;
        });
      } else {
        // Upload Failed

      }
    }*/
  }

  List<CameraDescription> cameras;
  Future _openCamera() async {
    WidgetsFlutterBinding.ensureInitialized();
    try {
      cameras = await availableCameras();
    } on CameraException catch (e) {}
    final imagePath = await Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => CameraHomeScreen(cameras)),
    );

    setState(() {
      _imagePath = imagePath;
    });

    if (imagePath != null) {
      image = Image.file(
        File(imagePath),
        height: _headerHeight,
        width: double.infinity,
        fit: BoxFit.cover,
      );
    }
  }
}

class TextOverlay extends StatefulWidget {
  TextField content;
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new _TextOverlay();
  }
}

class _TextOverlay extends State<TextOverlay> {
  static double _fontSize = 40.0;
  BuildContext context;
  Flexible content;
  int count = 0;
  bool editable = true;

  @override
  initState() {
    super.initState();
    content = new Flexible(
      child: new TextField(
          onChanged: (text) {
            updateTextField(text);
          },
          onEditingComplete: () {},
          maxLines: null,
          maxLength: 120,
          keyboardType: TextInputType.multiline,
          expands: true,
          textAlign: TextAlign.center,
          textAlignVertical: TextAlignVertical.center,

          style: new TextStyle(color: Colors.white, fontSize: _fontSize),
          decoration: InputDecoration(
            //border: InputBorder.none,
            //counterText: null,
            //helperText: null,
            /*counterStyle: TextStyle(color: Colors.red),
          helperStyle: TextStyle(color: Colors.blue),*/
            enabledBorder: OutlineInputBorder(
              borderSide: BorderSide(color: Colors.transparent, width: 1.0),
            ),
            hintText: 'Type any Text',
            hintStyle: TextStyle(fontSize: 40.0, color: Colors.redAccent),
          )),
    );
  }

  updateTextField(String text) {
    double device_height = MediaQuery.of(context).size.height;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    this.context = context;
    // TODO: implement build
    return new GestureDetector(
      onTap: () {},
      child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Padding(
              padding: EdgeInsets.all(10.0),
            ),
            Container(
              child: content,
            ),
          ]),
    );
  }
}
