@component('mail::message')
# Introduction
<div>{{@$order->customer->business_name}}</div>
<div>asdfasdfasdfasdfasdfasdfasdfasdfasdf</div>
@component('mail::button', ['url' => ''])
Button Text
@endcomponent

Thanks,<br>
{{ config('app.name') }}
@endcomponent
