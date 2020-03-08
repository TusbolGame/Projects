
import 'package:OurMenu/pages/ordersummary/ordersummarypage.dart';
import 'package:flutter/material.dart';

class BeautifulAlertDialog extends StatelessWidget {
  final String msg;
  final String path;
  BeautifulAlertDialog({this.msg, this.path});
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Dialog(
        elevation: 0,
        backgroundColor: Colors.transparent,
        child: Container(
          padding: EdgeInsets.only(right: 16.0),
          height: 150,
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(75),
              bottomLeft: Radius.circular(75),
              topRight: Radius.circular(10),
              bottomRight: Radius.circular(10)
            )
          ),
          child: Row(
            children: <Widget>[
              SizedBox(width: 20.0),
              CircleAvatar(
                radius: 55,
                backgroundColor: Colors.grey.shade200,
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(20.0),
                  ),
                  child: Image.asset(
                    'assets/images/waiter.png',
                  ),
                ),
              ),
              SizedBox(width: 20.0),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    SizedBox(height: 10.0),
                    Flexible(
                      child: Text(
                        msg),
                    ),
                    SizedBox(height: 10.0),
                    Row(children: <Widget>[
                      Expanded(
                        child: RaisedButton(
                          child: Icon(Icons.cancel, color: Colors.white,),
                          color: Colors.red,
                          colorBrightness: Brightness.dark,
                          onPressed: (){Navigator.pop(context);},
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20.0)),
                        ),
                      ),
                      SizedBox(width: 10.0),
                      Expanded(
                        child: RaisedButton(
                          child: Icon(Icons.done_all, color: Colors.white),
                          color: Colors.green,
                          colorBrightness: Brightness.dark,
                          onPressed: (){
                            if(path == '/OrderSummaryPage')
                            {
                              Navigator.pop(context);
                              Navigator.push(context, MaterialPageRoute(builder: (context) => OrderSummaryPage()),);
                            }
                            else if(path == '/OrderStepPage'){
                              int count = 0;
                              Navigator.of(context).popUntil((_) => count++ >= 2);
                            }
                          },
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20.0)),
                        ),
                      ),
                    ],)
                  ],
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}