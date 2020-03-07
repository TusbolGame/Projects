import 'package:flutter/material.dart';
import 'package:MiniTikTok/animations/spinner_animation.dart';
import 'package:MiniTikTok/resources/assets.dart';
import 'package:MiniTikTok/widgets/home/audio_spinner_icon.dart';
import 'package:MiniTikTok/widgets/home/controls/video_control_action.dart';
import 'package:MiniTikTok/widgets/home/video_metadata/user_profile.dart';
import 'package:MiniTikTok/widgets/home/video_metadata/video_desc.dart';

Widget challengeControls() {
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
                                videoControlAction(icon: AppIcons.recordvideo, label: "Record"),
                                videoControlAction(icon: AppIcons.heart, label: "17.8k"),
                                videoControlAction(icon: AppIcons.chat_bubble, label: "130"),
                                videoControlAction(
                                    icon: AppIcons.reply,
                                    label: "Share",
                                    size: 27),


                            ],
                        ),
                    ),
                )
            ],
        ),
    );
}
