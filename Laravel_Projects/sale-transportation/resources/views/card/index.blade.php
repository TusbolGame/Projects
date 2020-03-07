@extends('layouts.admin')
@section('content')

  {{-- table starts here --}}
  <div class="row mt-3">
    <div class="col-12">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">cards</h4>
          <h6 class="card-subtitle">Export data to Copy, Excel, PDF & @lang('main.print')</h6>
          <div class="table-responsive">
            <table id="myTable" class="display nowrap table table-hover table-striped table-bcarded" cellspacing="0"
                   width="100%">
              <thead>
              <tr>
                <th>Sr</th>
                <th>Business Name</th>
                <th>position number</th>
                <th>contract number</th>
                <th>Lorry reg #/s</th>
                <th>supplier invoice number</th>
                <th>supplier @lang('main.invoiceDate')</th>
                <th>product name</th>
                <th>amount</th>
                <th>Actions</th>
              </tr>
              </thead>
              <tfoot>
              <tr>
                <th>Sr</th>
                <th>Business Name</th>
                <th>position number</th>
                <th>contract number</th>
                <th>Lorry reg #/s</th>
                <th>supplier invoice number</th>
                <th>supplier @lang('main.invoiceDate')</th>
                <th>product name</th>
                <th>amount</th>
                <th>Actions</th>
              </tr>
              </tfoot>
              <tbody>
              @php
                $i=1;
              @endphp
              @foreach ($cards as $card)
                <tr>
                  <td>{{$i}}@php $i++ @endphp</td>
                  <td>{{ @$card->business_name }}</td>
                  <td>{{ @$card->position_number }}</td>
                  <td>{{ @$card->contract_number }}</td>
                  <td>{{ @$card->lorry_reg_number}}
                    {{-- @foreach (json_decode($card->lorry_reg_number) as $item)
                        @php
                            echo($item).' || ';
                        @endphp
                    @endforeach --}}

                  </td>
                  <td>{{ @$card->supplier_invoice_number }}</td>
                  <td>{{ @$card->supplier_invoice_date }}</td>
                  <td>{{ @$card->product_name }}</td>
                  <td>{{ @$card->amount }}</td>
                  {{-- Y-m-d Format --}}
                  {{-- <td>{{ date('d-m-Y', strtotime($card->created_at)) }}</td> --}}
                  <td>
                    {{-- ********* VIEW STARTS HERE********** --}}
                    <button type="button" class="btn btn-info btn-sm" data-toggle="modal"
                            data-target="#viewcard{{$card->id}}" data-whatever="@mdo">View
                    </button>
                    <div class="modal fade" id="viewcard{{$card->id}}" tabindex="-1" role="dialog"
                         aria-labelledby="viewcard{{$card->id}}Label1">
                      <div class="modal-dialog modal-xl" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h4 class="modal-title" id="viewcard{{$card->id}}Label1">View card</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                  aria-hidden="true">&times;</span></button>
                          </div>
                          <form method="POST" action="#">
                            {{-- @csrf
                            @method('PUT') --}}
                            <div class="modal-body">
                              <div class="row">
                                <div class="col-md-6">
                                  <div class="form-group">
                                    <h5>Business Name </h5>
                                    <input type="text" class="form-control" disabled value="{{$card->business_name}}">
                                  </div>
                                </div>

                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>Position Number </h5>
                                    <input type="text" disabled value="{{$card->position_number}}" class="form-control">
                                  </div>
                                </div>

                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>Contract Number </h5>
                                    <input type="text" disabled value="{{$card->contract_number}}" class="form-control">
                                  </div>
                                </div>

                              </div>
                              <div class="row">

                                <div class="col-md-6">
                                  <div class="form-group">
                                    <h5>Lorry Reg Numbers (Type and enter)</h5>
                                    <input type="text" disabled value="{{$card->lorry_reg_number}}"
                                           class="form-control">

                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>supplier invoice number </h5>
                                    <input type="text" disabled value="{{$card->supplier_invoice_number}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-3">
                                  <div class="form-group">
                                    <h5>supplier @lang('main.invoiceDate') </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->supplier_invoice_date}}"
                                           class="form-control">
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>product_name </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->product_name}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>variety </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->variety}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>caliper </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->caliper}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>nature </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->nature}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>packing </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->packing}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>quality </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->quality}}"
                                           class="form-control">
                                  </div>
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>price_on_departure </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->price_on_departure}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>price_on_arrival </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->price_on_arrival}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>weight </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->weight}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>amount </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->amount}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>amount_commission_CHF </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->amount_commission_CHF}}"
                                           class="form-control">
                                  </div>
                                </div>
                                <div class="col-md-4">
                                  <div class="form-group">
                                    <h5>amount_commission_EURO </h5>
                                    <input type="text" id="mdate" disabled value="{{$card->amount_commission_EURO}}"
                                           class="form-control">
                                  </div>
                                </div>
                              </div>

                            </div>
                            <div class="modal-footer">
                              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                          </form>
                        </div>
                      </div>
                    </div>
                    <!-- /.modal -->
                    {{-- ********* VIEW ENDS HERE *********** --}}


                    {{-- ********* EDIT STARTS HERE********** --}}
                    <button type="button" class="btn btn-success btn-sm" data-toggle="modal"
                            data-target="#updatecard{{$card->id}}" data-whatever="@mdo">@lang('main.edit')
                    </button>
                    <div class="modal fade" id="updatecard{{$card->id}}"
                         {{-- tabindex="-1"  --}}
                         role="dialog" aria-labelledby="updatecard{{$card->id}}Label1">
                      <div class="modal-dialog modal-xl" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h4 class="modal-title" id="updatecard{{$card->id}}Label1">Update card</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                  aria-hidden="true">&times;</span></button>
                          </div>
                          <form method="POST" action="{{route('card.update', $card)}}">
                            @csrf
                            @method('PUT')
                            <div class="modal-body">
                              <div class="row">
                                <div class="col-md-6">
                                  <label for="">Supplier</label><br>
                                  <select class="js-example-basic-single form-control"
                                          style="width: 100%"
                                          name="supplier_id"
                                          data-validation="required"
                                          data-validation-error-msg="This is a required field!"
                                          required>
                                  @foreach ($clients as $client)
                                    @if (@$card->business_name == $client->business_name)
                                      <option value="{{$client->id}}" selected>{{$client->business_name}}</option>
                                    @else
                                      <option value="{{$client->id}}">{{$client->business_name}}</option>
                                      @endif
                                      @endforeach
                                      </select>
                                </div>
                                <div class="col-md-6">
                                  <label for="">Customer</label><br>
                                  <select class="js-example-basic-single form-control"
                                          style="width: 100%"
                                          name="customer_id"
                                          data-validation="required"
                                          data-validation-error-msg="This is a required field!"
                                          required>
                                  @foreach ($clients as $client)
                                    @if (@$card->business_name == $client->business_name)
                                      <option value="{{$client->id}}" selected>{{$client->business_name}}</option>
                                    @else
                                      <option value="{{$client->id}}">{{$client->business_name}}</option>
                                      @endif
                                      @endforeach
                                      </select>

                                </div>


                                <div class="col-md-6">
                                  <label for="">Lorry Reg #/s</label><br>
                                  <input type="text" class="form-control" name="lorry_reg_number"
                                         value="{{ @$card->lorry_reg_number }}">
                                </div>

                                <div class="col-md-3">
                                  <label for="">Rate currency exchange</label><br>
                                  <input type="text" class="form-control" name="rate_currency_exchange"
                                         value="{{ @$card->rate_currency_exchange }}">
                                </div>

                                <div class="col-md-3">
                                  <label for="">Tare</label><br>
                                  <input type="text" class="form-control" name="tare"
                                         value="{{ @$card->tare }}">
                                </div>
                              </div>
                              <div class="row">
                                <div class="col-md-4">
                                  <label for="">Commission</label><br>
                                  <input type="text" class="form-control" name="commission"
                                         value="{{ @$card->commission }}">
                                </div>
                                <div class="col-md-4">
                                  <label for="">Nr Commission</label><br>
                                  <input type="text" class="form-control" name="nr_commission"
                                         value="{{ @$card->nr_commission }}">
                                </div>
                                <div class="col-md-4">
                                  <label for="">Total Commission</label><br>
                                  <input type="text" class="form-control" name="total_commission"
                                         value="{{ @$card->total_commission }}">
                                </div>

                              </div>
                            </div>
                            <div class="modal-footer">
                              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                              <input type="submit" class="btn btn-primary" value="Update card">
                            </div>
                          </form>
                        </div>
                      </div>
                    </div>
                    <!-- /.modal -->
                    {{-- ********* EDIT ENDS HERE *********** --}}

                    {{--  this is for permanant delete  --}}
                    <a class="btn btn-danger btn-sm" href="javascript:;" onclick="confirmDelete('{{$card->id}}')">@lang('main.delete')</a>
                    <form id="delete-card-{{$card->id}}"
                          action="{{ route('card.destroy', $card->id) }}"
                          method="POST" style="display: none;">
                      @csrf
                      @method('DELETE')
                    </form>
                  </td>
                </tr>
              @endforeach
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>
  </div>
  <!-- ============================================================== -->
  <!-- End PAge Content -->
  <!-- ============================================================== -->
@endsection

@section('scripts')

  @include('api.datatable-init')

  <script type="text/javascript">
    function confirmDelete(id) {
      let choice = confirm("Are You sure, You want to Delete this record ?")
      if (choice) {
        document.getElementById('delete-card-' + id).submit();
      }
    }
  </script>
  {{-- Select2 Library Start --}}
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/css/select2.min.css" rel="stylesheet"/>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/js/select2.min.js"></script>
  {{-- // In your Javascript (external .js resource or <script> tag) --}}
  <script>
    $(document).ready(function () {
      $('.js-example-basic-single').select2({
        tags: true
      });

      $('.lorry_reg_number-multiple').select2({
        tags: true
      });


    });
  </script>
  {{-- Select2 Library Ends --}}
@endsection
