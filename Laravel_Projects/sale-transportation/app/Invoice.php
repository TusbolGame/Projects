<?php

namespace App;

use App\Order;
use Illuminate\Database\Eloquent\Model;

class Invoice extends Model
{
    protected $guarded = ['id'];

    public function client(){
        return $this->belongsTo('App\Client');
    }


    public function transporter(){
        return $this->belongsTo('App\Transporter');
    }

    public function producer(){
        return $this->belongsTo('App\Producer');
    }


    public function order()
    {
        return $this->belongsTo('App\Order');
    }
}
