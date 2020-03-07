@extends('layouts.admin')
@section('content')

  {{-- table starts here --}}
  <div class="row mt-3">
    <div class="col-md-12">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">Elenco Clienti</h4>
          <div class="table-responsive">
            <table id="myTable" class="display nowrap table table-hover table-striped table-bordered"
                   cellspacing="0" width="100%">
              <thead>
              <tr>
                <th>Nr</th>
                <th>@lang('main.businessName')</th>
                <th>Codice</th>
                <th>Via</th>
                <th>@lang('main.houseNumber')</th>
                <th>Cap</th>
                <th>@lang('main.city')</th>
                <th>@lang('main.province')</th>
                <th>@lang('main.nation')</th>

                <th>@lang('main.action')</th>
              </tr>
              </thead>
              <tfoot>
              <tr>
                <th>Nr</th>
                <th>@lang('main.businessName')</th>
                <th>Codice</th>
                <th>Via</th>
                <th>@lang('main.houseNumber')</th>
                <th>Cap</th>
                <th>@lang('main.city')</th>
                <th>@lang('main.province')</th>
                <th>@lang('main.nation')</th>

                <th>@lang('main.action')</th>
              </tr>
              </tfoot>
              <tbody>
              @php
                $i=1;
              @endphp
              @foreach ($clients as $client)
                <tr>
                  <td>{{$i}}@php $i++ @endphp</td>
                  <td>{{ @$client->business_name }}</td>
                  <td>{{ @$client->code }}</td>
                  <td>{{ @$client->street }}</td>
                  <td>{{ @$client->hose_num }}</td>
                  <td>{{ @$client->zip_code }}</td>
                  <td>{{ @$client->town }}</td>
                  <td>{{ @$client->district }}</td>
                  <td>{{ @$client->nation_code }}
                  {{-- Y-m-d Format --}}

                  <td>

                    {{-- ********* View STARTS HERE********** --}}
                    <button type="button" class="btn btn-warning btn-sm" data-toggle="modal"
                            data-target="#previewclient{{$client->id}}" data-whatever="@mdo">@lang('main.preview')
                    </button>
                    <div class="modal fade" id="previewclient{{$client->id}}" tabindex="-1"
                         role="dialog" aria-labelledby="previewclient{{$client->id}}Label1">
                      <div class="modal-dialog modal-xl" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h4 class="modal-title" id="previewclient{{$client->id}}Label1">
                              Anteprima </h4>
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-label="Close"><span
                                  aria-hidden="true">&times;</span></button>
                          </div>
                          <form method="POST" action="{{route('client.update', $client)}}">
                            @csrf
                            @method('PUT')
                            <div class="modal-body">
                              <div class="modal-body">
                                <div class="row">
                                  <div class="col-md-9">
                                    <label for="">C1. @lang('main.businessName')</label><br>
                                    <input disabled type="text" class="form-control"
                                           name="business_name"
                                           value="{{$client->business_name}}">
                                  </div>
                                  <div class="col-md-3">
                                    <label for="">C2. Codice</label><br>
                                    <input disabled type="text" class="form-control"
                                           name="code" value="{{@$client->code}}">
                                  </div>
                                </div>

                                <div class="row">
                                  <div class="col-md-6">
                                    <label for="">C3. @lang('main.street')</label><br>
                                    <input disabled type="text" class="form-control"
                                           name="street"
                                           value="{{@$client->street}}">
                                  </div>
                                  <div class="col-md-6">
                                    <label for="">C4. @lang('main.houseNumber')</label><br>
                                    <input disabled type="text" class="form-control"
                                           name="hose_num"
                                           value="{{@$client->hose_num}}">
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-md-4">
                                    <label for="">C5. Cap.</label><br>
                                    <input disabled type="text" class="form-control zip"
                                           name="zip_code"
                                           value="{{@$client->zip_code}}">
                                  </div>
                                  <div class="col-md-4">
                                    <label for="">C6. @lang('main.city')</label><br>
                                    <input disabled type="text" class="form-control"
                                           name="town" value="{{@$client->town}}">
                                  </div>
                                  <div class="col-md-4">
                                    <label for="">C7. @lang('main.province')</label><br>
                                    <input disabled type="text" class="form-control"
                                           name="district"
                                           value="{{@$client->district}}">
                                  </div>
                                </div>

                                <div class="row">
                                  <div class="col-md-4">
                                    <label for="">C9. IVA / VAT</label><br>
                                    <input disabled type="text" class="form-control"
                                           name="vat" value="{{@$client->vat}}">
                                  </div>
                                  <div class="col-md-4">
                                    <label for="">C10. Numero di telefono pprincipale</label><br>
                                    <input disabled type="text" class="form-control phone"
                                           name="telephone"
                                           value="{{@$client->telephone}}">
                                  </div>
                                  <div class="col-md-4">
                                    <label for="">C11. Numero Cellulare Principale</label><br>
                                    <input disabled type="text" class="form-control phone"
                                           name="mobile"
                                           value="{{@$client->mobile}}">
                                  </div>
                                </div>
                                <br>
                                <div class="row">
                                  <div class="col-6">
                                    <div class="card card-body">

                                      <div class="col-12">
                                        <div class="form-group">
                                          <h5>C12. Nome 01</h5>
                                          <input disabled type="text"
                                                 name="private_phone_person_name_1"
                                                 value="{{@$client->private_phone_person_name_1}}"
                                                 class="form-control">
                                        </div>
                                      </div>
                                      <div class="col-12">
                                        <div class="form-group">
                                          <h5>C13. @lang('main.privateNumber') 01</h5>
                                          <input disabled type="text"
                                                 name="private_phone_1"
                                                 value="{{@$client->private_phone_1}}"
                                                 class="form-control">
                                        </div>
                                      </div>
                                    </div>
                                  </div>

                                  <div class="col-6">
                                    <div class="card card-body">
                                      <div class="col-12">
                                        <div class="form-group">
                                          <h5>C15. Nome 01</h5>
                                          <input disabled type="text"
                                                 name="private_phone_person_name_2"
                                                 value="{{@$client->private_phone_person_name_2}}"
                                                 class="form-control">
                                        </div>
                                      </div>
                                      <div class="col-12">
                                        <div class="form-group">
                                          <h5>C16. @lang('main.privateNumber') 02</h5>
                                          <input disabled type="text"
                                                 name="private_phone_2"
                                                 value="{{@$client->private_phone_2}}"
                                                 class="form-control">
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-6">
                                    <div class="form-group">
                                      <h5>C14. Email 01</h5>
                                      <input disabled type="email" name="email_1"
                                             value="{{@$client->email_1}}"
                                             class="form-control">
                                    </div>
                                  </div>
                                  <div class="col-6">
                                    <div class="form-group">
                                      <h5>C17. Email 02</h5>
                                      <input disabled type="email" name="email_2"
                                             value="{{@$client->email_2}}"
                                             class="form-control">
                                    </div>
                                  </div>
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
                    {{-- ********* View ENDS HERE *********** --}}

                    {{-- ********* EDIT STARTS HERE********** --}}
                    <button type="button" class="btn btn-success btn-sm" data-toggle="modal"
                            data-target="#updateclient{{$client->id}}" data-whatever="@mdo">@lang('main.edit')
                    </button>
                    <div class="modal fade" id="updateclient{{$client->id}}" tabindex="-1"
                         role="dialog" aria-labelledby="updateclient{{$client->id}}Label1">
                      <div class="modal-dialog modal-xl" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h4 class="modal-title" id="updateclient{{$client->id}}Label1">
                              Aggiorna</h4>
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-label="Close"><span
                                  aria-hidden="true">&times;</span></button>
                          </div>
                          <form method="POST" action="{{route('client.update', $client)}}">
                            @csrf
                            @method('PUT')
                            <div class="modal-body">
                              <div class="modal-body">
                                <div class="row">
                                  <div class="col-md-9">
                                    <label for="">C1. @lang('main.businessName')</label><br>
                                    <input type="text" class="form-control"
                                           name="business_name"
                                           value="{{$client->business_name}}">
                                  </div>
                                  <div class="col-md-3">
                                    <label for="">C2. Codice</label><br>
                                    <input type="text" class="form-control"
                                           name="code" value="{{@$client->code}}">
                                  </div>
                                </div>

                                <div class="row">
                                  <div class="col-md-6">
                                    <label for="">C3. @lang('main.street')</label><br>
                                    <input type="text" class="form-control"
                                           name="street"
                                           value="{{@$client->street}}">
                                  </div>
                                  <div class="col-md-6">
                                    <label for="">C4. @lang('main.houseNumber')</label><br>
                                    <input type="text" class="form-control"
                                           name="hose_num"
                                           value="{{@$client->hose_num}}">
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-md-4">
                                    <label for="">C5. Cap.</label><br>
                                    <input type="text" class="form-control zip"
                                           name="zip_code"
                                           value="{{@$client->zip_code}}">
                                  </div>
                                  <div class="col-md-4">
                                    <label for="">C6. @lang('main.city')</label><br>
                                    <input type="text" class="form-control"
                                           name="town" value="{{@$client->town}}">
                                  </div>
                                  <div class="col-md-4">
                                    <label for="">C7. @lang('main.province')</label><br>
                                    <input type="text" class="form-control"
                                           name="district"
                                           value="{{@$client->district}}">
                                  </div>
                                </div>

                                <div class="row">
                                  <div class="col-md-4">
                                    <label for="">C9. IVA / VAT</label><br>
                                    <input type="text" class="form-control"
                                           name="vat" value="{{@$client->vat}}">
                                  </div>
                                  <div class="col-md-4">
                                    <label for="">C10. Numero di telefono pprincipale</label><br>
                                    <input type="text" class="form-control phone"
                                           name="telephone"
                                           value="{{@$client->telephone}}">
                                  </div>
                                  <div class="col-md-4">
                                    <label for="">C11. Numero Cellulare Principale</label><br>
                                    <input type="text" class="form-control phone"
                                           name="mobile"
                                           value="{{@$client->mobile}}">
                                  </div>
                                </div>
                                <br>
                                <div class="row">
                                  <div class="col-6">
                                    <div class="card card-body">
                                      <div class="col-12">
                                        <div class="form-group">
                                          <h5>C12. Nome 01</h5>
                                          <input type="text"
                                                 name="private_phone_person_name_1"
                                                 value="{{@$client->private_phone_person_name_1}}"
                                                 class="form-control">
                                        </div>
                                      </div>
                                      <div class="col-12">
                                        <div class="form-group">
                                          <h5>C13. @lang('main.privateNumber') 02</h5>
                                          <input type="text"
                                                 name="private_phone_1"
                                                 value="{{@$client->private_phone_1}}"
                                                 class="form-control">
                                        </div>
                                      </div>
                                    </div>
                                  </div>

                                  <div class="col-6">
                                    <div class="card card-body">
                                      <div class="col-12">
                                        <div class="form-group">
                                          <h5>C15. Nome 02</h5>
                                          <input type="text"
                                                 name="private_phone_person_name_2"
                                                 value="{{@$client->private_phone_person_name_2}}"
                                                 class="form-control">
                                        </div>
                                      </div>
                                      <div class="col-12">
                                        <div class="form-group">
                                          <h5>C16. @lang('main.privateNumber') 02</h5>
                                          <input type="text"
                                                 name="private_phone_2"
                                                 value="{{@$client->private_phone_2}}"
                                                 class="form-control">
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-6">
                                    <div class="form-group">
                                      <h5>C14. Email 01</h5>
                                      <input type="email" name="email_1"
                                             value="{{@$client->email_1}}"
                                             class="form-control">
                                    </div>
                                  </div>
                                  <div class="col-6">
                                    <div class="form-group">
                                      <h5>C17. Email 02</h5>
                                      <input type="email" name="email_2"
                                             value="{{@$client->email_2}}"
                                             class="form-control">
                                    </div>
                                  </div>
                                </div>
                              </div>

                            </div>
                            <div class="modal-footer">
                              <button type="button" class="btn btn-default"
                                      data-dismiss="modal">Vicina
                              </button>
                              <input type="submit" class="btn btn-primary"
                                     value="Aggiorna">
                            </div>
                          </form>
                        </div>
                      </div>
                    </div>
                    <!-- /.modal -->
                    {{-- ********* EDIT ENDS HERE *********** --}}

                    {{--  this is for permanant delete  --}}
                    <a class="btn btn-danger btn-sm" href="javascript:;"
                       onclick="confirmDelete('{{$client->id}}')">@lang('main.delete')</a>
                    <form id="delete-client-{{$client->id}}"
                          action="{{ route('client.destroy', $client->id) }}"
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
              document.getElementById('delete-client-' + id).submit();
          }
      }
  </script>

@endsection
