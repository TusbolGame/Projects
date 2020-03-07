@extends('layouts.admin')
@section('content')
    <div class="card mt-3">
        <div class="card-body">
            <div class="card-heading"><h4>@lang('main.addNewTransporter')</h4></div>
            <form method="POST" action="{{route('transporter.store')}}">
            @csrf
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" name="name" class="form-control" placeholder="T1. Name"  required >
                    </div>

                    <div class="col-md-3">
                        <input type="text" name="mobile" class="form-control phone" placeholder="T2. Telephone" required>
                    </div>
                    <div class="col-md-3">
                        <input type="email" name="email" class="form-control" placeholder="T3. Email " required>
                    </div>
                    <div class="col-md-3">
                            
                        <input type="submit" class="btn btn-success" value="Crea">
                    </div>
                </div>

            </form>            
        </div>
    </div>
    {{-- ********* Add New Sale STARTS HERE********** --}}
    {{-- <button type="button" class="btn btn-success" data-toggle="modal" data-target="#addsale" data-whatever="@mdo">Create New Transporter/Exporter</button> --}}
    <div class="modal fade" id="addsale" tabindex="-1" role="dialog" aria-labelledby="addsaleLabel1">
            <div class="modal-dialog modal-xl" role="document">
                <div class="modal-content">
    <div class="modal-header">
        <h4 class="modal-title" id="addsaleLabel1">@lang('main.addNewTransporter')</h4>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    </div>
    <form method="POST" action="{{route('transporter.store')}}">
    @csrf
        <div class="modal-body">

        <div class="row">
            <div class="col-md-6">
                <label for="">* @lang('main.name')</label><br>
                <input type="text" name="name" class="form-control" required >
            </div>

            <div class="col-md-3">
                <label for="">Reg. @lang('main.number')</label><br>
                    <input type="text" class="form-control" name="reg_number">
            </div> --}}
            <div class="col-md-3">
                <label for="">IVA / VAT</label><br>
                <input type="text" class="form-control" name="vat">
            </div>

        </div>

        <div class="row">

            <div class="col-md-3">
                <label for="">@lang('main.type')</label><br>
                <select name="type" class="form-control">
                    <option value="Transporter" selected>@lang('main.transporter')</option>
                    <option value="Exporter">Esportatrice</option>
                </select>
            </div> 

            <div class="col-md-3">
                <label for="">@lang('main.mobile')</label><br>
                <input type="text" name="mobile" class="form-control" >
            </div>
            <div class="col-md-6">
                <label for="">Email</label><br>
                <input type="email" name="email" class="form-control" >
            </div>
        </div>

        {{-- <div class="row">
            <div class="col-md-12">
                <label for="">Address</label><br>
                <input type="text" name="address" class="form-control" >
            </div>
        </div> --}}
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">@lang('main.cancel')</button>
            <input type="submit" class="btn btn-success" value="Crea">
        </div>
        </form>
                </div>
            </div>
        </div>
    <!-- /.modal -->

{{-- table starts here --}}
<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Gestisci @lang('main.transporter')</h4>
                {{-- <h6 class="card-subtitle">Export data to Copy, Excel, PDF & @lang('main.print')</h6> --}}
                <div class="table-responsive">
                    <table id="transporter_table" class="display nowrap table table-hover table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                            <tr>
                                <th>Nr</th>
                                <th>@lang('main.transporter')</th>
                                {{-- <th>Reg #</th>
                                <th>VAT</th>
                                <th>Type</th> --}}
                                <th>@lang('main.mobile')</th>
                                <th>Email</th>
                                {{-- <th>Address</th> --}}
                                {{-- <th>Created at</th> --}}
                                <th>@lang('main.action')</th>
                            </tr>
                        </thead>
                        <tfoot>
                            <tr style="padding:0px;">
                                <th>Sr</th>
                                <th>@lang('main.transporter')</th>
                                {{-- <th>Reg #</th>
                                <th>VAT</th>
                                <th>Type</th> --}}
                                <th>@lang('main.mobile')</th>
                                <th>Email</th>
                                {{-- <th>Address</th> --}}
                                {{-- <th>Created at</th> --}}
                                <th>@lang('main.action')</th>
                            </tr>
                        </tfoot>
                        <tbody>
                            @php
                                $i=1;
                            @endphp
                            @foreach ($transporters as $transporter)
                                <tr>
                                    <td>{{$i}}@php $i++ @endphp</td>
                                    <td>{{ $transporter->name }}</td>
                                    {{-- <td>{{ $transporter->reg_number }}</td>
                                    <td>{{ $transporter->vat }}</td> --}}
                                    {{-- <td>
                                        @if ( @$transporter->type == 'Transporter')
                                            @php
                                                $txptr = "<span class=\"badge badge-secondary\">Transporter</span>";
                                            @endphp
                                        @elseif ( @$transporter->type == 'Exporter')
                                            @php
                                                $txptr = "<span class=\"badge badge-warning\">Exporter</span>";
                                            @endphp
                                        @endif
                                        {!! @$txptr !!}
                                    </td> --}}
                                    <td><a href="callto::{{ @$transporter->mobile }}">{{ @$transporter->mobile }}</a></td>
                                    <td><a href="mailto::{{ @$transporter->email }}">{{ @$transporter->email }}</a></td>
                                    {{-- <td>{{ @$transporter->address }}</td> --}}
                                    {{-- Y-m-d Format --}}
                                    {{-- <td>{{ date('d-m-Y', strtotime($transporter->created_at)) }}</td> --}}
                                    <td>
{{-- ********* VIEW STARTS HERE********** --}}
<button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#viewtransporter{{$transporter->id}}" data-whatever="@mdo">@lang('main.preview')</button>
<div class="modal fade" id="viewtransporter{{$transporter->id}}" tabindex="-1" role="dialog" aria-labelledby="viewtransporter{{$transporter->id}}Label1">
    <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
