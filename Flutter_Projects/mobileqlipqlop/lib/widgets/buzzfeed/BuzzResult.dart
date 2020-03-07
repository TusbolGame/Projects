import 'dart:convert';
import 'dart:developer';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:flutter/material.dart';
import 'package:QlipQlop/widgets/buzzfeed/JsonParser.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/challenge_page.dart';
import 'package:QlipQlop/resources/Constants.dart';
import 'package:QlipQlop/Auth/session.dart' as session;
import 'package:share/share.dart';
import 'package:transparent_image/transparent_image.dart';


class BuzzFeedResult extends StatefulWidget {
    //List<QuestionItem> questions;
    List<ResultItem> results;
    List<int> resultWeight;
    String url;
    int id;
    List<int> indexes;

    BuzzFeedResult({Key key, this.id, this.results, this.resultWeight, this.indexes, this.url}) : super(key: key);
    int questionIndex = 0;

    @override
    _BuzzFeedResult createState() {
        return new _BuzzFeedResult(id: id, results: this.results, resultWeight: this.resultWeight, indexes:indexes, url:this.url);
    }

}

class _BuzzFeedResult extends State<BuzzFeedResult>{
    List<ResultItem> results;
    List<int> resultWeight;
    String url;
    int id;
    String buzzfeed_answer = "";
    List<int> indexes;
    Map<String, dynamic> answer_result;
    _BuzzFeedResult({this.id, this.results, this.resultWeight, this.indexes, this.url});

    @override
    initState() {
        super.initState();
    }
    updateUI(){

        setState(() {
            //widget.questionIndex++;
        });
    }

    upsertanswer() async {
        String answer = jsonEncode(answer_result);
        final body = {
            "content_id": id.toString(),
            "answer_json": answer,
        };
        var data;
        try{
            data = await session.post(domain_url + upsert_answer, body);
        }
        catch(e){
            print(e.toString());
        }
        if(data["ok"] == true){
            showAlertDialog(context, data["id"].toString());
            return true;
        }
        else{
            showToast("Image Upload Failed!", Colors.red);
            return true;
        }
    }
    showAlertDialog(BuildContext context, String id) async{
        // show the dialog
        await showDialog(
            context: context,
            builder: (BuildContext context) {
                return AlertDialog(
                    title: Text("Challenge"),
                    content: Text("Would you like to share result?"),
                    actions: [
                        FlatButton(
                            child: Text("Back to Feed"),
                            onPressed: (){
                                Navigator.pushReplacementNamed(context, "/mainfeed");
                            },
                        ),
                        FlatButton(
                            child: Text("Share"),
                            onPressed: () async{
                                final body = {
                                    "id": id,
                                };
                                final data = await session.post(domain_url + share_answer, body);
                                try{
                                    String shareurl = data["share_url"];
                                    Navigator.pop(context);
                                    Share.share(share_prefix + shareurl, subject: "Share");
                                }
                                catch(e){
                                    Navigator.pop(context);
                                    showToast("Share error", Colors.red);
                                }

                            },
                        ),
                    ],
                );
            },
        );
    }
    showToast(String message, Color color){
        return Fluttertoast.showToast(
            msg: message,
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.CENTER,
            timeInSecForIos: 3,
            backgroundColor: color,
            textColor: Colors.white);
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
                                        onPressed:upsertanswer,
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
        answer_result = results[0].toJsonAttr(resultWeight, indexes);
        int count = 0;
        for(int i = 0; i < results.length; i++){
            List<String> minWeight = results[i].minWeight;
            List<String> maxWeight = results[i].maxWeight;
            for (int j = 0; j < minWeight.length; j++){
                if(resultWeight[j] >= int.parse(minWeight[j]) && resultWeight[j] <= int.parse(maxWeight[j])){
                    count++;
                }
                else{
                    continue;
                }
            }
            if(count == 4){
                answer_result = results[i].toJsonAttr(resultWeight, indexes);
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
                        body: Stack(
                            children: <Widget>[
                                Card(
                                    child: results[i].rimg != "" && results[i].rtxt != ""?
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
                                                                image: getImageUrl(results[i].rimg),
                                                                fit: BoxFit.fill,
                                                            ),
                                                        ),
                                                    ],
                                                ),
                                                /*child:new Image.network(getImageUrl(results[i].rimg),
                                                    fit: BoxFit.fitHeight,
                                                ),*/
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
                                                                image: getImageUrl(results[i].rimg),
                                                                fit: BoxFit.fill,
                                                            ),
                                                        ),
                                                    ],
                                                ),
                                                /*child: new Image.network(getImageUrl(results[i].rimg),
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
                                                    image: getImageUrl(results[0].rimg),
                                                    fit: BoxFit.fill,
                                                ),
                                            ),
                                        ],
                                    ),
                                    /*child: new Image.network(getImageUrl(results[0].rimg),
                                        fit: BoxFit.fitHeight,
                                    ),*/
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
                                                    image: getImageUrl(results[0].rimg),
                                                    fit: BoxFit.fill,
                                                ),
                                            ),
                                        ],
                                    ),
                                    /*child: new Image.network(getImageUrl(results[0].rimg),
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