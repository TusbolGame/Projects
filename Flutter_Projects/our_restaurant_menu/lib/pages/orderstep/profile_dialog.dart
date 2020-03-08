import 'package:OurMenu/pages/orderstep/favor_list.dart';
import 'package:OurMenu/resources/assets.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ProfileDialog extends StatelessWidget {
  final image = images[0];
  final TextStyle subtitle = TextStyle(fontSize: 12.0, color: Colors.grey);
  final TextStyle label = TextStyle(fontSize: 14.0, color: Colors.grey);

  Widget _buildTotals(BuildContext context) {
    return Container(
        padding: EdgeInsets.only(left: 20.0, right: 20.0),
        child: RaisedButton(
          color: Colors.green,
          onPressed: (){
            Navigator.pop(context);
          },
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: <Widget>[
              Text("Close", style: TextStyle(color: Colors.white)),
            ],
          ),
        )
    );
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: SizedBox(
        height: MediaQuery.of(context).size.height,
        child: Dialog(
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: <Widget>[
                Expanded(
                  child: ListView(
                    padding: EdgeInsets.only(top: 10.0, bottom: 10.0),
                    children: <Widget>[
                      GroceryListItemThree(
                        image: "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS-d4Ca7XZW8_2fvVX5yB0_7wmSGmDfryucl0DAXN8Snba3ZhEs",
                        subtitle: "\$2",
                        title: "Strawberry",
                      ),
                      GroceryListItemThree(
                        image: "https://dinnerthendessert.com/wp-content/uploads/2017/02/Mongolian-Beef-3.jpg",
                        subtitle: "\$5",
                        title: "Beef meet food",
                      ),
                    ],
                  ),
                ),
                SizedBox(height: 10.0,),
                _buildTotals(context)
              ],
            ),
          ),
        ),
      ),
    );
  }
}