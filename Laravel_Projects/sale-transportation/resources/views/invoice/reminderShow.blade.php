@extends('layouts.admin')

@section('content')
	<form method="post" action="">
		<div class="card card-body">
			<div class="row">
				<button type="submit" class="btn btn-success m-3"><i class="fa fa-paper-plane ">&nbsp;&nbsp;</i>@lang('main.send')</button>
				<a id="preview_button"  class="btn btn-primary m-3" style="color: white"><i class="fa fa-street-view ">&nbsp;&nbsp;</i>@lang('main.preview')</a>
				<a id="edit_button"  class="btn btn-primary m-3" style="display: none; color: white"><i class="fa fa-edit ">&nbsp;&nbsp;</i>@lang('main.edit')</a>
				<a id="print_button" onclick="printDiv('printableArea')" href="" class="noprint btn btn-warning m-3" style="color:white; display: none;"><i class="fa fa-print">&nbsp;&nbsp;</i>Stampa</a>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div class="form-group mt-5">
                        <div id="user-dtails"></div>
                        <textarea name="email_content" class="summernote  form-control" cols="30" rows="10" id="reminder_email_content">{{@$details[0]->email_content}}</textarea>
                    </div>
				</div>
			</div>
		</div>
		<br/>
		<div class="card card-body">
			<div class="row">
			<h3>Bonjour / Guten Tag</h3><hr/>
			<p>Vi invitiamo ad effettuare il pagamento della fattura emessa</p><br/>
			<p>da: <span style="color:blue">(SELLER O5)</span></p><br/>
			
  numero: (invoice number O9) del:(Invoice Date O10). 
Nous Vous invitons Il verificatore le fratture de: (SELLER O5) numero: (invoice number O9) Data (Invoice Date O10) comme specifie et à les payer. 
Wir laden Sie ein, die Rechnungen (SELLER O5) wie zu (invoice number O9) von (Invoice Date O10)  angegeben ueberpruefen und zu bezhalen. </div>
		</div>		
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

        function printDiv(divName) {
            var printContents = document.getElementById(divName).innerHTML;
            var originalContents = document.body.innerHTML;

            document.body.innerHTML = printContents;

            window.print();

            document.body.innerHTML = originalContents;
        }
    </script>
@endsection
