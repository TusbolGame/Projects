import 'dart:io';

import 'package:QlipQlop/resources/Todo.dart';
import 'package:flutter/material.dart';
import 'package:QlipQlop/animations/spinner_animation.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/widgets/home/audio_spinner_icon.dart';
import 'package:QlipQlop/widgets/home/controls/video_control_action.dart';
import 'package:QlipQlop/widgets/home/video_metadata/user_profile.dart';
import 'package:QlipQlop/widgets/home/video_metadata/video_desc.dart';
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:fluttertoast/fluttertoast.dart';

Widget onScreenControls(BuildContext context, String data_url) {
  return Container(
    child: Row(
      children: <Widget>[
        //Expanded(flex: 5, child: videoDesc()),
        Expanded(
            flex: 5,
            child:
            Text(' ')
        ),
        Expanded(
          flex: 1,
          child: Container(
            padding: EdgeInsets.only(bottom: 60, right: 0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[

                //userProfile(),
                /*videoControlAction(icon: AppIcons.recordvideo, label: "Record"),
                videoControlAction(icon: AppIcons.heart, label: "17.8k"),
                videoControlAction(icon: AppIcons.chat_bubble, label: "130"),
                */
                /*new GestureDetector(
                  onTap: (){
                    exit(0);
                    stopwatch = new Stopwatch()..start();
                    tapcount++;
                    if(tapcount == 2)
                      exit(0);
                    else
                      return Fluttertoast.showToast(
                          msg: "Please tap again to exit",
                          toastLength: Toast.LENGTH_SHORT,
                          gravity: ToastGravity.BOTTOM,
                          timeInSecForIos: 1,
                          backgroundColor: Colors.green,
                          textColor: Colors.white);
                  },
                  child: videoControlAction(
                      icon: AppIcons.backbutton,
                      size: 50,
                  ),

                ),*/

                /*Column(
                  children: <Widget>[
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
                        backgroundColor: Colors.grey,
                      ),
                    ),
                  ],
                ),*/
                SpinnerAnimation(
                  body: audioSpinner(context, data_url),
                ),


              ],
            ),
          ),
        )
      ],
    ),
  );
}
