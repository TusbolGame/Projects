import 'dart:convert';
import 'package:OurMenu/pages/ordersummary/familyorderlistitem.dart';
import 'package:OurMenu/pages/ordersummary/orderparser.dart';
import 'package:OurMenu/widgets/waveclip.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:tree_view/tree_view.dart';

final GlobalKey<ScaffoldState> _key = GlobalKey<ScaffoldState>();

class DetailTreePage extends StatelessWidget {
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

    List<String> steps;
    final _steps = json.decode(jsonFoodSteps);
    steps = new List<String>.from(_steps["foodstep"]);

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
                          "Order Detail",
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

                        for(var customer in groupData.customers){
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
                                customers.length, (i) => i)
                                .map((customer_index) {
                              var customername =
                                  customers[customer_index].customername;
                              var foodsteps =
                                  customers[customer_index].foodsteps;

                              var customer_totalProducts = 0;
                              var customer_totalPrice = 0.0;
                              for(var stepitems in foodsteps){
                                for(var stepitem in stepitems.stepitems){
                                  customer_totalProducts = customer_totalProducts + stepitem.product.count;
                                  customer_totalPrice = customer_totalPrice + stepitem.product.count * stepitem.product.price;
                                }
                              }

                              return Parent(
                                parent: Padding(
                                  padding: EdgeInsets.only(left: 20.0),
                                  child: Card(
                                    child: ListTile(
                                      title: Text(customername + " (" + customer_totalProducts.toString() + " products"+ "/\$" + customer_totalPrice.toString() + ")"),
                                      leading: Icon(Icons.person),
                                    ),
                                  ),
                                ),
                                childList: ChildList(
                                  children: List<int>.generate(
                                      foodsteps.length, (i) => i)
                                      .map((foodstep_index) {
                                    var foodsteptitle =
                                        foodsteps[foodstep_index].steptitle;
                                    var stepitems =
                                        foodsteps[foodstep_index].stepitems;

                                    var step_totalProducts = 0;
                                    var step_totalPrice = 0.0;

                                    for(var stepitem in stepitems){
                                      step_totalProducts = step_totalProducts + stepitem.product.count;
                                      step_totalPrice = step_totalPrice + stepitem.product.count * stepitem.product.price;
                                    }

                                    return Parent(
                                        parent: Padding(
                                          padding:
                                          EdgeInsets.only(left: 40.0),
                                          child: Card(
                                            child: ListTile(
                                              title: Text(foodsteptitle + " (" + step_totalProducts.toString() + " products"+ "/\$" + step_totalPrice.toString() + ")"),
                                              leading:
                                              Icon(Icons.cloud_circle),
                                            ),
                                          ),
                                        ),
                                        childList: ChildList(
                                          children: List<int>.generate(
                                              stepitems.length,
                                                  (i) => i)
                                              .map((stemitem_index) {
                                            var producttitle =
                                                stepitems[stemitem_index]
                                                    .itemname;
                                            Product product =
                                                stepitems[stemitem_index]
                                                    .product;
                                            return Padding(
                                              padding: EdgeInsets.only(
                                                  left: 60.0),
                                              child: Card(
                                                child: ListTile(
                                                  title: Text(producttitle +
                                                      " (" +
                                                      product.count
                                                          .toString() +
                                                      " products/" +
                                                      "\$" +
                                                      (product.price *
                                                          product.count)
                                                          .toString() + ")"),
                                                  leading: Icon(
                                                      Icons.cloud_circle),
                                                ),
                                              ),
                                            );
                                          }).toList(),
                                        ));
                                  }).toList(),
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
