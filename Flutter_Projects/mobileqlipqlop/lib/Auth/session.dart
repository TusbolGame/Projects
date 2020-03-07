library QlipQlop.session;

import 'package:QlipQlop/widgets/profile/ProfileParser.dart';
import 'package:audioplayers/audioplayers.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:QlipQlop/resources/Constants.dart';
import 'package:path/path.dart';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:http_parser/http_parser.dart';
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:QlipQlop/widgets/buzzfeed/BuzzHome.dart';
import 'package:QlipQlop/widgets/website/WebviewFeed.dart';
import 'package:QlipQlop/widgets/audioplay/player_widget.dart';
import 'package:progress_dialog/progress_dialog.dart';
import 'package:fluttertoast/fluttertoast.dart';

int feed_index = 0;
String feed_id;
String feed_type;
String feed_url;
YoutubePlayerController challenge_youtubePlayerController;
VideoPlayerController challenge_videoPlayerController;
var quiz_data;
AudioPlayer audioPlayer;

Map<String, String> headers = {};
dynamic content_feeds;

ProgressDialog pr;

dynamic profileData;

ProfileData userProfile;


Future<Map> get(String url) async {
  http.Response response = await http.get(url, headers: headers);
  updateCookie(response);
  return json.decode(response.body);
}

Future<Map> post(String url, dynamic data) async {
  http.Response response;
  try{
    response = await http.post(url, body: data, headers: headers);
  }
  catch(e){
    print(e);
  }

  updateCookie(response);
  return json.decode(response.body);
}

Future<Map> getContentFeed(String url) async {
  http.Response response;
  try{
    response = await http.post(url, body: null, headers: headers);
  }
  catch(e){
    print(e);
  }
  content_feeds = json.decode(response.body);
}

dynamic uploadFile(String path, String prefix, String type) async{

  try{
    var uri = Uri.parse(domain_url + challengeUpload_url);
    var request = http.MultipartRequest('POST', uri);
    request.headers.addAll(headers);

    request.fields["prefix"] = prefix;
    request.files.add(await http.MultipartFile.fromPath('file', path,
        contentType: new MediaType(type, extension(path))));
    final response = await request.send();
    final respStr = await response.stream.bytesToString();
    return respStr;
  }
  catch(e){
    return null;
  }
}

void updateCookie(http.Response response) {
  String rawCookie = response.headers['set-cookie'];
  if (rawCookie != null) {
    int index = rawCookie.indexOf(';');
    headers['cookie'] =
    (index == -1) ? rawCookie : rawCookie.substring(0, index);
  }
}

void showProgress(BuildContext context, String msg){
  pr = new ProgressDialog(context,type: ProgressDialogType.Normal, isDismissible: true, showLogs: true);
  pr.style(
      message: msg,
      progressTextStyle: TextStyle(
          color: Colors.black, fontSize: 13.0, fontWeight: FontWeight.w400),
      messageTextStyle: TextStyle(
          color: Colors.black, fontSize: 19.0, fontWeight: FontWeight.w600)
  );
  pr.show();
}
void dismissProgress(){
  pr.hide().then((isHidden) {
    print(isHidden);
  });
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