<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateCardsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('cards', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->string('business_name')->nullable(); // client/transporter
            $table->string('position_number')->nullable(); // client/transporter
            $table->string('contract_number')->nullable(); // client/transporter
            $table->string('lorry_reg_number')->nullable(); // client/transporter
            $table->string('supplier_invoice_number')->nullable(); // client/transporter
            $table->string('supplier_invoice_date')->nullable(); // client/transporter
            $table->string('product_name')->nullable(); // client/transporter
            $table->string('variety')->nullable(); // client/transporter
            $table->string('caliper')->nullable(); // client/transporter
            $table->string('nature')->nullable(); // client/transporter
            $table->string('packing')->nullable(); // client/transporter
            $table->string('quality')->nullable(); // client/transporter
            $table->string('price_on_departure')->nullable(); // client/transporter
            $table->string('price_on_arrival')->nullable(); // client/transporter
            $table->string('weight')->nullable(); // client/transporter
            $table->string('amount')->nullable(); // client/transporter
            $table->string('amount_commission_CHF')->nullable(); // client/transporter
            $table->string('amount_commission_EURO')->nullable(); // client/transporter

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
        Schema::dropIfExists('cards');
    }
}
