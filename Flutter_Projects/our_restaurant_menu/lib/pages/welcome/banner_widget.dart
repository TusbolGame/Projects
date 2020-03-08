import 'package:OurMenu/widgets/network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_swiper/flutter_swiper.dart';

class WelcomeBannerPage extends StatefulWidget {
  @override
  _WelcomeBannerPageState createState() => new _WelcomeBannerPageState();
}

var bannerItems = [
  "Pancake",
  "Meal",
  "Fries",
  "French Fries",
  "Cup Cake",
  "Breakfast",
  "Burger",
  "Avocado",
  "Cherry",
  "Cake"
];
var bannerImages = [
  'https://media.eggs.ca/assets/RecipePhotos/_resampled/FillWyIxMjgwIiwiNzIwIl0/Fluffy-Pancakes-New-CMS.jpg',
  'https://www.diabetes.org/sites/default/files/styles/paragraph_50_50/public/2019-06/hearthealthy-desktop-5050-min.jpg',
  'https://cms.splendidtable.org/sites/default/files/styles/w2000/public/french-fries.jpg?itok=FS-YwUYH',
  'http://www.sakinahalalgrill.com/wp-content/uploads/2018/04/french-fries.jpg',
  'https://i2.wp.com/www.biggerbolderbaking.com/wp-content/uploads/2017/09/1C5A0996.jpg?w=1214&ssl=1',
  'https://d9hyo6bif16lx.cloudfront.net/live/img/production/detail/menu/breakfast_breakfast-classics_big-two-do-breakfast.jpg',
  'https://www.carnival.com/~/media/CCLUS/Images/Recipe/landing-pages/guy-fieri-burger-hero.jpg',
  'https://www.bbcgoodfood.com/sites/default/files/guide/guide-image/2018/09/avocados-title.jpg',
  'https://d3eh3svpl1busq.cloudfront.net/AqOsDSWHlMyaklhBnvzkgRREKBfEHKZc/assets/static/source/rev-d88fb7b/wp-content/uploads/2019/04/fullsizeoutput_a85b-735x490.jpeg',
  'https://www.bakemag.com/ext/resources/images/2019/6/HispanicCakeDesign.jpg?1561037530'
];

class _WelcomeBannerPageState extends State<WelcomeBannerPage> {
  @override
  Widget build(BuildContext context) {
    var screenWidth = MediaQuery.of(context).size.width;

    return new Container(
        color: Colors.white,
        width: screenWidth,
        height: screenWidth * 9 / 16,
        child: new ClipRRect(
          borderRadius: BorderRadius.only(topLeft: Radius.circular(20.0), topRight: Radius.circular(20.0)),
          child: Padding(
            padding: EdgeInsets.only(top: 20, bottom: 10),
            child: Swiper(
              itemBuilder: (BuildContext context, int index) {
                return new Container(
                  child: Stack(
                    fit: StackFit.expand,
                    children: <Widget>[
                      ClipRRect(
                        borderRadius: BorderRadius.all(Radius.circular(20.0)),
                        child: PNetworkImage(
                          bannerImages[index],
                          fit: BoxFit.fill,
                        ),
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
                              bannerItems[index],
                              style: TextStyle(
                                  fontSize: 25.0, color: Colors.white),
                            ),
                          ),
                        ],
                      )
                    ],
                  ),
                );
              },
              itemCount: 10,
              viewportFraction: 0.8,
              scale: 0.9,
            ),
          ),
        ));
  }
}
