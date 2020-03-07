
import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';
import 'package:flutter/cupertino.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:flutter/foundation.dart';
import 'package:progress_dialog/progress_dialog.dart';
import 'package:QlipQlop/Auth/session.dart' as session;



class AppVideoPlayer extends StatefulWidget {
//  final VideoPlayerController _video_controller;
  final String video_url;
  AppVideoPlayer(this.video_url);

  @override
  _AppVideoPlayerState createState() {
      return new _AppVideoPlayerState(video_url: video_url);
  }
}

class _AppVideoPlayerState extends State<AppVideoPlayer> {

  final String video_url;
  _AppVideoPlayerState({this.video_url});
  ProgressDialog pr;

  @override
  void initState() {
    super.initState();
    session.challenge_videoPlayerController = VideoPlayerController.network(video_url)
      ..initialize().then((_) {
        session.challenge_videoPlayerController.play();
        setState(() {});

      });
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: session.challenge_videoPlayerController.value.initialized
          ? AspectRatio(
              aspectRatio: session.challenge_videoPlayerController.value.aspectRatio,
              child: VideoPlayer(session.challenge_videoPlayerController),

            )
          : Container(
              color: Colors.black,
              child: CircularProgressIndicator(
                backgroundColor: Colors.tealAccent,
              )
          ),

    );
  }
  Widget showLoader(){
    pr = new ProgressDialog(context, isDismissible: true, showLogs: true);
    pr.show();

  }
  @override
  void didUpdateWidget(Widget oldWidget){
    super.didUpdateWidget(oldWidget);
  }

  @override
  void dispose() {
    super.dispose();
    session.challenge_videoPlayerController.dispose();
  }
  /*@override
  void deactivate() {
    // Pauses video while navigating to next page.
    if(video_controller != null)
      video_controller.seekTo(Duration.zero);
    video_controller.pause();
    super.deactivate();
  }*/
}

class YoutubeVideoPlayer extends StatefulWidget {
  final String youtube_url;
  YoutubeVideoPlayer(this.youtube_url);

  @override
  _YoutubeVideoPlayerState createState() {
      String id = YoutubePlayer.convertUrlToId(youtube_url);

      return new _YoutubeVideoPlayerState(youtube_url: youtube_url);
  }
}

class _YoutubeVideoPlayerState extends State<YoutubeVideoPlayer> {
  final String youtube_url;
  _YoutubeVideoPlayerState({this.youtube_url});
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey();

  TextEditingController _idController;
  TextEditingController _seekToController;
  bool _isPlayerReady = false;



  @override
  void initState() {
    super.initState();
    String id = YoutubePlayer.convertUrlToId(youtube_url);
    session.challenge_youtubePlayerController = YoutubePlayerController(
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
    session.challenge_youtubePlayerController.load(id);
    session.challenge_youtubePlayerController..addListener(listener);

    _idController = TextEditingController();
    _seekToController = TextEditingController();
  }

  void listener() {
    if (_isPlayerReady && mounted && !session.challenge_youtubePlayerController.value.isFullScreen) {
      setState(() {
      });
    }
  }

  @override
  void deactivate() {
    // Pauses video while navigating to next page.
    session.challenge_youtubePlayerController.pause();
    super.deactivate();
  }

  @override
  void dispose() {
    session.challenge_youtubePlayerController.dispose();
    _idController.dispose();
    _seekToController.dispose();
    super.dispose();
  }


  @override
  Widget build(BuildContext context) {
    return Center(
      key: _scaffoldKey,
      child:
      YoutubePlayer(
        controller: session.challenge_youtubePlayerController,
        topActions: <Widget>[
          SizedBox(width: 8.0),
          IconButton(
            icon: Icon(
              Icons.settings,
              color: Colors.white,
              size: 25.0,
            ),
            onPressed: () {
              return;
            },
          ),
        ],

        onReady: () {
          _isPlayerReady = true;
          setState(() {

          });
        },
        onEnded: (id) {
          session.challenge_youtubePlayerController.load(YoutubePlayer.convertUrlToId(youtube_url));
          //_showSnackBar('Next Video Started!');
        },
      )
    );
  }
  @override
  void didUpdateWidget(Widget oldWidget){
    super.didUpdateWidget(oldWidget);
  }
}
