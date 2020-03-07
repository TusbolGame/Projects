import 'package:flutter/material.dart';
import 'web_view_container.dart';

class WebviewHome extends StatelessWidget {
    //http://www.kevs3d.co.uk/dev/js1kdragons/
  static final links = ['http://www.google.com'];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        key: Key('123'),
        body: SafeArea(
            child: SingleChildScrollView(
                child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: links.map((link) => _urlButton(context, link)).toList(),
        ))));
  }

  Widget _urlButton(BuildContext context, String url) {
    return Container(
        padding: EdgeInsets.all(20.0),
        child: FlatButton(
          color: Theme.of(context).primaryColor,
          padding: const EdgeInsets.symmetric(horizontal: 50.0, vertical: 15.0),
          child: Text(url),
          onPressed: () => _handleURLButtonPress(context, url),
        ));
  }

  void _handleURLButtonPress(BuildContext context, String url) {
    /*Navigator.push(context,
        MaterialPageRoute(builder: (context) => WebViewContainer(url)));*/
  }
}
