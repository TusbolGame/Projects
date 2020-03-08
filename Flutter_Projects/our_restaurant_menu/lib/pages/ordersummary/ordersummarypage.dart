import 'dart:io';
import 'package:OurMenu/pages/categories/category_card.dart';
import 'package:OurMenu/pages/categories/detail_dialog.dart';
import 'package:OurMenu/pages/categories/glistitem.dart';
import 'package:OurMenu/pages/categories/gtypography.dart';
import 'package:OurMenu/pages/orderstep/favor_list.dart';
import 'package:OurMenu/pages/ordersummary/animated_bottombar.dart';
import 'package:OurMenu/pages/ordersummary/detail_tree.dart';
import 'package:OurMenu/pages/ordersummary/familyorderlistitem.dart';
import 'package:OurMenu/pages/ordersummary/myapp.dart';
import 'package:OurMenu/pages/ordersummary/summary_tree.dart';
import 'package:OurMenu/resources/assets.dart';
import 'package:OurMenu/widgets/fancyfab_button.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:OurMenu/widgets/common_widget.dart' as Common;
import 'package:flutter_custom_clippers/flutter_custom_clippers.dart';
import 'package:OurMenu/widgets/network_image.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:tree_view/tree_view.dart';

final GlobalKey<ScaffoldState> _key = GlobalKey<ScaffoldState>();

class OrderSummaryPage extends StatefulWidget {
  @override
  _OrderSummaryPageState createState() => new _OrderSummaryPageState();
}

class _OrderSummaryPageState extends State<OrderSummaryPage> {
  final Color primary = Color(0xff2ABF76);
  final Color active = Color(0xffDAFFED);
  int _currentPage;

  String pageTitle = "Order Summary";

  @override
  void initState() {
    _currentPage = 0;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    Widget _buildRow(IconData icon, String title, String route) {
      final TextStyle tStyle = TextStyle(color: active, fontSize: 16.0);
      return GestureDetector(
        onTap: () {
          Common.showToast("This wiill navigate to " + route, Colors.cyan);
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
                          Navigator.pop(context);
                          exit(0);
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

    getPage(int page) {
      switch (page) {
        case 0:
          return Center(
            child: SummaryTreePage(),
          );
        case 1:
          return Center(
            child: DetailTreePage(),
          );
      }
    }

    return new WillPopScope(
        child: Scaffold(
          key: _key,
          resizeToAvoidBottomPadding: false,
          drawer: buildDrawer(images[0], "fantasticDev", "fantasticDev"),
          body: getPage(_currentPage),
          floatingActionButton: FancyFab(
            icon: FontAwesomeIcons.qrcode,
            tooltip: "Fancy",
            onPressed: () {},
          ),
          bottomNavigationBar: AnimatedBottomNav(
              currentIndex: _currentPage,
              onChange: (index) {
                setState(() {
                  _currentPage = index;
                });
              }),
        ),
        onWillPop: () {
          Navigator.pop(context);
          return;
        });
  }
}
