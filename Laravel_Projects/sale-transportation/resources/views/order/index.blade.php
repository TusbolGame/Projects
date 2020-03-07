@extends('layouts.admin')
@section('content')
  <style>
    hr {
      height: 3px;
    }

    h5 {
      font-style: italic;
      color: red;
      font-family: "Helvetica Neue Light", "HelveticaNeue-Light", "Helvetica Neue", Calibri, Helvetica, Arial, sans-serif;
    }

    h4 {
      margin-top: 20px !important;
    }

    .basic {
      margin: 0 30px 0 30px;
    }

    input {
      font-size: 20px;
    }

    .form-control {
      display: block;
      width: 100%;
      height: calc(1.5em + .75rem + 2px);
      padding: .375rem .75rem;
      font-size: 1.2rem;
      font-weight: 200;
      line-height: 1.5;
      color: black;
      background-color: #fff;
      background-clip: padding-box;
      border: 2px solid #aaa;
      border-radius: .25rem;
      transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
    }

    .select2 .select2-selection--single {
      background-color: #fff;
      border: 2px solid #aaa;
      border-radius: 4px;
      font-size: 1.2rem;
      font-weight: 200;
      line-height: 1.5;
    }

    .btn-submit {
      background-image: linear-gradient(darkgreen, green, darkgreen) !important;
      color: white;
      -webkit-box-shadow: 0 2px 2px 0 rgba(40, 190, 189, 0.14), 0 3px 1px -2px rgba(40, 190, 189, 0.2), 0 1px 5px 0 rgba(40, 190, 189, 0.12);
      box-shadow: 0 2px 2px 0 rgba(40, 190, 189, 0.14), 0 3px 1px -2px rgba(40, 190, 189, 0.2), 0 1px 5px 0 rgba(40, 190, 189, 0.12);
      -webkit-transition: 0.2s ease-in;
      -o-transition: 0.2s ease-in;
      transition: 0.2s ease-in;
      font-size: 25px;
      margin-left: 50%;
      margin-top: 30px;
      margin-bottom: 10px;
      padding: 10px 20px 10px 20px;

    }

    .btn-submit:hover {
      color: white;
      background-image: linear-gradient(darkred, red, darkred) !important;
      -webkit-box-shadow: 0 14px 26px -12px rgba(40, 190, 189, 0.42), 0 4px 23px 0 rgba(0, 0, 0, 0.12), 0 8px 10px -5px rgba(40, 190, 189, 0.2);
      box-shadow: 4px 4px 8px grey;

    }

  </style>
  {{-- table starts here --}}
  <div class="row mt-3">
    <div class="col-12">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">Gestisci posizione</h4>
          <div class="card-body">
            <form id="form" action="{{route('order.getReport')}}" method="post">
              @csrf()
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="">Customer</label>
                    <select name="name" id="customer-selector"
                            class="js-example-basic-single form-control">
                      <option value="all" disabled selected></option>
                      @foreach($clients as $client)
                        <option value="{{$client['id']}}">{{$client['business_name']}}</option>
                      @endforeach
                    </select>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="">Mode</label>
                    <select name="mode" id="selector"
                            class="js-example-basic-single form-control">
                      <option value="all" disabled selected></option>
                      <option value="seller">seller</option>
                      <option value="buyer">buyer</option>
                    </select>
                  </div>
                </div>

                <div class="col-md-6">
                  <label for="">@lang('main.or')</label>
                  <div class="form-group">
                    <button type="submit" value="Search" class="mr-1 ml-1 md-1 btn btn-primary"><i
                          class="fa fa-search">&nbsp;</i> @lang('main.search')
                    </button>

                    <a class="btn btn-success mr-1 ml-1 md-1" href="{{url('/order')}}"
                       id="all-button">
                      <i class="fas fa-plus-square">&nbsp </i> Tutti gli ordini</a>
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="table-responsive">
            <table id="myTable" class="display nowrap table table-hover table-striped table-bordered"
                   cellspacing="0" width="100%">
              <thead>
              <tr>
                <th>Nr.</th>
                <th>@lang('main.seller')</th>
                <th>@lang('main.buyer')</th>
                <th>@lang('main.producer')</th>
                <th>@lang('main.contactNumber')</th>
                <th>@lang('main.product')</th>
                <th>@lang('main.type')</th>
                <th>@lang('main.caliber')</th>
                <th>@lang('main.startPrice')</th>
                <th>@lang('main.action')</th>
              </tr>
              </thead>
              <tfoot>
              <tr>
                <th>Nr.</th>
                <th>@lang('main.seller')</th>
                <th>@lang('main.buyer')</th>
                <th>@lang('main.producer')</th>
                <th>@lang('main.contactNumber')</th>
                <th>@lang('main.product')</th>
                <th>@lang('main.type')</th>
                <th>@lang('main.caliber')</th>
                <th>@lang('main.startPrice')</th>
                <th>@lang('main.action')</th>
              </tr>
              </tfoot>
              <tbody>
              @php
                $i=1;
              @endphp
              @foreach ($orders as $order)
                <tr>
                  <td>{{ @$order->number}}</td>
                  <td>{{ @$order->seller->business_name }}</td>
                  <td>{{ @$order->buyer->business_name }}</td>
                  <td>{{ @$order->transporter->name }}</td>
                  <td>{{ @$order->product}}</td>
                  <td>{{ @$order->type }}</td>
                  <td>{{ @$order->caliper }}</td>
                  <td>{{ @$order->price_at_departure }}</td>
                  <td>{{ date('d-m-Y', strtotime($order->created_at)) }}</td>
                  <td>
                    {{-- ********* EDIT STARTS HERE********** --}}
                    <button onclick="update({{@$order->seller->id}})" id="" type="button" class="btn btn-primary btn-sm"
                            data-toggle="modal"
                            data-target="#updateorder{{$order->id}}" data-whatever="@mdo">@lang('main.edit')
                    </button>
                    <div class="modal fade" id="updateorder{{$order->id}}" role="dialog"
                         aria-labelledby="updateorder{{$order->id}}Label1">
                      <div class="modal-dialog modal-xl" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h4 class="modal-title" id="updateorder{{$order->id}}Label1">Posizione di aggiornamento</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                          </div>
                          <input type="hidden" value="{{route('order.update', $order)}}" id="action">
                          <form method="post" action="{{route('order.updateComplete', $order)}}" id="asdf">
                            @csrf
                            <div class="modal-body">
                              <div class="row">
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O1. @lang('main.nation')</h5>
                                    <select name="nation_code" id="" class="form-control">
                                      @if (@$order->nation_code == "FR")
                                        <option value="FR" selected>Francia</option>
                                        <option value="DE">Germania</option>
                                        <option value="B">Belgio</option>
                                        <option value="IT">Italia</option>
                                        <option value="A">Australia</option>
                                      @elseif(@$order->nation_code == "FR")
                                        <option value="FR">Francia</option>
                                        <option value="DE" selected>Germania
                                        </option>
                                        <option value="B">Belgio</option>
                                        <option value="IT">Italia</option>
                                        <option value="A">Australia</option>
                                      @elseif(@$order->nation_code == "DE")
                                        <option value="FR">Francia</option>
                                        <option value="DE">Germania</option>
                                        <option value="B" selected>Belgio
                                        </option>
                                        <option value="IT">Italia</option>
                                        <option value="A">Australia</option>
                                      @elseif(@$order->nation_code == "IT")
                                        <option value="FR">Francia</option>
                                        <option value="DE">Germania</option>
                                        <option value="B">Belgio</option>
                                        <option value="IT" selected>Italia
                                        </option>
                                        <option value="A">Australia</option>
                                      @elseif(@$order->nation_code == "A")
                                        <option value="FR">Francia</option>
                                        <option value="DE">Germania</option>
                                        <option value="B">Belgio</option>
                                        <option value="IT">Italia</option>
                                        <option value="A" selected>Australia
                                        </option>
                                      @elseif(@$order->nation_code == "B")
                                        <option value="FR">Francia</option>
                                        <option value="DE">Germania</option>
                                        <option value="B" selected>Belgio</option>
                                        <option value="IT">Italia</option>
                                        <option value="A">Australia
                                        </option>
                                      @else
                                        <option value="" style="font-size: 1px" selected disabled></option>
                                        <option value="FR">Francia</option>
                                        <option value="DE">Germania</option>
                                        <option value="B">Belgio</option>
                                        <option value="IT">Italia</option>
                                        <option value="A">Australia</option>
                                      @endif
                                    </select>
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <h5>O2. @lang('main.code')</h5>
                                  <input id="code" type="text" name="code_nr" class="form-control"
                                         value="{{@$order->code_nr}}" readonly="readonly">
                                </div>

                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O3. @lang('main.number')</h5>
                                    <input type="text" id="number" name="number" class="form-control readonly"
                                           value="{{@$order->number}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O4. @lang('main.loadingDay')</h5>
                                    <input type="text" name="loading_day" id="mdate{{$order->id}}" class="form-control"
                                           value="{{@$order->loading_day}}" required>
                                  </div>
                                </div>
                              </div>

                              <div class="row">
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>O5. @lang('main.seller')</h5>
                                    <select class="js-example-basic-single form-control" name="seller_id"
                                            data-validation="required"
                                            data-validation-error-msg="This is a required field!" required>
                                      @foreach ($clients as $client)
                                        @if (@$order->seller->business_name == $client->business_name)
                                          <option value="{{$client->id}}"
                                                  selected>{{$client->business_name}}</option>
                                        @else
                                          <option value="{{$client->id}}">{{$client->business_name}}</option>
                                        @endif
                                      @endforeach
                                    </select>
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>O6. @lang('main.buyer')</h5>
                                    <select class="js-example-basic-single form-control"
                                            name="buyer_id"
                                            data-validation="required"
                                            data-validation-error-msg="This is a required field!"
                                            required>
                                      @foreach ($clients as $client)
                                        @if (@$order->buyer->business_name == $client->business_name)
                                          <option value="{{$client->id}}"
                                                  selected>{{$client->business_name}}</option>
                                        @else
                                          <option value="{{$client->id}}">{{$client->business_name}}</option>
                                        @endif
                                      @endforeach
                                    </select>
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>O7. @lang('main.producer')</h5>
                                    <select class="js-example-basic-single form-control" name="producer_id">
                                      @foreach ($producers as $producer)
                                        @if (@$order->producer->name == $producer->name)
                                          <option value="{{$producer->id}}" selected>{{$producer->name}}</option>
                                        @else
                                          <option value="{{$producer->id}}">{{$producer->name}}</option>
                                        @endif
                                      @endforeach
                                    </select>
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O8. @lang('main.contactNumber')</h5>
                                    <input type="text" name="contract_nr" class="form-control"
                                           value="{{@$order->contract_nr}}">
                                  </div>
                                </div>

                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O9. @lang('main.invoiceNumber')</h5>
                                    <input type="text" name="invoice_number" class="form-control"
                                           value="{{@$order->invoice_number}}">
                                  </div>
                                </div>

                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O10. @lang('main.invoiceDate')</h5>
                                    <input type="text" id="mdate2{{$order->id}}" name="invoice_date"
                                           class="form-control"
                                           value="{{@$order->invoice_date}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O11. @lang('main.totalEuro')</h5>
                                    <input type="text" name="amount_in_euro" class="form-control"
                                           value="{{@$order->amount_in_euro}}">
                                  </div>
                                </div>

                              </div>
                              <div class="row">
                                <div class="col-md-12">
                                  <div class="form-group">
                                    @php
                                      $lorry_reg_numbers = explode(',',$order->lorry_reg_number);
                                    @endphp
                                    <h5>O12. @lang('main.truckPlate') (inserisci e premi
                                      invio)</h5>
                                    <select class="lorry_reg_number-multiple form-control"
                                            name="lorry_reg_number[]"
                                            multiple="multiple"
                                            required>
                                      @foreach ($lorry_reg_numbers as $lorry_reg_number)
                                        <option value="{{$lorry_reg_number}}" selected>{{$lorry_reg_number}}</option>
                                      @endforeach
                                    </select>
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O13. @lang('main.transporter')</h5>
                                    <select class="js-example-basic-single form-control"
                                            name="transporter_id">
                                      @foreach ($transporters as $transporter)
                                        @if (@$order->transporter->name == $transporter->name)
                                          <option value="{{$transporter->id}}"
                                                  selected>{{$transporter->name}}</option>
                                        @else
                                          <option value="{{$transporter->id}}">{{$transporter->name}}</option>
                                        @endif
                                      @endforeach
                                    </select>
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O14. @lang('main.weightLoaded') (Kg)</h5>
                                    <input type="text" name="weight_loaded_kg"
                                           class="form-control"
                                           value="{{@$order->weight_loaded_kg}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>015. @lang('main.weighBilled') (Kg)</h5>
                                    <input type="text" name="weight_invoiced_kg"
                                           class="form-control"
                                           value="{{@$order->weight_invoiced_kg}}"
                                           id="weight_invoiced_kg{{$order->id}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O16. @lang('main.product')</h5>
                                    <input type="text" name="product"
                                           class="form-control"
                                           value="{{@$order->product}}">
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O17. @lang('main.type')</h5>
                                    <input type="text" name="type"
                                           class="form-control"
                                           value="{{@$order->type}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O18. @lang('main.caliber')</h5>
                                    <input type="text" name="caliper"
                                           class="form-control"
                                           value="{{@$order->caliper}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O19. @lang('main.nature')</h5>
                                    <input type="text" name="nature"
                                           class="form-control"
                                           value="{{@$order->nature}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O20. @lang('main.packaging')</h5>
                                    <input type="text" name="packaging"
                                           class="form-control"
                                           value="{{@$order->packaging}}">
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O21. @lang('main.quality')</h5>
                                    <input type="text" name="quality"
                                           class="form-control"
                                           value="{{@$order->quality}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O22. @lang('main.tara')</h5>
                                    <input type="text" name="tare"
                                           class="form-control"
                                           value="{{@$order->tare}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O23. @lang('main.startPrice')</h5>
                                    <input type="text" name="price_at_departure"
                                           class="form-control"
                                           value="{{@$order->price_at_departure}}">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>O24. @lang('main.arrivalPrice')</h5>
                                    <input type="text" name="price_at_arriaval"
                                           class="form-control"
                                           value="{{@$order->price_at_arriaval}}">
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>O25. @lang('main.ourCommission') (E/Ton)</h5>
                                    <input type="text" name="euro_x_kg"
                                           class="form-control"
                                           value="{{@$order->euro_x_kg}}"
                                           id="our_commission{{$order->id}}">
                                  </div>
                                </div>

                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>O26. @lang('main.totalCommission')</h5>
                                    <input type="text" name="total_commission"
                                           class="form-control readonly"
                                           value="{{@$order->total_commission}}"
                                           id="total_commission{{$order->id}}">
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>O27. @lang('main.rate')</h5>
                                    <input type="text" name="exchange_rate"
                                           class="form-control "
                                           value="{{@$order->exchange_rate}}"
                                           id="exchange_rate{{$order->id}}">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>O28. Totale commissione in CHF</h5>
                                    <input type="text" name="swiss_francs"
                                           class="form-control readonly"
                                           value="{{@$order->swiss_francs}}"
                                           id="swiss_francs{{$order->id}}">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>O29. @lang('main.rebate') (%)</h5>
                                    <input type="text" name="rebate_granted"
                                           class="form-control"
                                           value="{{@$order->rebate_granted}}"
                                           id="rebate{{$order->id}}">
                                  </div>
                                </div>
                              </div>

                            </div>
                            <div class="modal-footer">
                              <div class="btn btn-primary" onclick="save()" id="asdfasdf">@lang('main.save')</div>
                              <input type="submit" class="btn btn-success"
                                     value="Complete Order">
                              <button type="button" class="btn btn-warning"
                                      data-dismiss="modal">Vicina
                              </button>
                            </div>
                          </form>
                          <script>
                            function save() {
                              var aa = $("#asdf :input");
                              console.log(aa);
                              for (var i = 0; i < aa.length; i++) {
                                if (aa[i].id !== "code" && aa[i].id !== "seller_id" && aa[i].id !== "buyer_id") {
                                  console.log(i);
                                  var value = aa[i].value;
                                }
                              }
                              var a = $('#action').val();
                              document.getElementById('asdf').action = a;
                              document.getElementById('asdf').submit();
                            }
                          </script>
                        </div>
                      </div>
                    </div>
                    {{--  this is for permanant delete  --}}
                    <a class="btn btn-danger btn-sm" href="javascript:;"
                       onclick="confirmDelete('{{$order->id}}')">@lang('main.delete')</a>
                    <form id="delete-order-{{$order->id}}"
                          action="{{ route('order.destroy', $order->id) }}"
                          method="POST" style="display: none;">
                      @csrf
                      @method('DELETE')
                    </form>
                  </td>
                </tr>

                <script>

                  $(document).ready(function () {

                    var weight_invoiced_kg = '{{@$order->weight_invoiced_kg}}';
                    var our_commission = '{{@$order->euro_x_kg}}';
                    var exchange_rate = '{{@$order->exchange_rate}}';
                    var total_commission = '{{@$order->total_commission}}';

                    $('#weight_invoiced_kg{{$order->id}}').keyup(function () {
                      weight_invoiced_kg = $('#weight_invoiced_kg{{$order->id}}').val();
                      our_commission = $('#our_commission{{$order->id}}').val();
                      // console.log(weight_invoiced_kg);
                      $('#total_commission{{$order->id}}').val(Math.round(weight_invoiced_kg * our_commission / 1000 * 100) / 100);
                      total_commission = Math.round(weight_invoiced_kg * our_commission / 1000 * 100) / 100;
                      exchange_rate = $('#exchange_rate{{$order->id}}').val();
                      $('#swiss_francs{{$order->id}}').val(Math.round(total_commission * exchange_rate * 100) / 100);
                    });
                    $('#our_commission{{$order->id}}').keyup(function () {
                      our_commission = $('#our_commission{{$order->id}}').val();
                      weight_invoiced_kg = $('#weight_invoiced_kg{{$order->id}}').val();
                      $('#total_commission{{$order->id}}').val(Math.round(weight_invoiced_kg * our_commission / 1000 * 100) / 100);
                      total_commission = Math.round(weight_invoiced_kg * our_commission / 1000 * 100) / 100;
                      exchange_rate = $('#exchange_rate{{$order->id}}').val();
                      $('#swiss_francs{{$order->id}}').val(Math.round(total_commission * exchange_rate * 100) / 100);
                    });
                    $('#exchange_rate{{$order->id}}').keyup(function () {
                      exchange_rate = $('#exchange_rate{{$order->id}}').val();
                      total_commission = $('#total_commission{{$order->id}}').val();
                      $('#swiss_francs{{$order->id}}').val(Math.round(total_commission * exchange_rate * 100) / 100);
                    });


                    $('#mdate{{$order->id}}').bootstrapMaterialDatePicker({weekStart: 0, time: false});
                    $('#mdate2{{$order->id}}').bootstrapMaterialDatePicker({weekStart: 0, time: false});
                    $('#mdate3{{$order->id}}').bootstrapMaterialDatePicker({weekStart: 0, time: false});


                    $('.js-example-basic-single').select2({
                      // tags: true
                      sorter: function (data) {
                        /* Sort data using lowercase comparison */
                        return data.sort(function (a, b) {
                          a = a.text.toLowerCase();
                          b = b.text.toLowerCase();
                          if (a > b) {
                            return 1;
                          } else if (a < b) {
                            return -1;
                          }
                          return 0;
                        });
                      },

                    });

                    $('.lorry_reg_number-multiple').select2({
                      tags: true,

                      sorter: function (data) {
                        /* Sort data using lowercase comparison */
                        return data.sort(function (a, b) {
                          a = a.text.toLowerCase();
                          b = b.text.toLowerCase();
                          if (a > b) {
                            return 1;
                          } else if (a < b) {
                            return -1;
                          }
                          return 0;
                        });
                      },

                    });


                  });
                </script>


              @endforeach
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
@endsection

@section('scripts')
  @include('api.datatable-init')
  <script type="text/javascript">
    function confirmDelete(id) {
      let choice = confirm("Are You sure, You want to Delete this record ?")
      if (choice) {
        document.getElementById('delete-order-' + id).submit();
      }
    }
  </script>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/js/select2.min.js"></script>


  <script>
    function printDiv(divName) {
      var printContents = document.getElementById(divName).innerHTML;
      var originalContents = document.body.innerHTML;
      document.body.innerHTML = printContents;
      window.print();
      document.body.innerHTML = originalContents;
      window.location.href = "{{route('order.index')}}";
    }

    function update(id) {
      $.ajax({
        url: "{{url('/number')}}",
        type: "GET",
        data: {
          id: id,
        },
        success: function (data) {
          $('#cache').val(data.number);
          var year = new Date().getFullYear().toString();
          var code = $('#code').val();
          var pre = year.substring(2);
          if (data.number < 10) {
            $('#number').val(code + pre + '0' + data.number);
          } else {
            $('#number').val(code + pre + data.number);
          }

        }
      });
    }
  </script>

@endsection
