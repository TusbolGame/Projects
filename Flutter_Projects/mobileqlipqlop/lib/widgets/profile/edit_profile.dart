import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:QlipQlop/widgets/profile/ProfileParser.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:image_picker/image_picker.dart';
import 'package:QlipQlop/Auth/session.dart' as session;
import 'package:QlipQlop/resources/Constants.dart';
import 'package:http/http.dart' as http;
import 'package:async/async.dart';
import 'package:path/path.dart';
import 'package:http_parser/http_parser.dart';

class EditProfile extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(
          "EDIT PROFILE",
          style: new TextStyle(fontSize: 18.0, fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
      ),
      body: new EditProfileScreen(),
    );
  }
}

class EditProfileScreen extends StatefulWidget {
  @override
  State createState() => new EditProfileScreenState();
}

class EditProfileScreenState extends State<EditProfileScreen> {
  File avatarImageFile, backgroundImageFile;
  String sex;
  BuildContext context;


  final _usernameController = TextEditingController();

  Future getImage(bool isAvatar) async {
    var result = await ImagePicker.pickImage(source: ImageSource.gallery);
    if(isAvatar){
      session.showProgress(context, "Profile Avatar Uploading...");
      avatarImageFile = result;
      await uploadPic().then((result){
        setState(() {
          session.dismissProgress();
        });
      });
    }
    /*setState(() {
      if (isAvatar) {
        avatarImageFile = result;
        uploadPic();
      } else {
        backgroundImageFile = result;
      }
    });*/
  }

