import 'dart:io';

import 'package:QlipQlop/widgets/profile/profile.dart';
import 'package:QlipQlop/widgets/reward/rewind_home.dart';
import 'package:QlipQlop/widgets/reward/rewind_list.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'dart:convert';

import 'package:bottom_navy_bar/bottom_navy_bar.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:QlipQlop/bottom_nav_bar.dart';
import 'package:QlipQlop/widgets/home/home_page.dart';
import 'package:QlipQlop/widgets/home/home_header.dart';
import 'package:wakelock/wakelock.dart';
import 'package:QlipQlop/widgets/profile/edit_profile.dart';
import 'package:QlipQlop/Auth/session.dart' as session;
import 'package:QlipQlop/resources/Constants.dart';

enum LoginStatus { notSignIn, signIn }

class Home extends StatefulWidget{
    @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new _Home();
  }



}
class _Home extends State<Home>{

    LoginStatus _loginStatus = LoginStatus.signIn;


    signOut() async {
        SharedPreferences preferences = await SharedPreferences.getInstance();
        setState(() {
            preferences.setInt("value", null);
            preferences.setString("name", null);
            preferences.setString("email", null);
            preferences.setString("id", null);

            preferences.commit();
            _loginStatus = LoginStatus.notSignIn;
        });
    }

    @override
    void initState(){
        super.initState();
        //Wakelock.enable();
    }
    @override
    void dispose(){
        super.dispose();
    }
    @override
    Widget build(BuildContext context){

        return new WillPopScope(
            onWillPop: () {
                exit(0);
                return;
            },
            child: new Scaffold(
                body: Stack(
                    children: <Widget>[
                        DefaultTabController(
                            initialIndex: 1,
                            length: 3,

                            child: Scaffold(
                                body: TabBarView(
                                    children: [
                                        new RewardPage(),
                                        new HomeScreen(),
                                        new Profile(),
                                    ],
                                ),
                            ),
                        ),

                        //BottomNavigation(signOut),
                        //homeHeader(),
                    ],
                ),
            ),
        );
    }

}