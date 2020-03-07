import 'dart:async';
import 'dart:io';

import 'package:QlipQlop/resources/assets.dart';
import 'package:QlipQlop/widgets/profile/edit_profile.dart';
import 'package:QlipQlop/widgets/slidewidget/pages/home_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:image_picker/image_picker.dart';
import 'package:QlipQlop/Auth/session.dart' as session;
import 'package:QlipQlop/resources/Constants.dart';
import 'package:carousel_slider/carousel_slider.dart';

class Profile extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(
          "PROFILE",
          style: new TextStyle(fontSize: 18.0, fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
      ),
      body: new ProfileScreen(),
    );
  }
}

class ProfileScreen extends StatefulWidget {
  @override
  State createState() => new ProfileScreenState();
}

class ProfileScreenState extends State<ProfileScreen> {
  String avatarImageFile, backgroundImageFile;
  String sex;

  Future getImage(bool isAvatar) async {
    var result;
    if (session.userProfile.ProfilePic == "") {
      result = null;
    } else {
      result = domain_url + session.userProfile.ProfilePic;
    }
    avatarImageFile = result;
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    timeDilation = 1.0;
    getImage(true);
    return new SingleChildScrollView(
      child: new Column(
        children: <Widget>[
          new Container(
//            color: Color(0xffc2fffb),
            child: new Stack(
              children: <Widget>[
                // Background
                (backgroundImageFile == null)
                    ? new Image.asset(
                        'assets/images/bg_uit.jpg',
                        width: double.infinity,
                        height: 150.0,
                        fit: BoxFit.cover,
                      )
                    : new Image.network(
                        backgroundImageFile,
                        width: double.infinity,
                        height: 150.0,
                        fit: BoxFit.cover,
                      ),
                // Avatar and button
                new Positioned(
                  child: new Stack(
                    children: <Widget>[
                      (avatarImageFile == null)
                          ? new Image.asset(
                              'assets/images/ic_avatar.png',
                              width: 70.0,
                              height: 70.0,
                            )
                          : new Material(
                              color: Colors.transparent,
                              child: ClipRRect(
                                borderRadius: BorderRadius.circular(35.0),
                                child: Image.network(
                                  avatarImageFile,
                                  width: 70.0,
                                  height: 70.0,
                                  fit: BoxFit.cover,
                                ),
                              )),
                      new Material(
                        child: new IconButton(
                          icon: new Icon(
                            Icons.mode_edit,
                            color: Colors.grey,
                            size: 30,
                            semanticLabel: "Edit Profile",
                          ),
                          onPressed: () async{
                            await Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => new EditProfile())).then((result){
                                      setState(() {

                                      });
                            });
                          },
                          padding: new EdgeInsets.only(top: 40, left: 60),
                          //highlightColor: Colors.transparent,
                          iconSize: 30.0,
                          alignment: Alignment.bottomRight,
                          //hoverColor: Colors.teal,
                        ),
                        borderRadius:
                            new BorderRadius.all(new Radius.circular(40.0)),
                        color: Colors.transparent,
                      ),
                    ],
                  ),
                  top: 115.0,
                  left: MediaQuery.of(context).size.width / 2 - 70 / 2,
                )
              ],
            ),
            width: double.infinity,
            height: 200.0,
          ),
          new Column(
            children: <Widget>[
              // Username
              new Container(
                child: new Text(
                  'Username',
                  style: new TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 14.0,
                      color: Colors.amber),
                ),
                margin: new EdgeInsets.only(left: 10.0, bottom: 5.0, top: 10.0),
              ),
              new Container(
                child: Center(
                  child: session.userProfile.FullName == ""
                      ? Text(
                          "Not set yet",
                          style: TextStyle(color: Colors.grey),
                        )
                      : Text(
                          session.userProfile.FullName,
                          style: TextStyle(color: Colors.grey),
                        ),
                ),
                margin: new EdgeInsets.only(left: 30.0, right: 30.0),
              ),

              // Country
              /*new Container(
                child: new Text(
                  'Country',
                  style: new TextStyle(fontWeight: FontWeight.bold, fontSize: 14.0, color: Colors.amber),
                ),
                margin: new EdgeInsets.only(left: 10.0, top: 30.0, bottom: 5.0),
              ),
              new Container(
                child: new TextFormField(
                  decoration: new InputDecoration(
                      hintText: 'Input Country',
                      border: new UnderlineInputBorder(),
                      contentPadding: new EdgeInsets.all(5.0),
                      hintStyle: new TextStyle(color: Colors.grey)),
                ),
                margin: new EdgeInsets.only(left: 30.0, right: 30.0),
              ),
              */
              // Address
              new Container(
                child: new Text(
                  'Email',
                  style: new TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 14.0,
                      color: Colors.amber),
                ),
                margin: new EdgeInsets.only(left: 10.0, top: 30.0, bottom: 5.0),
              ),
              new Container(
                child: Center(
                  child: Text(
                    session.userProfile.Email,
                    style: TextStyle(color: Colors.grey),
                  ),
                ),
                margin: new EdgeInsets.only(left: 30.0, right: 30.0),
              ),
              new Container(
                margin: new EdgeInsets.only(top:30.0, bottom: 30.0, left: 10.0, right: 10.0),
                child: Center(
                  child: CarouselSlider.builder(
                    itemCount: 15,
                    height: 150.0,
                    itemBuilder: (BuildContext context, int itemIndex) =>
                        Container(
                          margin: EdgeInsets.only(left:5.0, right:5.0),
                          child: new Image.asset(
                            'assets/images/bg_uit.jpg',
                            width: double.infinity,
                            height: 100.0,
                            fit: BoxFit.cover,
                          ),
                        ),
                  )
                ),
              ),
              new Container(
                margin: new EdgeInsets.only(top:30.0, bottom: 30.0, left: 10.0, right: 10.0),
                child: Center(
                    child: CarouselSlider.builder(
                      itemCount: 15,
                      height: 150.0,
                      itemBuilder: (BuildContext context, int itemIndex) =>
                          Container(
                            margin: EdgeInsets.only(left:5.0, right:5.0),
                            child: new Image.asset(
                              'assets/images/bg_uit.jpg',
                              width: double.infinity,
                              height: 100.0,
                              fit: BoxFit.cover,
                            ),
                          ),
                    )
                ),
              ),
            ],
            crossAxisAlignment: CrossAxisAlignment.start,
          )
        ],
      ),
      padding: new EdgeInsets.only(bottom: 20.0),
    );
  }
}
