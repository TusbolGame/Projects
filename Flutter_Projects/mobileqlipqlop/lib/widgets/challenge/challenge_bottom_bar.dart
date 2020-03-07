import 'dart:convert';

import 'package:QlipQlop/resources/Constants.dart';
import 'package:QlipQlop/widgets/Home.dart';
import 'package:QlipQlop/widgets/buzzfeed/BuzzHome.dart';
import 'package:QlipQlop/widgets/home/controls/video_control_action.dart';
import 'package:QlipQlop/widgets/home/home_page.dart';
import 'package:QlipQlop/widgets/imageupload/screen/HomeScreen.dart';
import 'package:QlipQlop/widgets/videoupload/screen/HomeScreen.dart';
import 'package:QlipQlop/widgets/website/home.dart';
import 'package:QlipQlop/widgets/website/web_view_container.dart';
import 'package:flutter/material.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/resources/dimen.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:image_picker/image_picker.dart';
import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:QlipQlop/widgets/buzzfeed/JsonParser.dart';
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:QlipQlop/bottom_nav_bar.dart';
import 'package:QlipQlop/Auth/session.dart' as session;

enum LoginStatus { notSignIn, signIn }

class ChallengeBottomNavigation extends StatefulWidget {

    String url;
    VideoPlayerController vController;
    YoutubePlayerController yController;
    ChallengeBottomNavigation(this.url, this.vController, this.yController);

    @override
    State<StatefulWidget> createState() => _ChallengeBottomNavigation(this.url, this.vController, this.yController);
}

class _ChallengeBottomNavigation extends State<ChallengeBottomNavigation> {
    VideoPlayerController vController;
    YoutubePlayerController yController;
    String url;
    _ChallengeBottomNavigation(this.url, this.vController, this.yController);
    signOut() {

    }

    VideoControl(BuildContext context){
        //Navigator.of(context).push(new FadeEffect());
    }
    ImageControl(){
        return Fluttertoast.showToast(
            msg: "ImageWidget",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIos: 1,
            backgroundColor: Colors.red,
            textColor: Colors.white);
    }
    BuzzfeedControl(){
        return Fluttertoast.showToast(
            msg: "BuzzfeedWidget",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIos: 1,
            backgroundColor: Colors.red,
            textColor: Colors.white);
    }
    WebviewControl(){
        return Fluttertoast.showToast(
            msg: "WebviewWidget",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIos: 1,
            backgroundColor: Colors.red,
            textColor: Colors.white);
    }

