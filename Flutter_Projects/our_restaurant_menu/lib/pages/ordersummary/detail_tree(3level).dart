import 'dart:convert';

import 'package:OurMenu/pages/ordersummary/familyorderlistitem.dart';
import 'package:OurMenu/pages/ordersummary/orderparser.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:tree_view/tree_view.dart';

final GlobalKey<ScaffoldState> _key = GlobalKey<ScaffoldState>();

class DetailTreePage extends StatelessWidget {

  var jsonData = '{"order": [{"devicename": "Group1","customers": [{"customername": "Member1","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member2","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member3","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member4","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]}]},{"devicename": "Group2","customers": [{"customername": "Member1","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member2","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member3","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member4","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]}]},{"devicename": "Group3","customers": [{"customername": "Member1","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member2","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member3","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]},{"customername": "Member4","foodsteps": [{"steptitle": "Apetitizer","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "MainMeal","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Soup","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]},{"steptitle": "Desert","stepitem": [{"itemname": "AAA","productitem": [{"count": 3,"price": 3}]},{"itemname": "BBB","productitem": [{"count": 1,"price": 5}]},{"itemname": "CCC","productitem": [{"count": 2,"price": 2}]}]}]}]}]}';

  @override
  Widget build(BuildContext context) {
    var screenHeight = MediaQuery.of(context).size.height;
    var screenWidth = MediaQuery.of(context).size.width;
    double statusBarHeight = MediaQuery.of(context).padding.top;

    List<Device> groups;
    List<dynamic> devices = jsonDecode(jsonData)["order"] as List<dynamic>;
    groups = devices.map<Device>((json) => Device.fromJson(json)).toList();

    return Container(
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
            Expanded(
              child: Stack(
                children: <Widget>[
                  Container(
                    height: MediaQuery.of(context).size.height,
                  ),
                  SingleChildScrollView(
                    child: Center(
                      child: Container(
                        height: MediaQuery.of(context).size.height,
                        width:
                        MediaQuery.of(context).size.width * 0.9,
                        child: TreeView(
                          hasScrollBar: true,
                          parentList: List<int>.generate(groups.length, (i) => i).map((group_index) {
                            var groupData = groups[group_index];
                            var customers = groupData.customers;
                            return Parent(
                              parent: FamilyOrderListItem(
                                image: 'assets/images/family.png',
                                family: groupData.deviceName,
                                orderinfo: "3 products / \$15",
                              ),
                              childList: ChildList(
                                children:List<int>.generate(customers.length, (i) => i).map((customer_index) {
                                  var customername = customers[customer_index].customername;
                                  var foodsteps = customers[customer_index].foodsteps;
                                  return Parent(
                                    parent: Padding(
                                      padding: EdgeInsets.only(
                                          left: 20.0),
                                      child: Card(
                                        child: ListTile(
                                          title: Text("Item 2"),
                                          leading: Icon(
                                              Icons.cloud_circle),
                                        ),
                                      ),
                                    ),
                                    childList: ChildList(
                                      children: List<int>.generate(foodsteps.length, (i) => i).map((foodstep_index) {
                                        var foodsteptitle = foodsteps[foodstep_index].steptitle;
                                        var stepitems = foodsteps[foodstep_index].stepitems;
                                        return Padding(
                                          padding: EdgeInsets.only(
                                              left: 40.0),
                                          child: Card(
                                            child: ListTile(
                                              title:
                                              Text("Item 2-1"),
                                              leading: Icon(Icons
                                                  .cloud_circle),
                                            ),
                                          ),
                                        );
                                      }).toList(),

                                    ),
                                  );
                                }).toList(),
                              ),
                            );
                          }).toList(),
                        ),
                      ),
                    ),
                  )
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
