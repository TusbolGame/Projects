import 'package:flutter/material.dart';
import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/resources/dimen.dart';
import 'package:fluttertoast/fluttertoast.dart';

enum LoginStatus { notSignIn, signIn }

class BottomNavigation extends StatefulWidget {

  final VoidCallback signOut;
  BottomNavigation(this.signOut);

  @override
  State<StatefulWidget> createState() => _BottomNavigation();
}

class _BottomNavigation extends State<BottomNavigation> {

  signOut() {
    setState(() {
      widget.signOut();
    });
  }

  FeedControl(){
    return Fluttertoast.showToast(
        msg: "VideoWidget",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: Colors.red,
        textColor: Colors.white);
  }
  CategoryControl(){
    return Fluttertoast.showToast(
        msg: "TODO: Category",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: Colors.red,
        textColor: Colors.white);
  }
  RewardControl(){
    return Fluttertoast.showToast(
        msg: "TODO: Reward",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: Colors.red,
        textColor: Colors.white);
  }
  ProfileControl(){
    return Fluttertoast.showToast(
        msg: "TODO: Profile",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: Colors.red,
        textColor: Colors.white);
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.max,
      mainAxisAlignment: MainAxisAlignment.end,
      crossAxisAlignment: CrossAxisAlignment.end,
      children: <Widget>[
        Divider(
          height: 2,
          color: Colors.grey[700],
        ),
        Container(
          height: 47,
          color: Colors.transparent,
          child: Padding(
              padding: EdgeInsets.only(top: 7),
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisSize: MainAxisSize.max,
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Expanded(
                    flex: 1,
                    child: FlatButton(
                      onPressed: FeedControl,
                      child: Column(
                        children: <Widget>[
                          Icon(
                            AppIcons.feedbutton,
                            color: Colors.white,
                            size: 20,
                          ),
                          Padding(
                            padding: EdgeInsets.only(top: Dimen.textSpacing),
                            child: Text(
                              "Feed",
                              style: TextStyle(
                                  fontSize: Dimen.bottomNavigationTextSize,
                                  color: Colors.white),
                            ),
                          )
                        ],
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FlatButton(
                      onPressed: CategoryControl,
                      child: Column(
                        children: <Widget>[
                          Icon(
                            AppIcons.categorybutton,
                            color: Colors.white,
                            size: 20,
                          ),
                          Padding(
                            padding: EdgeInsets.only(top: Dimen.textSpacing),
                            child: Text(
                              "Category",
                              style: TextStyle(
                                  fontSize: Dimen.bottomNavigationTextSize,
                                  color: Colors.white),
                            ),
                          )
                        ],
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FlatButton(
                      onPressed: RewardControl,
                      child: Column(
                        children: <Widget>[
                          Icon(
                            AppIcons.rewardbutton,
                            color: Colors.white,
                            size: 20,
                          ),
                          Padding(
                            padding: EdgeInsets.only(top: Dimen.textSpacing),
                            child: Text(
                              "Reward",
                              style: TextStyle(
                                  fontSize: Dimen.bottomNavigationTextSize,
                                  color: Colors.white),
                            ),
                          )
                        ],
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: FlatButton(
                      onPressed: ProfileControl,
                      child: Column(
                        children: <Widget>[
                          Icon(
                              AppIcons.profilebutton,
                              color: Colors.white,
                              size: 20
                          ),
                          Padding(
                            padding: EdgeInsets.only(top: Dimen.textSpacing),
                            child: Text(
                              "Profile",
                              style: TextStyle(
                                  fontSize: Dimen.bottomNavigationTextSize,
                                  color: Colors.white),
                            ),
                          )
                        ],
                      ),
                    ),
                  ),
                  /*Expanded(
                    flex: 1,
                    child: FlatButton(
                      onPressed: signOut,
                      child: Column(
                        children: <Widget>[
                          Icon(AppIcons.logoutbutton, color: Colors.white, size: 20),
                          Padding(
                            padding: EdgeInsets.only(top: Dimen.textSpacing),
                            child: Text(
                              "Logout",
                              style: TextStyle(
                                  fontSize: Dimen.bottomNavigationTextSize,
                                  color: Colors.white),
                            ),
                          )
                        ],
                      ),
                    ),
                  ),*/
                ],
              )),
        )
      ],
    );
  }
}