    @override
    Widget build(BuildContext context) {
        return Column(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.end,
            crossAxisAlignment: CrossAxisAlignment.end,
            children: <Widget>[
                Divider(
                    height: 2,
                    color: Colors.grey[700],
                ),
                Container(
                    height: 47,
                    color: Colors.transparent,
                    child: Padding(
                        padding: EdgeInsets.only(top: 7),
                        child: Row(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            mainAxisSize: MainAxisSize.max,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                                Expanded(
                                    flex: 1,
                                    child: FlatButton(
                                        onPressed: () async{

                                            Navigator.pop(context);
                                            Navigator.push(
                                                context,
                                                MaterialPageRoute(
                                                    builder: (context) => VideoHomeScreen(url)),
                                            );
                                        },
                                        child: Column(
                                            children: <Widget>[
                                                Icon(
                                                    AppIcons.recordvideo,
                                                    color: Colors.white,
                                                    size: 20,
                                                ),
                                                Padding(
                                                    padding: EdgeInsets.only(top: Dimen.textSpacing),
                                                    child: Text(
                                                        "Video",
                                                        style: TextStyle(
                                                            fontSize: Dimen.bottomNavigationTextSize,
                                                            color: Colors.white),
                                                    ),
                                                )
                                            ],
                                        ),
                                    ),
                                ),
                                Expanded(
                                    flex: 1,
                                    child: FlatButton(
                                        onPressed: (){
                                            //Navigator.pop(context);
                                            Navigator.push(
                                                context,
                                                MaterialPageRoute(
                                                    builder: (context) => ImageHomeScreen(url)),
                                            );
                                        },
                                        child: Column(
                                            children: <Widget>[
                                                Icon(
                                                    AppIcons.takepicture,
                                                    color: Colors.white,
                                                    size: 20,
                                                ),
                                                Padding(
                                                    padding: EdgeInsets.only(top: Dimen.textSpacing),
                                                    child: Text(
                                                        "Image",
                                                        style: TextStyle(
                                                            fontSize: Dimen.bottomNavigationTextSize,
                                                            color: Colors.white),
                                                    ),
                                                )
                                            ],
                                        ),
                                    ),
                                ),
                                Expanded(
                                    flex: 1,
                                    child: FlatButton(

                                        onPressed: () async{
                                            List<QuestionItem> questions;
                                            List<ResultItem> results;
                                            List<int> resultWeight;

                                            final body = {

                                            };
                                            final response = await session.post(domain_url + buzzfeed_url, body);
                                            var data;
                                            int feed_id;
                                            List<dynamic> feeds = response["feed"] as List<dynamic>;
                                            for(int i = 0; i < feeds.length; i++){
                                                if(feeds[i]["content_type"] == "quiz"){
                                                    data = jsonDecode(feeds[i]["quiz_json"]);
                                                    feed_id = feeds[i]["id"];
                                                    break;
                                                }
                                            }
                                            List<dynamic> _questions = data["questions"] as List<dynamic>;
                                            List<dynamic> _results = data["result_rules"] as List<dynamic>;
                                            questions = _questions.map<QuestionItem>((json) => QuestionItem.fromJson(json)).toList();
                                            results = _results.map<ResultItem>((json) => ResultItem.fromJson(json)).toList();
                                            int dimens = results[0].maxWeight.length;
                                            resultWeight = List.filled(dimens, 0);
                                            Navigator.pop(context);
                                            Navigator.push(
                                                context,
                                                MaterialPageRoute(
                                                    builder: (context) => BuzzFeedHome(id: feed_id, questions: questions, results: results, dimens: dimens, resultWeight: resultWeight, url:url)),
                                            );
                                        },
                                        child: Column(
                                            children: <Widget>[
                                                Icon(
                                                    AppIcons.buzzfeed,
                                                    color: Colors.white,
                                                    size: 20,
                                                ),
                                                Padding(
                                                    padding: EdgeInsets.only(top: Dimen.textSpacing),
                                                    child: Text(
                                                        "Buzzfeed",
                                                        style: TextStyle(
                                                            fontSize: Dimen.bottomNavigationTextSize,
                                                            color: Colors.white),
                                                    ),
                                                )
                                            ],
                                        ),
                                    ),
                                ),
                                Expanded(
                                    flex: 1,
                                    child: FlatButton(
                                        onPressed: (){
                                            Navigator.pop(context);
                                            Navigator.push(
                                                context,
                                                MaterialPageRoute(
                                                    builder: (context) => WebViewContainer('http://www.kevs3d.co.uk/dev/js1kdragons/')),
                                            );
                                        },
                                        child: Column(
                                            children: <Widget>[
                                                Icon(
                                                    AppIcons.webview,
                                                    color: Colors.white,
                                                    size: 20
                                                ),
                                                Padding(
                                                    padding: EdgeInsets.only(top: Dimen.textSpacing),
                                                    child: Text(
                                                        "Webview",
                                                        style: TextStyle(
                                                            fontSize: Dimen.bottomNavigationTextSize,
                                                            color: Colors.white),
                                                    ),
                                                )
                                            ],
                                        ),
                                    ),
                                ),
                                Expanded(
                                    flex: 1,
                                    child: FlatButton(
                                        onPressed: (){
                                            //Navigator.pop(context);
                                            Navigator.push(
                                                context,
                                                MaterialPageRoute(
                                                    builder: (context) => new Home()),
                                            );
                                        },
                                        child: Column(
                                            children: <Widget>[
                                                Icon(AppIcons.backbutton, color: Colors.white, size: 20),
                                                Padding(
                                                    padding: EdgeInsets.only(top: Dimen.textSpacing),
                                                    child: Text(
                                                        "Back",
                                                        style: TextStyle(
                                                            fontSize: Dimen.bottomNavigationTextSize,
                                                            color: Colors.white),
                                                    ),
                                                )
                                            ],
                                        ),
                                    ),
                                ),
                            ],
                        )),
                )
            ],
        );
    }
}


