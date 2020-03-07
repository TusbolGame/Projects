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
    <form method="post" action="{{route('home.order_email', $order)}}">
        @csrf
        <div class="card card-body p-5">
            <div class="row">
                <button type="submit" class="btn btn-success m-3"><i class="fa fa-paper-plane ">&nbsp;&nbsp;</i>invia
                </button>
                <a id="preview_button" class="btn btn-primary m-3" style="color: white"><i class="fa fa-street-view ">&nbsp;&nbsp;</i>@lang('main.preview')</a>
                <a id="edit_button" class="btn btn-primary m-3" style="display: none; color: white"><i class="fa fa-edit ">&nbsp;&nbsp;</i>@lang('main.edit')</a>
                <a id="print_button" href="javascript:window.print()" class="noprint btn btn-warning m-3"><i class="fa fa-print">&nbsp;&nbsp;</i>@lang('main.print')</a>
            </div>
            {{-- Editable Content --}}
            <div class="col-md-12 text-center">
                <div class="">
                    <div class="form-group mt-5">
                        <div id="user-dtails"></div>
                        <textarea name="email_content" class="summernote  form-control" cols="30" rows="10" id="reminder_email_content">{{@$details[0]->email_content}}</textarea>
                    </div>
                </div>
            </div>
            {{-- From DB --}}
           <div class="card card-body">
               <p>Fattura da: Facture de: Rechnung von: <span style=" text-decoration-line: underline;font-weight:bold">{{@$order->seller->business_name}}</span>
                   N. <span style=" text-decoration-line: underline;font-weight:bold">{{@$order->invoice_number}}</span>
                   del: de: am:<span style=" text-decoration-line: underline;font-weight:bold">{{@$order->invoice_date}}</span>
                   Totale: Total: Somme: <span style=" text-decoration-line: underline;font-weight:bold">{{@$order->amount_in_euro}}</span>
               </p>
           </div>

            {{-- Greeting --}}
            <div class="row mt-3">
                <div class="col-md-12 text-left">
                    <img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png" alt="logo" height="180px">
                    <div class="" style="margin-top: 30px">
                        <p>Via Balestra Nr. 33 – 6900 LUGANO – (CH)</p>
                        <p>Kontakt: Bertamino Giovanni Tf: +41 79 8398400</p>
                        <p>E-mail: joe@joesfood.ch & bertamino58@tiscali.it</p>
                        <p>Website: http://www.joesfood.ch</p>
                        <p>Nr. Registro Commercio: CH-501.1.018.030-4 - IDI: CHE-409.161.060</p>
                        <p>Partita IVA / TVA: CH-501.1.018.030-4</p>
                    </div>
                </div>
            </div>
        </div>
    </form>
@endsection

@section('scripts')
    <script src={{asset("assets/plugins/tinymce/tinymce.min.js")}}></script>
    <script>
        $(document).ready(function () {

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
                $('#preview_button').css('display', 'none');
                $('#edit_button').css('display', 'inline-block');
                $('#print_button').css('display', 'inline-block');
                $('#mceu_20').css('display', 'none');
                $('#user-dtails').html(user_details);
                $('#user-dtails').css('display', 'block');
            });
            $('#edit_button').on('click', function () {
                $('#preview_button').css('display', 'inline-block');
                $('#edit_button').css('display', 'none');
                $('#print_button').css('display', 'none');
                $('#mceu_20').css('display', 'inline-block');
                $('#user-dtails').css('display', 'none');
            });

            function get_tinymce_content() {
                var content = tinymce.get("reminder_email_content").getContent();
                return content;
            }
        });
    </script>
@endsection
