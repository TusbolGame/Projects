@extends('layouts.admin')
@section('content')
  @php $count = count($orders); @endphp
  @if($count > 0)
    <div class="row mt-3">
      <div class="col-md-12">
        <div class="card">
          <div class="card-body">
            <h4 class="noprint">@lang('main.filter')</h4>
            <div class="card noprint">
              <div class="card-body">
                <form id="form" action="{{route('home.getreport')}}" method="POST">
                  @csrf()
                  <div class="row">
                    <div class="col-md-3">
                      <div class="form-group">
                        <label for="">@lang('main.selectSeller')</label>
                        <select name="id" id="customer-selector" class="js-example-basic-single form-control">
                          <option value="all" disabled selected></option>
                          @foreach (@$clients as $client)
                            @if (@$client['id'] == $customer_info)
                              <option value="{{$client['id']}}" selected>{{@$client['business_name']}}</option>
                            @else
                              <option value="{{$client['id']}}">{{@$client['business_name']}}</option>
                            @endif
                          @endforeach
                        </select>
                      </div>
                    </div>
                    <div class="col-md-3">
                      <div class="form-group">
                        <label for="">@lang('main.selectMode')</label>
                        <select name="mode" id="mode-selector" class="js-example-basic-single form-control">
                          @if($mode=='1')
                            <option disabled></option>
                            <option value="0"> Seller</option>
                            <option value="1" selected> Buyer</option>
                          @elseif($mode=='0')
                            <option disabled></option>
                            <option value="0" selected> Seller</option>
                            <option value="1"> Buyer</option>
                          @else
                            <option disabled selected></option>
                            <option value="0"> Seller</option>
                            <option value="1"> Buyer</option>
                          @endif
                        </select>
                      </div>
                    </div>
                    <div class="col-md-6">
                      <label for="">@lang('main.or')</label>
                      <div class="form-group">
                        <button type="submit" value="Search" class="mr-1 ml-1 md-1 btn btn-primary">
                          <i class="fa fa-search">&nbsp;</i> @lang('main.search')
                        </button>
                        <a class="btn btn-success mr-1 ml-1 md-1" href="{{url('/home')}}" id="all-button">
                          <i class="fas fa-plus-square">&nbsp; </i> Tutti gli ordini</a>
                        <a href="{{route('home.orders_email_show', $orders[0]->seller->business_name )}}" class="btn btn-danger mr-1 ml-1 md-1" id="email-button">
                          <i class="fa fa-envelope">&nbsp;</i>Invia e-mail elenco ordini
                        </a>
                      </div>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>

        </div>
        <div class="card">
          <div class="card-body">
            <div class="table-responsive">
              <table id="hometable1" class="display nowrap table table-hover table-striped table-bordered"
                     cellspacing="0" width="100%">
                <thead>
                <tr>
                  <th>@lang('main.positionNumber')</th>
                  <th>@lang('main.nation')</th>
                  <th>@lang('main.seller')</th>
                  <th>@lang('main.buyer')</th>
                  <th>@lang('main.product')</th>
                  <th>@lang('main.weight') (Kg)</th>
                  <th>@lang('main.price')</th>
                  <th>@lang('main.status')</th>
                  <th>@lang('main.action')</th>
                </tr>
                </thead>
                <tfoot>
                <tr style="padding:0px;">
                  <th>@lang('main.positionNumber')</th>
                  <th>@lang('main.nation')</th>
                  <th>@lang('main.seller')</th>
                  <th>@lang('main.buyer')</th>
                  <th>@lang('main.product')</th>
                  <th>@lang('main.weight') (Kg)</th>
                  <th>@lang('main.price')</th>
                  <th>@lang('main.status')</th>
                  <th>@lang('main.action')</th>
                </tr>
                </tfoot>
                <tbody>


                @foreach ($orders as $order)
                  <tr>
                    <td>{{ @$order->number }}</td>
                    <td>{{ @$order->nation_code }}</td>

                    <input type="hidden" name="condition" value="0">
                    <td>
                      <span type="submit" class="badge badge-danger"
                            style="color: white; background-color: red">{{@$order->seller->business_name}}</span>
                    </td>

                    <input type="hidden" name="condition" value="1">
                    <td>
                      <span type="submit" class="badge badge-danger"
                            style="color: white; background-color: blue">{{@$order->buyer->business_name}}</span>
                    </td>

                    <td>{{@$order->product}}</td>
                    <td>{{@$order->weight_invoiced_kg}}</td>
                    <td>{{@$order->amount_in_euro}}</td>
                    <td>

                      <div class="switch" style="display: inline-block">
                        @if (@$order->invoice->payment_status == '1')
                          <form method="POST"
                                action="{{route('home_payment', $order->invoice->id)}}"
                                id="form{{$order->invoice->id}}">
                            @csrf
                            {{-- @method('PUT') --}}
                            <input type="hidden" name="invoice_id"
                                   value="{{$order->invoice->id}}">
                            <label>
                              <input type="checkbox" class="checkbox"
                                     checked name="payment_status"
                                     onchange="$('#form{{$order->invoice->id}}').submit();"
                                     value="1"><span class="lever"></span></label>
                          </form>
                        @else
                          <form method="POST"
                                action="{{route('home_payment', $order->invoice->id)}}"
                                id="form{{$order->invoice->id}}">
                            @csrf
                            {{-- @method('PUT') --}}
                            <input type="hidden" name="invoice_id"
                                   value="{{$order->invoice->id}}">
                            <label>
                              <input type="checkbox" class="checkbox"
                                     name="payment_status"
                                     onchange="$('#form{{$order->invoice->id}}').submit();"
                                     value="1"><span class="lever"></span></label>
                          </form>
                        @endif

                      </div>
                      @if (@$order->invoice->payment_status == '1')
                        <span class="badge badge-success">@lang('main.paid')</span>
                      @else
                        <span class="badge badge-danger">@lang('main.toPaid')</span>
                      @endif

                    </td>
                    <td>
                      <a href="{{route('home.order_email_show',$order)}}"
                         class="btn btn-success mr-1 btn-sm" id="email-button">
                        <i class="fa fa-envelope">&nbsp;</i>@lang('main.sendMail')</a>
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
                            <form method="post" action="{{route('home.updateComplete', $order)}}" id="asdf">
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
                                      <input class="form-control" name="seller_id" readonly="readonly" value="{{@$order->seller_id}}">
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
                                <input type="submit" class="btn btn-success"
                                       value="Modifica Order">
                                <button type="button" class="btn btn-warning"
                                        data-dismiss="modal">Vicina
                                </button>
                              </div>
                            </form>
                          </div>
                        </div>
                      </div>
                      {{--  this is for permanant delete  --}}
                      <a class="btn btn-danger  btn-sm" style="color: white" id="delete"><i
                            class="fa fa-minus"> </i> @lang('main.delete')</a>
                      <input type="hidden" value="{{route('home.order_delete',$order)}}"
                             id="address">
                    </td>
                  </tr>
                @endforeach

                </tbody>
              </table>
            </div>
          </div>
          {{-- <h6 class="card-subtitle">Export data to Copy, Excel, PDF & @lang('main.print')</h6> --}}

        </div>

      </div>
    </div>
    <!-- ============================================================== -->
    <!-- End PAge Content -->
    <!-- ============================================================== -->
  @else
    <div class="card card-body">
      No data!
    </div>

  @endif
@endsection

@section('scripts')
  @include('api.datatable-init')

  <script>
    $(document).ready(function () {
      $('.js-example-basic-single').select2({
        // tags: true,
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

      $('#delete').on('click', function () {
        var address = $('#address').val();
        var con = confirm("Are you going to delete?");
        if (con == true) {
          window.location.href = address;
        }

      })
    });
  </script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/js/select2.min.js"></script>
  <script>
    $(document).ready(function () {
      $('.js-example-basic-single').select2({
        tags: true
      });

      if ($('#mode-selector').val() == null) {
        $('#all-button').css("display", "none");
        $('#email-button').css("display", "none");
      }
      $("#email-button").on("click", function () {
        $('#form').attr('action', "{{route('invoice.email_table')}}");
        $("#form").submit();
        e.preventDefault();
      });


    });
  </script>
  <style>

  </style>

@endsection
