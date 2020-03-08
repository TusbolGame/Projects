import 'package:OurMenu/food/avocado.dart';
import 'package:OurMenu/food/cake.dart';
import 'package:OurMenu/food/fdhome.dart';
import 'package:OurMenu/food/recipe_list.dart';
import 'package:OurMenu/food/recipe_single.dart';
import 'package:OurMenu/pages/categories/category.dart';
import 'package:OurMenu/pages/orderstep/orderstep.dart';
import 'package:OurMenu/pages/ordersummary/ordersummarypage.dart';
import 'package:OurMenu/pages/qrscan/qr_scan.dart';
import 'package:OurMenu/pages/splash/spash.dart';
import 'package:OurMenu/pages/welcome/welcome.dart';
import 'package:OurMenu/resources/constants.dart';
import 'package:flutter/material.dart';
import 'package:barcode_scan/barcode_scan.dart';
import 'package:flutter/services.dart';
import 'package:sqflite/sqflite.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:splashscreen/splashscreen.dart';

void main() => runApp(OurMenu());

class OurMenu extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.portraitDown,
      DeviceOrientation.portraitUp,
    ]);
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: SplashPage(),
      routes: <String, WidgetBuilder>{
        '/WelcomePage': (BuildContext context) => new WelcomePage(),
        '/OrderStepPage': (BuildContext context) => new OrderStepPage(),
        '/CategoryPage': (BuildContext context) => new CategoryPage(),
        '/OrderSummaryPage': (BuildContext context) => new OrderSummaryPage(),
      },
    );
  }
}

