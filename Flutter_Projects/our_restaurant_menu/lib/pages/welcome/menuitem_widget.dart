import 'package:OurMenu/pages/orderstep/orderstep.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';

class MenuItemPage extends StatefulWidget {
  @override
  _MenuItemPageState createState() => new _MenuItemPageState();
}

var menuItems = [
  "Food and Drink",
  "All Products",
  "Drinks",
  "Coffee",
  "Low Fat",
  "Today Menu"
];
var menuImages = [
  'assets/welcome/food_drink.png',
  'assets/welcome/all_products.png',
  'assets/welcome/drinks.png',
  'assets/welcome/coffee_menu.png',
  'assets/welcome/low_fat.png',
  'assets/welcome/today_menu.png',
];

class _MenuItemPageState extends State<MenuItemPage> {

  printClick(String item) {
      Navigator.push(context, MaterialPageRoute(builder: (context) => OrderStepPage()),);
  }

  showToast(String msg, Color color) {
    Fluttertoast.showToast(
        msg: msg,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: color,
        textColor: Colors.white,
        fontSize: 16.0
    );
  }

  @override
  Widget build(BuildContext context) {
    return new Expanded(
        child: ClipRRect(
          borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20), topRight: Radius.circular(20)),
          child: Stack(
            children: <Widget>[
              Image.asset(
                  "assets/images/menu_back.jpg",
                  fit: BoxFit.cover,
                height: double.infinity,
              ),
              Column(
                children: <Widget>[
                  Expanded(
                      child: Padding(
                        padding: EdgeInsets.only(bottom: 0),
                        child: GridView.count(
                          crossAxisCount: 2,
                          children: List.generate(menuImages.length, (index) {
                            return Center(
                                child: Padding(
                                  padding: EdgeInsets.only(top: 0, bottom: 20),
                                  child: ClipRRect(
                                    borderRadius: BorderRadius.all(
                                        Radius.circular(20)
                                    ),
                                    child: new GestureDetector(
                                      onTap: () => printClick(menuItems[index]),
                                      child: Container(
                                          /*decoration: BoxDecoration(
                                              border: Border.all(
                                                color: Color(0xFFEA4829),
                                                width: 5
                                              ),
                                              borderRadius: BorderRadius.all(
                                                  Radius.circular(20)
                                              ),
                                          ),*/
                                          child: Image.asset(
                                            menuImages[index],
                                            fit: BoxFit.cover,
                                          )
                                      ),
                                    ),
                                  ),
                                )
                            );
                          }),
                        ),
                      )
                  )
                ],
              ),
            ],
          ),
        )
    );
  }
}
