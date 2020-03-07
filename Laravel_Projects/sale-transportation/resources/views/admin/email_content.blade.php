@extends('layouts.admin')
@section('content')
    <style>

        table{
            width: 100%;
        }

        .table td, .table th {
            padding: 0rem;
            vertical-align: top;
            border-top: 1px solid #dee2e6;
        }
        .card-body{
            width: 100%;
        }


        hr{
            height: 1px;
            color: red;
            background-color: red;
            border: none;
        }
    </style>


    {{-- <a href="{{route('order_pdf', $order)}}" class="btn btn-success">Save as pdf</a> --}}
    <div class="card card-body">
        <style>

            /* p{
                padding: 0px;
                margin: 0px;
            } */

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

        <div class="row">
            <a href="{{route('invoice.reminderEmail', $invoice)}}" class="btn btn-success m-3"><i class="fa fa-paper-plane ">&nbsp;&nbsp;</i>@lang('main.send')</a>
            <a href="javascript:window.print()" class="noprint btn btn-primary m-3"><i class="fa fa-print">&nbsp;&nbsp;</i>@lang('main.preview')</a>
            <a href="javascript:window.print()" class="noprint btn btn-primary m-3"><i class="fa fa-print">&nbsp;&nbsp;</i>@lang('main.print')</a>
            <div class="col-md-12 text-center">
                <div class="">
                    <p>
                        {{-- Sollecito a mezzo email
                        <br>
                        <br>
                        <br>
                        Buongiorno BonJour/Guten Tag,<br>
                        Vi invitiamo a controllare le fatture di seguito indicate ed a provvedee al pagamento delle stesse<br>
                        Nous Vous invitons à vèrifier les factures comme spécifiè et à les payer.<br>
                        Wir laden Sie ein, die Rechnungen wie angegeben zu ueberpruefen und zu bezhalen.<br>
        <br><br>

                        Cordiali saluti / Cordialment / Mit freindlichen Gruessen
        <br><br>

                        Bertamino<br><br> --}}
                        {!! @$reminder_email_content !!}
                        <span class="text-danger">
                    Fattura Numero: {{@$invoice->order->invoice_number}}
                            <br/>
                    Invoice Amount: {{@$invoice->order->amount_in_euro}}
                            <br/>
                </span>
                    </p>
                    <br/><br/>
                    <img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png" alt="logo" height="120px">
                    <p>Via Balestra Nr. 33 – 6900 LUGANO – (CH)
                        <br>Kontakt: Bertamino Giovanni  Tf: +41 79 8398400
                        <br>E-mail: joe@joesfood.ch  & bertamino58@tiscali.it
                        <br>Website: http://www.joesfood.ch
                        <br>Nr. Registro Commercio: CH-501.1.018.030-4 - IDI: CHE-409.161.060
                        <br>Partita IVA / TVA: CH-501.1.018.030-4</p>
                </div>
            </div>
        </div>
    </div>

@endsection

@section('scripts')
    <script>
        $(document).ready(function () {
            content = $('#content_r').val();
            $('#content').html(content);
        });
    </script>
@endsection
