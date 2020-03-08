import 'package:OurMenu/resources/assets.dart';
import 'package:flutter/material.dart';
import 'package:OurMenu/pages/orderstep/gtypography.dart';
import 'package:OurMenu/widgets/network_image.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class FamilyOrderListItem extends StatelessWidget {
  const FamilyOrderListItem({
    Key key,
    @required this.family,
    @required this.orderinfo,
    @required this.image,
    this.price,
  }) : super(key: key);

  final String family;
  final String orderinfo;
  final String image;
  final double price;

  @override
  Widget build(BuildContext context) {
    return Card(
      color: Color(0xFFF0FFC1),
      child: Row(
        children: <Widget>[
          const SizedBox(width: 10.0),
          Container(
              height: 80.0,
              width: 80.0,
              child: ClipRRect(
                borderRadius: BorderRadius.all(Radius.circular(10)),
                child: Icon(
                  FontAwesomeIcons.users,
                  size: 50.0,
                  color: Colors.blue,
                )
              )
          ),
          const SizedBox(width: 30.0),
          Expanded(
            child: Container(
              height: 100.0,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  SizedBox(height: 20.0,),
                  new Text(
                    family,
                    style: TextStyle(
                      color: Color(0xFF3AB5AB),
                      fontSize: 20.0,
                    ),
                  ),
                  SizedBox(height: 10.0,),
                  new Text(
                    orderinfo,
                    style: TextStyle(
                      color: Color(0xFF82C91E),
                      fontSize: 17.0,
                    ),
                  )
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
