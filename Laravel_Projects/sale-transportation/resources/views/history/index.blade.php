@extends('layouts.admin')
@section('content')
    <div class="row mt-3" style="opacity: 0.96">
        <div class="col-md-12">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">Storico Fatture</h4>
                    <div class="table-responsive">
                        <table id="invoiceTable" class="display nowrap table table-hover table-striped table-bordered"
                               cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>Fattura Numero</th>
                                <th>@lang('main.positionNumber')</th>
                                <th>Recipient</th>
                                <th>@lang('main.invoiceDate')</th>
                                <th>Totale Fattura (€)</th>
                                <th>Commission (CHF)</th>
                                <th>@lang('main.paymentStatus')</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tfoot>
                            <tr style="padding:0px;">
                                <th>Fattura Numero</th>
                                <th>@lang('main.positionNumber')</th>
                                <th>Recipient</th>
                                <th>@lang('main.invoiceDate')</th>
                                <th>Totale Fattura (€)</th>
                                <th>Commission (CHF)</th>
                                <th>@lang('main.paymentStatus')</th>
                                <th>Actions</th>
                            </tr>
                            </tfoot>
                            <tbody>
                            @php
                                $i=1;
                                $total_invoice_amount = null;
                                $total_commission = null;
                            @endphp
                            @foreach ($invoices as $invoice)
                                <tr>
                                    <td>{{ @$invoice->id }}</td>
                                    <td>{{ @$invoice->order->number }}</td>
                                    <td>
                                        @if($invoice->customer_id == $invoice->seller_id)
                                            <span style="background: red; color: white; font-size: 11px; padding: 2px 5px; border-radius: 15px">@lang('main.seller')</span>
                                        @else
                                            <span style="background: blue; color: white; font-size: 11px; padding: 2px 5px; border-radius: 15px">@lang('main.buyer')</span>
                                        @endif
                                        {{ @$invoice->customer->business_name }}
                                    </td>
                                    <td>{{ date('d-m-Y', strtotime($invoice->order->invoice_date)) }}</td>

                                    <td>
                                        @if (@$invoice->payment_status != '1')
                                            @php
                                                $total_invoice_amount = $total_invoice_amount + $invoice->order->total_commission;
                                            @endphp
                                        @endif
                                        {{ @$invoice->order->total_commission}}
                                    </td>
                                    <td>
                                        @php
                                            $total_commission = $total_commission + $invoice->order->swiss_francs;
                                        @endphp
                                        {{ @$invoice->order->swiss_francs }}
                                    </td>
                                    <td>

                                        <div class="switch" style="display: inline-block">
                                            @if (@$invoice->payment_status == '1')
                                                @php
                                                    $checked = 'checked';
                                                @endphp
                                            @else
                                                @php
                                                    $checked = '';
                                                @endphp
                                            @endif
                                            <form method="POST" action="{{route('invoice_payment', $invoice->id)}}"
                                                  id="form{{$invoice->id}}">
                                                @csrf
                                                {{-- @method('PUT') --}}
                                                <input type="hidden" name="invoice_id" value="{{$invoice->id}}">
                                                <label>
                                                    <input type="checkbox" class="checkbox"
                                                           <?= $checked ?> name="payment_status"
                                                           onchange="$('#form{{$invoice->id}}').submit();"
                                                           value="1"><span class="lever"></span></label>
                                            </form>
                                        </div>
                                        @if (@$invoice->payment_status == '1')
                                            @php
                                                $show_status = '<span class="badge badge-success">Pagado</span>';
                                            @endphp
                                        @else
                                            @php
                                                $show_status = '<span class="badge badge-danger">Para ser pagado</span>';
                                            @endphp
                                        @endif
                                        {!! @$show_status !!}
                                    </td>
                                    <td>
                                        <a href="{{route('invoice.show', $invoice)}}" class="btn btn-warning  btn-sm">@lang('main.sendMail')</a>
                                    </td>
                                </tr>
                            @endforeach

                            <tr style="background-color:silver">
                                <td></td>
                                <td></td>
                                <td></td>
                                <td>Sum</td>
                                <td><i>€ {{$total_invoice_amount}}</i></td>
                                <td><i>CHF {{$total_commission}}</i></td>
                                <td></td>
                                <td></td>
                            </tr>

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
                document.getElementById('delete-invoice-' + id).submit();
            }
        }
    </script>

    {{-- // In your Javascript (external .js resource or <script> tag) --}}
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


        });
    </script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/js/select2.min.js"></script>
    <script>
        $(document).ready(function () {
            $('.js-example-basic-single').select2({
                tags: true
            });
            if ($('#customer-selector').val() == "all" || $('#payment-selector').val() != "unpaid") {
                $('#email-button').css("display", "none");
            }
            if ($('#customer-selector').val() == "all" && $('#payment-selector').val() == "all") {
                $('#all-button').css("display", "none");
            }
            $("#email-button").on("click", function () {
                $('#form').attr('action', "{{route('invoice.email_table')}}");
                $("#form").submit();
                e.preventDefault();
            });

        });
    </script>



    {{-- Select2 Library Ends --}}

@endsection

