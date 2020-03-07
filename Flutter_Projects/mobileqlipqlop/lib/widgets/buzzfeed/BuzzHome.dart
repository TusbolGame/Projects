import 'dart:convert';
import 'dart:ui';

import 'package:QlipQlop/widgets/buzzfeed/BuzzResult.dart';
import 'package:QlipQlop/widgets/buzzfeed/JsonParser.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:math' as math;
import 'package:collection/collection.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/challenge_page.dart';
import 'package:QlipQlop/resources/Constants.dart';
import 'package:transparent_image/transparent_image.dart';


class BuzzFeedHome extends StatefulWidget {
    List<QuestionItem> questions;
    List<ResultItem> results;
    int dimens;
    String url;
    int id;
    List<int> resultWeight;

    BuzzFeedHome({Key key, this.id, this.questions, this.results, this.dimens, this.url, this.resultWeight}) : super(key: key);




    @override
    _BuzzFeedHome createState() {
        return new _BuzzFeedHome(id: this.id, questions: this.questions, results: this.results, url:this.url, resultWeight:this.resultWeight);
    }

}

class _BuzzFeedHome extends State<BuzzFeedHome>{

    final GlobalKey scaffoldKey = new GlobalKey();
    List<QuestionItem> questions;
    List<ResultItem> results;
    String url;
    int id;
    List<int> indexes;
    List<int> resultWeight;
    int questionIndex = 0;
    _BuzzFeedHome({this.id, this.questions, this.results, this.url, this.resultWeight});

    BuildContext context;

    @override
    initState() {
        super.initState();
        indexes = new List<int>();
    }
    updateUI(int oid){
        print(questionIndex.toString());
        indexes.add(oid);
        if(questionIndex + 1 == questions.length){
            for(int i = 0; i < questions[questionIndex].options[oid - 1].weight.length; i++){
                widget.resultWeight[i] += questions[questionIndex].options[oid - 1].weight[i];
            }
//            Navigator.pop(context);
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => new BuzzFeedResult(id: id, results: widget.results, resultWeight: widget.resultWeight, indexes: indexes, url:url)),
            );
            return;
        }
        setState(() {
          questionIndex++;
          for(int i = 0; i < questions[questionIndex].options[oid - 1].weight.length; i++){
              widget.resultWeight[i] += questions[questionIndex].options[oid - 1].weight[i];
          }
        });
    }

    @override
    Widget build (BuildContext context) {
        this.context = context;
        return new WillPopScope(
            onWillPop: (){
                Navigator.pop(context);
                /*Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => new ChallengePage(url)),
                );*/
                return;
            },
            child: new Scaffold(
                key: scaffoldKey,
                appBar: new AppBar(
                    title: Center(
                      child: new Text(
                        questions[questionIndex].qtxt,
                        textAlign: TextAlign.end,
                        style: new TextStyle(
                            fontSize: 18.0,
                            fontWeight: FontWeight.bold,
                            color: Colors.white),
                      ),
                    )
                ),
                body: new Container(
                    child: new MyGridView( buzzHome: this ,options: questions[questionIndex].options, url:this.url)
                )
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

class MyGridView extends StatefulWidget {
    final _BuzzFeedHome buzzHome;
    final List<Option> options;
    String url;
    MyGridView({Key key, this.buzzHome, this.options, this.url}) : super(key: key);
    BuildContext context;

  @override

  _MyGridView createState() {
    return new _MyGridView(buzzHome: this.buzzHome, options: this.options, url: this.url);
  }
}
class _MyGridView extends State<MyGridView>{

    final _BuzzFeedHome buzzHome;
    final List<Option> options;
    String url;
    _MyGridView({Key key, this.buzzHome, this.options, this.url});
    BuildContext context;

    @override
    Widget build(BuildContext context) {
        return new Scaffold(
            body: Container(
                child: GridView.count(
                    primary: false,
                    crossAxisCount: 2,
                    padding: EdgeInsets.all(16.0),
                    childAspectRatio: 8.0 / 9.0,
                    children: _getGridViewItems(context),
                ),
                color: Colors.black,
            )
        );
    }

    _getGridViewItems(BuildContext context) {
        List<Widget> allWidgets = new List<Widget>();

        for (int i = 0; i < buzzHome.questions[buzzHome.questionIndex].options.length; i++) {
            var widget = _getGridItemUI(context, buzzHome.questions[buzzHome.questionIndex].options[i]);
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
                                            /*Navigator.push(
                                                context,
                                                MaterialPageRoute(
                                                    builder: (context) => new ChallengePage(url)),
                                            );*/
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
                color: Colors.amberAccent,
                child: item.oimg != "" && item.otxt != ""?
                new Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: <Widget>[
                        Expanded(
                            child: Stack(
                                children: [
                                    Center(child: CircularProgressIndicator(
                                        backgroundColor: Colors.tealAccent,
                                    )),
                                    Container(
                                        width: MediaQuery.of(context).size.width,
                                        height: MediaQuery.of(context).size.height,
                                        padding: EdgeInsets.all(3.0),
                                        child: FadeInImage.memoryNetwork(
                                            placeholder: kTransparentImage,
                                            image: getImageUrl(item.oimg),
                                            fit: BoxFit.fill,
                                        ),
                                    ),
                                ],
                            ),
                            /*child:new Image.network( getImageUrl(item.oimg),
                                fit: BoxFit.fitHeight,
                            ),*/
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
                            child: Stack(
                                children: [
                                    Center(child: CircularProgressIndicator(
                                        backgroundColor: Colors.tealAccent,
                                    )),
                                    Container(
                                        width: MediaQuery.of(context).size.width,
                                        height: MediaQuery.of(context).size.height,
                                        padding: EdgeInsets.all(3.0),
                                        child: FadeInImage.memoryNetwork(
                                            placeholder: kTransparentImage,
                                            image: getImageUrl(item.oimg),
                                            fit: BoxFit.fill,
                                        ),
                                    ),
                                ],
                            ),
                            /*child: new Image.network(getImageUrl(item.oimg),
                                fit: BoxFit.fitHeight,
                            ),*/
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
        widget.
        buzzHome.updateUI(item.oid);

    }

}
