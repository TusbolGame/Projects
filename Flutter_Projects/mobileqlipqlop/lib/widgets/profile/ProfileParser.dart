import 'package:QlipQlop/resources/Constants.dart';
class ProfileData{
  int Id;
  String Email;
  int VerifiedAt;
  String FullName;
  String ProfilePic;

  ProfileData({
    this.Id,
    this.Email,
    this.VerifiedAt,
    this.FullName,
    this.ProfilePic
  });
  factory ProfileData.fromJson(dynamic json){
    return ProfileData(
        Id: json["Id"] as int,
        Email: json["Email"] as String,
        VerifiedAt: json["VerifiedAt"] as int,
        FullName: json["FullName"] as String,
        ProfilePic: json["ProfilePic"] as String
    );
  }
}