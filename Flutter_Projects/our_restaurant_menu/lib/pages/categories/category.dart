import 'dart:io';
import 'package:OurMenu/pages/categories/category_card.dart';
import 'package:OurMenu/pages/categories/detail_dialog.dart';
import 'package:OurMenu/pages/categories/glistitem.dart';
import 'package:OurMenu/pages/categories/gtypography.dart';
import 'package:OurMenu/resources/assets.dart';
import 'package:OurMenu/widgets/beautiful_alert_dialog.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:OurMenu/widgets/common_widget.dart' as Common;
import 'package:flutter_custom_clippers/flutter_custom_clippers.dart';
import 'package:OurMenu/widgets/network_image.dart';
import 'package:flutter_swiper/flutter_swiper.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

final GlobalKey<ScaffoldState> _key = GlobalKey<ScaffoldState>();

class CategoryPage extends StatefulWidget {
  @override
  _CategoryPageState createState() => new _CategoryPageState();
}

class _CategoryPageState extends State<CategoryPage> {
  final Color primary = Color(0xff2ABF76);
  final Color active = Color(0xffDAFFED);

  @override
  void initState() {
    super.initState();
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
      onTap: () {
        Navigator.push(
            context, MaterialPageRoute(builder: (_) => CategoryCard()));
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
                  borderRadius: BorderRadius.all(Radius.circular(20.0)),
                  gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [Colors.transparent, Color(0xFF2B2B2B)])),
            ),
            Column(
              mainAxisAlignment: MainAxisAlignment.end,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Padding(
                  padding: EdgeInsets.fromLTRB(10, 0, 0, 10),
                  child: Text(
                    categories[index] + "\n3/\$15",
                    style: TextStyle(fontSize: 20.0, color: Colors.white),
                  ),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }

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

    var categoryItems = [
      [
        'Soup',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/swqoo0hez3lf9vcmams1',
        4
      ],
      [
        'Chicken',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/m535dhmbxd3amotsmo43',
        3
      ],
      [
        'Pork',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/xn0u0i12frszvwqr8fni',
        4
      ],
      [
        'Beef',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/v3etxdf6s8jiqglasazd',
        5
      ],
      [
        'Desert',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/hyamrn1r5knn05kbreky',
        2
      ],
    ];
    var categoryData = [
      [
        'APPETIZER "Mititei" Romanian Charbroiled Sausages',
        '3 homemade beef and pork skinless sausages, fried polenta and mixed greens.',
        '\$13.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/m535dhmbxd3amotsmo43',
        2
      ],
      [
        'Bruschetta',
        'Grilled bread topped with diced tomatoes, olive oil, garlic, herbs and Parmesan cheese.',
        '\$13.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/ewbvc01ivs0h0jtrgdvo',
        3
      ],
      [
        '"Mamaliga" Soft Polenta',
        'Topped with creme fraiche and shredded feta cheese.',
        '\$7.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/fx0rrasopwam671vzpei',
        1
      ],
      [
        '"Perisoare" Meatball Soup',
        'Savory tomato broth, homemade meatballs and vegetables.',
        '\$8.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/swqoo0hez3lf9vcmams1',
        5
      ],
      [
        'House Salad',
        'Mixed greens, tomatoes, cucumbers, red onions and bell peppers with house vinaigrette.',
        '\$6.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/kybf1bvby9csh14rxu6o',
        2
      ],
      [
        'Roasted Beet Salad',
        'Mixed greens, roasted beets, goat cheese, clementines and caramelized walnuts with house vinaigrette.',
        '\$10.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/ew8ixzn28kacmfcpq2fw',
        3
      ],
      [
        '"Ardei Copti" Roasted Red Peppers Salad',
        'Homemade roasted red peppers, onions, cucumbers and olives in garlic vinaigrette.',
        '\$9.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/s3ai5hmduagut1bfp8io',
        5
      ],
      [
        'Bucharest Signature Braised Short Rib Goulash',
        'Slow-braised short ribs. Served over gnocchi and topped with a picante paprika tomato sauce, pearl onions and green beans.',
        '\$27.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/enguicupaoa9wlrmjmsr',
        4
      ],
      [
        'Romanian Grilled Skirt Steak',
        'Topped with sautéed onions, peppers, mushrooms and red wine demi-glaze alongside roasted red potatoes and broccoli.',
        '\$27.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/v3etxdf6s8jiqglasazd',
        4
      ],
      [
        'Roasted Chicken Paprikash',
        'Bone-in 1/2 roasted chicken topped with a paprika, sour cream gravy, green beans and pearl onions and served with mamaliga.',
        '\$25.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/rfr3uxis0gx9k6ablv8a',
        3
      ],
      [
        'Pork Schnitzel',
        'Lightly-breaded fried meat. Served with horseradish spetzel and prepared with bacon, spinach and red pepper coulis.',
        '\$23.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/ehgfp9g5bac68swsdqqi',
        5
      ],
      [
        'Sarmale Stuffed Cabbage Dinner',
        '5 pickled cabbage rolls, stuffed with ground pork and rice in a tomato sauce served with mamaliga.',
        '\$21.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/rqswhani1p0tasimhztu',
        5
      ],
      [
        'DINNER "Mititei" Romanian Charbroiled Sausages Dinner',
        '5 homemade beef and pork skinless sausages. Served with homemade fries or roasted potatoes.',
        '\$17.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/jhxcfrj8ueyr8n4allzy',
        4
      ],
      [
        'Vegetarian Volcano Goulash',
        'Grilled eggplant, layered with sauteed cabbage, bell peppers and spinach, over couscous and topped with rustic tomato sauce.',
        '\$20.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/xn0u0i12frszvwqr8fni',
        2
      ],
      [
        'Romanian Village Salmon',
        'Seared salmon filet, red pepper coulis on the side, over a bed of braised leaks, spinach and red peppers and couscous.',
        '\$25.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/lewwsmgefhfmymfltmi0',
        5
      ],
      [
        'Mixed Grilled Meats Board',
        '2 mititei, grilled pork shoulder blade, 2 stuffed cabbage and grilled chicken breast. Served with roasted potato or homemade fries.',
        '\$33.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/hggkpfeqragqk1d22wiu',
        3
      ],
      [
        'Dobos Cake',
        'Chocolate butter cream, caramel and toasted almonds.',
        '\$9.00',
        'https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/hyamrn1r5knn05kbreky',
        4
      ]
    ];

    Widget _categoryCard() {
      return Column(
        children:
            List<int>.generate(categoryItems.length, (i) => i).map((index) {
          return new Card(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Container(
                    width: MediaQuery.of(context).size.width,
                    height: MediaQuery.of(context).size.width * 9 / 16,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.all(
                        Radius.circular(10.0),
                      ),
                      image: DecorationImage(
                          fit: BoxFit.cover,
                          image: CachedNetworkImageProvider(
                              categoryItems[index][1])),
                    ),
                  ),
                ),
                ExpansionTile(
                  title: Row(
                    children: <Widget>[
                      Text(
                        categoryItems[index][0],
                        style: TextStyle(
                            fontSize: 25.0,
                            fontWeight: FontWeight.bold,
                            color: Color(0xFF00A99D)),
                      ),
                      SizedBox(
                        width: 10,
                      ),
                      Row(
                        mainAxisSize: MainAxisSize.min,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: List<int>.generate(
                            categoryItems[index][2], (i) => i).map((index) {
                          return Icon(
                            Icons.star,
                            color: Colors.yellow,
                            size: 25,
                          );
                        }).toList(),
                      )
                    ],
                  ),
                  children: List<int>.generate(categoryData.length, (i) => i)
                      .map((index) {
                    return GestureDetector(
                      onTap: () {
                        showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return FoodDetailDialog(
                                foodData: categoryData[index],
                              );
                            });
                      },
                      child: Card(
                        child: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: <Widget>[
                            Expanded(
                              flex: 2,
                              child: Column(
                                mainAxisSize: MainAxisSize.max,
                                children: <Widget>[
                                  Container(
                                    width: MediaQuery.of(context).size.width *
                                        2 /
                                        3,
                                    height:
                                        MediaQuery.of(context).size.width / 15,
                                    child: Padding(
                                      padding: EdgeInsets.fromLTRB(
                                          10.0, 2.0, 2.0, 1.0),
                                      child: Align(
                                        alignment: Alignment.centerLeft,
                                        child: Text(
                                          categoryData[index][0],
                                          overflow: TextOverflow.ellipsis,
                                          maxLines: 10,
                                          style: TextStyle(
                                              color: Color(0xFF00A99D),
                                              fontSize: 20.0),
                                        ),
                                      ),
                                    ),
                                  ),
                                  Container(
                                    width: MediaQuery.of(context).size.width *
                                        2 /
                                        3,
                                    height:
                                        MediaQuery.of(context).size.width / 5,
                                    child: Padding(
                                      padding: EdgeInsets.fromLTRB(
                                          10.0, 1.0, 2.0, 1.0),
                                      child: Align(
                                        alignment: Alignment.centerLeft,
                                        child: Text(
                                          categoryData[index][1],
                                          overflow: TextOverflow.ellipsis,
                                          maxLines: 10,
                                        ),
                                      ),
                                    ),
                                  ),
                                  Container(
                                    width: MediaQuery.of(context).size.width *
                                        2 /
                                        3,
                                    height:
                                        MediaQuery.of(context).size.width / 15,
                                    child: Padding(
                                      padding: EdgeInsets.fromLTRB(
                                          10.0, 1.0, 2.0, 2.0),
                                      child: Align(
                                        alignment: Alignment.centerLeft,
                                        child: Row(
                                          children: <Widget>[
                                            Text(
                                              categoryData[index][2],
                                              overflow: TextOverflow.ellipsis,
                                              maxLines: 3,
                                              style: TextStyle(
                                                fontSize: 20.0,
                                                color: Color(0xFF636466),
                                              ),
                                            ),
                                            SizedBox(
                                              width: 10,
                                            ),
                                            Row(
                                              mainAxisSize: MainAxisSize.min,
                                              crossAxisAlignment:
                                                  CrossAxisAlignment.center,
                                              children: List<int>.generate(
                                                  categoryData[index][4],
                                                  (i) => i).map((index) {
                                                return Icon(
                                                  Icons.star,
                                                  color: Colors.yellow,
                                                  size: 25,
                                                );
                                              }).toList(),
                                            )
                                          ],
                                        ),
                                      ),
                                    ),
                                  )
                                ],
                              ),
                            ),
                            Expanded(
                              flex: 1,
                              child: Container(
                                width: MediaQuery.of(context).size.width / 3,
                                height: MediaQuery.of(context).size.width / 3,
                                decoration: BoxDecoration(
                                  borderRadius: BorderRadius.only(
                                      topRight: Radius.circular(10.0),
                                      bottomRight: Radius.circular(10.0)),
                                  image: DecorationImage(
                                      fit: BoxFit.cover,
                                      image: CachedNetworkImageProvider(
                                          categoryData[index][3])),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  }).toList(),
                ),
              ],
            ),
          );
        }).toList(),
      );
    }

    var highlightImages = [
      "https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/m535dhmbxd3amotsmo43",
      "https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/ewbvc01ivs0h0jtrgdvo",
      "https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/fx0rrasopwam671vzpei",
      "https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/swqoo0hez3lf9vcmams1",
      "https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/kybf1bvby9csh14rxu6o",
      "https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/ew8ixzn28kacmfcpq2fw",
      "https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/s3ai5hmduagut1bfp8io",
      "https://media-cdn.grubhub.com/image/upload/f_auto,fl_lossy,q_100,c_fill,w_376/enguicupaoa9wlrmjmsr",
    ];
    var highlightedTitles = [
      "Mititei",
      "Bruschetta",
      "Mamaliga",
      "Perisoare",
      "House Salad",
      "Roasted Beet Salad",
      "Ardei Copti",
      "Signature Braised Short Rib Goulash"
    ];
    Widget _highlighedProducts() {
      return Container(
        height: MediaQuery.of(context).size.width * 9 / 16,
        color: Colors.white,
        padding: EdgeInsets.all(16.0),
        child: Swiper(
          itemBuilder: (BuildContext context, int index) {
            return GestureDetector(
              onTap: () {
                var data = [
                  'Highlighted Food',
                  'This is Highlighted Food, so Liked or promoted, etc',
                  '\$14.00',
                  highlightImages[index]
                ];
                showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return FoodDetailDialog(
                        foodData: data,
                      );
                    });
              },
              child: Container(
                child: Stack(
                  fit: StackFit.expand,
                  children: <Widget>[
                    ClipRRect(
                      borderRadius: BorderRadius.all(Radius.circular(20.0)),
                      child: PNetworkImage(
                        highlightImages[index],
                        fit: BoxFit.cover,
                      ),
                    ),
                    Container(
                      decoration: BoxDecoration(
                          borderRadius: BorderRadius.all(Radius.circular(20.0)),
                          gradient: LinearGradient(
                              begin: Alignment.topCenter,
                              end: Alignment.bottomCenter,
                              colors: [Colors.transparent, Color(0xFF2B2B2B)])),
                    ),
                    Column(
                      mainAxisAlignment: MainAxisAlignment.end,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: <Widget>[
                        Padding(
                          padding: EdgeInsets.fromLTRB(10, 0, 0, 10),
                          child: Text(
                            highlightedTitles[index],
                            style: TextStyle(
                                fontSize: 25.0, color: Color(0xFF00E3D3)),
                          ),
                        ),
                        Row(
                          children: <Widget>[
                            SizedBox(
                              width: 10,
                            ),
                            Row(
                              mainAxisSize: MainAxisSize.min,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children:
                                  List<int>.generate(5, (i) => i).map((index) {
                                return Icon(
                                  Icons.star,
                                  color: Colors.yellow,
                                  size: 25,
                                );
                              }).toList(),
                            )
                          ],
                        )
                      ],
                    )
                  ],
                ),
              ),
            );
          },
          itemWidth: MediaQuery.of(context).size.width * 0.8,
          itemCount: highlightImages.length,
          layout: SwiperLayout.STACK,
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
                            Expanded(
                              flex: 1,
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: IconButton(
                                  icon: Icon(Icons.menu),
                                  color: Colors.white,
                                  onPressed: () {
                                    _key.currentState.openDrawer();
                                  },
                                ),
                              )
                            ),
                            Expanded(
                              flex: 5,
                              child: Align(
                                alignment: Alignment.center,
                                child: Center(
                                  child: Text(
                                    "Food Category",
                                    style: new TextStyle(
                                      color: Colors.white,
                                      fontSize: 20.0,
                                      fontWeight: FontWeight.w600,
                                      fontStyle: FontStyle.normal,
                                    ),
                                  ),
                                ),
                              ),
                            ),
                            Expanded(
                              flex: 1,
                              child: IconButton(
                                icon: Icon(Icons.navigate_before),
                                color: Colors.white,
                                onPressed: () {
                                  Navigator.pop(context);
                                },
                              )
                            )
                            /**/
                          ],
                        ),
                      ),
                    ),
                  ),
                  Expanded(
                    child: Stack(
                      children: <Widget>[
                        Container(
                          height: MediaQuery.of(context).size.height,
                        ),
                        SingleChildScrollView(
                          child: Column(
                            children: <Widget>[
                              SizedBox(
                                height: 30.0,
                              ),
                              _highlighedProducts(),
                              _categoryCard(),
                            ],
                          ),
                        )
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
          floatingActionButton: Padding(
            padding: EdgeInsets.only(bottom: 60.0),
            child: FloatingActionButton(
              heroTag: "category_floatingactionbutton",
              child: Icon(Icons.done_all),
              onPressed: (){
                showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return BeautifulAlertDialog(msg: "Do you have done order?", path: '/OrderStepPage',);
                    }
                );
              },
            ),
          ),
        ),
        onWillPop: () {
          Navigator.pop(context);
          return;
        });
  }
}
