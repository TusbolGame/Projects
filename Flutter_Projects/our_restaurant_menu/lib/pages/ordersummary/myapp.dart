import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:tree_view/tree_view.dart';

class TestPage extends StatefulWidget {
  @override
  _TestPageState createState() => _TestPageState();
}

class _TestPageState extends State<TestPage> {
  String responseBody =
      '{ "id": 0,"name": "A","children": [{  "id": 1, "name": "Aa","children": [{"id": 2,"name": "Aa1","children": null}]},{ "id": 3, "name": "Ab","children": [{"id": 4,"name": "Ab1","children": null},{"id": 5,"name": "Ab2","children": null}]}]}';

  @override
  Widget build(BuildContext context) {
    Map mapBody = jsonDecode(responseBody);

    return SafeArea(
      child: Scaffold(
        body: printGroupTree(
          mapBody,
        ),
      ),
    );
  }

  Widget printGroupTree(
    Map group, {
    double level = 0,
  }) {
    if (group['children'] != null) {
      List<Widget> subGroups = List<Widget>();

      for (Map subGroup in group['children']) {
        subGroups.add(
          printGroupTree(
            subGroup,
            level: level + 1,
          ),
        );
      }

      return Parent(
        parent: _card(
          group['name'],
          level * 20,
        ),
        childList: ChildList(
          children: subGroups,
        ),
      );
    } else {
      return _card(
        group['name'],
        level * 20,
      );
    }
  }

  Widget _card(
    String groupName,
    double leftPadding,
  ) {
    return Container(
      padding: EdgeInsets.only(
        left: leftPadding + 5,
        right: 20,
      ),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(50.0),
      ),
      height: 100,
      child: Row(
        children: <Widget>[
          Container(
            width: 250,
            child: Row(
              children: <Widget>[
                Container(
                  height: 70,
                  width: 70,
                  decoration: BoxDecoration(
                    shape: BoxShape.rectangle,
                    image: DecorationImage(
                      fit: BoxFit.fill,
                      image: NetworkImage(
                        'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a6/Rubik%27s_cube.svg/220px-Rubik%27s_cube.svg.png',
                      ),
                    ),
                  ),
                ),
                SizedBox(
                  width: 10,
                ),
                Flexible(
                  child: Text(
                    'SomeText',
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            child: SizedBox(),
          ),
          InkWell(
            //TODO:Empty method here
            onTap: () {},
            child: Icon(
              Icons.group_add,
              size: 40,
            ),
          )
        ],
      ),
    );
  }
}
