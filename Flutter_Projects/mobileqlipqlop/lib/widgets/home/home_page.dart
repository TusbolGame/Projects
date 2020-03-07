import 'package:QlipQlop/widgets/audioplay/player_widget.dart';
import 'package:QlipQlop/widgets/website/WebviewFeed.dart';
import 'package:QlipQlop/widgets/website/web_view_container.dart';
import 'package:flutter/material.dart';
import 'package:QlipQlop/onscreen_controls.dart';
import 'package:QlipQlop/widgets/home/home_video_renderer.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart';
import 'package:transparent_image/transparent_image.dart';
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import '../../main.dart';
import 'package:QlipQlop/Auth/session.dart' as session;
import 'package:QlipQlop/resources/Constants.dart';
import 'package:QlipQlop/widgets/buzzfeed/JsonParser.dart';
import 'package:http/http.dart' as http;
import 'package:QlipQlop/widgets/buzzfeed/BuzzHome.dart';
import 'dart:convert';
import 'package:flutter_swiper/flutter_swiper.dart';


class HomeScreen extends StatefulWidget with RouteAware{

  int count = 0;

  int currentIndex = 0;
  YoutubePlayerController yController = null;
  VideoPlayerController vController = null;

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new _HomeScreen(count: count, currentIndex: currentIndex);
  }
}

class _HomeScreen extends State<HomeScreen> with RouteAware, WidgetsBindingObserver {

  final RouteObserver<PageRoute> routeObserver = new RouteObserver<PageRoute>();
  int count;
  int currentIndex;
  String feed_type;
  _HomeScreen({this.count, this.currentIndex});

  dynamic content_feeds;

  @override
  void initState(){
    content_feeds = session.content_feeds;
    super.initState();
    if(session.feed_index == 0){
      _handlePageChanged(0);
    }

  }
  _handlePageChanged(int position) async{
    currentIndex = position;
    session.feed_index = position;
    print(session.feed_id);
    if(content_feeds["feed"][position]["content_type"] == "video"){
      feed_type = "video";
      session.feed_type = "video";
      session.feed_id = content_feeds["feed"][position]["id"].toString();

      if(content_feeds["feed"][position]["youtube_url"] == ""){
        session.feed_url = domain_url + content_feeds["feed"][position]["video_url"];
      }
      else{
        session.feed_url = content_feeds["feed"][position]["youtube_url"];
      }
    }
    else if(content_feeds["feed"][position]["content_type"] == "web"){
      feed_type = "web";
      session.feed_id = content_feeds["feed"][position]["id"].toString();
      session.feed_type = "web";

      String url;
      url = "http:" + content_feeds["feed"][position]["webview_url"];
      session.feed_url = url;
    }
    else if(content_feeds["feed"][position]["content_type"] == "quiz"){
      feed_type = "quiz";
      session.feed_type = "quiz";
      session.feed_id = content_feeds["feed"][position]["id"].toString();
      var quiz_data = content_feeds["feed"][position];
      session.quiz_data = quiz_data["quiz_json"];
    }
    else if(content_feeds["feed"][position]["content_type"] == "challenge"){
      feed_type = "challenge";
      session.feed_id = content_feeds["feed"][position]["id"].toString();
      session.feed_type = "challenge";
      session.feed_url = domain_url + content_feeds["feed"][position]["audio_url"];
    }
    else if(content_feeds["feed"][position]["content_type"] == "picture"){
      feed_type = "picture";
      session.feed_id = content_feeds["feed"][position]["id"].toString();
      session.feed_type = "picture";
    }
  }

