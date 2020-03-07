import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/challenge_page.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';

class WebViewContainer extends StatefulWidget {
  final url;

  WebViewContainer(this.url);

  @override
  createState() => _WebViewContainerState(this.url);
}

class _WebViewContainerState extends State<WebViewContainer> {
  var _url;
  final _key = UniqueKey();
  bool isLoading = true;

  _WebViewContainerState(this._url);

  final Set<Factory> gestureRecognizers = [
    Factory(() => EagerGestureRecognizer()),
  ].toSet();

  @override
  Widget build(BuildContext context) {
    return new WillPopScope(
          onWillPop: (){
              Navigator.pop(context);
          },
          child: Scaffold(
            //constraints: BoxConstraints(maxHeight: double.infinity),
            appBar: new AppBar(
              title: new Text("Back"),
              leading: new IconButton(
                icon: new Icon(Icons.arrow_back),
                onPressed: () => Navigator.of(context).pop(),
              ),
            ),

            body: Stack(
              children: <Widget>[
                WebView(
                  key: _key,
                  javascriptMode: JavascriptMode.unrestricted,
                  initialUrl: _url,
                  onPageFinished: (String value) {
                    setState(() {
                      isLoading = false;
                    });
                  },
                ),
                isLoading ? Center(
                  child: CircularProgressIndicator(
                    backgroundColor: Colors.tealAccent,
                  ),
                )
                    : Container(),
              ],
            ),
          ),
          /*child: new WebviewScaffold(
              appBar: new AppBar(
                title: new Text("Back"),
                leading: new IconButton(
                  icon: new Icon(Icons.arrow_back),
                  onPressed: () => Navigator.of(context).pop(),
                ),
              ),
              url: _url,
              primary: false,
              withJavascript: true,
              withZoom: true,
              withLocalStorage: true,
              hidden: true,
              initialChild: Container(
                color: Colors.black,
                child: const Center(
                  child: CircularProgressIndicator(),
                ),
              )
          ),*/

    );
  }
}
