import 'dart:convert';

import 'package:OurMenu/pages/ordersummary/familyorderlistitem.dart';
import 'package:OurMenu/pages/ordersummary/orderparser.dart';
import 'package:OurMenu/widgets/waveclip.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:tree_view/tree_view.dart';

final GlobalKey<ScaffoldState> _key = GlobalKey<ScaffoldState>();

class SummaryTreePage extends StatelessWidget {
  var jsonFoodSteps = '{"foodstep":["Apetitizer", "Main Meal", "Soup", "Desert"]}';
  var jsonData =
      '{"order": [{"devicename": "Group1","customers": [{"customername": "Member1","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member2","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member3","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member4","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]}]},{"devicename": "Group2","customers": [{"customername": "Member1","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member2","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member3","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member4","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]}]},{"devicename": "Group3","customers": [{"customername": "Member1","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member2","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member3","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member4","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]}]}]}';

  @override
  Widget build(BuildContext context) {
    var screenHeight = MediaQuery.of(context).size.height;
    var screenWidth = MediaQuery.of(context).size.width;
    double statusBarHeight = MediaQuery.of(context).padding.top;

    List<Device> groups;
    List<dynamic> devices = jsonDecode(jsonData)["order"] as List<dynamic>;
    groups = devices.map<Device>((json) => Device.fromJson(json)).toList();

    /*List<String> steps;
    final _steps = json.decode(jsonFoodSteps);
    steps = new List<String>.from(_steps["foodstep"]);*/

    return Container(
      height: screenHeight,
      width: screenWidth,
      child: SafeArea(
        top: false,
        bottom: true,
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
                  padding: EdgeInsets.fromLTRB(10, statusBarHeight, 10, 5),
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
                          "Order Summary",
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
            SizedBox(height: 30.0,),
            Expanded(
              child: Stack(
                children: <Widget>[
                  ClipPath(
                    clipper: WaveClipper2(),
                    child: Container(
                      child: Column(),
                      width: double.infinity,
                      height: 300,
                      decoration: BoxDecoration(
                          gradient: LinearGradient(
                              colors: [Color(0x22ff3a5a), Color(0x22fe494d)]),
                          borderRadius: BorderRadius.only(topRight: Radius.circular(20.0), topLeft: Radius.circular(20.0)),
                      ),
                    ),
                  ),
                  ClipPath(
                    clipper: WaveClipper3(),
                    child: Container(
                      child: Column(),
                      width: double.infinity,
                      height: 300,
                      decoration: BoxDecoration(
                          gradient: LinearGradient(
                              colors: [Color(0x44ff3a5a), Color(0x44fe494d)]),
                          borderRadius: BorderRadius.only(topRight: Radius.circular(20.0), topLeft: Radius.circular(20.0)),
                      ),
                    ),
                  ),

                  Container(
                    height: MediaQuery.of(context).size.height,
                    child: TreeView(
                      hasScrollBar: false,
                      parentList:
                      List<int>.generate(groups.length, (i) => i)
                          .map((group_index) {
                        var groupData = groups[group_index];
                        var customers = groupData.customers;
                        var totalProducts = 0;
                        var totalPrice = 0.0;


                        int stepCount = 0;

                        List<String> steps;
                        Map<String, String> stepMap = {};

                        for(var customer in groupData.customers){

                          if(customer.foodsteps.length > stepCount){
                            stepCount = customer.foodsteps.length;
                            steps = List<int>.generate(customer.foodsteps.length, (i) => i).map((__index) {
                              var step_title = customer.foodsteps[__index].steptitle;
                              int step_productcount = 0;
                              double step_price = 0;
                              for(var stepitem in customer.foodsteps[__index].stepitems){
                                step_productcount = step_productcount + stepitem.product.count;
                                step_price = step_price + stepitem.product.count * stepitem.product.price;
                              }
                              stepMap[step_title] = step_productcount.toString() + " products / \$" + step_price.toString();

                              return customer.foodsteps[__index].steptitle;
                            }).toList();
                          }

                          for(var foodstep in customer.foodsteps){
                            for(var step in foodstep.stepitems){
                              totalProducts = totalProducts + step.product.count;
                              totalPrice = totalPrice + step.product.count * step.product.price;
                            }
                          }
                        }

                        return Parent(
                          parent: FamilyOrderListItem(
                            image: 'assets/images/family.png',
                            family: groupData.deviceName,
                            orderinfo: totalProducts.toString() + " products / \$" + totalPrice.toString(),
                          ),
                          childList: ChildList(
                            children: List<int>.generate(
                                steps.length, (i) => i)
                                .map((step_index) {

                              String steptitle = steps[step_index];

                              return Parent(
                                parent: Padding(
                                  padding: EdgeInsets.only(left: 20.0),
                                  child: Card(
                                    child: ListTile(
                                      title: Text(steptitle + "(" + stepMap[steptitle] + ")"),
                                      leading: Icon(Icons.person),
                                    ),
                                  ),
                                ),
                                childList: ChildList(
                                  children: <Widget>[],
                                ),
                              );
                            }).toList(),
                          ),
                        );
                      }).toList(),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
