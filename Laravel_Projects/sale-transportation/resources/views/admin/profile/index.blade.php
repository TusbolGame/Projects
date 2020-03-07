@extends('layouts.admin')

@section('content')
    <style>
        .card-title{
            font-size: 30px;
            font-weight: bold;
            color:silver;
        }
    </style>
<div class="row mt-3">
    <div class="col-md-6">
        <div class="card">
            <div class="card-body">
                <div class="card-title">@lang('main.infomation')</div>
                <form action="{{route('profile.update_user', $user)}}" method="POST">
                    @csrf
                    @method('PUT')
                    <div class="form-group">
                        <label for="">@lang('main.name')</label>
                        <input type="text" name="name" value="{{$user->name}}" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="">Email</label>
                        <input type="text" name="email" value="{{$user->email}}" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="">@lang('main.oldPassword')</label>
                        <input type="text" name="old_password" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="">@lang('main.newPassword')</label>
                        <input type="text" name="new_password" class="form-control">
                    </div>
                    <div class="form-group">
                        <input type="submit" value="Update" class="btn btn-warning">
                    </div>
                </form>
            </div>
        </div>

        <div class="card">
            <div class="card-body">
                <div class="card-title">@lang('main.bankDetail')</div>
                <form action="{{route('profile.bank_details', $user)}}" method="POST">
                    @csrf
                    @method('PUT')
                    <div class="form-group">
                        <textarea name="bank_details" class="form-control" cols="30" rows="10" id="bank_details">{{@$user->bank_details}}</textarea>
                    </div>

                    <div class="form-group">
                        <input type="submit" value="Update" class="btn btn-warning">
                    </div>
                </form>
            </div>
        </div>

        <div class="card">
            <div class="card-body">
                <div class="card-title">@lang('main.orderReminderContent')</div>
                <form action="{{route('profile.email_content', $user)}}" method="POST">
                    @csrf
                    @method('PUT')
                    <div class="form-group">
                        <textarea name="email_content" class="form-control" cols="30" rows="10" id="email_content">{{@$user->email_content}}</textarea>
                    </div>

                    <div class="form-group">
                        <input type="submit" value="Update" class="btn btn-warning">
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="col-md-6">
        <div class="card">
            <div class="card-body">
                <div class="card-title">@lang('main.invoiceEmailContent')</div>
                <form action="{{route('profile.invoice_content', $user)}}" method="POST">
                    @csrf
                    @method('PUT')
                    <div class="form-group">
                        <textarea name="invoice_content" class="form-control" cols="30" rows="10" id="invoice_content">{{@$user->invoice_content}}</textarea>
                    </div>
                    <div class="form-group">
                        <input type="submit" value="Update" class="btn btn-warning">
                    </div>
                </form>
            </div>
        </div>
        <div class="card">
            <div class="card-body">
                <div class="card-title">@lang('main.reminderContent')</div>
                <form action="{{route('profile.missed_list_content', $user)}}" method="POST">
                    @csrf
                    @method('PUT')
                    <div class="form-group">
                        <textarea name="missed_list_content" class="form-control" cols="30" rows="10" id="missed_list_content">{{@$user->missed_list_content}}</textarea>
                    </div>

                    <div class="form-group">
                        <input type="submit" value="Update" class="btn btn-warning">
                    </div>
                </form>
            </div>
        </div>
        <div class="card">
            <div class="card-body">
                <div class="card-title">@lang('main.orderContent')</div>
                <form action="{{route('profile.reminder_email_content', $user)}}" method="POST">
                    @csrf
                    @method('PUT')
                    <div class="form-group">
                        <textarea name="reminder_email_content" class="form-control" cols="30" rows="10" id="reminder_email_content">{{@$user->reminder_email_content}}</textarea>
                    </div>

                    <div class="form-group">
                        <input type="submit" value="Update" class="btn btn-warning">
                    </div>
                </form>
            </div>
        </div>

    </div>



</div>

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

    });
    </script>
@endsection