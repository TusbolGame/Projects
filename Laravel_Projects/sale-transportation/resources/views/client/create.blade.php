@extends('layouts.admin')
@section('content')
    <style>
        hr{
            height: 3px;
        }
        h5{
            font-style: italic;
            color: red;
            font-family: "Helvetica Neue Light", "HelveticaNeue-Light", "Helvetica Neue", Calibri, Helvetica, Arial, sans-serif;
        }
        h4{
            margin-top: 20px!important;
        }
        .basic{
            margin: 0 30px 0 30px;
        }

        input{
            font-size: 20px;
        }

        .form-control {
            display: block;
            width: 100%;
            height: calc(1.5em + .75rem + 2px);
            padding: .375rem .75rem;
            font-size: 1.2rem;
            font-weight: 400;
            line-height: 1.5;
            color: black;
            background-color: #fff;
            background-clip: padding-box;
            border: 2px solid #aaa;
            border-radius: .25rem;
            transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out;
        }

        .select2 .select2-selection--single {
            background-color: #fff;
            border: 2px solid #aaa;
            border-radius: 4px;
            font-size: 1.2rem;
            font-weight: 400;
            line-height: 1.5;
        }

        .btn-submit {
            background-image: linear-gradient(darkgreen, green, darkgreen)!important;
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

        .btn-submit:hover{
            color: white;
            background-image: linear-gradient(darkred, red, darkred)!important;
            -webkit-box-shadow: 0 14px 26px -12px rgba(40, 190, 189, 0.42), 0 4px 23px 0 rgba(0, 0, 0, 0.12), 0 8px 10px -5px rgba(40, 190, 189, 0.2);
            box-shadow: 4px 4px 8px grey;

        }

    </style>
<div class="row mt-3">
<div class="col-md-12">
    <div class="card">
        <div class="card-body">
            <h2 class="card-title" style="font-weight: bold">@lang('main.newClient')</h2>

            <form class="" method="POST" action="{{route('client.store')}}">
                @csrf
                <div class="row">
                    <div class="col-md-9">
                        <div class="form-group">
                            <h5>C1. @lang('main.businessName') *<span class="text-danger">*</span></h5>
                            <input type="text" name="business_name" class="form-control"  data-validation="required "
                            data-validation-error-msg="This is a required field!" placeholder="Example: Jone Doe"
                            required />
                        </div>
                    </div>

                    <div class="col-md-3">
                        <div class="form-group">
                            <h5>C2. Codice</h5>
                            <input type="text" name="code" class="form-control code" placeholder="Example: 1778">
                        </div>
                    </div>

                </div>

                <div class="row">
                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C3. @lang('main.street')</h5>
                            <input type="text" name="street" class="form-control" placeholder="Example: Ronco Regina">
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C4. @lang('main.houseNumber')</h5>
                            <input type="text" name="hose_num" class="form-control" placeholder="Example: Sector 12">
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C5. Cap.</h5>
                            <input type="text" name="zip_code" class="form-control zip" placeholder="Example: 45636-456">
                        </div>
                    </div>
                </div>


                <div class="row">
                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C6. @lang('main.city')</h5>
                            <input type="text" name="town" class="form-control" placeholder="Example:  Ponte Tresa">
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C7. @lang('main.province')</h5>
                            <input type="text" name="district" class="form-control" placeholder="Example: Ponte Tresa">
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C8. @lang('main.nation')</h5>
                            <select name="nation_code" id="nation" class="form-control nation">
                                <option value="" style="font-size: 1px" selected disabled></option>
                                <option value="FR">France</option>
                                <option value="DE">Germany</option>
                                <option value="B">Belgium</option>
                                <option value="IT">Italy</option>
                                <option value="A">Austria</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C9. IVA / VAT</h5>
                            <input type="text" name="vat" class="form-control vat" placeholder="Example: 123456789">
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C10. Numero di telefono pprincipale</h5>
                            <input type="text" name="telephone" class="form-control phone" placeholder="Example: (+33)234-234-2345">
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="form-group">
                            <h5>C11. Numero Cellulare Principale</h5>
                            <input type="text" name="mobile" class="form-control phone" placeholder="Example: (+33)234-234-2345">
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 col-sm-12">
                        <div class="card card-body">
                            <h3>@lang('main.person') 1</h3>
                            <div class="col-md-12">
                                <div class="form-group">
                                    <h5>C12. Nome </h5>
                                    <input type="text" name="private_phone_person_name_1" class="form-control" placeholder="Example: Jone Doe">
                                </div>
                            </div>

                            <div class="col-md-12">
                                <div class="form-group">
                                    <h5>C13. @lang('main.privateNumber')</h5>
                                    <input type="text" name="private_phone_1" class="form-control phone" placeholder="Example: (+33)234-234-2345">
                                </div>
                            </div>

                            <div class="col-12">
                                <div class="form-group">
                                    <h5>C14. Email</h5>
                                    <input type="email" name="email_1" class="form-control" placeholder="Example: jone@gmail.com">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6 col-sm-12">
                        <div class="card card-body">
                            <h3>@lang('main.person') 2</h3>
                            <div class="col-md-12">
                                <div class="form-group">
                                    <h5>C15. @lang('main.name') </h5>
                                    <input type="text" name="private_phone_person_name_2" class="form-control" placeholder="Example: Jone Doe">
                                </div>
                            </div>

                            <div class="col-md-12">
                                <div class="form-group">
                                    <h5>C16. @lang('main.privateNumber')</h5>
                                    <input type="text" name="private_phone_2" class="form-control phone" placeholder="Example: (+33)234-234-2345">
                                </div>
                            </div>


                            <div class="col-12">
                                <div class="form-group">
                                    <h5>C17. Email</h5>
                                    <input type="email" name="email_2" class="form-control" placeholder="Example: jone@gmail.com">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <input type="submit" value="Completare" class="btn btn-submit">
            </form>
        </div>
    </div>
    <!-- ============================================================== -->
    <!-- End PAge Content -->

</div>
<!-- ============================================================== -->
<!-- End Container fluid  -->
<!-- ============================================================== -->
</div>
@endsection

@section('scripts')
    @include('api.formvalidator-init')
@endsection
