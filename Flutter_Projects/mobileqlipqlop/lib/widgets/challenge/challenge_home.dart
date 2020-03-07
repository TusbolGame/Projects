import 'package:QlipQlop/widgets/challenge/challenge_controls.dart';
import 'package:flutter/material.dart';
import 'package:QlipQlop/onscreen_controls.dart';
import 'package:QlipQlop/widgets/home/home_video_renderer.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:video_player/video_player.dart';
import 'package:QlipQlop/Auth/session.dart' as session;

class ChallengeHomeScreen extends StatelessWidget {

    final String video_url;
    ChallengeHomeScreen(this.video_url);

    int count = 0;
    int currentIndex = 0;
    YoutubePlayerController yController;
    VideoPlayerController vController;

    _handlePageChanged(int page) {
//    return Fluttertoast.showToast(
//        msg: "Current Video Index: " + currentIndex.toString(),
//        toastLength: Toast.LENGTH_SHORT,
//        gravity: ToastGravity.BOTTOM,
//        timeInSecForIos: 1,
//        backgroundColor: Colors.red,
//        textColor: Colors.white);
    }

    @override
    void didChangeAppLifecycleState(AppLifecycleState state) {
        if(state == AppLifecycleState.resumed){
            // user returned to our app
            if(vController != null){
                vController.play();
            }
            else{
                yController.play();
            }
        }else if(state == AppLifecycleState.inactive){
            // app is inactive
            if(vController != null){
                vController.pause();
            }
            else{
                yController.pause();
            }
        }else if(state == AppLifecycleState.paused){
            // user is about quit our app temporally
            if(vController != null){
                vController.pause();
            }
            else{
                yController.pause();
            }
        }else if(state == AppLifecycleState.detached){
            // app suspended (not used in iOS)
        }
    }

    @override
    Widget build(BuildContext context) {
        return PageView.builder(
            scrollDirection: Axis.vertical,
            controller: PageController(
                initialPage: 0,
            ),
            onPageChanged: _handlePageChanged,
            itemBuilder: (context, position) {
                if(video_url.contains('youtube'))
                {
                    currentIndex = position;
                    String id = YoutubePlayer.convertUrlToId(video_url);
                    session.challenge_youtubePlayerController =  YoutubePlayerController(
                        initialVideoId: id,
                        flags: YoutubePlayerFlags(
                            mute: false,
                            autoPlay: true,
                            disableDragSeek: false,
                            loop: false,
                            isLive: false,
                            forceHideAnnotation: true,
                            hideControls: true,
                        ),
                    );
                    return Container(
                        color: Colors.black,
                        child: Stack(
                            //children: <Widget>[YoutubeVideoPlayer(video_url), challengeControls()],
                            children: <Widget>[YoutubeVideoPlayer(video_url)],
                        ),
                    );
                }
                else{
                    currentIndex = position;
                    session.challenge_videoPlayerController = new VideoPlayerController.network(
                        video_url);
                    return Container(
                        color: Colors.black,
                        child: Stack(
                            children: <Widget>[AppVideoPlayer(video_url)],
                            //children: <Widget>[AppVideoPlayer(video_url), challengeControls()],
                        ),
                    );
                }

            },
            itemCount: 1);
    }
}
