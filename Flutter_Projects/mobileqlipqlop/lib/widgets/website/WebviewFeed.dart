import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter_webview_plugin/flutter_webview_plugin.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/challenge_page.dart';
import 'package:QlipQlop/Auth/session.dart' as session;

class WebViewFeed extends StatefulWidget {
  final url;

  WebViewFeed(this.url);

  @override
  createState() => _WebViewFeedState(this.url);
}

class _WebViewFeedState extends State<WebViewFeed> {
  var _url;
  String videoUrl;
  bool isLoading = true;
  final _key = UniqueKey();

  _WebViewFeedState(this._url);

  final Set<Factory> gestureRecognizers = [
    Factory(() => EagerGestureRecognizer()),
  ].toSet();

  @override
  Widget build(BuildContext context) {
    return new WillPopScope(
        onWillPop: (){
          exit(0);
          return;
        },
        /*child: new WebviewScaffold(
            url: _url,
//            primary: false,
            withJavascript: true,
            withZoom: true,
            withLocalStorage: true,
            hidden: true,
            appCacheEnabled: true,
            scrollBar: true,
            
            initialChild: Container(
              color: Colors.black,
              child: const Center(
                child: CircularProgressIndicator(
                  backgroundColor: Colors.tealAccent,
                ),
              ),
            )
        )*/
        child: new Scaffold(
            backgroundColor: Colors.black,
            body: ConstrainedBox(
              constraints: BoxConstraints(maxHeight: double.infinity),
              child: Stack(
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
                  isLoading ? Center( child: CircularProgressIndicator(
                    backgroundColor: Colors.tealAccent,
                  ),)
                    : Container(),
                ],
              ),
            ),
        )
    );
  }
}
