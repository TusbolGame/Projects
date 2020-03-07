import 'dart:convert';

import 'package:MiniTikTok/widgets/buzzfeed/BuzzResult.dart';
import 'package:MiniTikTok/widgets/buzzfeed/JsonParser.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:math' as math;
import 'package:collection/collection.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:MiniTikTok/resources/assets.dart';
import 'package:MiniTikTok/challenge_page.dart';


class BuzzFeedHome extends StatefulWidget {
    List<QuestionItem> questions;
    List<ResultItem> results;
    int dimens;
    String url;
    BuzzFeedHome({Key key, this.questions, this.results, this.dimens, this.url}) : super(key: key);
    int questionIndex = 0;
    List<int> resultWeight;


    @override
    _BuzzFeedHome createState() {
        resultWeight = List.filled(dimens, 0);
        return new _BuzzFeedHome(questions: this.questions, results: this.results, url:this.url);
    }

}

class _BuzzFeedHome extends State<BuzzFeedHome>{

    final GlobalKey scaffoldKey = new GlobalKey();
    List<QuestionItem> questions;
    List<ResultItem> results;
    String url;
    _BuzzFeedHome({this.questions, this.results, this.url});

    BuildContext context;

    @override
    initState() {
        super.initState();
    }
    updateUI(int oid){
        if(widget.questionIndex + 1 == questions.length){
            for(int i = 0; i < questions[widget.questionIndex].options[oid - 1].weight.length; i++){
                widget.resultWeight[i] += questions[widget.questionIndex].options[oid - 1].weight[i];
            }
            Navigator.pop(context);
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => new BuzzFeedResult(results: widget.results, resultWeight: widget.resultWeight, url:url)),
            );
            return;
        }
        setState(() {
          widget.questionIndex++;
          for(int i = 0; i < questions[widget.questionIndex].options[oid - 1].weight.length; i++){
              widget.resultWeight[i] += questions[widget.questionIndex].options[oid - 1].weight[i];
          }
        });
    }


    getHomePageBody(BuildContext context) {
        return new MyGridView( buzzHome: this ,options: questions[widget.questionIndex].options, url:this.url);
    }

    @override
    Widget build (BuildContext context) {
        this.context = context;
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
                key: scaffoldKey,
                appBar: new AppBar(
                    title: new Text(
                        questions[widget.questionIndex].qtxt,
                        style: new TextStyle(
                            fontSize: 18.0,
                            fontWeight: FontWeight.bold,
                            color: Colors.black87),
                    ),
                    leading: FloatingActionButton(
                        heroTag: "Backbutton",
                        onPressed: (){
                            Navigator.pop(context);
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => new ChallengePage(url, null, null)),
                            );
                        },
                        child: Icon(
                            AppIcons.backbutton,
                            color: Colors.white,
                            size: 20,
                        ),
                        backgroundColor: Colors.deepOrangeAccent,
                    ),
                ),
                body: new Padding(
                    padding: EdgeInsets.fromLTRB(0.0, 10.0, 0.0, 0.0),
                    child: getHomePageBody(context))
            )
        );
        /*return Scaffold(
            key: scaffoldKey,
            appBar: new AppBar(
                title: new Text(
                    questions[widget.questionIndex].qtxt,
                    style: new TextStyle(
                        fontSize: 18.0,
                        fontWeight: FontWeight.bold,
                        color: Colors.black87),
                ),
                leading: FloatingActionButton(
                    heroTag: "Backbutton",
                    onPressed: (){
                        Navigator.pop(context);
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => new ChallengePage(url, null, null)),
                        );
                    },
                    child: Icon(
                        AppIcons.backbutton,
                        color: Colors.white,
                        size: 20,
                    ),
                    backgroundColor: Colors.deepOrangeAccent,
                ),
            ),
            body: new Padding(
                padding: EdgeInsets.fromLTRB(0.0, 10.0, 0.0, 0.0),
                child: getHomePageBody(context))
        );*/

    }

}

class MyGridView extends StatelessWidget {
    final _BuzzFeedHome buzzHome;
    final List<Option> options;
    String url;
    MyGridView({Key key, this.buzzHome, this.options, this.url}) : super(key: key);
    BuildContext context;

    @override
    Widget build(BuildContext context) {
        return GridView.count(
            crossAxisCount: 2,
            padding: EdgeInsets.all(16.0),
            childAspectRatio: 8.0 / 9.0,
            children: _getGridViewItems(context),
        );
    }

    _getGridViewItems(BuildContext context) {
        List<Widget> allWidgets = new List<Widget>();
        for (int i = 0; i < options.length; i++) {
            var widget = _getGridItemUI(context, options[i]);
            allWidgets.add(widget);
        };
        return allWidgets;
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
                                    flex: 5,
                                    child: Text(''),
                                ),
                                Expanded(
                                    flex: 1,
                                    child: FloatingActionButton(
                                        heroTag: "Backbutton",
                                        onPressed: (){
                                            Navigator.pop(context);
                                            Navigator.push(
                                                context,
                                                MaterialPageRoute(
                                                    builder: (context) => new ChallengePage(url, null, null)),
                                            );
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
    // Create individual item
    _getGridItemUI(BuildContext context, Option item) {
        return new InkWell(
            onTap: () {
                _showNext(context, item);
            },
            child: new Card(
                child: item.oimg != "" && item.otxt != ""?
                new Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: <Widget>[
                        Expanded(
                            child:new Image.network(item.oimg,
                                        fit: BoxFit.fitHeight,
                                    ),
                        ),
                        new Column(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            children: <Widget>[
                                new Center(
                                    child: new Text(
                                        item.otxt,
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
                item.oimg != "" ?
                new Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                        Expanded(
                            child: new Image.network(item.oimg,
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
                                    item.otxt,
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
            ));
    }

    /// This will show snackbar at bottom when user tap on Grid item
    _showNext(BuildContext context, Option item) {

        buzzHome.updateUI(item.oid);

    }
}
