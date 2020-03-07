<?php

namespace App;

use App\Order;
use Illuminate\Database\Eloquent\Model;

class Client extends Model
{
    protected $guarded = ['id'];

    public function order()
    {
        return $this->hasOne('App\Order');
    }
    public function invoice()
    {
        return $this->hasOne('App\Invoice');
    }
}
