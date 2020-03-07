import 'package:QlipQlop/challenge_page.dart';
import 'package:QlipQlop/widgets/buzzfeed/BuzzHome.dart';
import 'package:QlipQlop/widgets/imageupload/screen/HomeScreen.dart';
import 'package:QlipQlop/widgets/videoupload/screen/HomeScreen.dart';
import 'package:QlipQlop/widgets/website/WebviewFeed.dart';
import 'package:QlipQlop/widgets/website/web_view_container.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/resources/dimen.dart';
import 'package:flutter/cupertino.dart';
import 'package:QlipQlop/resources/Todo.dart';
import 'package:QlipQlop/widgets/home/home_video_renderer.dart' as globals;
import 'package:intl/intl.dart';
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:QlipQlop/Auth/session.dart' as session;
import 'package:QlipQlop/widgets/buzzfeed/JsonParser.dart';

class Spinner extends StatefulWidget {
  @override
  State createState() => new _Spinner();
}

class _Spinner extends State<Spinner> {
  BuildContext context;
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: new Center(
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            FlatButton(
              //onPressed: VideoWidget,
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
            FlatButton(
              //onPressed: ImageWidget,
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
                      "Photo",
                      style: TextStyle(
                          fontSize: Dimen.bottomNavigationTextSize,
                          color: Colors.white),
                    ),
                  )
                ],
              ),
            ),
            FlatButton(
              //onPressed: BuzzfeedWidget,
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
            FlatButton(
              //onPressed: WebviewWidget,
              child: Column(
                children: <Widget>[
                  Icon(AppIcons.webview, color: Colors.white, size: 20),
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
          ],
        ),
      ),
    );
  }
}


openChallenge(BuildContext context, String data_url){

  if(session.feed_type == "video"){
    Navigator.push(
      context,
      MaterialPageRoute(
          builder: (context) => new VideoHomeScreen(data_url)),
    );
  }
  else if(session.feed_type == "picture"){
    Navigator.push(
      context,
      MaterialPageRoute(
          builder: (context) => new ImageHomeScreen(data_url)),
    );
  }
  else if(session.feed_type == "challenge"){
    Navigator.push(
      context,
      MaterialPageRoute(
          builder: (context) => new VideoHomeScreen(data_url)),
    );
  }
  else if(session.feed_type == "web"){
    Navigator.push(
      context,
      MaterialPageRoute(
          builder: (context) => new WebViewContainer(session.feed_url)),
    );
  }
  else if(session.feed_type == "quiz"){
    List<QuestionItem> questions;
    List<ResultItem> results;
    List<int> resultWeight;

    var data = session.quiz_data;
    List<dynamic> _questions = data["questions"] as List<dynamic>;
    List<dynamic> _results = data["result_rules"] as List<dynamic>;
    questions = _questions.map<QuestionItem>((json) => QuestionItem.fromJson(json)).toList();
    results = _results.map<ResultItem>((json) => ResultItem.fromJson(json)).toList();
    int dimens = results[0].maxWeight.length;
    resultWeight = List.filled(dimens, 0);
    /*Navigator.push(
      context,
      MaterialPageRoute(
          builder: (context) => BuzzFeedHome(id: int.parse(session.feed_id), questions: questions, results: results, dimens: dimens, resultWeight: resultWeight)),
    );*/
  }




}


Widget audioSpinner(BuildContext context, String data_url) {
  return new GestureDetector(
    onTap: () {
      openChallenge(context, data_url);
    },
    child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisSize: MainAxisSize.max,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Container(
              width: 50.0,
              height: 50.0,
              decoration: BoxDecoration(
                  gradient: audioDiscGradient,
                  shape: BoxShape.circle,
                  image: DecorationImage(
                      image: AssetImage("assets/images/challenge.png")))
          ),
        ]),
  );
}

LinearGradient get audioDiscGradient => LinearGradient(colors: [
      Colors.grey[800],
      Colors.grey[900],
      Colors.grey[900],
      Colors.grey[800]
    ], stops: [
      0.0,
      0.4,
      0.6,
      1.0
    ], begin: Alignment.bottomLeft, end: Alignment.topRight);


