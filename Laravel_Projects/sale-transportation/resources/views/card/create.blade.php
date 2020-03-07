@extends('layouts.admin')
@section('content')

  <div class="row mt-3">
    <div class="col-12">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">New card</h4>
          <h6 class="card-subtitle">Create new card</h6>
          <hr>
          <form class="" method="POST" action="{{route('card.store')}}">
            @csrf
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <h5>Business Name </h5>
                  <select class="js-example-basic-single form-control" name="business_name"
                          data-validation="required"
                          data-validation-error-msg="This is a required field!"
                          required>
                    <option disabled selected>Select an option</option>
                    @foreach ($clients as $client)
                      <option value="{{$client->business_name}}">{{$client->business_name}}</option>
                    @endforeach
                    @foreach ($transporters as $transporter)
                      <option value="{{$transporter->name}}">{{$transporter->name}}</option>
                    @endforeach

                  </select>
                </div>
              </div>

              <div class="col-md-3">
                <div class="form-group">
                  <h5>Position Number </h5>
                  <input type="text" name="position_number" class="form-control">
                </div>
              </div>

              <div class="col-md-3">
                <div class="form-group">
                  <h5>Contract Number </h5>
                  <input type="text" name="contract_number" class="form-control">
                </div>
              </div>

            </div>
            <div class="row">

              <div class="col-md-6">
                <div class="form-group">
                  <h5>Lorry Reg Numbers (Type and enter)</h5>
                  <select class="lorry_reg_number-multiple form-control" name="lorry_reg_number"
                          multiple="multiple"
                          data-validation="required"
                          data-validation-error-msg="This is a required field!"
                          required>
                  </select>

                </div>
              </div>
              <div class="col-md-3">
                <div class="form-group">
                  <h5>supplier invoice number </h5>
                  <input type="text" name="supplier_invoice_number" class="form-control">
                </div>
              </div>
              <div class="col-md-3">
                <div class="form-group">
                  <h5>supplier @lang('main.invoiceDate') </h5>
                  <input type="text" id="mdate" name="supplier_invoice_date" class="form-control">
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <h5>product name </h5>
                  <input type="text" id="mdate" name="product_name" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>variety </h5>
                  <input type="text" id="mdate" name="variety" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>caliper </h5>
                  <input type="text" id="mdate" name="caliper" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>nature </h5>
                  <input type="text" id="mdate" name="nature" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>packing </h5>
                  <input type="text" id="mdate" name="packing" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>quality </h5>
                  <input type="text" id="mdate" name="quality" class="form-control">
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <h5>price on departure </h5>
                  <input type="text" id="mdate" name="price_on_departure" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>price on arrival </h5>
                  <input type="text" id="mdate" name="price_on_arrival" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>weight </h5>
                  <input type="text" id="mdate" name="weight" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>amount </h5>
                  <input type="text" id="mdate" name="amount" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>amount commission CHF </h5>
                  <input type="text" id="mdate" name="amount_commission_CHF" class="form-control">
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <h5>amount commission EURO </h5>
                  <input type="text" id="mdate" name="amount_commission_EURO" class="form-control">
                </div>
              </div>
            </div>
            <input type="submit" value="Submit" class="btn btn-success">
          </form>
        </div>
      </div>
      <!-- ============================================================== -->
      <!-- End PAge Content -->
      <!-- ============================================================== -->

    </div>
    <!-- ============================================================== -->
    <!-- End Container fluid  -->
    <!-- ============================================================== -->
  </div>


@endsection



@section('scripts')

  {{-- Select2 Library Start --}}
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/css/select2.min.css" rel="stylesheet"/>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/js/select2.min.js"></script>
  {{-- // In your Javascript (external .js resource or <script> tag) --}}
  <script>
    $(document).ready(function () {
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
  {{-- Select2 Library Ends --}}

  @include('api.formvalidator-init')
@endsection
