import 'package:QlipQlop/widgets/history/timeline.dart';
import 'package:QlipQlop/widgets/slidewidget/pages/home_page.dart';
import 'package:flutter/cupertino.dart';
import 'dart:math';
/**
 * Author: Damodar Lohani
 * profile: https://github.com/lohanidamodar
  */

import 'package:flutter/material.dart';

class RewardPage extends StatefulWidget {
  static final String path = "lib/src/pages/animations/rewind_home.dart";
  @override
  _RewardPageState createState() => _RewardPageState();
}

class _RewardPageState extends State<RewardPage> {
  int _currentPage;
  BuildContext context;

  String title = "LeaderBoard";
  final title_list = [
    "LeaderBoard",
    "Reward",
    "Claim/History"
  ];

  @override
  void initState() {
    _currentPage = 0;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    this.context = context;
    return Scaffold(
      appBar: AppBar(
        title: Text(title),
        centerTitle: true,
      ),
      backgroundColor: Colors.grey.shade300,
      body: getPage(_currentPage),
      bottomNavigationBar: AnimatedBottomNav(
          currentIndex: _currentPage,
          onChange: (index) {
            setState(() {
              _currentPage = index;
              title = title_list[index];
            });
          }),
    );
  }

  Widget leaderboardListView(BuildContext context) {

    final avatar_urls = [
      'https://pbs.twimg.com/profile_images/501759258665299968/3799Ffxy.jpeg',
      'https://images-na.ssl-images-amazon.com/images/M/MV5BMjA3NjYzMzE1MV5BMl5BanBnXkFtZTgwNTA4NDY4OTE@._V1_UX172_CR0,0,172,256_AL_.jpg',
      'https://images-na.ssl-images-amazon.com/images/M/MV5BODFjZTkwMjItYzRhMS00OWYxLWI3YTUtNWIzOWQ4Yjg4NGZiXkEyXkFqcGdeQXVyMTQ0ODAxNzE@._V1_UX172_CR0,0,172,256_AL_.jpg',
      'https://images.unsplash.com/photo-1496081081095-d32308dd6206?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=dd302358c7e18c27c4086e97caf85781',
      'https://faces.design/faces/w/w22.png',
      'https://randomuser.me/api/portraits/women/17.jpg',
      'https://randomuser.me/api/portraits/women/26.jpg',
      'https://images.pexels.com/photos/61100/pexels-photo-61100.jpeg?h=350&auto=compress&cs=tinysrgb',
      'https://images-na.ssl-images-amazon.com/images/M/MV5BMjUzZTJmZDItODRjYS00ZGRhLTg2NWQtOGE0YjJhNWVlMjNjXkEyXkFqcGdeQXVyMTg4NDI0NDM@._V1_UY256_CR42,0,172,256_AL_.jpg',
      'https://randomuser.me/api/portraits/women/82.jpg',
      'https://images.pexels.com/photos/355164/pexels-photo-355164.jpeg?h=350&auto=compress&cs=tinysrgb',
      'https://images-na.ssl-images-amazon.com/images/M/MV5BMjAwNjM3NjY5MF5BMl5BanBnXkFtZTcwMjM4NTYwOQ@@._V1_UY256_CR0,0,172,256_AL_.jpg',
      'https://images.pexels.com/photos/413723/pexels-photo-413723.jpeg?h=350&auto=compress&cs=tinysrgb',
      'https://images.unsplash.com/photo-1496671431883-c102df9ae8f9?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=84d0b5da11ab2535ea4d207095366988'
    ];
    final names = ['Abigail', 'Charlotte', 'Lillian', 'Hannah',
      'Kennedy', 'Samantha', 'Anna', 'Peyton', 'Annabelle', 'Caroline', 'Serenity',
      'Violet', 'Stella', 'Autumn'
    ];

    final prize_paths = [
      'assests/images/gold_prize.png',
      'assests/images/silver_prize.png',
      'assests/images/bronze_prize.png',
    ];

    return ListView.builder(
      itemCount: avatar_urls.length,
      itemBuilder: (context, index) {
        return new Card(
          color: Colors.teal,
          child: new Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Expanded(
//                alignment: Alignment.centerLeft,
                child: Align(
                  alignment: Alignment.centerLeft,
                  child: new Container(

                    child: Padding(
                      padding: EdgeInsets.all(5.0),
                      child: CircleAvatar(
                        radius: 30.0,
                        backgroundImage:NetworkImage(avatar_urls[index]),
                        backgroundColor: Colors.transparent,
                      ),
                    ),
                  ),
                ),

              ),
              Expanded(
//                alignment: Alignment.center,
                child: new Container(
                    child: Center(
                      child: Text(
                        names[index],
                        style: TextStyle(
                            fontSize: 17.0,
                            color: Colors.white
                        ),
                      ),
                    )
                ),
              ),
              Expanded(
//                alignment: Alignment.centerRight,
                child: Align(
                  alignment: Alignment.centerRight,
                  child: new Container(
                    child: Padding(
                      padding: EdgeInsets.all(5.0),
                      child: Image.asset(
                        'assets/images/prize_icon.png',
                        fit: BoxFit.fitHeight,
                        height: 50.0,

//                      prize_paths[Random().nextInt(100) % 3]
                      ),
                    ),
                  ),
                ),

              )
            ],
          ),
        );
      },
    );
  }

  getPage(int page) {
    switch(page) {
      case 0:
        return Center(child: Container(child: leaderboardListView(context),));
      case 1:
        return Center(child: Container(child: SlideWidgetPage(),));
      case 2:
        return Center(child: Container(child: TimelinePage(title: "",),));
    }
  }
}

