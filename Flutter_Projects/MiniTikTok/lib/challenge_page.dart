import 'package:MiniTikTok/widgets/challenge/challenge_bottom_bar.dart';
import 'package:MiniTikTok/widgets/challenge/challenge_home.dart';
import 'package:flutter/material.dart';
import 'package:MiniTikTok/resources/assets.dart';
import 'package:MiniTikTok/resources/dimen.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:MiniTikTok/bottom_nav_bar.dart';
import 'package:MiniTikTok/widgets/home/home_page.dart';
import 'package:MiniTikTok/Auth/authsystem.dart';
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:MiniTikTok/widgets/Home.dart';
import 'package:wakelock/wakelock.dart';


class ChallengePage extends StatefulWidget {
  //final VoidCallback signOut;
  //MainMenu(this.signOut);
  final String data_url;
  VideoPlayerController vController;
  YoutubePlayerController yController;
  ChallengePage(this.data_url, this.vController, this.yController);

  @override
  _ChallengePage createState() => _ChallengePage(data_url, vController, yController);
}

enum LoginStatus { notSignIn, signIn }

class _ChallengePage extends State<ChallengePage> {

  LoginStatus _loginStatus = LoginStatus.notSignIn;

  final String data_url;
  VideoPlayerController vController;
  YoutubePlayerController yController;
  _ChallengePage(this.data_url, this.vController, this.yController);
  signOut() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    Navigator.pop(context);
    setState(() {
      preferences.setInt("value", null);
      preferences.setString("name", null);
      preferences.setString("email", null);
      preferences.setString("id", null);
      preferences.commit();
      _loginStatus = LoginStatus.notSignIn;
    });
  }


  int currentIndex = 0;
  String selectedIndex = 'TAB: 0';

  String email = "", name = "", id = "";
  TabController tabController;

  getPref() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    setState(() {
      id = preferences.getString("id");
      email = preferences.getString("email");
      name = preferences.getString("name");
    });
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    getPref();
    //Wakelock.enable();
  }

  @override
  Widget build(BuildContext context) {

    return new WillPopScope(
      onWillPop: (){
        Navigator.pop(context);
        Navigator.push(
          context,
          MaterialPageRoute(
              builder: (context) => new Home()),
        );
        return;
      },
      child: new Scaffold(
        body: Stack(
          children: <Widget>[
            ChallengeHomeScreen(data_url),
            ChallengeBottomNavigation(data_url, vController, yController),
            //homeHeader(),
          ],
        ),
      ),
    );

  }
  callToast(String msg) {
    Fluttertoast.showToast(
        msg: "$msg",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: Colors.red,
        textColor: Colors.white,
        fontSize: 16.0);
  }
}