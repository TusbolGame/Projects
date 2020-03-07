<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateOrdersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('orders', function (Blueprint $table) {
            $table->bigIncrements('id')->unique();
            $table->unsignedInteger('supplier_id')->nullable();
            $table->unsignedInteger('customer_id')->nullable();
            $table->unsignedInteger('transporter_id')->nullable();
            $table->string('lorry_reg_number')->nullable();
            $table->unsignedInteger('producer_id')->nullable();
            $table->string('rate_currency_exchange')->nullable(); //Rate of currency exchange
            $table->string('tare')->nullable(); // Tare
            $table->string('commission')->nullable(); // Commission
            $table->string('nr_commission')->nullable(); // Nr Commission
            $table->string('total_commission')->nullable(); // Total commission (our commission) in euro with rate of exchange

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('orders');
    }
}