class AnimatedBottomNav extends StatelessWidget {
  final int currentIndex;
  final Function(int) onChange;
  const AnimatedBottomNav({Key key, this.currentIndex, this.onChange})
      : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Container(
      height: kToolbarHeight,
      decoration: BoxDecoration(color: Colors.white),
      child: Row(
        children: <Widget>[
          Expanded(
            child: InkWell(
              onTap: () => onChange(0),
              child: BottomNavItem(
                icon: Icons.home,
                title: "LeaderBoard",
                isActive: currentIndex == 0,
              ),
            ),
          ),
          Expanded(
            child: InkWell(
              onTap: () => onChange(1),
              child: BottomNavItem(
                icon: Icons.verified_user,
                title: "Rewards",
                isActive: currentIndex == 1,
              ),
            ),
          ),
          Expanded(
            child: InkWell(
              onTap: () => onChange(2),
              child: BottomNavItem(
                icon: Icons.menu,
                title: "Claims/History",
                isActive: currentIndex == 2,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class BottomNavItem extends StatelessWidget {
  final bool isActive;
  final IconData icon;
  final Color activeColor;
  final Color inactiveColor;
  final String title;
  const BottomNavItem(
      {Key key,
      this.isActive = false,
      this.icon,
      this.activeColor,
      this.inactiveColor,
      this.title})
      : super(key: key);
  @override
  Widget build(BuildContext context) {
    return AnimatedSwitcher(
      transitionBuilder: (child, animation) {
        return SlideTransition(
          position: Tween<Offset>(
            begin: const Offset(0.0, 1.0),
            end: Offset.zero,
          ).animate(animation),
          child: child,
        );
      },
      duration: Duration(milliseconds: 500),
      reverseDuration: Duration(milliseconds: 200),
      child: isActive
          ? Container(
              color: Colors.white,
              padding: const EdgeInsets.all(8.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Text(
                    title,
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      color: activeColor ?? Theme.of(context).primaryColor,
                    ),
                  ),
                  const SizedBox(height: 5.0),
                  Container(
                    width: 5.0,
                    height: 5.0,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: activeColor ?? Theme.of(context).primaryColor,
                    ),
                  ),
                ],
              ),
            )
          : Icon(
              icon,
              color: inactiveColor ?? Colors.grey,
            ),
    );
  }
}
