@component('mail::message')

<style>
.card{
padding: 10px;
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

<div class="card card-body"  style="display:block!important;">
@php $invoice @endphp
<div class="row"  >
@php $content = html_entity_decode(@$invoice->content, ENT_QUOTES); @endphp
<div style = "display:block;width:100%;">@php print_r($content); @endphp</div>
</div>

 <div class="row mt-3">
            <div class="col-md-12 text-center">
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
        <div class="row">
            <div class="col-md-6">
                <div class="card card-body">
                    <p class="text-center"> <span class="red" style="font-size:35px"><b>FATTURA</b></span></p>
                </div>
                <div class="card card-body">
                    <h4 class="text-center"> Fattura Esportatore/ Facture d'exportation / Exportrechnung Nr.</h4>
                    <p class="text-center underline"><b>  {{ @$invoice->order->invoice_number}}</b></p>
                </div>
                <div class="card card-body">
                    <h4 class="text-center"> Posizione / Position</h4>
                    <p class="text-center underline"><b>  {{ @$invoice->order->number}}</b></p>
                </div>

            </div>


            <div class="col-md-6">
                <div class="card card-body">
                    <h4 class="text-center"> Spett.le Ditta/Maison/Firma</h4>
                    <p class="text-center underline"><b> {{ @$invoice->order->seller->business_name }}</b>
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
                    <b>Lugano  <span class="underline"> {{@$invoice->place}} / {{date("d-m-Y", strtotime($invoice->order->invoice_date))}}
                </span></b>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card card-body">
                    <b> Part. Iva/Tva destinatario:  <span class="underline"> {{@$invoice->order->seller->vat}}</span></b>
                </div>
            </div>
        </div>
        {{-- Bank details --}}
        <div class="row">
            <div class="col-md-12">
                <div class="card card-body">
                    <h3 class="text-center"> Bank Details</h3>
                    <p>Modalità di pagamento: Bonifico Bancario a 30 gg/ Mode de paiment: Virement Bancaire de 30 jours. Zahlungsart: 30 Tage Bankueberweisung.</p>
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
                    <table data-toggle="table" data-height="250" class="table-striped table-bordered" >
                        <tbody>
                        <tr>
                            <td width="90%">Descrizione</td>
                            <td>Importo</td>
                        </tr>
                        <tr>
                            <td>
                                Provvigione su vendita effettuata nostro tramite inerente/  Commission en vente / Provision bei Verkauf
                                <br>
                                Fattura Esportatore / Facture d'esportation / Exportrechnung Nr <span class="underline"> {{@$invoice->order->invoice_number}}</span> Del <span class="underline"> {{date("d/m/Y", strtotime(@$invoice->order->invoice_date))}}

                                </span>
                                <br>
                                Camion / Lkw  <span class="underline">{{@$invoice->order->lorry_reg_number}}</span>
                                <br>
                                Destinatario /Destinataire / Empfaenger  <span class="underline">{{ @$invoice->order->seller->business_name }}</span>
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
                    <table data-toggle="table" data-height="250"  class="table-bordered">
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
                    Fattura non soggetta a Iva / Facture non soumise à la TVA /Rechnung ohne Mehrwersteuer
                    <br>
                    Cambio / éxchange de monnaie /Wechselstube  CHF 1= {{@$invoice->order->exchange_rate}} EURO
                </p>
            </div>
        </div>
        <br>
        <br>

        <div class="row">
            <div class="col-md-12">
                <p class="text-center">
                    Kontakt: Bertamino Giovanni  Tf: +41 79 8398400
                    <br>
                    Website: http://www.joesfood.ch<br/>
                    E-mail: joe@joesfood. ch  & bertamino58@tiscali.it
                </p>
            </div>
        </div>
		</div>

</div>


@endcomponent