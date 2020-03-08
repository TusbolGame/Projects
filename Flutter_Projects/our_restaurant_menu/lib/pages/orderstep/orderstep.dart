import 'dart:io';
import 'package:OurMenu/food/recipe_single.dart';
import 'package:OurMenu/pages/categories/category.dart';
import 'package:OurMenu/pages/categories/category_card.dart';
import 'package:OurMenu/pages/orderstep/family_dialog.dart';
import 'package:OurMenu/pages/orderstep/profile_dialog.dart';
import 'package:OurMenu/pages/orderstep/ribbon-clipper.dart';
import 'package:OurMenu/resources/assets.dart';
import 'package:OurMenu/widgets/beautiful_alert_dialog.dart';
import 'package:OurMenu/widgets/waveclip.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:OurMenu/widgets/common_widget.dart' as Common;
import 'package:floating_ribbon/floating_ribbon.dart';
import 'package:flutter_custom_clippers/flutter_custom_clippers.dart';
import 'package:skeleton_text/skeleton_text.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';

final GlobalKey<ScaffoldState> _key = GlobalKey<ScaffoldState>();

class OrderStepPage extends StatefulWidget {
  @override
  _OrderStepPageState createState() => new _OrderStepPageState();
}

class _OrderStepPageState extends State<OrderStepPage> {
  final Color primary = Color(0xff2ABF76);
  final Color active = Color(0xffDAFFED);

  @override
  void initState() {
    super.initState();
  }

