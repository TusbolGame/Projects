<?php

namespace App;

use App\Order;
use Illuminate\Database\Eloquent\Model;

class Transporter extends Model
{
    protected $guarded = [];

    public function order()
    {
        return $this->hasOne('App\Order');
    }
    public function invoice()
    {
        return $this->hasOne('App\Invoice');
    }
}
