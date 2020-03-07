@component('mail::message')
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

<body>
<style>

/* p{
    padding: 0px;
    margin: 0px;
} */

form p {
  margin-bottom: 10px;
  text-align: center!important; }


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
<div>
    <div class="form-group mt-5" >
        <div id="user_dtails"></div>
        <input type="hidden" value="{{@$invoice->content}}" id="details_content">
    </div>
    <script>
        var details_content = document.getElementById("details_content").value;
        document.getElementById("user_details").innerHTML = details_content;
    </script>
</div>

    <div class="row">
        <div class="col-md-12 text-center">
            <div class="">
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
    <br>
    <div class="row">
        <div class="col-md-6">
            <div class="card ">
                <div class="card-body text-center">
                <p> <span class="red text-center" style="font-size:25px"><b class=" text-center">FATTURA</b></span></p>
                </div>
            </div>


            <div class="card">
                <div class="card-body text-center">
                    <h4> Fattura Esportatore/ Facture d'exportation / Exportrechnung Nr.</h4>
                    <p class="underline text-center"><b>  {{ @$invoice->order->invoice_number}}</b></p>
                </div>
            </div>
            <div class="card ">
                <div class="card-body text-center">
                <h4 class=""> Posizione / Position</h4>
                <p class=" underline text-center"><b>  {{ @$invoice->order->number}}</b></p>
            </div>
            </div>

        </div>


        <div class="col-md-6">
            <div class="card ">
                <div class="card-body text-center">
                    <h4 class="text-center"> Spett.le Ditta/Maison/Firma</h4>
                    <p class="text-center"><b> {{ @$invoice->order->customer->business_name }}</b>
                    <br>
                    {{ @$invoice->order->customer->address }}
                    <br>
                    {{ @$invoice->order->customer->email_1 }}
                    </p>
                </div>
            </div>
        </div>

    </div>

    <div class="row">
        <div class="col-md-6">
            <div class="card ">
                <div class="card-body">
                <b>Lugano  <span class="underline"> {{@$invoice->place}} / {{date("d-m-Y", strtotime(@$invoice->date))}}
                </span></b>
            </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card ">
                <div class="card-body">
                <b> Part. Iva/Tva destinatario:  <span class="underline"> {{@$invoice->vat}}</span></b>
            </div>
            </div>
        </div>
    </div>
    {{-- Bank details --}}
    <div class="row">
        <div class="col-md-12">
            <div class="card ">
                <div class="card-body">
                <h3 class="text-center"> Bank Details</h3>
                <p>Modalità di pagamento: Bonifico Bancario a 30 gg/ Mode de paiment: Virement Bancaire de 30 jours. Zahlungsart: 30 Tage Bankueberweisung.</p>
                <p>Intestato a/Dirigé verse /Ging zu: Joe's Food di Bertamino Giovanni</p>
                <p>{!! Auth::user()->bank_details !!}</p>
            </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="card ">
                <div class="card-body" style="width:100%">
                <table class="table-striped table-bordered">
                    <tbody>
                        <tr>
                            <td width="90%">Descrizione</td>
                            <td width="10%">Importo</td>
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
                                Destinatario /Destinataire / Empfaenger  <span class="underline">{{ @$invoice->order->customer->business_name }}</span>
                            </td>
                            <td>CHF {{@$invoice->order->swiss_francs}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">

        </div>

        <div class="col-md-6">
            <div class="card ">
                <div class="card-body">
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
    </div>

    <div class="row">
        <div class="col-md-12">
                <p><b><span>NOTA</span></b></p>
                <p>
                 Fattura non soggetta a Iva / Facture non soumise à la TVA /Rechnung ohne Mehrwersteuer
                 <br>
                Cambio / éxchange de monnaie /Wechselstube  CHF 1= {{@$invoice->order->exchange_rate}} EURO
                </p>
        </div>
    </div>
    <br>

    <div class="row">
        <div class="col-md-12">
            <p class="text-center">
            Kontakt: Bertamino Giovanni  Tf: +41 79 8398400
            <br>
            Website: http://www.joesfood.chE-mail: joe@joesfood. ch  & bertamino58@tiscali.it
            </p>
        </div>
    </div>



@endcomponent