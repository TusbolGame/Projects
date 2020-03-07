@component('mail::message')
<style>
.card{
padding: 5px;
margin: 5px;
}

p{
padding: 5px;
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
</style>
<div class="card card-body p-5">
<div class="row"  >
@php $content = html_entity_decode(@$order->content, ENT_QUOTES); @endphp
<div style = "display:block;width:100%;">@php print_r($content); @endphp</div>
</div>
<div class="card card-body">
<p>Fattura da: Facture de: Rechnung von: <span style=" text-decoration-line: underline;font-weight:bold">{{@$order->seller->business_name}}</span>
N. <span style=" text-decoration-line: underline;font-weight:bold">{{@$order->invoice_number}}</span> del: de: am:<span style=" text-decoration-line: underline;font-weight:bold">{{@$order->invoice_date}}</span> Totale: Total: Somme: <span style=" text-decoration-line: underline;font-weight:bold">{{@$order->amount_in_euro}}</span>
</p>
</div>
<div class="row">

<div class="col-md-12 text-left">

<img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png" alt="logo"
height="180px">
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

@endcomponent
