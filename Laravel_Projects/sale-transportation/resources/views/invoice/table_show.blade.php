@extends('layouts.admin')

@section('content')

    <style>

        .card{

            padding: 30px;

            margin: 5px;

        }



        p{

            padding: 0px;

            margin: 0px;

          

        }



        b{

            font-weight: 500;

        }



        td{

            font-weight: 500;

            padding-left: 10px;

            padding-top: 10px;

            padding-bottom: 10px;

        }



        .red{

            color:red;

        }



        .underline{

            text-decoration: underline;

        }

    </style>

@php $invoic = $invoices[0] @endphp

<form action="{{route('invoice.unpaid_mail', $invoic->customer_id)}}" method="POST">

@csrf

    {{-- <a href="{{route('order_pdf', $order)}}" class="btn btn-success">Save as pdf</a> --}}

    <div class="card card-body">

        <div class="row">

            <button type="submit" class="btn btn-success m-3"><i class="fa fa-paper-plane ">&nbsp;&nbsp;</i>@lang('main.send')</button>

            <a id="preview_button"  class="btn btn-primary m-3" style="color: white"><i class="fa fa-street-view ">&nbsp;&nbsp;</i>@lang('main.preview')</a>

            <a id="edit_button"  class="btn btn-primary m-3" style="display: none; color: white"><i class="fa fa-edit ">&nbsp;&nbsp;</i>@lang('main.edit')</a>

            <a id="print_button" href="javascript:window.print()" class="noprint btn btn-warning m-3" style="display: none;"><i class="fa fa-print">&nbsp;&nbsp;</i>@lang('main.print')</a>



        </div>



        <br>



        <div class="form-group mt-5" >

            <div id="user-dtails"></div>

            <textarea name="content" class="summernote  form-control" cols="30" rows="10" id="reminder_email_content">{{@$main_data[0]->missed_list_content}}</textarea>

        </div>



        <h3 style="font-weight: bold; color: silver">Unpaid Content</h3>



        <div class="table-responsive">

            <table id="invoiceTable" class="display nowrap table table-hover table-striped table-bordered"

                   cellspacing="0" width="100%">

                <thead>

                <tr>

                    <th>No</th>

                    <th>@lang('main.positionNumber')</th>

                    <th>Fattura Numero</th>

                    <th>@lang('main.invoiceDate')</th>

                    <th>product</th>

                    <th>Weight</th>



                    <th>Totale Fattura (€)</th>

                    <th>Commission (CHF)</th>

                </tr>

                </thead>



                <tbody>



                @php

                    $i=1;

                    $total_invoice_amount = null;

                    $total_commission = null;

                    $num = 0;

                @endphp

                @foreach ($invoices as $invoice)

                    @php

                        $num++;

                    @endphp

                    <tr>

                        <td>{{@$num}}</td>

                        <td>{{ @$invoice->order->number }}</td>

                        <td>{{ @$invoice->id }}</td>

                        <td>

                            @if($invoice->customer_id == $invoice->seller_id)

                                <span style="background: red; color: white; font-size: 11px; padding: 2px 5px; border-radius: 15px">@lang('main.seller')</span>

                            @else

                                <span style="background: blue; color: white; font-size: 11px; padding: 2px 5px; border-radius: 15px">@lang('main.buyer')</span>

                            @endif

                            {{ date('d-m-Y', strtotime($invoice->order->invoice_date)) }}

                        </td>

                        <td>{{ @$invoice->order->product}}</td>

                        <td>{{ @$invoice->order->weight_loaded_kg}} t</td>



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

                    </tr>

                @endforeach



                <tr style="background-color:silver">

                    <td></td>

                    <td></td>

                    <td></td>

                    <td></td>

                    <td></td>

                    <td>Sum</td>

                    <td><i>€ {{$total_invoice_amount}}</i></td>

                    <td><i>(CHF) {{$total_commission}}</i></td>



                </tr>



                </tbody>

            </table>

        </div>

        <div class="col-md-12 text-left">

            <div class="">

                <img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png" alt="logo" height="180px">

                <p>Via Balestra Nr. 33 – 6900 LUGANO – (CH)</p>

                <p>Kontakt: Bertamino Giovanni  Tf: +41 79 8398400</p>

                <p>E-mail: joe@joesfood.ch  & bertamino58@tiscali.it</p>

                <p>Website: http://www.joesfood.ch</p>

                <p>Nr. Registro Commercio: CH-501.1.018.030-4 - IDI: CHE-409.161.060</p>

                <p>Partita IVA / TVA: CH-501.1.018.030-4</p>

            </div>

        </div>

        </div>



        <br>





</form>





@endsection



@section('scripts')

    <script src={{asset("assets/plugins/tinymce/tinymce.min.js")}}></script>

    <script>

        $(document).ready(function() {



            if ($("#email_content").length > 0) {

                tinymce.init({

                    selector: "textarea#email_content",

                    theme: "modern",

                    height: 200,

                    plugins: [

                        "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",

                        "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",

                        "save table contextmenu directionality emoticons template paste textcolor"

                    ],

                    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | l      ink image | print preview media fullpage | forecolor backcolor emoticons",



                });

            }



            if ($("#invoice_content").length > 0) {

                tinymce.init({

                    selector: "textarea#invoice_content",

                    theme: "modern",

                    height: 200,

                    plugins: [

                        "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",

                        "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",

                        "save table contextmenu directionality emoticons template paste textcolor"

                    ],

                    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | l      ink image | print preview media fullpage | forecolor backcolor emoticons",



                });

            }



            if ($("#missed_list_content").length > 0) {

                tinymce.init({

                    selector: "textarea#missed_list_content",

                    theme: "modern",

                    height: 200,

                    plugins: [

                        "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",

                        "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",

                        "save table contextmenu directionality emoticons template paste textcolor"

                    ],

                    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | l      ink image | print preview media fullpage | forecolor backcolor emoticons",



                });

            }



            if ($("#reminder_email_content").length > 0) {

                tinymce.init({

                    selector: "textarea#reminder_email_content",

                    theme: "modern",

                    height: 200,

                    plugins: [

                        "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",

                        "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",

                        "save table contextmenu directionality emoticons template paste textcolor"

                    ],

                    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | l      ink image | print preview media fullpage | forecolor backcolor emoticons",



                });

            }



            if ($("#bank_details").length > 0) {

                tinymce.init({

                    selector: "textarea#bank_details",

                    theme: "modern",

                    height: 200,

                    plugins: [

                        "advlist autolink link image lists charmap print preview hr anchor pagebreak spellchecker",

                        "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",

                        "save table contextmenu directionality emoticons template paste textcolor"

                    ],

                    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | l      ink image | print preview media fullpage | forecolor backcolor emoticons",



                });

            }



            var bank = $('#bank_hidden').val();

            $('#bank').html(bank);



            $('#preview_button').on('click', function () {

                var user_details = get_tinymce_content();

                $('#preview_button').css('display','none');

                $('#edit_button').css('display', 'inline-block');

                $('#print_button').css('display', 'inline-block');

                $('#mceu_20').css('display', 'none');

                $('#user-dtails').html(user_details);

                $('#user-dtails').css('display', 'block');

            });

            $('#edit_button').on('click', function () {

                $('#preview_button').css('display','inline-block');

                $('#edit_button').css('display', 'none');

                $('#print_button').css('display', 'none');

                $('#mceu_20').css('display', 'inline-block');

                $('#user-dtails').css('display', 'none');

            });

            function get_tinymce_content(){

                var content = tinymce.get("reminder_email_content").getContent();

                return content;

            }

        });

    </script>

@endsection