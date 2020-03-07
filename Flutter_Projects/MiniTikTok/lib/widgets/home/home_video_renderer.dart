
import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';
import 'package:flutter/cupertino.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:flutter/foundation.dart';
import 'package:progress_dialog/progress_dialog.dart';



class AppVideoPlayer extends StatefulWidget {
  final VideoPlayerController _video_controller;
  final String video_url;
  AppVideoPlayer(this.video_url, this._video_controller);

  VideoPlayerController get videoController => _video_controller;

  @override
  _AppVideoPlayerState createState() {
      /*_video_controller = new VideoPlayerController.network(
          video_url);*/
      return new _AppVideoPlayerState(video_url: video_url, video_controller: _video_controller);
  }
}

class _AppVideoPlayerState extends State<AppVideoPlayer> {

  final String video_url;
  VideoPlayerController video_controller;
  _AppVideoPlayerState({this.video_url, this.video_controller});
  ProgressDialog pr;

  VideoPlayerController _video_controller;

  @override
  void initState() {
    super.initState();
    _video_controller = VideoPlayerController.network(video_url)
      ..initialize().then((_) {
        _video_controller.play();
        setState(() {});

      });
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: _video_controller.value.initialized
          ? AspectRatio(
              aspectRatio: _video_controller.value.aspectRatio,
              child: VideoPlayer(_video_controller),

            )
          : Container(
              color: Colors.black,
              child: CircularProgressIndicator()),

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
    _video_controller.dispose();
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
  final YoutubePlayerController youtube_controller;
  final String youtube_url;
  YoutubeVideoPlayer(this.youtube_url, this.youtube_controller);

  YoutubePlayerController get youtubeController => youtube_controller;


  @override
  _YoutubeVideoPlayerState createState() {
      String id = YoutubePlayer.convertUrlToId(youtube_url);

      return new _YoutubeVideoPlayerState(youtube_url: youtube_url);
  }
}

class _YoutubeVideoPlayerState extends State<YoutubeVideoPlayer> {
  final String youtube_url;
  YoutubePlayerController y_outube_controller;
  _YoutubeVideoPlayerState({this.youtube_url});
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey();

  TextEditingController _idController;
  TextEditingController _seekToController;
  bool _isPlayerReady = false;



  @override
  void initState() {
    super.initState();
    String id = YoutubePlayer.convertUrlToId(youtube_url);
    y_outube_controller = YoutubePlayerController(
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
    y_outube_controller.load(id);
    y_outube_controller..addListener(listener);

    _idController = TextEditingController();
    _seekToController = TextEditingController();
  }

  void listener() {
    if (_isPlayerReady && mounted && !y_outube_controller.value.isFullScreen) {
      setState(() {
      });
    }
  }

  @override
  void deactivate() {
    // Pauses video while navigating to next page.
    y_outube_controller.pause();
    super.deactivate();
  }

  @override
  void dispose() {
    y_outube_controller.dispose();
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
        controller: y_outube_controller,
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
          y_outube_controller.load(youtube_url);
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
