import 'package:flutter/material.dart';
import 'package:MiniTikTok/onscreen_controls.dart';
import 'package:MiniTikTok/widgets/home/home_video_renderer.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:video_player/video_player.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import '../../main.dart';

class HomeScreen extends StatefulWidget with RouteAware{

  final List<String> urls = [
    'https://www.youtube.com/watch?v=JmNiSDvZvYQ',
    'https://ia600402.us.archive.org/5/items/23Kidsswimming/23_kidsswimming.mp4',
    'https://www.youtube.com/watch?v=s2h0tFWwqFc',
    'https://archive.org/download/Jaydedman-Swimming542/Jaydedman-Swimming542_512kb.mp4',
    'https://www.youtube.com/watch?v=-UzYFyaeGEY',
    'https://archive.org/download/bliptv-20131014-215005-BigCatRescue-SwimmingBobcat236/bliptv-20131014-215005-BigCatRescue-SwimmingBobcat236.mp4',
    'https://www.youtube.com/watch?v=kJKhOLpv0Oo',
    'https://archive.org/download/Flickr-5375942528/Swimming_Ray-5375942528.mp4',
    'https://www.youtube.com/watch?v=OCDRmpS8N5s',
    'https://archive.org/download/npctv-Safer_Swimming/Safer_Swimming.mp4',
    'https://www.youtube.com/watch?v=5HLW2AI1Ink',
    'https://archive.org/download/Flickr-4298744730/Utilikilt_Swimming-4298744730.mp4',
    'https://www.youtube.com/watch?v=xnWzozZHYgQ',
    'https://archive.org/download/wrestling_knoakesVSadick/wrestling_knoakesVSadick_512kb.mp4',
    'https://www.youtube.com/watch?v=OWWZkgVySak',
    'https://archive.org/download/wrestling_smurataVSlbeslisle/wrestling_smurataVSlbeslisle_512kb.mp4',
    'https://www.youtube.com/watch?v=OdCtZLFZmxQ',
    'https://archive.org/download/FreestyleSwimming/FreestyleAugust09_512kb.mp4',
    'https://www.youtube.com/watch?v=MRgc0VKfRdo',
    'https://archive.org/download/MuhammadAliVsRussianBoxers/MuhammadAliVsRussianBoxers_512kb.mp4',
    'https://www.youtube.com/watch?v=-k7n60ZelRo',
    'https://archive.org/download/BasketballTechniques/basketball_512kb.mp4',
    'https://www.youtube.com/watch?v=OCDRmpS8N5s',
    'https://archive.org/download/RoadTrip2005-2008/road_trip_2005-2008_512kb.mp4',
    'https://www.youtube.com/watch?v=F1j6ROFyGeU',
    'https://archive.org/download/AsianCupFinal-2007/First-Half-Art_512kb.mp4',
    'https://www.youtube.com/watch?v=XN7jvcoKQZ8',
    'https://archive.org/download/rectal_spective_1/rectal_spective_1_512kb.mp4',
    'https://www.youtube.com/watch?v=5UGSLcMxqSA',
    'https://archive.org/download/TommyLoughranVsJamesBraddock/TommyLoughranVsJamesBraddock_512kb.mp4',
    'https://www.youtube.com/watch?v=3_BUeHB1dm4',
    'https://archive.org/download/rectal_spective_3/rectal_spective_3_512kb.mp4',
    'https://www.youtube.com/watch?v=PEbM9s4gsUs',
    'https://archive.org/download/IMG/IMGP0230_512kb.mp4',
    'https://www.youtube.com/watch?v=c28SMt8CraI',
    'https://geyserfalls.com/wp-content/uploads/2016/05/HD-Water-in-Swimming-Pool-Footage-Loop.mp4',
  ];
  int count = 0;

  int currentIndex = 0;
  YoutubePlayerController yController = null;
  VideoPlayerController vController = null;

  _handlePageChanged(int page) {
//    return Fluttertoast.showToast(
//        msg: "Current Video Index: " + currentIndex.toString(),
//        toastLength: Toast.LENGTH_SHORT,
//        gravity: ToastGravity.BOTTOM,
//        timeInSecForIos: 1,
//        backgroundColor: Colors.red,
//        textColor: Colors.white);
  }

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new _HomeScreen(urls: urls, count: count, currentIndex: currentIndex, yController: yController, vController: vController);
  }
}

class _HomeScreen extends State<HomeScreen> with RouteAware, WidgetsBindingObserver {

  final RouteObserver<PageRoute> routeObserver = new RouteObserver<PageRoute>();

  List<String> urls;
  int count;
  int currentIndex;
  YoutubePlayerController yController;
  VideoPlayerController vController;
  _HomeScreen({this.urls, this.count, this.currentIndex, this.yController, this.vController});

  @override
  void initState() {
    WidgetsBinding.instance.addObserver(this);
    super.initState();
  }
  _handlePageChanged(int page) {

  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return PageView.builder(
        scrollDirection: Axis.vertical,
        controller: PageController(
          initialPage: 0,
        ),
        onPageChanged: _handlePageChanged,
        itemBuilder: (context, position) {
          if(urls[position].contains('youtube'))
          {
            vController = null;
            yController = null;
            YoutubeVideoPlayer player = YoutubeVideoPlayer(urls[position], yController);
            currentIndex = position;
            return Container(
              color: Colors.black,
              child: Stack(
                children: <Widget>[player, onScreenControls(context, urls[position], null, yController)],
              ),
            );
          }
          else{
            yController = null;
            vController = null;

            AppVideoPlayer player = new AppVideoPlayer(urls[position], vController);
            currentIndex = position;
            return Container(
              color: Colors.black,
              child: Stack(
                children: <Widget>[player, onScreenControls(context, urls[position], vController, null)],
              ),
            );
          }
        },
        itemCount: urls.length);
  }
  @override
  void dispose() {
    routeObserver.unsubscribe(this);
    WidgetsBinding.instance.removeObserver(this);
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