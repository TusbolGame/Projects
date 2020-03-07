import 'dart:developer';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:flutter/material.dart';
import 'package:MiniTikTok/widgets/buzzfeed/JsonParser.dart';
import 'package:MiniTikTok/resources/assets.dart';
import 'package:MiniTikTok/challenge_page.dart';


class BuzzFeedResult extends StatefulWidget {
    //List<QuestionItem> questions;
    List<ResultItem> results;
    List<int> resultWeight;
    String url;

    BuzzFeedResult({Key key, this.results, this.resultWeight, this.url}) : super(key: key);
    int questionIndex = 0;

    @override
    _BuzzFeedResult createState() {
        return new _BuzzFeedResult(results: this.results, resultWeight: this.resultWeight, url:this.url);
    }

}

class _BuzzFeedResult extends State<BuzzFeedResult>{
    List<ResultItem> results;
    List<int> resultWeight;
    String url;
    _BuzzFeedResult({this.results, this.resultWeight, this.url});

    @override
    initState() {
        super.initState();
    }
    updateUI(){

        setState(() {
            //widget.questionIndex++;
        });
    }

    Widget _getButtonFab() {
        return Column(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.end,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
                Container(
                    color: Colors.transparent,
                    child: Padding(
                        padding: EdgeInsets.only(bottom: 20),
                        child: Row(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            mainAxisSize: MainAxisSize.max,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                                Expanded(
                                    flex: 4,
                                    child: Text(''),
                                ),
                                Expanded(
                                    flex: 1,
                                    child: FloatingActionButton(
                                        heroTag: "upload",
                                        onPressed: (){
                                            return Fluttertoast.showToast(
                                                msg: "ImageWidget",
                                                toastLength: Toast.LENGTH_SHORT,
                                                gravity: ToastGravity.BOTTOM,
                                                timeInSecForIos: 1,
                                                backgroundColor: Colors.red,
                                                textColor: Colors.white);
                                        },
                                        child: Icon(
                                            Icons.cloud_upload,
                                            color: Colors.white,
                                            size: 20,
                                        ),
                                        backgroundColor: Colors.green,
                                    ),
                                ),
                                Expanded(
                                    flex: 1,
                                    child: FloatingActionButton(
                                        heroTag: "Backbutton",
                                        onPressed: (){
                                            Navigator.pop(context);
                                        },
                                        child: Icon(
                                            AppIcons.backbutton,
                                            color: Colors.white,
                                            size: 20,
                                        ),
                                        backgroundColor: Colors.lightBlue,
                                    ),
                                ),
                            ],
                        )),
                )
            ],
        );
    }

    @override
    Widget build (BuildContext context) {
        int count = 0;
        for(int i = 0; i < results.length; i++){
            List<int> minWeight = results[i].minWeight;
            List<int> maxWeight = results[i].maxWeight;
            for (int j = 0; j < minWeight.length; j++){
                if(resultWeight[j] >= minWeight[j] && resultWeight[j] <= maxWeight[j]){
                    count++;
                }
                else{
                    continue;
                }
            }
            if(count == 4){
                return new WillPopScope(
                    onWillPop: (){
                        Navigator.pop(context);
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => new ChallengePage(url, null, null)),
                        );
                        return;
                    },
                    child: new Scaffold(
                        body: Stack(
                            children: <Widget>[
                                Card(
                                    child: results[i].rimg != "" && results[i].rtxt != ""?
                                    new Column(
                                        crossAxisAlignment: CrossAxisAlignment.stretch,
                                        mainAxisAlignment: MainAxisAlignment.start,
                                        children: <Widget>[
                                            Expanded(
                                                child:new Image.network(results[i].rimg,
                                                    fit: BoxFit.fitHeight,
                                                ),
                                            ),
                                            new Column(
                                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                                crossAxisAlignment: CrossAxisAlignment.stretch,
                                                children: <Widget>[
                                                    new Center(
                                                        child: new Text(
                                                            results[i].rtxt,
                                                            style: new TextStyle(
                                                                fontSize: 20.0,
                                                                fontWeight: FontWeight.bold,
                                                            ),
                                                            textAlign: TextAlign.center,

                                                        ),
                                                    )
                                                ],
                                            )
                                        ],
                                    )
                                        :
                                    results[i].rimg != "" ?
                                    new Column(
                                        crossAxisAlignment: CrossAxisAlignment.stretch,
                                        mainAxisAlignment: MainAxisAlignment.center,
                                        children: <Widget>[
                                            Expanded(
                                                child: new Image.network(results[i].rimg,
                                                    fit: BoxFit.fitHeight,
                                                ),
                                            )
                                        ],
                                    ):
                                    new Column(
                                        crossAxisAlignment: CrossAxisAlignment.stretch,
                                        mainAxisAlignment: MainAxisAlignment.start,
                                        children: <Widget>[
                                            Expanded(
                                                child: new Center(
                                                    child: new Text(
                                                        results[i].rtxt,
                                                        style: new TextStyle(
                                                            fontSize: 20.0,
                                                            fontWeight: FontWeight.bold,
                                                        ),
                                                        textAlign: TextAlign.center,

                                                    ),
                                                ),
                                            ),
                                        ],
                                    ),
                                    elevation: 2.0,
                                    margin: EdgeInsets.all(5.0),
                                ),
                                _getButtonFab()
                            ],
                        ),
                    )
                );
                /*return Scaffold(
                    body: Stack(
                        children: <Widget>[
                            Card(
                                child: results[i].rimg != "" && results[i].rtxt != ""?
                                new Column(
                                    crossAxisAlignment: CrossAxisAlignment.stretch,
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    children: <Widget>[
                                        Expanded(
                                            child:new Image.network(results[i].rimg,
                                                fit: BoxFit.fitHeight,
                                            ),
                                        ),
                                        new Column(
                                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                            crossAxisAlignment: CrossAxisAlignment.stretch,
                                            children: <Widget>[
                                                new Center(
                                                    child: new Text(
                                                        results[i].rtxt,
                                                        style: new TextStyle(
                                                            fontSize: 20.0,
                                                            fontWeight: FontWeight.bold,
                                                        ),
                                                        textAlign: TextAlign.center,

                                                    ),
                                                )
                                            ],
                                        )
                                    ],
                                )
                                    :
                                results[i].rimg != "" ?
                                new Column(
                                    crossAxisAlignment: CrossAxisAlignment.stretch,
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: <Widget>[
                                        Expanded(
                                            child: new Image.network(results[i].rimg,
                                                fit: BoxFit.fitHeight,
                                            ),
                                        )
                                    ],
                                ):
                                new Column(
                                    crossAxisAlignment: CrossAxisAlignment.stretch,
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    children: <Widget>[
                                        Expanded(
                                            child: new Center(
                                                child: new Text(
                                                    results[i].rtxt,
                                                    style: new TextStyle(
                                                        fontSize: 20.0,
                                                        fontWeight: FontWeight.bold,
                                                    ),
                                                    textAlign: TextAlign.center,

                                                ),
                                            ),
                                        ),
                                    ],
                                ),
                                elevation: 2.0,
                                margin: EdgeInsets.all(5.0),
                            ),
                            _getButtonFab()
                        ],
                    ),
                );*/

            }

        }

        return Scaffold(
            body: Stack(
                children: <Widget>[
                    new Card(
                        child: results[0].rimg != "" && results[0].rtxt != ""?
                        new Column(
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            mainAxisAlignment: MainAxisAlignment.start,
                            children: <Widget>[
                                Expanded(
                                    child:new Image.network(results[0].rimg,
                                        fit: BoxFit.fitHeight,
                                    ),
                                ),
                                new Column(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    crossAxisAlignment: CrossAxisAlignment.stretch,
                                    children: <Widget>[
                                        new Center(
                                            child: new Text(
                                                results[0].rtxt,
                                                style: new TextStyle(
                                                    fontSize: 20.0,
                                                    fontWeight: FontWeight.bold,
                                                ),
                                                textAlign: TextAlign.center,

                                            ),
                                        )
                                    ],
                                ),
                            ],
                        )
                            :
                        results[0].rimg != "" ?
                        new Column(
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                                Expanded(
                                    child: new Image.network(results[0].rimg,
                                        fit: BoxFit.fitHeight,
                                    ),
                                )
                            ],
                        ):
                        new Column(
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            mainAxisAlignment: MainAxisAlignment.start,
                            children: <Widget>[
                                Expanded(
                                    child: new Center(
                                        child: new Text(
                                            results[0].rtxt,
                                            style: new TextStyle(
                                                fontSize: 20.0,
                                                fontWeight: FontWeight.bold,
                                            ),
                                            textAlign: TextAlign.center,

                                        ),
                                    ),
                                ),
                            ],
                        ),
                        elevation: 2.0,
                        margin: EdgeInsets.all(5.0),
                    ),
                    _getButtonFab()
                ],
            ),
        );

    }

}