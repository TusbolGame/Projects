@extends('layouts.admin')
@section('content')
  <div class="row mt-3">
    <div class="col-md-12">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">Gestisci Fatture</h4>
          <div class="table-responsive">
            <table id="invoiceTable" class="display nowrap table table-hover table-striped table-bordered"
                   cellspacing="0" width="100%">
              <thead>
              <tr>

                <th>@lang('main.positionNumber')</th>
                <th>@lang('main.recipient')</th>
                <th>@lang('main.invoiceDate')</th>
                <th>@lang('main.invoice') (€)</th>
                <th>@lang('main.invoiceAmount') (Chf)</th>
                <th>@lang('main.paymentStatus')</th>
                <th>@lang('main.action')</th>
              </tr>
              </thead>
              <tfoot>
              <tr style="padding:0px;">
                <th>@lang('main.positionNumber')</th>
                <th>@lang('main.recipient')</th>
                <th>@lang('main.invoiceDate')</th>
                <th>@lang('main.invoice') (€)</th>
                <th>@lang('main.invoiceAmount') (Chf)</th>
                <th>@lang('main.paymentStatus')</th>
                <th>@lang('main.action')</th>
              </tr>
              </tfoot>
              <tbody>

              @foreach ($invoices as $invoice)
                <tr>

                  <td>{{ @$invoice->order->number }}</td>
                  <td>
                    @if($invoice->customer_id == $invoice->seller_id)
                      <span
                          style="background: red; color: white; font-size: 11px; padding: 2px 5px; border-radius: 15px">@lang('main.seller')</span>
                    @else
                      <span
                          style="background: blue; color: white; font-size: 11px; padding: 2px 5px; border-radius: 15px">@lang('main.buyer')</span>
                    @endif
                    {{ @$invoice->customer->business_name }}
                  </td>
                  <td>{{@$invoice->order->invoice_date}}</td>

                  <td>
                    {{ @$invoice->order->total_commission}}
                  </td>
                  <td>
                    {{ @$invoice->order->swiss_francs }}
                  </td>
                  <td>

                    <div class="switch" style="display: inline-block">
                      @if (@$invoice->payment_status == '1')
                        <form method="POST" action="{{route('invoice_payment', $invoice->id)}}"
                              id="form{{$invoice->id}}">
                          @csrf
                          {{-- @method('PUT') --}}
                          <input type="hidden" name="invoice_id" value="{{$invoice->id}}">
                          <label>
                            <input type="checkbox" class="checkbox"
                                   checked name="payment_status"
                                   onchange="$('#form{{$invoice->id}}').submit();"
                                   value="1"><span class="lever"></span></label>
                        </form>
                      @else
                        <form method="POST" action="{{route('invoice_payment', $invoice->id)}}"
                              id="form{{$invoice->id}}">
                          @csrf
                          {{-- @method('PUT') --}}
                          <input type="hidden" name="invoice_id" value="{{$invoice->id}}">
                          <label>
                            <input type="checkbox" class="checkbox"
                                   name="payment_status"
                                   onchange="$('#form{{$invoice->id}}').submit();"
                                   value="1"><span class="lever"></span></label>
                        </form>
                      @endif
                    </div>
                    @if (@$invoice->payment_status == '1')
                      <span class="badge badge-success">Pagado</span>
                    @else
                      <span class="badge badge-danger">Para ser pagado</span>
                    @endif

                  </td>
                  <td>
                    <button type="button" class="btn btn-success btn-sm" data-toggle="modal"
                            data-target="#previeworder{{@$invoice->order->id}}"
                            data-whatever="@mdo">@lang('main.preview')
                    </button>
                    <button onclick="printDiv('printableArea')"
                            class="btn btn-warning btn-sm">@lang('main.print')</button>
                    <div class="modal fade" id="previeworder{{$invoice->order->id}}"
                         {{-- tabindex="-1"  --}}
                         role="dialog" aria-labelledby="previeworder{{$invoice->order->id}}Label1">
                      <div class="modal-dialog modal-xl" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h4 class="modal-title" id="previeworder{{$invoice->order->id}}Label1">
                              Invoice Content</h4>

                            <button type="button" class="close" data-dismiss="modal"
                                    aria-label="Close"><span aria-hidden="true">&times;</span></button>
                          </div>

                          <div class="modal-body">
                            <div class="card card-body">

                              @php $invoice @endphp
                              <div id="printableArea">
                                <style>
                                  .card {
                                    padding: 10px;
                                    margin: 5px;
                                  }

                                  p {
                                    padding: 0px;
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

                                  .underline {
                                    text-decoration: underline;
                                  }
                                </style>
                                <div class="row mt-3">
                                  <div class="col-md-12 text-center">
                                    <div class="">
                                      <img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png"
                                           alt="logo" height="180px">
                                      <p>Via Balestra Nr. 33 – 6900 LUGANO – (CH)</p>
                                      <p>Kontakt: Bertamino Giovanni Tf: +41 79 8398400</p>
                                      <p>E-mail: joe@joesfood.ch & bertamino58@tiscali.it</p>
                                      <p>Website: http://www.joesfood.ch</p>
                                      <p>Nr. Registro Commercio: CH-501.1.018.030-4 - IDI: CHE-409.161.060</p>
                                      <p>Partita IVA / TVA: CH-501.1.018.030-4</p>
                                    </div>
                                  </div>
                                </div>
                                <br>
                                <div class="row">
                                  <div class="col-md-6">
                                    <div class="card card-body">
                                      <p class="text-center"><span class="red"
                                                                   style="font-size:35px"><b>FATTURA</b></span></p>
                                    </div>
                                    <div class="card card-body">
                                      <h4 class="text-center"> Fattura Esportatore/ Facture d'exportation /
                                        Exportrechnung Nr.</h4>
                                      <p class="text-center underline"><b>  {{ @$invoice->order->invoice_number}}</b>
                                      </p>
                                    </div>
                                    <div class="card card-body">
                                      <h4 class="text-center"> Posizione / Position</h4>
                                      <p class="text-center underline"><b>  {{ @$invoice->order->number}}</b></p>
                                    </div>

                                  </div>


                                  <div class="col-md-6">
                                    <div class="card card-body">
                                      <h4 class="text-center"> Spett.le Ditta/Maison/Firma</h4>
                                      <p class="text-center underline">
                                        <b> {{ @$invoice->order->seller->business_name }}</b>
                                        <br>
                                        {{ @$invoice->order->seller->street }} {{ @$invoice->order->seller->hose_num }}
                                        <br>
                                        {{ @$invoice->order->seller->zip_code }} {{@$invoice->order->seller->city}}
                                        <br>
                                        {{ @$invoice->order->seller->nation_code }}-

                                        @switch($invoice->order->seller->nation_code)
                                          @case("A")
                                          Austria
                                          @break
                                          @case('B')
                                          Belgio
                                          @break
                                          @case('FR')
                                          Francia
                                          @break
                                          @case('DE')
                                          Germania
                                          @break
                                          @case('IT')
                                          Italia
                                          @break
                                        @endswitch
                                        <br>
                                        {{@$invoice->order->seller->district}}
                                        <br>
                                        {{ @$invoice->order->seller->email_1 }}
                                      </p>
                                    </div>

                                  </div>

                                </div>

                                <div class="row">
                                  <div class="col-md-6">
                                    <div class="card card-body">
                                      <b>Lugano <span class="underline"> {{@$invoice->place}}
                                          / {{date("d-m-Y", strtotime($invoice->order->invoice_date))}}
</span></b>
                                    </div>
                                  </div>
                                  <div class="col-md-6">
                                    <div class="card card-body">
                                      <b> Part. Iva/Tva destinatario: <span
                                            class="underline"> {{@$invoice->order->seller->vat}}</span></b>
                                    </div>
                                  </div>
                                </div>
                                {{-- Bank details --}}
                                <div class="row">
                                  <div class="col-md-12">
                                    <div class="card card-body">
                                      <h3 class="text-center"> Bank Details</h3>
                                      <p>Modalità di pagamento: Bonifico Bancario a 30 gg/ Mode de paiment: Virement
                                        Bancaire de 30 jours. Zahlungsart: 30 Tage Bankueberweisung.</p>
                                      <br>
                                      <p>Intestato a/Dirigé verse /Ging zu: Joe's Food di Bertamino Giovanni</p>
                                      <br>
                                      <p>{!! Auth::user()->bank_details !!}</p>
                                    </div>
                                  </div>
                                </div>

                                <div class="row">
                                  <div class="col-md-12">
                                    <div class="card card-body">
                                      <table data-toggle="table" data-height="250" class="table-striped table-bordered">
                                        <tbody>
                                        <tr>
                                          <td width="90%">Descrizione</td>
                                          <td>Importo</td>
                                        </tr>
                                        <tr>
                                          <td>
                                            Provvigione su vendita effettuata nostro tramite inerente/ Commission en
                                            vente / Provision bei Verkauf
                                            <br>
                                            Fattura Esportatore / Facture d'esportation / Exportrechnung Nr <span
                                                class="underline"> {{@$invoice->order->invoice_number}}</span> Del <span
                                                class="underline"> {{date("d/m/Y", strtotime(@$invoice->order->invoice_date))}}

</span>
                                            <br>
                                            Camion / Lkw <span
                                                class="underline">{{@$invoice->order->lorry_reg_number}}</span>
                                            <br>
                                            Destinatario /Destinataire / Empfaenger <span
                                                class="underline">{{ @$invoice->order->seller->business_name }}</span>
                                          </td>
                                          <td>
                                            CHF {{@$invoice->order->swiss_francs}}<br>

                                          </td>
                                        </tr>
                                        </tbody>
                                      </table>
                                    </div>
                                  </div>
                                </div>
                                <div class="row">
                                  <div class="col-md-6">

                                  </div>

                                  <div class="col-md-6">
                                    <div class="card card-body">
                                      <table data-toggle="table" data-height="250" class="table-bordered">
                                        <tbody>
                                        <tr>
                                          <td>Importo Imponibile totale provvigione</td>
                                          <td>CHF {{@$invoice->order->swiss_francs}}</td>
                                        </tr>
                                        <tr>
                                          <td>Importo totale</td>
                                          <td>CHF {{@$invoice->order->swiss_francs}}</td>
                                        </tr>
                                        <tr>
                                          <td>Importo totale al cambio</td>
                                          <td>EURO {{@$invoice->order->total_commission}}</td>
                                        </tr>

                                        </tbody>
                                      </table>

                                    </div>
                                  </div>
                                </div>


                                <div class="row">
                                  <div class="col-md-12">
                                    <p><b><span>NOTES</span></b></p>
                                    <p>
                                      Fattura non soggetta a Iva / Facture non soumise à la TVA /Rechnung ohne
                                      Mehrwersteuer
                                      <br>
                                      Cambio / éxchange de monnaie /Wechselstube CHF
                                      1= {{@$invoice->order->exchange_rate}} EURO
                                    </p>
                                  </div>
                                </div>
                                <br>
                                <br>

                                <div class="row">
                                  <div class="col-md-12">
                                    <p class="text-center">
                                      Kontakt: Bertamino Giovanni Tf: +41 79 8398400
                                      <br>
                                      Website: http://www.joesfood.ch<br/>
                                      E-mail: joe@joesfood. ch & bertamino58@tiscali.it
                                    </p>
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

                        </div>
                      </div>
                    </div>
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
        document.getElementById('delete-invoice-' + id).submit();
      }
    }
  </script>
  <script>
    $(document).ready(function () {


      if ($('#customer-selector').val() == "all") {
        $('#email-button').css("display", "none");
        $('#all-button').css("display", "none");
      } else {
        $('#email-button').css("display", "inline-block");
        $('#all-button').css("display", "inline-block");
      }
      $("#email-button").on("click", function () {
        $('#form').attr('action', "{{route('invoice.email_table')}}");
        $("#form").submit();
        e.preventDefault();
      });

    });
  </script>
  <script>
    function printDiv(divName) {
      var printContents = document.getElementById(divName).innerHTML;
      var originalContents = document.body.innerHTML;

      document.body.innerHTML = printContents;

      window.print();

      document.body.innerHTML = originalContents;
      window.location.href = "{{route('invoice.history')}}";
    }
  </script>
@endsection