  Widget functionButtons() {
    return Row(
      mainAxisSize: MainAxisSize.max,
      children: <Widget>[
        Expanded(
          child: Padding(
            padding: EdgeInsets.only(left: 10, right: 5, top: 10, bottom: 10),
            child: ClipRRect(
              borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(30),
                  bottomLeft: Radius.circular(30)),
              child: RaisedButton(
                color: Colors.green,
                colorBrightness: Brightness.dark,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Icon(Icons.supervisor_account),
                    Text(
                        " Order",
                        style: TextStyle(
                          fontSize: 20
                        ),
                    ),
                  ],
                ),
                onPressed: () => _familyDialog(context),
              ),
            ),
          ),
        ),
        Expanded(
          child: Padding(
            padding: EdgeInsets.only(left: 5, right: 10, top: 10, bottom: 10),
            child: ClipRRect(
              borderRadius: BorderRadius.only(
                  topRight: Radius.circular(30),
                  bottomRight: Radius.circular(30)),
              child: RaisedButton(
                color: Colors.green,
                colorBrightness: Brightness.dark,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Text(
                        "Profile ",
                      style: TextStyle(
                          fontSize: 20
                      ),
                    ),
                    Icon(Icons.favorite),
                  ],
                ),
                onPressed: () => _profileDialog(context),
              ),
            ),
          ),
        )
      ],
    );
  }

  final Color color1 = Color(0xffB5192F);
  final Color color2 = Color(0xffE21F3D);
  final Color color3 = Color(0xffFE1949);
  final Color color4 = Color(0xffF0631C);

  var apetitizerImages = [
    "https://drop.ndtv.com/albums/COOKS/pasta-vegetarian/pastapenne.jpg",
    "https://images.theconversation.com/files/194291/original/file-20171113-27595-ox08qm.jpg",
    "https://www.rd.com/wp-content/uploads/2017/05/easy-mexican-appetizers-FT.jpg"
  ];
  var apetitizerCategories = ["Foods", "Drinks", "All Products"];

  var mainmealImages = [
    "https://soyummy.com/wp-content/uploads/2017/08/Creamy_Tuscan_Shrimp_Pasta_Still-Edit-e1504050165671.jpg",
    "https://data.whicdn.com/images/20561875/original.jpg",
    "https://thumbs.dreamstime.com/z/burgers-beef-fried-potatoes-glass-cold-beer-homemade-wooden-table-dark-background-85766273.jpg"
  ];
  var mainmealCategories = ["Foods", "Drinks", "All Products"];

  var soupImages = [
    "https://www.inspiredtaste.net/wp-content/uploads/2018/10/Homemade-Vegetable-Soup-Recipe-2-1200.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a8/Common_alcoholic_beverages.jpg/290px-Common_alcoholic_beverages.jpg",
    "https://drinksfeed.com/wp-content/blogs.dir/1/files/2014/06/soup_and_wine.png"
  ];
  var soupCategories = ["Foods", "Drinks", "All Products"];

  var desertImages = [
    "http://fullhdwall.com/wp-content/uploads/2017/01/Beautiful-Dessert-Food.jpg",
    "https://media.graytvinc.com/images/810*456/alcohol+pixabay+mgn2.jpg",
    "https://hscweb3.hsc.usf.edu/health/publichealth/news/wp-content/uploads/2015/06/hookah.jpg",
    "http://i.gzn.jp/img/2016/09/29/komeda-shironoir-caramelringo/00_m.jpg"
  ];
  var desertCategories = ["Foods", "Drinks", "Smoke", "All Products"];

  Widget _buildItem(BuildContext context, products, categories, index) {
    return GestureDetector(
      onTap: (){
        Navigator.push(context, MaterialPageRoute(builder: (context) => CategoryPage()),);
      },
      child: Container(
        margin: EdgeInsets.only(right: 20),
//        width: 150,
        child: Stack(
          children: <Widget>[
            Container(
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(15.0),
                  image: DecorationImage(
                    image: CachedNetworkImageProvider(
                      products[index],
                    ),
                    fit: BoxFit.cover,
                  )),
              height: 150,
            ),
            Container(
              decoration: BoxDecoration(
                  borderRadius:
                  BorderRadius.all(Radius.circular(20.0)),
                  gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [
                        Colors.transparent,
                        Color(0xFF2B2B2B)
                      ])),
            ),
            Column(
              mainAxisAlignment: MainAxisAlignment.end,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Padding(
                  padding: EdgeInsets.fromLTRB(10, 0, 0, 10),
                  child: Text(
                    categories[index] + "\n3/\$15",
                    style: TextStyle(
                        fontSize: 20.0, color: Colors.white),
                  ),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }
  Widget stepWidgets() {
    return Stack(
      children: <Widget>[
        Container(
          height: MediaQuery.of(context).size.height,
//          width: MediaQuery.of(context).size.width,
        ),
        Padding(
          padding: EdgeInsets.all(10),
          child: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Padding(
                  padding: const EdgeInsets.only(left: 0, top: 16, right: 0),
                  child: Center(
                    child: ClipPath(
                      clipper: RibbonClipper(offset: 20),
                      child: Container(
                        height: MediaQuery.of(context).size.height * 3 / 50,
                        width: MediaQuery.of(context).size.width * 8 / 16  ,
                        color: Color(0xFF852196),
                        child: Center(
                            child: Text(
                                "Apetitizers",
                                style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 20
                                ),
                            )
                        ),
                      ),
                    ),
                  ),
                ),
                SizedBox(height: 20.0),
                Container(
                  height: 350,
                  child: StaggeredGridView.count(
                    crossAxisCount: 2,
                    crossAxisSpacing: 15.0,
                    mainAxisSpacing: 15.0,
                    primary: false,
                    padding: EdgeInsets.symmetric(horizontal: 10.0, vertical: 8.0),
                    children: List<int>.generate(mainmealImages.length, (i) => i).map((index) {
                      return _buildItem(context, mainmealImages, mainmealCategories, index);
                    }).toList(),
                    staggeredTiles: [
                      StaggeredTile.extent(1, 150.0),
                      StaggeredTile.extent(1, 150.0),
                      StaggeredTile.extent(2, 150.0),
                    ],
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(left: 16.0, top: 16),
                  child: Center(
                    child: ClipPath(
                      clipper: RibbonClipper(offset: 20),
                      child: Container(
                        height: MediaQuery.of(context).size.height * 3 / 50,
                        width: MediaQuery.of(context).size.width * 8 / 16  ,
                        color: Color(0xFF852196),
                        child: Center(
                            child: Text(
                              "Main Meal",
                              style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 20
                              ),
                            )
                        ),
                      ),
                    ),
                  ),
                ),
                SizedBox(height: 20.0),
                Container(
                    height: 350,
                    child: StaggeredGridView.count(
                      crossAxisCount: 2,
                      crossAxisSpacing: 8.0,
                      mainAxisSpacing: 12.0,
                      primary: false,
                      padding: EdgeInsets.symmetric(horizontal: 8.0, vertical: 8.0),
                      children: List<int>.generate(mainmealImages.length, (i) => i).map((index) {
                        return Builder(
                          builder: (BuildContext context) {
                            return _buildItem(context, mainmealImages, mainmealCategories, index);
                          },
                        );
                      }).toList(),
                      staggeredTiles: [
                        StaggeredTile.extent(1, 150.0),
                        StaggeredTile.extent(1, 150.0),
                        StaggeredTile.extent(2, 150.0),
                      ],
                    ),
                ),
                Padding(
                  padding: const EdgeInsets.only(left: 16.0, top: 16),
                  child: Center(
                    child: ClipPath(
                      clipper: RibbonClipper(offset: 20),
                      child: Container(
                        height: MediaQuery.of(context).size.height * 3 / 50,
                        width: MediaQuery.of(context).size.width * 8 / 16  ,
                        color: Color(0xFF852196),
                        child: Center(
                            child: Text(
                              "Soup",
                              style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 20
                              ),
                            )
                        ),
                      ),
                    ),
                  ),
                ),
                SizedBox(height: 20.0),
                Container(
                    height: 350,
                    child: StaggeredGridView.count(
                      crossAxisCount: 2,
                      crossAxisSpacing: 8.0,
                      mainAxisSpacing: 12.0,
                      primary: false,
                      padding: EdgeInsets.symmetric(horizontal: 8.0, vertical: 8.0),
                      children: List<int>.generate(soupImages.length, (i) => i).map((index) {
                        return Builder(
                          builder: (BuildContext context) {
                            return _buildItem(context, soupImages, soupCategories, index);
                          },
                        );
                      }).toList(),
                      staggeredTiles: [
                        StaggeredTile.extent(1, 150.0),
                        StaggeredTile.extent(1, 150.0),
                        StaggeredTile.extent(2, 150.0),
                      ],
                    ),
                ),
                Padding(
                  padding: const EdgeInsets.only(left: 16.0, top: 16),
                  child: Center(
                    child: ClipPath(
                      clipper: RibbonClipper(offset: 20),
                      child: Container(
                        height: MediaQuery.of(context).size.height * 3 / 50,
                        width: MediaQuery.of(context).size.width * 8 / 16  ,
                        color: Color(0xFF852196),
                        child: Center(
                            child: Text(
                              "Desert",
                              style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 20
                              ),
                            )
                        ),
                      ),
                    ),
                  ),
                ),
                SizedBox(height: 20.0),
                Container(
                    height: 350,
                    child: StaggeredGridView.count(
                      crossAxisCount: 2,
                      crossAxisSpacing: 8.0,
                      mainAxisSpacing: 12.0,
                      primary: false,
                      padding: EdgeInsets.symmetric(horizontal: 8.0, vertical: 8.0),
                      children: List<int>.generate(desertImages.length, (i) => i).map((index) {
                        return Builder(
                          builder: (BuildContext context) {
                            return _buildItem(context, desertImages, desertCategories, index);
                          },
                        );
                      }).toList(),
                      staggeredTiles: [
                        StaggeredTile.extent(1, 150.0),
                        StaggeredTile.extent(1, 150.0),
                        StaggeredTile.extent(1, 150.0),
                        StaggeredTile.extent(1, 150.0),
                      ],
                    ),
                ),
              ],
            ),
          ),
        )
      ],
    );
  }


  Widget waveWidget(){
    return Stack(
      children: <Widget>[
        ClipPath(
          clipper: WaveClipper(),
          child: Container(
            height: 350,
            decoration: BoxDecoration(
                gradient: LinearGradient(
                    colors: [Color(0xfffb53c6), Color(0xffb91d73)]
                ),
                borderRadius: BorderRadius.only(topRight: Radius.circular(20), topLeft: Radius.circular(20))
            ),
            child: Column(
              children: <Widget>[
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 32),
                  child: Material(
                    elevation: 5.0,
                    borderRadius: BorderRadius.all(Radius.circular(30)),
                    child: Container(
                      width: MediaQuery.of(context).size.width,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
  _familyDialog(BuildContext context) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return FamilyDialog();
        });
  }
  _profileDialog(BuildContext context) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return ProfileDialog();
        });
  }
  DateTime currentBackPressTime;
  @override
  Widget build(BuildContext context) {
    var screenHeight = MediaQuery.of(context).size.height;
    var screenWidth = MediaQuery.of(context).size.width;

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
                    borderRadius: BorderRadius.only(
                        bottomLeft: Radius.circular(20.0),
                        bottomRight: Radius.circular(20.0)),
                    child: Container(
                      color: Color(0xFFFC2343),
                      child: Padding(
                        padding:
                            EdgeInsets.fromLTRB(10, statusBarHeight, 10, 5),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: <Widget>[
                            IconButton(
                              icon: Icon(Icons.menu),
                              color: Colors.white,
                              onPressed: () {
                                _key.currentState.openDrawer();
                              },
                            ),
                            Center(
                              child: Text(
                                "Food Step",
                                style: new TextStyle(
                                  color: Colors.white,
                                  fontSize: 20.0,
                                  fontWeight: FontWeight.w600,
                                  fontStyle: FontStyle.normal,
                                ),
                              ),
                            ),
                            IconButton(
                              icon: Icon(Icons.navigate_before),
                              color: Colors.white,
                              onPressed: () {
                                Navigator.pop(context);
                              },
                            )
                          ],
                        ),
                      ),
                    ),
                  ),
                  functionButtons(),
//                  stepWidgets()
                  Expanded(
                    child: Stack(
                      children: <Widget>[
                        waveWidget(),
                        stepWidgets()
                      ],
                    ),
                  ),

                ],
              ),
            ),
          ),
          floatingActionButton: FloatingActionButton(
            heroTag: "orderstep_floatingactionbutton",
            child: Icon(Icons.done_all),
            onPressed: (){
              showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    return BeautifulAlertDialog(msg: "Do all Customers have done order?", path: '/OrderSummaryPage',);
                  }
              );
            },
          ),
        ),
        onWillPop: () {
          Navigator.pop(context);
          return;
        });
  }
}