<div class="modal-header">
<h4 class="modal-title" id="viewtransporter{{$transporter->id}}Label1">@lang('main.preview')</h4>
<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
</div>
<form method="POST" action="#">
{{-- @csrf
@method('PUT') --}}
<div class="modal-body">
<div class="row">
    <div class="col-md-4">
        <label for="">@lang('main.name')</label><br>
        <input type="text" class="form-control" disabled value="{{$transporter->name}}">
    </div>

    {{-- <div class="col-md-3">
        <label for="">Reg. @lang('main.number')/label><br>
        <input type="text" class="form-control" disabled value="{{$transporter->reg_number}}">
    </div>
    <div class="col-md-3">
        <label for="">VAT</label><br>
        <input type="text" class="form-control" disabled value="{{$transporter->vat}}">
    </div> --}}


    <div class="col-md-4">
        <label for="">@lang('main.mobile')</label><a href="callto:{{@$transporter->mobile}}"> {{@$transporter->mobile}}</a><br>

        <input type="text" class="form-control" disabled value="{{@$transporter->mobile}}">
    </div>
    <div class="col-md-4">
        <label for="">Email</label><a href="mailto:{{@$transporter->email}}"> {{@$transporter->email}}</a><br>
        <input type="email" class="form-control" disabled value="{{@$transporter->email}}">
    </div>
</div>
{{-- <div class="row">
    <div class="col-md-3">
        <label for="">Type</label><br>
        <input type="text" class="form-control" disabled value="{{@$transporter->type}}">
    </div>

</div>
<div class="row">
    <div class="col-md-12">
        <label for="">Address</label><br>
        <input type="text" class="form-control" disabled value="{{$transporter->address}}">
    </div>
</div> --}}

</div>
<div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">@lang('main.cancel')</button>
</div>
</form>
        </div>
    </div>
</div>
                                <!-- /.modal -->
{{-- ********* VIEW ENDS HERE *********** --}}


{{-- ********* EDIT STARTS HERE********** --}}
<button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#updatetransporter{{$transporter->id}}" data-whatever="@mdo">@lang('main.edit')</button>
<div class="modal fade" id="updatetransporter{{$transporter->id}}" tabindex="-1" role="dialog" aria-labelledby="updatetransporter{{$transporter->id}}Label1">
<div class="modal-dialog modal-xl" role="document">
    <div class="modal-content">
<div class="modal-header">
<h4 class="modal-title" id="updatetransporter{{$transporter->id}}Label1">Trasportatore di aggiornamenti</h4>
<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
</div>
<form method="POST" action="{{route('transporter.update', $transporter)}}">
@csrf
@method('PUT')
<div class="modal-body">
<div class="modal-body">
<div class="row">
    <div class="col-md-4">
        <label for="">@lang('main.name')</label><br>
        <input type="text" class="form-control" name="name"  value="{{$transporter->name}}">
    </div>

    <div class="col-md-4">
        <label for="">@lang('main.mobile')</label><br>
        <input type="text" class="form-control" name="mobile" value="{{$transporter->mobile}}">
    </div>
    <div class="col-md-4">
        <label for="">Email</label><br>
        <input type="email" class="form-control" name="email" value="{{@$transporter->email}}">
    </div>
    {{-- <div class="col-md-3">
        <label for="">Reg. @lang('main.number')/label><br>
        <input type="text" class="form-control" name="reg_number" value="{{$transporter->reg_number}}">
    </div>
    <div class="col-md-3">
        <label for="">VAT</label><br>
        <input type="text" class="form-control" name="vat" value="{{$transporter->vat}}">
    </div> --}}

</div>

<div class="row">

    {{-- <div class="col-md-3">
        <label for="">Type</label><br>
            <select name="type" id=""  class="form-control">
                @if(@$transporter->type == "Transporter")
                    <option value="Transporter" selected>Transporter</option>
                    <option value="Exporter">Exporter</option>
                @elseif(@$transporter->type == "Exporter")
                    <option value="Transporter">Transporter</option>
                    <option value="Exporter" selected>Exporter</option>
                @endif
            </select>
    </div> --}}


</div>
{{-- <div class="row">
    <div class="col-md-12">
        <label for="">Address</label><br>
        <input type="text" class="form-control" name="address" value="{{$transporter->address}}">
    </div>
</div> --}}

</div>

</div>
<div class="modal-footer">
<button type="button" class="btn btn-default" data-dismiss="modal">@lang('main.cancel')</button>
<input type="submit" class="btn btn-success" value="Update">
</div>
</form>
    </div>
</div>
</div>
                                <!-- /.modal -->
{{-- ********* EDIT ENDS HERE *********** --}}

        {{--  this is for permanant delete  --}}
        <a class="btn btn-danger btn-sm" href="javascript:;" onclick="confirmDelete('{{$transporter->id}}')">@lang('main.delete')</a>
            <form id="delete-transporter-{{$transporter->id}}"
                action="{{ route('transporter.destroy', $transporter->id) }}"
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
function confirmDelete(id){
    let choice = confirm("Are You sure, You want to Delete this record ?")
    if(choice){
        document.getElementById('delete-transporter-'+id).submit();
    }
}
</script>

@endsection