  @override
  Widget build(BuildContext context){
    int feed_count = content_feeds["feed"].length;
    print(session.feed_id);
    // TODO: implement build
    return new Scaffold(
        body: new Swiper(
          // ignore: missing_return
          itemBuilder: (BuildContext context, int position) {
            if(content_feeds["feed"][position]["content_type"] == "video"){
              var feed_data = content_feeds["feed"][position];
              if(feed_data["youtube_url"] == ""){

                AppVideoPlayer player = new AppVideoPlayer(domain_url + feed_data["video_url"]);
                currentIndex = position;
                return Container(
                  color: Colors.black,
                  child: Stack(
                    children: <Widget>[player, onScreenControls(context, domain_url + feed_data["video_url"])],
                  ),
                );
              }
              else{
                YoutubeVideoPlayer player = YoutubeVideoPlayer(feed_data["youtube_url"]);
                currentIndex = position;
                return Container(
                  color: Colors.black,
                  child: Stack(
                    children: <Widget>[player, onScreenControls(context, feed_data["youtube_url"])],
                  ),
                );
              }
            }
            else if(content_feeds["feed"][position]["content_type"] == "web"){
              var feed_data = content_feeds["feed"][position];
              String url;
              url = "http:" + content_feeds["feed"][position]["webview_url"];

              var page;

              if(content_feeds["feed"][position]["video_url"] != ""){
                AppVideoPlayer player = new AppVideoPlayer(domain_url + feed_data["video_url"]);
                currentIndex = position;
                return Container(
                  color: Colors.black,
                  child: Stack(
                    children: <Widget>[player, onScreenControls(context, domain_url + feed_data["video_url"])],
                  ),
                );
              }
              else if(content_feeds["feed"][position]["youtube_url"] != ""){
                YoutubeVideoPlayer player = YoutubeVideoPlayer(feed_data["youtube_url"]);
                currentIndex = position;
                return Container(
                  color: Colors.black,
                  child: Stack(
                    children: <Widget>[player, onScreenControls(context, feed_data["youtube_url"])],
                  ),
                );
              }
              else if(content_feeds["feed"][position]["picture_url"] != ""){
                var page = Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Image.network(domain_url + content_feeds["feed"][position]["picture_url"], fit: BoxFit.fill,)
                  ],
                );
                return Container(
                  color: Colors.black,
                  child: Stack(children: <Widget>[page, onScreenControls(context, domain_url + content_feeds["feed"][position]["picture_url"])],
                  ),
                );
              }
              //page = WebViewFeed(url);
              page = Center(
                child:
                  Text(
                      content_feeds["feed"][position]["title"],
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 30,
                      ),
                  )
              );
              return Container(
                color: Colors.black,
                child: Stack(
                  children: <Widget>[page, onScreenControls(context, domain_url + feed_data["video_url"])],
                ),
              );
            }
            else if(content_feeds["feed"][position]["content_type"] == "quiz"){
              List<QuestionItem> questions;
              List<ResultItem> results;
              List<int> resultWeight;
              var quiz_data = content_feeds["feed"][position];
              var data = quiz_data["quiz_json"];
              int feed_id;
              feed_id = quiz_data["id"];
              List<dynamic> _questions = jsonDecode(data)["questions"] as List<dynamic>;
              List<dynamic> _results = jsonDecode(data)["result_rules"] as List<dynamic>;
              questions = _questions.map<QuestionItem>((json) => QuestionItem.fromJson(json)).toList();
              results = _results.map<ResultItem>((json) => ResultItem.fromJson(json)).toList();
              int dimens = results[0].maxWeight.length;
              resultWeight = List.filled(dimens, 0);

              BuzzFeedHome page;
              page = BuzzFeedHome(id: feed_id, questions: questions, results: results, dimens: dimens, url:domain_url + buzzfeed_url, resultWeight: resultWeight);

              return Container(
                color: Colors.black,
                child: Stack(
                  children: <Widget>[page, onScreenControls(context, domain_url + buzzfeed_url)],
                ),
              );
            }
            else if(content_feeds["feed"][position]["content_type"] == "challenge"){
              /*var page = Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  PlayerWidget(url: domain_url + content_feeds["feed"][position]["audio_url"])
                ],
              );
              return Container(
                color: Colors.black,
                child: Stack(children: <Widget>[page, onScreenControls(context, domain_url + content_feeds["feed"][position]["audio_url"])],
                ),
              );*/
              var feed_data = content_feeds["feed"][position];
              if(content_feeds["feed"][position]["video_url"] != ""){
                AppVideoPlayer player = new AppVideoPlayer(domain_url + feed_data["video_url"]);
                currentIndex = position;
                return Container(
                  color: Colors.black,
                  child: Stack(
                    children: <Widget>[player, onScreenControls(context, domain_url + feed_data["video_url"])],
                  ),
                );
              }
              else if(content_feeds["feed"][position]["youtube_url"] != ""){
                YoutubeVideoPlayer player = YoutubeVideoPlayer(feed_data["youtube_url"]);
                currentIndex = position;
                return Container(
                  color: Colors.black,
                  child: Stack(
                    children: <Widget>[player, onScreenControls(context, feed_data["youtube_url"])],
                  ),
                );
              }
              else if(content_feeds["feed"][position]["picture_url"] != ""){
                var page = Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Image.network(domain_url + content_feeds["feed"][position]["picture_url"], fit: BoxFit.fill,)
                  ],
                );
                return Container(
                  color: Colors.black,
                  child: Stack(children: <Widget>[page, onScreenControls(context, domain_url + content_feeds["feed"][position]["picture_url"])],
                  ),
                );
              }
              //page = WebViewFeed(url);
              var page;
              page = Center(
                  child:
                  Text(
                    content_feeds["feed"][position]["title"],
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 30,
                    ),
                  )
              );
              return Container(
                color: Colors.black,
                child: Stack(
                  children: <Widget>[page, onScreenControls(context, domain_url + feed_data["audio_url"])],
                ),
              );
            }
            else if(content_feeds["feed"][position]["content_type"] == "picture"){
              var page = Stack(
                children: [
                  Center(child: CircularProgressIndicator(
                    backgroundColor: Colors.tealAccent,
                  )),
                  Center(
                    child: FadeInImage.memoryNetwork(
                      placeholder: kTransparentImage,
                      image: domain_url + content_feeds["feed"][position]["picture_url"],
                    ),
                  ),
                ],
              );
              return Container(
                color: Colors.black,
                child: Stack(children: <Widget>[page, onScreenControls(context, domain_url + content_feeds["feed"][position]["picture_url"])],
                ),
              );
            }
          },
          index: session.feed_index,
          autoplay: false,
          itemCount: feed_count,
          scrollDirection: Axis.vertical,
          onIndexChanged: _handlePageChanged,
        ));



    /*return PageView.builder(

        scrollDirection: Axis.vertical,
        controller: PageController(
          initialPage: 0,
        ),
        onPageChanged: _handlePageChanged,
        // ignore: missing_return
        itemBuilder: (context, position){
          if(content_feeds["feed"][position]["content_type"] == "video"){
            var feed_data = content_feeds["feed"][position];
            if(feed_data["youtube_url"] == ""){
              yController = null;
              vController = null;

              AppVideoPlayer player = new AppVideoPlayer(domain_url + feed_data["video_url"], vController);
              currentIndex = position;
              return Container(
                color: Colors.black,
                child: Stack(
                  children: <Widget>[player, onScreenControls(context, domain_url + feed_data["video_url"], vController, null)],
                ),
              );
            }
            else{
              vController = null;
              yController = null;
              YoutubeVideoPlayer player = YoutubeVideoPlayer(feed_data["youtube_url"], yController);
              currentIndex = position;
              return Container(
                color: Colors.black,
                child: Stack(
                  children: <Widget>[player, onScreenControls(context, feed_data["youtube_url"], null, yController)],
                ),
              );
            }
          }
          else if(content_feeds["feed"][position]["content_type"] == "web"){
            var feed_data = content_feeds["feed"][position];
            var page = WebViewFeed('https:' + feed_data["webview_url"], feed_data["webview_url"]);
            return Container(
              color: Colors.black,
              child: Stack(
                children: <Widget>[page, onScreenControls(context, domain_url + feed_data["video_url"], vController, null)],
              ),
            );
          }
          else if(content_feeds["feed"][position]["content_type"] == "quiz"){
            List<QuestionItem> questions;
            List<ResultItem> results;
            var quiz_data = content_feeds["feed"][position];
            var data = quiz_data["quiz_json"];
            int feed_id;
            feed_id = quiz_data["id"];
            List<dynamic> _questions = jsonDecode(data)["questions"] as List<dynamic>;
            List<dynamic> _results = jsonDecode(data)["result_rules"] as List<dynamic>;
            questions = _questions.map<QuestionItem>((json) => QuestionItem.fromJson(json)).toList();
            results = _results.map<ResultItem>((json) => ResultItem.fromJson(json)).toList();
            int dimens = results[0].maxWeight.length;

            var page = BuzzFeed(id: feed_id, questions: questions, results: results, dimens: dimens, url:domain_url + buzzfeed_url);
            return Container(
              color: Colors.black,
              child: Stack(
                children: <Widget>[page, onScreenControls(context, domain_url + buzzfeed_url, vController, null)],
              ),
            );
          }
          else if(content_feeds["feed"][position]["content_type"] == "challenge"){
            var page = PlayerWidget(url: domain_url + content_feeds["feed"][position]["audio_url"]);
            return Container(
              color: Colors.black,
              child: Stack(
                children: <Widget>[page, onScreenControls(context, domain_url + content_feeds["feed"][position]["audio_url"], vController, null)],
              ),
            );
          }
          else if(content_feeds["feed"][position]["content_type"] == "image"){
            return Text("Image");
          }
        },
        itemCount: feed_count);*/

  }
  @override
  void dispose() {
    super.dispose();
  }
  @override
  /*void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      // user returned to our app
      if (vController != null) {
        vController.play();
      } else {
        yController.play();
      }
    } else if (state == AppLifecycleState.inactive) {
      // app is inactive
      if (vController != null) {
        vController.pause();
      } else {
        yController.pause();
      }
    } else if (state == AppLifecycleState.paused) {
      // user is about quit our app temporally
      if (vController != null) {
        vController.pause();
      } else {
        yController.pause();
      }
    } else if (state == AppLifecycleState.detached) {
      if (vController != null) {
        vController.pause();
      } else {
        yController.pause();
      }
    }
  }*/
  @override
  void didChangeDependencies() {
    routeObserver.subscribe(this, ModalRoute.of(context));
    super.didChangeDependencies();
  }

  @override
  void didPopNext() {
    /*if (vController != null) {
      vController.play();
    } else {
      yController.play();
    }*/
    super.didPopNext();
  }

  @override
  void didPushNext() {
    /*if (vController != null) {
      vController.pause();
    } else {
      yController.pause();
    }*/
    super.didPushNext();
  }

}