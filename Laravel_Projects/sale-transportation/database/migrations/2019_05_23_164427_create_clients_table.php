<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateClientsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        // clients can be suppliers or customers
        Schema::create('clients', function (Blueprint $table) {
            $table->bigIncrements('id')->unique();
            $table->string('business_name', 300)->nullable();
            $table->string('code', 10)->nullable();
            $table->string('address', 500)->nullable();
            $table->string('vat', 50)->nullable();
            $table->string('telephone', 50)->nullable();
            $table->string('mobile', 50)->nullable();
            $table->string('private_phone_1', 50)->nullable();
            $table->string('private_phone_person_name_1', 50)->nullable();
            $table->string('private_phone_2', 50)->nullable();
            $table->string('private_phone_person_name_2', 50)->nullable();
            $table->string('email_1', 100)->nullable();
            $table->string('email_2', 100)->nullable();
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
        Schema::dropIfExists('clients');
    }
}
