
import 'package:OurMenu/pages/categories/gtypography.dart';
import 'package:OurMenu/widgets/network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class FoodDetailDialog extends StatelessWidget{

  var foodData;
  FoodDetailDialog({this.foodData});

  @override
  Widget build(BuildContext context) {

    Widget _buildItemCard() {
      return Stack(
        children: <Widget>[
          Card(
            color: Color(0xFFF3D5D7),
            margin: EdgeInsets.only(top: 20.0, left: 20.0, right: 20.0),
            child: Container(
              padding: EdgeInsets.all(20.0),
              child: Column(
                children: <Widget>[
                  Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: <Widget>[
                      IconButton(
                        onPressed: (){},
                        icon: Icon(
                            Icons.favorite,
                            color: Colors.red,
                        ),
                      )
                    ],
                  ),
                  Container(
                    width: MediaQuery.of(context).size.width,
                    child: PNetworkImage(foodData[3], height: 200,),
                  ),
                  SizedBox(height: 10.0,),
                  GroceryTitle(text:foodData[0]),
                  SizedBox(height: 5.0,),
                  GrocerySubtitle(text: foodData[2], color: Color(0xFF00A99D),)
                ],
              ),
            ),
          ),
        ],
      );
    }
    Widget _buildFoodContent(int index) {
      return Column(
        children: <Widget>[
          Expanded(
            child: ListView(
              children: <Widget>[
                _buildItemCard(),
                Container(
                    padding: EdgeInsets.all(30.0),
                    child: GrocerySubtitle(text: foodData[1])),
                /*Container(padding: EdgeInsets.only(left: 20.0, bottom: 10.0),child: GroceryTitle(text: "Related Items")),
                GroceryListItemTwo(title: "Mixed Grilled Meats Board", image: brocoli, subtitle: "1"),
                GroceryListItemTwo(title: "Romanian Village Salmon", image: cabbage, subtitle: "1"),*/
              ],
            ),
          ),
          Row(
            children: <Widget>[
              Expanded(
                child: Padding(
                  padding: EdgeInsets.only(right: 5.0),
                  child: Container(
                    color: Colors.green,
                    child: FlatButton(
                      color: Colors.green,
                      onPressed: (){
                        Navigator.pop(context);
                      },
                      child: Text("Add to Cart"),
                    ),
                  ),
                ),
              ),
              Expanded(
                child: Padding(
                  padding: EdgeInsets.only(left: 5.0),
                  child: Container(
                    color: Colors.green,
                    child: FlatButton(
                      color: Colors.green,
                      onPressed: (){
                        Navigator.pop(context);
                      },
                      child: Text("Cancel"),
                    ),
                  ),
                ),
              )
            ],
          )
        ],
      );
    }
    
    return Center(
      child: SizedBox(
        height: MediaQuery.of(context).size.height,
        child: Dialog(
          backgroundColor: Color(0xFFD7F3D5),
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Stack(
              children: <Widget>[
                Column(
                  children: <Widget>[
                    Expanded(
                      child: _buildFoodContent(0),
                    )
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}