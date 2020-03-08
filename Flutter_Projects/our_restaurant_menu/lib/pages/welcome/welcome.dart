import 'dart:io';
import 'package:OurMenu/pages/welcome/banner_widget.dart';
import 'package:OurMenu/pages/welcome/menuitem_widget.dart';
import 'package:flutter/material.dart';
import 'package:OurMenu/pages/profile/animation.dart';
import 'package:OurMenu/pages/profile/oval-right-clipper.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:OurMenu/widgets/common_widget.dart' as Common;

final GlobalKey<ScaffoldState> _key = GlobalKey<ScaffoldState>();

class WelcomePage extends StatefulWidget {
  @override
  WelcomePageState createState() => WelcomePageState();
}

class WelcomePageState extends State<WelcomePage> {
  final Color primary = Color(0xff2ABF76);
  final Color active = Color(0xffDAFFED);

  @override
  void initState() {
    super.initState();
  }
  DateTime currentBackPressTime;
  @override
  Widget build(BuildContext context) {

    var screenHeight = MediaQuery.of(context).size.height;
    var screenWidth = MediaQuery.of(context).size.width;

    Widget _buildRow(IconData icon, String title, String route) {
      final TextStyle tStyle = TextStyle(color: active, fontSize: 16.0);
      return GestureDetector(
        onTap: (){
          Common.showToast("This will navigate to " + route, Colors.cyan);
        },
        child: Container(
          color: Colors.transparent,
          width: MediaQuery.of(context).size.width,
          padding: const EdgeInsets.symmetric(vertical: 8.0),
          child: Row(children: [
            Icon(
              icon,
              color: active,
            ),
            SizedBox(width: 10.0),
            Text(
              title,
              style: tStyle,
            ),
          ]),
        ),
      );
    }

    Divider _buildDivider() {
      return Divider(
        color: active,
      );
    }
    buildDrawer(String url, String userName, String userID) {
      final String image = url;
      return ClipPath(
        clipper: OvalRightBorderClipper(),
        child: Drawer(
          child: Container(
            padding: const EdgeInsets.only(left: 16.0, right: 40),
            decoration: BoxDecoration(
                color: primary, boxShadow: [BoxShadow(color: Colors.black45)]),
            width: 300,
            child: SafeArea(
              child: SingleChildScrollView(
                child: Column(
                  children: <Widget>[
                    Container(
                      alignment: Alignment.centerRight,
                      child: IconButton(
                        icon: Icon(
                          Icons.power_settings_new,
                          color: active,
                        ),
                        onPressed: () {
                          onWillPop();
                        },
                      ),
                    ),
                    Container(
                      height: 90,
                      alignment: Alignment.center,
                      decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          gradient: LinearGradient(
                              colors: [Color(0xFF52FF9A), Color(0xFF3DBF74)])),
                      child: CircleAvatar(
                        radius: 40,
                        backgroundImage: CachedNetworkImageProvider(
                          image,
                        ),
                        backgroundColor: Colors.transparent,
                      ),
                    ),
                    SizedBox(height: 5.0),
                    Text(
                      userName,
                      style: TextStyle(color: Colors.white, fontSize: 18.0),
                    ),
                    Text(
                      userID,
                      style: TextStyle(color: active, fontSize: 16.0),
                    ),
                    SizedBox(height: 30.0),
                    _buildRow(Icons.home, "Home", "/WelcomePage"),
                    _buildDivider(),
                    _buildRow(Icons.person_pin, "Your profile", "/ProfilePage"),
                    _buildDivider(),
                    _buildRow(Icons.settings, "Settings", "/SettingPage"),
                    _buildDivider(),
                    _buildRow(Icons.favorite, "Favorite", "/FavoritePage"),
                    _buildDivider(),
                    _buildRow(Icons.help, "Help", "/HelpPage"),
                    _buildDivider(),
                  ],
                ),
              ),
            ),
          ),
        ),
      );
    }
    double statusBarHeight = MediaQuery.of(context).padding.top;

    return new WillPopScope(
        child: Scaffold(
          key: _key,
          resizeToAvoidBottomPadding: false,
          drawer: buildDrawer(images[0], "fantasticDev", "fantasticDev"),
          body: Container(
            height: screenHeight,
            width: screenWidth,
            child: SafeArea(
                top: false,
                child: Column(
                  mainAxisSize: MainAxisSize.max,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    ClipRRect(
                      borderRadius: BorderRadius.only(bottomLeft: Radius.circular(20.0), bottomRight: Radius.circular(20.0)),
                      child: Container(
                        color: Color(0xFFFC2343),
                        child: Padding(
                          padding: EdgeInsets.fromLTRB(10, statusBarHeight, 10, 5),
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: <Widget>[
                              IconButton(
                                icon: Icon(Icons.menu),
                                color: Colors.white,
                                onPressed: (){
                                  _key.currentState.openDrawer();
                                },
                              ),
                              Center(
                                child: Text(
                                  "Welcome to Craft Restaurant",
                                  style: new TextStyle(
                                    color: Colors.white,
                                    fontSize: 20.0,
                                    fontWeight: FontWeight.w600,
                                    fontStyle: FontStyle.normal,
                                  ),
                                ),
                              ),
                              IconButton(
                                icon: Icon(Icons.power_settings_new),
                                color: Colors.white,
                                onPressed: (){
                                  onWillPop();
                                },
                              )
                            ],
                          ),
                        ),
                      ),
                    ),
                    WelcomeBannerPage(),
                    MenuItemPage(),
                  ],
                ),
              ),
            ),
          ),
        onWillPop: (){
          onWillPop();
          return;
        }
    );
  }
  Future<bool> onWillPop() {
    DateTime now = DateTime.now();
    if (currentBackPressTime == null ||
        now.difference(currentBackPressTime) > Duration(seconds: 2)) {
      currentBackPressTime = now;
      Fluttertoast.showToast(
          msg: "Tap again to exit",
          gravity: ToastGravity.BOTTOM,
      );
      return Future.value(false);
    }
    exit(0);
    return Future.value(true);
  }
}