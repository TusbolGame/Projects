@component('mail::message')

<style>
table{width: 100%;}
.table td, .table th {padding: 0rem;vertical-align: top;border-top: 1px solid #dee2e6;}
.card-body{width: 100%;}
hr{height: 1px;color: red;background-color: red;border: none;}
</style>
<body>
<style>
/* p{
padding: 0px;
margin: 0px;
} */
b{font-weight: 500;}
td{font-weight: 500;padding-left: 10px;padding-top: 10px;padding-bottom: 10px;}
.red{color:red;}
.underline{text-decoration: underline;}

</style>
<style>
#content > p {
width: 100% !important;
}
</style>
<div class="card card-body" style="display:block">
<h3 style="font-weight: bold; color: silver">Contenuto dell'ordine</h3>
<div class="card card-body" style="display:block">
<div class="row" style="display:block!important;">
@php $content = html_entity_decode(@$orders[0]->content, ENT_QUOTES); @endphp
<div style = "display:block;width:100%;">@php print_r($content); @endphp</div>
</div>
<br>
<div class="card card-body"style="display:block!important;">
<div class="table-responsive">
<table id="invoiceTable" class="display nowrap table table-hover table-striped table-bordered"
cellspacing="0" width="100%">
<thead>
<tr>
<th>No</th>
<th>@lang('main.positionNumber')</th>
<th>A partire dal</th>
<th>In cui si</th>
<th>@lang('main.product')</th>
<th>@lang('main.weight') (t)</th>
<th>Data</th>
</tr>
</thead>

<tbody>
@php
$i=1;
$total_invoice_amount = null;
$total_commission = null;
$num = 0;
@endphp
@foreach ($orders as $order)
<tr>
<td>{{@$i}}</td>
<td>{{@$order->number}}</td>
<td>{{@$order->buyer->business_name}}</td>
<td>{{@$order->buyer->nation_code}}</td>
<td>{{@$order->product}}</td>
<td>{{@$order->weight_loaded_kg}}</td>
<td>{{@$order->loading_day}}</td>
@php $i++ @endphp
</tr>
@endforeach
</tbody>
</table>
</div>
</div>
</div>
<br>
<div class="row">
<div class="col-md-12 " style="text-align: left">

<img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png" alt="logo"
height="180px">
<div class="card card-body" style="margin-top: 30px">

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
</body>


@endcomponent
