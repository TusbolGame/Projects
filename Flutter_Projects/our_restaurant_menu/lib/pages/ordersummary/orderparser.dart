
import 'dart:convert';

class FoodStep{
  List foodsteps;
  FoodStep({
    this.foodsteps
  });
  factory FoodStep.fromJson(dynamic json){
    return FoodStep(foodsteps: jsonDecode(json));
  }
}

class Device{
  String deviceName;
  List<Customer> customers;
  Device({
    this.deviceName,
    this.customers
  });
  factory Device.fromJson(dynamic json){
    List _customers = json["customers"];
    return Device(
      deviceName: json["devicename"] as String,
      customers: _customers.map<Customer>((json) => Customer.fromJson(json)).toList()
    );
  }
}
class Customer{
  String customername;
  List<FoodStepItem> foodsteps;
  Customer({
    this.customername,
    this.foodsteps
  });
  factory Customer.fromJson(dynamic json){
    List _foodsteps = json["foodsteps"];
    return Customer(
      customername: json["customername"] as String,
      foodsteps: _foodsteps.map<FoodStepItem>((json) => FoodStepItem.fromJson(json)).toList()
    );
  }
}
class FoodStepItem{
  String steptitle;
  List<StepItem> stepitems;
  FoodStepItem({
    this.steptitle,
    this.stepitems
  });
  factory FoodStepItem.fromJson(dynamic json){
    List _stepitems = json["stepitem"];
    return FoodStepItem(
      steptitle: json["steptitle"] as String,
      stepitems: _stepitems.map<StepItem>((json) => StepItem.fromJson(json)).toList()
    );
  }
}
class StepItem{
  String itemname;
  Product product;
  StepItem({
    this.itemname,
    this.product
  });
  factory StepItem.fromJson(dynamic json){
    return StepItem(
      itemname: json["itemname"] as String,
      product: Product.fromJson(json["productitem"][0])
    );
  }
}
class Product{
  int count;
  double price;
  Product({
    this.count,
    this.price
  });
  factory Product.fromJson(dynamic json){
    return Product(
      count: json["count"] as int,
      price: json["price"].toDouble() as double
    );
  }
}
