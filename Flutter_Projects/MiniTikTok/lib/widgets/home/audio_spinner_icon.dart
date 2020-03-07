import 'package:MiniTikTok/challenge_page.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:MiniTikTok/resources/assets.dart';
import 'package:MiniTikTok/resources/dimen.dart';
import 'package:flutter/cupertino.dart';
import 'package:MiniTikTok/resources/Todo.dart';
import 'package:MiniTikTok/widgets/home/home_video_renderer.dart' as globals;
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';

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

Widget audioSpinner(Todo todo, VideoPlayerController vController, YoutubePlayerController yController) {
  return new GestureDetector(
    onTap: () {
      /*if(vController != null && yController != null){
        vController.seekTo(Duration.zero);
        vController.pause();
        yController.seekTo(Duration.zero);
        yController.pause();
      }
      else if(vController != null){
        vController.seekTo(Duration.zero);
        vController.pause();
      }
      else{
        yController.seekTo(Duration.zero);
        yController.pause();
      }*/
      Navigator.pop(todo.context);
      Navigator.push(
        todo.context,
        MaterialPageRoute(
            builder: (context) => new ChallengePage(todo.data_url, vController, yController)),
      );

      //Navigator.of(todo.context).push(new FadeEffect(todo.data_url));
      /*return Fluttertoast.showToast(
          msg: "Tap",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIos: 1,
          backgroundColor: Colors.red,
          textColor: Colors.white);*/
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


