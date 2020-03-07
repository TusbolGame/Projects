
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
    List<int> minWeight;
    List<int> maxWeight;
    String rtxt;
    String rimg;
    ResultItem({
        this.rid,
        this.minWeight,
        this.maxWeight,
        this.rtxt,
        this.rimg
    });
    factory ResultItem.fromJson(dynamic json){
        return ResultItem(
            rid: json["rid"] as int,
            minWeight: new List<int>.from(json["weight_min"]),
            maxWeight: new List<int>.from(json["weight_max"]),
            rtxt: json["rtxt"] as String,
            rimg: json["rimg"] as String
        );
    }
}