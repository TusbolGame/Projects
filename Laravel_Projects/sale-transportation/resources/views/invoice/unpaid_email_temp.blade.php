@component('mail::message')

<style>



table{

width: 100%;

}

div{
    display: block!important;
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








<div class="card card-body">

@php $content = html_entity_decode(@$invoices[0]->content, ENT_QUOTES); @endphp
<div style = "display:block;width:100%;">@php print_r($content); @endphp</div>

</div>



</div>



<br>



<h3 style="font-weight: bold; color: silver">Unpaid Content</h3>



<div class="table-responsive">

<table id="invoiceTable" class="display nowrap table table-hover table-striped table-bordered"

cellspacing="0" width="100%">

<thead>

<tr>

<th>No</th>

<th>@lang('main.positionNumber')</th>

<th>Invoice N.</th>

<th>@lang('main.invoiceDate')</th>

<th>product</th>

<th>Weight</th>

<th>Totale Fattura (€)</th>

<th>Commissione (CHF)</th>

</tr>

</thead>



<tbody>



@php

$i=1;

$total_invoice_amount = null;

$total_commission = null;

$num = 0;

@endphp

@foreach ($invoices as $invoice)

@php

$num++;

@endphp

<tr>

<td>{{@$num}}</td>

<td>{{ @$invoice->order->number }}</td>

<td>{{ @$invoice->id }}</td>

<td>

@if($invoice->customer_id == $invoice->seller_id)

<span style="background: red; color: white; font-size: 11px; padding: 2px 5px; border-radius: 15px">selled</span>

@else

<span style="background: blue; color: white; font-size: 11px; padding: 2px 5px; border-radius: 15px">buyed</span>

@endif

{{ date('d-m-Y', strtotime($invoice->order->invoice_date)) }}

</td>

<td>{{ @$invoice->order->product}}</td>

<td>{{ @$invoice->order->weight_loaded_kg}} t</td>



<td>

@if (@$invoice->payment_status != '1')

@php

	$total_invoice_amount = $total_invoice_amount + $invoice->order->total_commission;

@endphp

@endif

{{ @$invoice->order->total_commission}}

</td>

<td>

@php

$total_commission = $total_commission + $invoice->order->swiss_francs;

@endphp

{{ @$invoice->order->swiss_francs }}

</td>

</tr>

@endforeach



<tr style="background-color:silver">

<td></td>

<td></td>

<td></td>

<td></td>

<td></td>

<td>Sum</td>

<td><i>€ {{$total_invoice_amount}}</i></td>

<td><i>(CHF) {{$total_commission}}</i></td>



</tr>



</tbody>

</table>

</div>

<div class="row">

    <div class="col-md-12 text-left">

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

<style>

#content > p{

width: 100%!important;

}

</style>

<input type="hidden" value="{{@$invoices[0]->content}}" id="content_r">


</body>


@endcomponent