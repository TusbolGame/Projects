import 'package:QlipQlop/widgets/challenge/challenge_bottom_bar.dart';
import 'package:QlipQlop/widgets/challenge/challenge_home.dart';
import 'package:flutter/material.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/resources/dimen.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:QlipQlop/bottom_nav_bar.dart';
import 'package:QlipQlop/widgets/home/home_page.dart';
import 'package:QlipQlop/Auth/authsystem.dart';
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:QlipQlop/widgets/Home.dart';
import 'package:wakelock/wakelock.dart';


class ChallengePage extends StatefulWidget {
  final String data_url;
  ChallengePage(this.data_url);

  @override
  _ChallengePage createState() => _ChallengePage(data_url);
}

enum LoginStatus { notSignIn, signIn }

class _ChallengePage extends State<ChallengePage> {

  LoginStatus _loginStatus = LoginStatus.notSignIn;

  final String data_url;
  _ChallengePage(this.data_url);
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
//            ChallengeBottomNavigation(data_url, vController, yController),
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