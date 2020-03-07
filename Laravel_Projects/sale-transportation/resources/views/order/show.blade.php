@extends('layouts.admin')
@section('content')
  <style>
    .card {
      padding: 5px;
      margin: 5px;
    }

    p {
      padding: 5px;
      margin: 0px;
    }

    b {
      font-weight: 500;
    }

    td {
      font-weight: 500;
      padding-left: 10px;
      padding-top: 10px;
      padding-bottom: 10px;
    }

    .red {
      color: red;
    }
  </style>

  <a href="javascript:window.print()" class="noprint btn btn-success">@lang('main.print')</a>
  {{-- <a href="{{route('order_pdf', $order)}}" class="btn btn-success">Save as pdf</a> --}}
  <div class="card card-body">
    <div class="row">
      <div class="col-md-4">
        <br>
        <div class="card card-body">
          <h4 class="red">Our Position</h4>
          <p><b>Code</b> {{@$order->nation_code}}{{@$order->code_nr}}</p>
          <p><b>Nr</b> {{@$order->number}}</p>
        </div>
      </div>
      <div class="col-md-4">
        <div class="text-center">
          <img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png" alt="logo" height="180px">
        </div>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-md-6">
        <div class="card card-body">
          <h4 class="red">Supplier/Exporter</h4>
          <p><b>Supplier/Exporter Name</b> {{@$order->supplier->business_name}}</p>
          <p><b>Code</b> {{@$order->supplier->code}}</p>
        </div>
        <div class="card card-body">
          <h4><span class="red">Client / Recipent</span></h4>
          <p><b>Client/Recipent Name</b> {{@$order->customer->business_name}}</p>
          <p><b>Code</b> {{@$order->customer->code}}</p>
        </div>
        <div class="card card-body">
          <h4><span class="red">Producer and departure place</span></h4>
          <p>{{@$order->producer->name}}</p>
          <p>{{@$order->producer->reg_number}}</p>
          <p>{{@$order->producer->address}}</p>
        </div>
      </div>
      <div class="col-md-6">
        <div class="card card-body">
          <p><span class="red">Contract Number</span> <b>NR </b> {{@$order->contract_nr}}</p>
          <h4><p class="red"> Supplier / Exporter Invoice</p></h4>
            <p><b>NR</b> {{@$order->invoice_number}} <b>Del </b>
              <i></i>{{date("d-m-Y", strtotime(@$order->invoice_date))}}</p>
            <p><b>Amount in Euro</b> {{@$order->amount_in_euro}}</p>
        </div>
        <div class="card card-body">
          <h4><span class="red">Lorry's plate registration n</span></h4>
          {{@$order->lorry_reg_number}}
        </div>

        <div class="card card-body">
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>
              <td width="50%"><h4 class="red">Weight loaded kg</h4></td>
              <td width="50%">{{@$order->weight_loaded_kg}}</td>
            </tr>
            </tbody>
          </table>
        </div>
        <div class="card card-body">
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>
              <td width="50%"><h4 class="red">Weight invoiced kg</h4></td>
              <td width="50%">{{@$order->weight_invoiced_kg}}</td>
            </tr>
            </tbody>
          </table>
        </div>

        <div class="card card-body">
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>
              <td width="50%"><h4 class="red">Loading Day</h4></td>
              <td width="50%">{{date("d-m-Y", strtotime(@$order->loading_day))}}</td>
            </tr>
            </tbody>
          </table>
        </div>

      </div>

    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="card card-body">
          <table data-toggle="table" data-height="250" class="table-striped table-bordered">
            <tbody>
            <tr>
              <td>Product</td>
              <td>Type</td>
              <td>Caliper</td>
              <td>Nature</td>
              <td>Packaging</td>
              <td>Quality</td>
              <td>Tare</td>
            </tr>
            <tr>
              <td>{{@$order->product}}</td>
              <td>{{@$order->type}}</td>
              <td>{{@$order->caliper}}</td>
              <td>{{@$order->nature}}</td>
              <td>{{@$order->packaging}}</td>
              <td>{{@$order->quality}}</td>
              <td>{{@$order->tare}}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="card card-body">
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>
              <td><h4>Price on departure</h4></td>
              <td><span class="red"> EURO</span> {{@$order->price_at_departure}}<span class="red" class="float-right"> x Ton</span>
              </td>
            </tr>
            <tr>
              <td><h4>Price on arrival</h4></td>
              <td><span class="red"> EURO</span> {{@$order->price_at_arriaval}}<span class="red" class="float-right"> x Ton</span>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="col-md-6">
        <div class="card card-body">
          {{-- <h4><span class="red">Our Commission</span>
              <span class="red"> EURO</span> {{@$order->euro_x_kg}}<span class="red"> x KG</span>
          </h4> --}}
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>
              <td><h4>Our Commission</h4></td>
              <td><span class="red"> EURO</span> {{@$order->euro_x_kg}}<span class="red"> x Ton</span></td>
            </tr>
            </tbody>
          </table>

        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-4">
        <div class="card card-body">
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>
              <td><h5>Total our Commission</h5></td>
              <td><span class="red"> EURO</span> {{@$order->total_commission}}<span class="red"> x Ton</span></td>

            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card card-body">
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>

              <td><h4>Exchange Rate</h4></td>
              <td><span class="red"> EURO</span> {{@$order->exchange_rate}}<span class="red"> x KG</span></td>

            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card card-body">
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>
              <td><h4>Swiss Francs</h4></td>
              <td><span class="red"> EURO</span> {{@$order->swiss_francs}}<span class="red"> x KG</span></td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>


    </div>


    <div class="row">
      <div class="col-md-12">
        <div class="card card-body">
          <h4 class="text-center red"><b><span>NOTA</span></b></h4>
          <table data-toggle="table" data-height="250" class="table-bordered">
            <tbody>
            <tr>
              <td><h4>Discount Euro</h4></td>
              <td><span class="red"> EURO</span> {{@$order->rebate_granted}}<span class="red"> x KG</span></td>
              <td><h4>Transporter</h4></td>
              <td>{{@$order->transporter->name}}, {{@$order->transporter->address}}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

  </div>

@endsection
