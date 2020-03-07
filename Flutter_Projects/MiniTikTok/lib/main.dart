import 'package:flutter/material.dart';
import 'package:MiniTikTok/Auth/authsystem.dart';
import 'package:camera/camera.dart';
import 'dart:async';
import 'package:MiniTikTok/widgets/videoupload/constant/Constant.dart';
import 'package:MiniTikTok/widgets/videoupload/screen/CameraHomeScreen.dart';
import 'package:MiniTikTok/bottom_nav_bar.dart';
import 'package:MiniTikTok/widgets/home/home_page.dart';
import 'package:MiniTikTok/widgets/home/home_header.dart';

final RouteObserver<PageRoute> routeObserver = new RouteObserver<PageRoute>();

enum LoginStatus { notSignIn, signIn }

void main() => runApp(

    new MaterialApp(

      /*initialRoute: '/',
      routes: {
        '/': (context) => Login(),
      },*/
      navigatorObservers: <NavigatorObserver>[routeObserver],
      debugShowCheckedModeBanner: false,
      home: Login(),
        /*home: Scaffold(
            body: Stack(
                children: <Widget>[
                    HomeScreen(),
                    BottomNavigation(signOut),
                    //homeHeader(),
                ],
            ),
        ),*/
    )
);
signOut(){

}

