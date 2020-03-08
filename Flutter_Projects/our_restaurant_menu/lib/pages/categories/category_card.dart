import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

class CategoryCard extends StatelessWidget
{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        height: 130,
        child: Card(
          color: Color(0xFFF0FFC1),
          elevation: 10,
          child: Row(
            children: <Widget>[
              Padding(
                padding: EdgeInsets.all(10.0),
                child: GestureDetector(
                  onTap: () {

                  },
                  child: Container(
                    width: 100.0,
                    height: 100.0,
                    decoration: BoxDecoration(
                        color: Colors.red,
                        image: DecorationImage(
                            image: AssetImage('assets/images/cook_mark.png'),
                            fit: BoxFit.cover),
                        borderRadius:
                        BorderRadius.all(Radius.circular(75.0)),
                        boxShadow: [
                          BoxShadow(blurRadius: 7.0, color: Colors.black)
                        ]),
                  ),
                ),
              ),
              GestureDetector(
                onTap: () {
                  return showDialog<void>(
                    context: context,
                    barrierDismissible: false,
                    builder: (BuildContext conext) {
                      return AlertDialog(
                        title: Text('Not in stock'),
                        content:
                        const Text('This item is no longer available'),
                        actions: <Widget>[
                          FlatButton(
                            child: Text('Ok'),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                        ],
                      );
                    },
                  );
                },
                child: Container(
                    padding: EdgeInsets.all(30.0),
                    child: Chip(
                      label: Text('@anonymous'),
                      shadowColor: Colors.blue,
                      backgroundColor: Colors.green,
                      elevation: 10,
                      autofocus: true,
                    )),
              ),
            ],
          ),
        ),
      ),
    );
  }
}