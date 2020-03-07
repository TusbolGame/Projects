import 'dart:io';

import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';

class VideoPlayerScreen extends StatefulWidget {
  //String path;
  VideoPlayerScreen({Key key, this.path}) : super(key: key);
  final String path;
  _VideoPlayerScreenState screenState;

  @override
  _VideoPlayerScreenState createState(){
    screenState = new _VideoPlayerScreenState(path);
    return screenState;
  }

  void videoPlay(){
    screenState.videoPlay();
  }
  void videoPause(){
    screenState.videoPause();
  }
  void videoStop(){
    screenState.videoStop();
  }
}

class _VideoPlayerScreenState extends State<VideoPlayerScreen> {
  VideoPlayerController playerController;
  VoidCallback listener;
  String path;

  BuildContext context;

  _VideoPlayerScreenState(this.path);

  @override
  @override
  void initState() {
    super.initState();

    listener = () {
    };
    initializeVideo();
    //playerController.play();
  }

  void initializeVideo() {
    if(path != null){
      playerController = VideoPlayerController.file(File(path))
        ..addListener(listener)
        ..setVolume(1.0)
        ..initialize();
    }
    else{
      return;
    }
  }
  void videoPlay()
  {
    playerController.play();
  }
  void videoPause()
  {
    playerController.pause();
  }
  void videoStop()
  {
    playerController.seekTo(Duration.zero);
    playerController.pause();
  }

  @override
  void deactivate() {
    if (playerController != null) {
      playerController.setVolume(0.0);
      playerController.removeListener(listener);
    }
    super.deactivate();
  }

  @override
  void dispose() {
    if (playerController != null) playerController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    this.context = context;
    return Scaffold(
        body: Stack(fit: StackFit.expand, children: <Widget>[
          new AspectRatio(
              aspectRatio: 9 / 16,
              child: Container(
                child: (playerController != null
                    ? VideoPlayer(
                        playerController,
                      )
                    : Container()),
              )),
        ]));
  }
}
