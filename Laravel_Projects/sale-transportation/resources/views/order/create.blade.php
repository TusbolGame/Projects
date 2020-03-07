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
      font-weight: bolder !important;
    }

    .form-control {
      display: block;
      width: 100%;
      height: calc(1.5em + .75rem + 2px);
      padding: .375rem .75rem;
      font-size: 1.2rem;
      font-weight: bolder;
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
      font-weight: bolder;
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

  <div class="row mt-3">
    <div class="col-12">
      <div class="card">
        <div class="card-body">
          <h2 class="card-title" style="font-weight: bold; ">@lang('main.newOrder')</h2>
          <input type="hidden" value="{{route('order.complete')}}" id="action">
          <form class="" method="POST" action="{{route('order.store')}}" id="asdf">
            @csrf
            <h4 style="color:red; padding:0%; margin:0%">@lang('main.basicInformation')</h4>
            <hr style="margin-top:2px">
            <div class="basic">
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O1. @lang('main.nation')</h5>
                    <select name="nation_code" id="" class="form-control" required>
                      <option value="" style="font-size: 1px" selected disabled></option>
                      <option value="A">Austria</option>
                      <option value="B">Belgio</option>
                      <option value="FR">Francia</option>
                      <option value="DE">Germania</option>
                      <option value="IT">Italia</option>
                    </select>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O2. @lang('main.code') </h5>
                    <input type="text" name="code_nr" class="form-control" id="code" required>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O3. @lang('main.number')</h5>
                    <input type="text" name="number" class="form-control readonly"
                           value="" id="number" required>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O4. @lang('main.loadingDay')</h5>
                    <input type="text" name="loading_day" id="mdate" class="form-control" required>
                  </div>
                </div>

              </div>
              <div class="row">
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O5. @lang('main.seller')</h5>
                    <select class="js-example-basic-single form-control" name="seller_id"
                            data-validation="required"
                            id="seller_id"
                            data-validation-error-msg="This is a required field!"
                            required>
                      <option style="font-size: 1px" disabled selected></option>
                      @foreach ($clients as $client)
                        <option value="{{$client->id}}">{{$client->business_name}}</option>
                      @endforeach
                    </select>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O6. @lang('main.buyer')</h5>
                    <select class="js-example-basic-single form-control" name="buyer_id"
                            data-validation="required"
                            id="buyer_id"
                            data-validation-error-msg="This is a required field!"
                            required>
                      <option style="font-size: 1px" disabled selected></option>
                      @foreach ($clients as $client)
                        <option value="{{$client->id}}">{{$client->business_name}}</option>
                      @endforeach
                    </select>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O7. @lang('main.producer')</h5>
                    <select class="js-example-basic-single form-control" name="producer_id"
                            data-validation="" required>
                      <option style="font-size: 1px" disabled selected></option>
                      @foreach ($producers as $producer)
                        <option value="{{$producer->id}}">{{$producer->name}}</option>
                      @endforeach
                    </select>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O8. @lang('main.contactNumber')</h5>
                    <input type="text" name="contract_nr" class="form-control num" required>
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O9. @lang('main.invoiceNumber')</h5>
                    <input type="text" name="invoice_number" class="form-control" required>
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O10. @lang('main.invoiceDate') </h5>
                    <input type="text" id="mdate2" name="invoice_date" class="form-control" required>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O11. @lang('main.totalEuro')</h5>
                    <input type="text" name="amount_in_euro" class="form-control euro" required>
                  </div>
                </div>

              </div>
            </div>

            <h4 style="color:red; padding:0%; margin:0%">@lang('main.transporter') </h4>
            <hr style="margin-top:2px">
            <div class="basic">
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O12. @lang('main.truckPlate') @lang('main.truckexplain')</h5>
                    <select class="lorry_reg_number-multiple form-control" name="lorry_reg_number[]"
                            multiple="multiple" required>
                    </select>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O13. @lang('main.transporter')</h5>
                    <select class="js-example-basic-single form-control" name="transporter_id" required>
                      <option style="font-size: 1px" disabled selected></option>
                      @foreach ($transporters as $transporter)
                        <option value="{{$transporter->id}}">{{$transporter->name}}</option>
                      @endforeach
                    </select>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O14. @lang('main.weightLoaded') (Kg)</h5>
                    <input type="text" name="weight_loaded_kg" class="form-control ton" required>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>015. @lang('main.weighBilled') (Kg)</h5>
                    <input type="text" name="weight_invoiced_kg" id="weight_invoiced_kg"
                           class="form-control ton" required>
                  </div>
                </div>
              </div>
            </div>

            <h4 style="color:red; padding:0%; margin:0%">@lang('main.product')</h4>
            <hr style="margin-top:2px">
            <div class="basic">
              <div class="row">
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O16. @lang('main.product')</h5>
                    <input type="text" name="product" class="form-control" required>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O17. @lang('main.type')</h5>
                    <input type="text" name="type" class="form-control" required>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O18. @lang('main.caliber')</h5>
                    <input type="text" name="caliper" class="form-control" required>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O19. Natura </h5>
                    <input type="text" name="nature" class="form-control" required>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O20. @lang('main.packaging')</h5>
                    <input type="text" name="packaging" class="form-control" required>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O21. @lang('main.quality')</h5>
                    <input type="text" name="quality" class="form-control raty" required>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <h5>O22. @lang('main.tara')</h5>
                    <input type="text" name="tare" class="form-control" required>
                  </div>
                </div>
              </div>
            </div>

            <h4 style="color:red; padding:0%; margin:0%">@lang('main.costDetail')</h4>
            <hr style="margin-top:2px">
            <div class="basic">
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <h5>O23. @lang('main.startPrice')</h5>
                    <input type="text" name="price_at_departure" class="form-control euro" required>
                  </div>
                </div>
                <div class="col-md-6">
                  <div class="form-group">
                    <h5>O24. @lang('main.arrivalPrice')</h5>
                    <input type="text" name="price_at_arriaval" class="form-control euro" required>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O25. @lang('main.ourCommission') (E/Ton)</h5>
                    <input type="text" name="euro_x_kg" class="form-control" id="our_commission" required>
                  </div>
                </div>

                <div class="col-md-8">
                  <div class="form-group">
                    <h5>O26. @lang('main.totalCommission')</h5>
                    <input type="text" name="total_commission" id="total_commission"
                           class="form-control readonly" required>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O27. @lang('main.rate')</h5>
                    <input type="text" name="exchange_rate" class="form-control" id="exchange_rate" required>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O28. Totale commissione in CHF</h5>
                    <input type="text" name="swiss_francs" class="form-control readonly"
                           id="swiss_francs" required>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="form-group">
                    <h5>O29. @lang('main.rebate') (%) </h5>
                    <input type="text" id="rebate" name="rebate_granted" class="form-control" required>
                  </div>
                </div>
              </div>
            </div>
            <div class="text-center">
              <div id="saveButton" class="btn btn-warning btn-lg m-3">@lang('main.save')</div>
              <button id="submit_order" type="submit" class="btn btn-success btn-lg m-3">@lang('main.complete')</button>
            </div>
          </form>
        </div>
      </div>
      <!-- ============================================================== -->
      <!-- End PAge Content -->
      <!-- ============================================================== -->
      <input type="hidden" id="cache">
    </div>
    <!-- ============================================================== -->
    <!-- End Container fluid  -->
    <!-- ============================================================== -->
  </div>


@endsection



@section('scripts')


  {{-- Select2 Library Start --}}
  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/js/select2.min.js"></script>
  {{-- // In your Javascript (external .js resource or <script> tag) --}}
  <script>
    $(document).ready(function () {

      var weight_invoiced_kg = '{{@$order->weight_invoiced_kg}}';
      var our_commission = '{{@$order->euro_x_kg}}';
      var exchange_rate = '{{@$order->exchange_rate}}';
      var total_commission = '{{@$order->total_commission}}';

      $('#weight_invoiced_kg').keyup(function () {
        weight_invoiced_kg = $('#weight_invoiced_kg').val();
        our_commission = $('#our_commission').val();
        // console.log(weight_invoiced_kg);
        $('#total_commission').val(Math.round(weight_invoiced_kg * our_commission / 1000 * 100) / 100);
        total_commission = Math.round(weight_invoiced_kg * our_commission / 1000 * 100) / 100;
        exchange_rate = $('#exchange_rate').val();
        $('#swiss_francs').val(Math.round(total_commission * exchange_rate * 100) / 100);
      });
      $('#our_commission').keyup(function () {
        our_commission = $('#our_commission').val();
        weight_invoiced_kg = $('#weight_invoiced_kg').val();
        $('#total_commission').val(Math.round(weight_invoiced_kg * our_commission / 1000 * 100) / 100);
        total_commission = Math.round(weight_invoiced_kg * our_commission / 1000 * 100) / 100;
        exchange_rate = $('#exchange_rate').val();
        $('#swiss_francs').val(Math.round(total_commission * exchange_rate * 100) / 100);
      });
      $('#exchange_rate').keyup(function () {
        exchange_rate = $('#exchange_rate').val();
        total_commission = $('#total_commission').val();
        $('#swiss_francs').val(Math.round(total_commission * exchange_rate * 100) / 100);
      });


      $('#mdate').bootstrapMaterialDatePicker({weekStart: 0, time: false});
      $('#mdate2').bootstrapMaterialDatePicker({weekStart: 0, time: false});
      $('#mdate3').bootstrapMaterialDatePicker({weekStart: 0, time: false});


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
  <script>
    $('#seller_id').on('change', function () {
      var id = $('#seller_id').val();
      $.ajax({
        url: "{{url('/number')}}",
        type: "GET",
        data : {
          id: id,
        },
        success: function (data) {
          $('#cache').val(data.number);
          var year = new Date().getFullYear().toString();
          var code = $('#code').val();
          var pre = year.substring(2);
          if(data.number<10){
            $('#number').val(code+pre+'0'+data.number);
          }else{
            $('#number').val(code+pre+data.number);
          }

        }
      });
    });

    $('#code').keyup(function () {
      var year = new Date().getFullYear().toString();
      var pre = year.substring(2);
      var number = $('#cache').val();
      var code = $('#code').val();
      if(number<10){
        $('#number').val(code+pre+'0'+number);
      }else{
        $('#number').val(code+pre+number);
      }
    });

    $('#saveButton').on('click', function () {

      var aa=$("#asdf :input");
      console.log(aa);
      for(var i=0;i<aa.length;i++){
        if(aa[i].id!=="code"&&aa[i].id!=="seller_id"&&aa[i].id!=="buyer_id"){
          console.log(i);
          var value = aa[i].value;
        }

      }
      var code = $('#code').val();
      var seller = $("#seller_id").val();
      var buyer = $("#buyer_id").val();
      if(code===""||seller===""||buyer===""){
        return alert('You have to enter seller and code, buyer!');
      }
      var a = $('#action').val();
      document.getElementById('asdf').action = a;
      var delayInMilliseconds = 100; //1 second
      var con = confirm("Would you save these data?");
      if(con===true){
        document.getElementById('asdf').submit();
      }

    })
  </script>

  @include('api.formvalidator-init')
@endsection
