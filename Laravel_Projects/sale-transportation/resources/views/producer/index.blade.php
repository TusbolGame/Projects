@extends('layouts.admin')
@section('content')
    <div class="modal fade" style="z-index: 1050!important;" id="addsale" tabindex="-1" role="dialog" aria-labelledby="addsaleLabel1">

        <div class="modal-dialog modal-lg"  role="document">
            <div class="modal-content" >
                <div class="modal-header">
                    <h3 class="modal-title" id="addsaleLabel1">@lang('main.addNewProducer')</h3>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Vicina"><span
                                aria-hidden="true">&times;</span></button>
                </div>
                <style>
                    label{
                        font-style: italic;
                        color: red;
                        font-family: "Helvetica Neue Light", "HelveticaNeue-Light", "Helvetica Neue", Calibri, Helvetica, Arial, sans-serif;
                    }
                    .row{
                        margin-bottom: 15px;
                    }
                    .modal-body{
                        padding-left: 30px;
                        padding-right: 30px;
                    }
                </style>
                <form method="POST" action="{{route('producer.store')}}">
                    @csrf
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-4">
                                <label for="">P1. Nome*</label><br>
                                <input type="text" name="name" class="form-control" placeholder="Example: Jone Doe" required>
                            </div>

                            <div class="col-md-4">
                                <label for="">P2. @lang('main.mobile')</label><br>
                                <input type="text" name="mobile" class="form-control phone" placeholder="Example: (+33)234-234-2345">
                            </div>
                            <div class="col-md-4">
                                <label for="">P3. Email</label><br>
                                <input type="email" name="email" class="form-control" placeholder="Example: Joe@gmail.com">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <label for="">P4. Via</label><br>
                                <input type="text" name="street" class="form-control" placeholder="Example: Ponte Tresa">
                            </div>
                            <div class="col-md-6">
                                <label for="">P5. @lang('main.houseNumber')</label><br>
                                <input type="text" name="hose_num" class="form-control" placeholder="Example: sector22">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <label for="">P6. Cap</label><br>
                                <input type="text" name="zip_code" class="form-control zip" placeholder="Example: 23452-345">
                            </div>
                            <div class="col-md-6" >
                                <label for="">P7. @lang('main.city')</label><br>
                                <input type="text" name="city" class="form-control" placeholder="Example: Ponte Tresa">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <label for="">P8. @lang('main.district')</label><br>
                                <input type="text" name="district" class="form-control" placeholder="Example: Via la Piana">
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>P9. @lang('main.nation') </label><br>
                                    <select name="nation" id="" class="form-control">
                                        <option value="" style="font-size: 1px" selected disabled></option>
                                        <option value="A">Austria</option>
                                        <option value="B">Belgio</option>
                                        <option value="FR">Francia</option>
                                        <option value="DE">Germania</option>
                                        <option value="IT">Italia</option>

                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">@lang('main.cancel')</button>
                        <input type="submit" class="btn btn-success" value="Create">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- /.modal -->
    <br>

    {{-- table starts here --}}
    <div class="row">
        <div class="col-12">
            <div class="card">
                <button type="button" class="btn btn-success mt-3 ml-3" style="width: 200px" data-toggle="modal" data-target="#addsale"
                        data-whatever="@mdo">@lang('main.addNewProducer')</button>

                <div class="card-body">
                    <h4 class="card-title">Gestisci Produttore</h4>
                    {{-- <h6 class="card-subtitle">Export data to Copy, Excel, PDF & @lang('main.print')</h6> --}}
                    <div class="table-responsive">
                        <table id="produser_table" class="display nowrap table table-hover table-striped table-bordered"
                               cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>Nr</th>
                                <th>@lang('main.name')</th>
                                <th>@lang('main.houseNumber')</th>
                                <th>Via</th>
                                <th>@lang('main.city')</th>
                                <th>Cap</th>
                                <th>@lang('main.mobile')</th>
                                <th>Email</th>
                                <th>@lang('main.action')</th>
                            </tr>
                            </thead>
                            <tfoot>
                            <tr style="padding:0px;">
                                <th>Nr</th>
                                <th>@lang('main.name')</th>
                                <th>@lang('main.houseNumber')</th>
                                <th>Via</th>
                                <th>@lang('main.city')</th>
                                <th>Cap</th>
                                <th>@lang('main.mobile')</th>
                                <th>Email</th>
                                <th>@lang('main.action')</th>
                            </tr>
                            </tfoot>
                            <tbody>
                            @php
                                $i=1;
                            @endphp
                            @foreach ($producers as $producer)
                                <tr>
                                    <td>{{$i}}@php $i++ @endphp</td>
                                    <td>{{ $producer->name }}</td>
                                    <td><a href="mailto::{{ @$producer->hose_num }}">{{ @$producer->hose_num }}</a></td>
                                    <td><a href="mailto::{{ @$producer->street }}">{{ @$producer->street }}</a></td>
                                    <td><a href="mailto::{{ @$producer->city }}">{{ @$producer->city }}</a></td>
                                    <td><a href="mailto::{{ @$producer->zip_code }}">{{ @$producer->zip_code }}</a></td>
                                    <td><a href="callto::{{ @$producer->mobile }}">{{ @$producer->mobile }}</a></td>
                                    <td><a href="mailto::{{ @$producer->email }}">{{ @$producer->email }}</a></td>
                                    {{-- <td>{{ @$producer->address }}</td> --}}
                                    {{-- Y-m-d Format --}}
                                    {{-- <td>{{ date('d-m-Y', strtotime($producer->created_at)) }}</td> --}}
                                    <td>
                                        {{-- ********* VIEW STARTS HERE********** --}}
                                        <button type="button" class="btn btn-info btn-sm" data-toggle="modal"
                                                data-target="#viewproducer{{$producer->id}}" data-whatever="@mdo">@lang('main.preview')
                                        </button>
                                        <div class="modal fade" id="viewproducer{{$producer->id}}" tabindex="-1"
                                             role="dialog" aria-labelledby="viewproducer{{$producer->id}}Label1">
                                            <div class="modal-dialog modal-xl" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h4 class="modal-title"
                                                            id="viewproducer{{$producer->id}}Label1">@lang('main.preview')</h4>
                                                        <button type="button" class="close" data-dismiss="modal"
                                                                aria-label="Close"><span
                                                                    aria-hidden="true">&times;</span></button>
                                                    </div>
                                                    <form method="POST" action="#">
                                                        {{-- @csrf
                                                        @method('PUT') --}}
                                                        <div class="modal-body">
                                                            <div class="row">
                                                                <div class="col-md-7">
                                                                    <label for="">@lang('main.name')</label><br>
                                                                    <input type="text" class="form-control" disabled
                                                                           value="{{$producer->name}}">
                                                                </div>
                                                                <div class="col-md-2">
                                                                    <label for="">Numeri reg</label><br>
                                                                    <input type="text" class="form-control" disabled
                                                                           value="{{$producer->reg_number}}">
                                                                </div>
                                                                <div class="col-md-3">
                                                                    <label for="">VAT</label><br>
                                                                    <input type="text" class="form-control" disabled
                                                                           value="{{$producer->vat}}">
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-md-6">
                                                                    <label for="">@lang('main.mobile')</label><a
                                                                            href="callto:{{@$producer->mobile}}"> {{@$producer->mobile}}</a><br>

                                                                    <input type="text" class="form-control" disabled
                                                                           value="{{@$producer->mobile}}">
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label for="">Email</label><a
                                                                            href="mailto:{{@$producer->email}}"> {{@$producer->email}}</a><br>
                                                                    <input type="email" class="form-control" disabled
                                                                           value="{{@$producer->email}}">
                                                                </div>
                                                            </div>
                                                            {{-- <div class="row">
                                                                <div class="col-md-12">
                                                                    <label for="">Address</label><br>
                                                                    <input type="text" class="form-control" disabled value="{{$producer->address}}">
                                                                </div>
                                                            </div> --}}
                                                            <div class="row">
                                                                <div class="col-md-6">
                                                                    <label for="">Via</label><br>
                                                                    <input type="text" name="street"
                                                                           class="form-control" disabled
                                                                           value="{{$producer->street}}">
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label for="">Cap</label><br>
                                                                    <input type="text" name="zip_code"
                                                                           class="form-control" disabled
                                                                           value="{{$producer->zip_code}}">
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label for="">@lang('main.city')</label><br>
                                                                    <input type="text" name="city" class="form-control"
                                                                           disabled value="{{$producer->city}}">
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label for="">@lang('main.nation')</label><br>
                                                                    <input type="text" name="nation"
                                                                           class="form-control" disabled
                                                                           value="{{$producer->nation}}">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-default"
                                                                    data-dismiss="modal">Vicina
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.modal -->
                                        {{-- ********* VIEW ENDS HERE *********** --}}


                                        {{-- ********* EDIT STARTS HERE********** --}}
                                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal"
                                                data-target="#updateproducer{{$producer->id}}" data-whatever="@mdo">@lang('main.edit')
                                        </button>
                                        <div class="modal fade" id="updateproducer{{$producer->id}}" tabindex="-1"
                                             role="dialog" aria-labelledby="updateproducer{{$producer->id}}Label1">
                                            <div class="modal-dialog modal-xl" role="document">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h4 class="modal-title"
                                                            id="updateproducer{{$producer->id}}Label1">Update
                                                            producer</h4>
                                                        <button type="button" class="close" data-dismiss="modal"
                                                                aria-label="Close"><span
                                                                    aria-hidden="true">&times;</span></button>
                                                    </div>
                                                    <form method="POST"
                                                          action="{{route('producer.update', $producer)}}">
                                                        @csrf
                                                        @method('PUT')
                                                        <div class="modal-body">
                                                            <div class="modal-body">
                                                                <div class="row">
                                                                    <div class="col-md-7">
                                                                        <label for="">@lang('main.name')</label><br>
                                                                        <input type="text" class="form-control"
                                                                               name="name" value="{{$producer->name}}">
                                                                    </div>
                                                                    <div class="col-md-2">
                                                                        <label for="">Numeri reg</label><br>
                                                                        <input type="text" class="form-control"
                                                                               name="reg_number"
                                                                               value="{{$producer->reg_number}}">
                                                                    </div>
                                                                    <div class="col-md-3">
                                                                        <label for="">VAT</label><br>
                                                                        <input type="text" class="form-control"
                                                                               name="vat" value="{{$producer->vat}}">
                                                                    </div>
                                                                </div>

                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <label for="">@lang('main.mobile')</label><br>
                                                                        <input type="text" class="form-control"
                                                                               name="mobile"
                                                                               value="{{$producer->mobile}}">
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <label for="">Email</label><br>
                                                                        <input type="email" class="form-control"
                                                                               name="email"
                                                                               value="{{@$producer->email}}">
                                                                    </div>
                                                                </div>
                                                                {{-- <div class="row">
                                                                    <div class="col-md-12">
                                                                        <label for="">Address</label><br>
                                                                        <input type="text" class="form-control" name="address" value="{{$producer->address}}">
                                                                    </div>
                                                                </div> --}}
                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <label for="">Via</label><br>
                                                                        <input type="text" name="street"
                                                                               class="form-control"
                                                                               value="{{$producer->street}}">
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <label for="">Cap</label><br>
                                                                        <input type="text" name="zip_code"
                                                                               class="form-control"
                                                                               value="{{$producer->zip_code}}">
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <label for="">@lang('main.city')</label><br>
                                                                        <input type="text" name="city"
                                                                               class="form-control"
                                                                               value="{{$producer->city}}">
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <label for="">@lang('main.nation')</label><br>
                                                                        <input type="text" name="nation"
                                                                               class="form-control"
                                                                               value="{{$producer->nation}}">
                                                                    </div>
                                                                </div>
                                                            </div>

                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-default"
                                                                    data-dismiss="modal">Vicina
                                                            </button>
                                                            <input type="submit" class="btn btn-success" value="Update">
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.modal -->
                                        {{-- ********* EDIT ENDS HERE *********** --}}

                                        {{--  this is for permanant delete  --}}
                                        <a class="btn btn-danger btn-sm" href="javascript:;"
                                           onclick="confirmDelete('{{$producer->id}}')">@lang('main.delete')</a>
                                        <form id="delete-producer-{{$producer->id}}"
                                              action="{{ route('producer.destroy', $producer->id) }}"
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
                document.getElementById('delete-producer-' + id).submit();
            }
        }
    </script>

@endsection
