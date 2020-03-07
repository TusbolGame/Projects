import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:MiniTikTok/resources/assets.dart';
import 'package:MiniTikTok/challenge_page.dart';

class WebViewContainer extends StatefulWidget {
  final url;
  final String videoUrl;

  WebViewContainer(this.url, this.videoUrl);

  @override
  createState() => _WebViewContainerState(this.url, this. videoUrl);
}

class _WebViewContainerState extends State<WebViewContainer> {
  var _url;
  String videoUrl;
  final _key = UniqueKey();

  _WebViewContainerState(this._url, this.videoUrl);

  @override
  Widget build(BuildContext context) {
    return new WillPopScope(
          onWillPop: (){
              Navigator.pop(context);
              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => new ChallengePage(videoUrl, null, null)),
              );
              return;
          },
          child: new Scaffold(
              appBar: AppBar(
                  leading: FloatingActionButton(
                      heroTag: "Backbutton",
                      onPressed: (){
                          Navigator.pop(context);
                          Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => new ChallengePage(videoUrl, null, null)),
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
              body: Column(
                  children: [
                      Expanded(
                          child: WebView(
                              key: _key,
                              javascriptMode: JavascriptMode.unrestricted,
                              initialUrl: _url))
                  ],
              ))
      );
    /*return Scaffold(
        appBar: AppBar(
            leading: FloatingActionButton(
                heroTag: "Backbutton",
                onPressed: (){
                    Navigator.pop(context);
                    Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) => new ChallengePage(videoUrl, null, null)),
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
        body: Column(
          children: [
            Expanded(
                child: WebView(
                    key: _key,
                    javascriptMode: JavascriptMode.unrestricted,
                    initialUrl: _url))
          ],
        ));*/
  }
}
