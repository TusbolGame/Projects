<?php

namespace App;

use App\Client;
use App\Invoice;
use App\Producer;
use App\Transporter;
use Illuminate\Database\Eloquent\Model;

class Order extends Model
{
    protected $guarded = ['id'];
    public function customer(){
    return $this->belongsTo('App\Client','customer_id');
    }

    public function seller(){
        return $this->belongsTo('App\Client','seller_id');
    }


    public function buyer(){
        return $this->belongsTo('App\Client','buyer_id');
    }


    public function transporter(){
        return $this->belongsTo('App\Transporter','transporter_id');
    }

    public function producer(){
        return $this->belongsTo('App\Producer','producer_id');
    }


    public function invoice(){
        return $this->hasOne('App\Invoice');
    }

}
