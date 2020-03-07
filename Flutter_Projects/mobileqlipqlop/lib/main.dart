import 'package:flutter/material.dart';
import 'package:QlipQlop/Auth/authsystem.dart';
import 'package:camera/camera.dart';
import 'dart:async';
import 'package:QlipQlop/widgets/videoupload/constant/Constant.dart';
import 'package:QlipQlop/widgets/videoupload/screen/CameraHomeScreen.dart';
import 'package:QlipQlop/bottom_nav_bar.dart';
import 'package:QlipQlop/widgets/home/home_page.dart';
import 'package:QlipQlop/widgets/home/home_header.dart';

final RouteObserver<PageRoute> routeObserver = new RouteObserver<PageRoute>();

enum LoginStatus { notSignIn, signIn }

void main() => runApp(MaterialApp(
      /*initialRoute: '/',
      onGenerateRoute: (RouteSettings settings) {
        switch (settings.name) {
          case "/":
            return MaterialPageRoute(
                builder: (context) => Login());
            break;
          case "/mainfeed":
            return MaterialPageRoute(
                builder: (context) => MainMenu());
            break;
        }
        return null;
      },*/
      initialRoute: '/',
      routes: <String, WidgetBuilder>{
        '/': (BuildContext context) => Login(),
        '/mainfeed': (BuildContext context) => MainMenu(),
        '/a' :(context) => SecondPage(),
      },
      navigatorObservers: <NavigatorObserver>[routeObserver],
      debugShowCheckedModeBanner: false,
      //home: Login(),
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

class SecondPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Second Page'),
      ),
      body: Center(
        child: RaisedButton(
            child: Text('Back To HomeScreen'),
            color: Theme.of(context).primaryColor,
            textColor: Colors.white,
            onPressed: () => Navigator.pop(context)),
      ),
    );
  }
}