  uploadPic() async{
    var stream = http.ByteStream(DelegatingStream.typed(avatarImageFile.openRead()));
    var length = await avatarImageFile.length();
    var response;
    var respStr;
    try{
      var uri = Uri.parse(domain_url + profilePicUpload_url);
      var request = http.MultipartRequest('POST', uri);
      request.headers.addAll(session.headers);

      request.fields["aaa"] = "challenge";
      request.files.add(await http.MultipartFile.fromPath('file', avatarImageFile.path,
          contentType: new MediaType('image', extension(avatarImageFile.path))));
      response = await request.send();
      respStr = await response.stream.bytesToString();
      try{
        session.userProfile.ProfilePic = jsonDecode(respStr)["local_url"];
      }
      catch (e){
        print(e);
        session.showToast("Profile Avatar Update Failed.", Colors.red);
      }
    }
    catch(e){
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    this.context = context;
    timeDilation = 1.0;
    return new SingleChildScrollView(
      child: new Column(
        children: <Widget>[
          new Container(
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
                    : new Image.file(
                        backgroundImageFile,
                        width: double.infinity,
                        height: 150.0,
                        fit: BoxFit.cover,
                      ),

                // Button change background
                new Positioned(
                  child: new Material(
                    child: new IconButton(
                      icon: new Image.asset(
                        'assets/images/ic_camera.png',
                        width: 30.0,
                        height: 30.0,
                        fit: BoxFit.cover,
                      ),
                      onPressed: () async{
                        getImage(false);
                      },
                      padding: new EdgeInsets.all(0.0),
                      highlightColor: Colors.black,
                      iconSize: 30.0,
                    ),
                    borderRadius: new BorderRadius.all(new Radius.circular(30.0)),
                    color: Colors.grey.withOpacity(0.5),
                  ),
                  right: 5.0,
                  top: 5.0,
                ),

                // Avatar and button
                new Positioned(
                  child: new Stack(
                    children: <Widget>[
                      if (session.userProfile.ProfilePic == "") new Image.asset(
                              'assets/images/ic_avatar.png',
                              width: 70.0,
                              height: 70.0,
                            )
                      //else if(session.userProfile.ProfilePic == "") new Text("aa")
                      else new Material(
                              color: Colors.transparent,
                              child: ClipRRect(
                                borderRadius: BorderRadius.circular(35.0),
                                child: Image.network(
                                  domain_url + session.userProfile.ProfilePic,
                                  width: 70.0,
                                  height: 70.0,
                                  fit: BoxFit.cover,
                                ),
                              )
                            ),
                      new Material(
                        child: new IconButton(
                          icon: new Image.asset(
                            'assets/images/ic_camera.png',
                            width: 40.0,
                            height: 40.0,
                            fit: BoxFit.cover,
                          ),
                          onPressed: () async{
                            getImage(true);
                          } ,
                          padding: new EdgeInsets.all(0.0),
                          highlightColor: Colors.black,
                          iconSize: 70.0,
                        ),
                        borderRadius: new BorderRadius.all(new Radius.circular(40.0)),
                        color: Colors.grey.withOpacity(0.5),
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
                  style: new TextStyle(fontWeight: FontWeight.bold, fontSize: 14.0, color: Colors.amber),
                ),
                margin: new EdgeInsets.only(left: 10.0, bottom: 5.0, top: 10.0),
              ),
              new Container(
                child: new TextFormField(
                  controller: _usernameController,
                  decoration: new InputDecoration(
                      hintText: 'Input Username',
                      border: new UnderlineInputBorder(),
                      contentPadding: new EdgeInsets.all(5.0),
                      hintStyle: new TextStyle(color: Colors.grey)),
                ),
                margin: new EdgeInsets.only(left: 30.0, right: 30.0),
              ),

              // Country
              new Container(
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

              // Address
              new Container(
                child: new Text(
                  'Address',
                  style: new TextStyle(fontWeight: FontWeight.bold, fontSize: 14.0, color: Colors.amber),
                ),
                margin: new EdgeInsets.only(left: 10.0, top: 30.0, bottom: 5.0),
              ),
              new Container(
                child: new TextFormField(
                  decoration: new InputDecoration(
                      hintText: 'Input Address',
                      border: new UnderlineInputBorder(),
                      contentPadding: new EdgeInsets.all(5.0),
                      hintStyle: new TextStyle(color: Colors.grey)),
                ),
                margin: new EdgeInsets.only(left: 30.0, right: 30.0),
              ),

              // About me
              new Container(
                child: new Text(
                  'About me',
                  style: new TextStyle(fontWeight: FontWeight.bold, fontSize: 14.0, color: Colors.amber),
                ),
                margin: new EdgeInsets.only(left: 10.0, top: 30.0, bottom: 5.0),
              ),
              new Container(
                child: new TextFormField(
                  decoration: new InputDecoration(
                      hintText: 'Input about yourself',
                      border: new UnderlineInputBorder(),
                      contentPadding: new EdgeInsets.all(5.0),
                      hintStyle: new TextStyle(color: Colors.grey)),
                ),
                margin: new EdgeInsets.only(left: 30.0, right: 30.0),
              ),

              // About me
              new Container(
                child: new Text(
                  'Phone',
                  style: new TextStyle(fontWeight: FontWeight.bold, fontSize: 14.0, color: Colors.amber),
                ),
                margin: new EdgeInsets.only(left: 10.0, top: 30.0, bottom: 5.0),
              ),
              new Container(
                child: new TextFormField(
                  decoration: new InputDecoration(
                      hintText: '0123456789',
                      border: new UnderlineInputBorder(),
                      contentPadding: new EdgeInsets.all(5.0),
                      hintStyle: new TextStyle(color: Colors.grey)),
                  keyboardType: TextInputType.number,
                ),
                margin: new EdgeInsets.only(left: 30.0, right: 30.0),
              ),

              // Sex
              new Container(
                child: new Text(
                  'Sex',
                  style: new TextStyle(fontWeight: FontWeight.bold, fontSize: 14.0, color: Colors.amber),
                ),
                margin: new EdgeInsets.only(left: 10.0, top: 30.0, bottom: 5.0),
              ),
              new Container(
                child: new DropdownButton<String>(
                  items: <String>['Male', 'Female'].map((String value) {
                    return new DropdownMenuItem<String>(
                      value: value,
                      child: new Text(value),
                    );
                  }).toList(),
                  onChanged: (value) {
                    setState(() {
                      sex = value;
                    });
                  },
                  hint: sex == null
                      ? new Text('Male')
                      : new Text(
                          sex,
                          style: new TextStyle(color: Colors.black),
                        ),
                  style: new TextStyle(color: Colors.black),
                ),
                margin: new EdgeInsets.only(left: 50.0),
              ),
              new Container(
                child: Center(
                    child: FlatButton.icon(
                        color: Color(0xff99d5e6),
                        onPressed: () async{
                          print("Save Button");
                          var data = {
                            "full_name": _usernameController.text,
                            "profile_pic": session.userProfile.ProfilePic
                          };
                          await session.post(domain_url + updateProfile_url, data).then((response){
                            session.showToast("Profile Updated", Colors.green);
                            dynamic profile = response["user"];
                            session.userProfile = ProfileData.fromJson(profile);
                          });
                        },
                        icon: Icon(
                            Icons.check,
                            size: 30.0,
                            color: Color(0xff2da6fb),
                        ),
                        label: Text(
                            "Save",
                            style: TextStyle(
                              color: Color(0xff2da6fb),
                              fontSize: 21.0
                            ),
                        )
                    )
                  /*child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: <Widget>[
                      IconButton(
                        icon: Icon(Icons.check),
                        tooltip: 'Update Profile',
                        onPressed: () {

                        },
                      ),
                      Text("Save")
                    ],
                  )*/
                ),
              )
            ],
            crossAxisAlignment: CrossAxisAlignment.start,
          )
        ],
      ),
      padding: new EdgeInsets.only(bottom: 20.0),
    );
  }
}
