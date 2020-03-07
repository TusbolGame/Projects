
import 'package:QlipQlop/resources/Constants.dart';

String getImageUrl(String imgUrl){
    if(imgUrl.startsWith("/")){
        return domain_url + imgUrl;
    }
    else{
        imgUrl;
    }
}

class QuestionItem{
    int qid;
    String qtxt;
    String qimg;
    List<Option> options;

    QuestionItem({
        this.qid,
        this.qtxt,
        this.qimg,
        this.options
    });
    factory QuestionItem.fromJson(dynamic json){
        List _options = json["options"];
        return QuestionItem(
            qid: json["qid"] as int,
            qtxt: json["qtxt"] as String,
            qimg: json["qimg"] as String,
            options: _options.map<Option>((json) => Option.fromJson(json)).toList()
        );
    }
}
class Option{
    int oid;
    List<int> weight;
    String otxt;
    String oimg;
    Option({
        this.oid,
        this.weight,
        this.otxt,
        this.oimg
    });
    factory Option.fromJson(dynamic json){
        return Option(
            oid: json["oid"] as int,
            weight: new List<int>.from(json["weight"]),
            otxt: json["otxt"],
            oimg: json["oimg"]
        );
    }
}

class ResultItem{
    int rid;
    String rtxt;
    String rimg;
    List<String> minWeight;
    List<String> maxWeight;

    ResultItem({
        this.rid,
        this.rtxt,
        this.rimg,
        this.minWeight,
        this.maxWeight,
    });
    factory ResultItem.fromJson(dynamic json){
        return ResultItem(
            rid: json["rid"] as int,
            rtxt: json["rtxt"] as String,
            rimg: json["rimg"] as String,
            minWeight: new List<String>.from(json["weight_min"]),
            maxWeight: new List<String>.from(json["weight_max"]),

        );
    }
    Map<String, dynamic> toJsonAttr(List<int> weights, List<int> indexes){
        return{
            "rid" : this.rid,
            "rtxt" : this.rtxt,
            "rimg" : this.rimg,
            "weight_min" : this.minWeight,
            "weight_max" : this.maxWeight,
            "weight" : weights,
            "answer_ids" : indexes
        };
    }
}
