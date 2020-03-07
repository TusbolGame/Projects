<?php

/* @var $factory \Illuminate\Database\Eloquent\Factory */

use App\Producer;
use Faker\Generator as Faker;

$factory->define(Producer::class, function (Faker $faker) {
    return [
        'name' => $faker->firstNameMale,
        'vat' => $faker->ean13,
        'reg_number' => $faker->numberBetween($min = 1000, $max = 9000),
        'mobile' => $faker->e164PhoneNumber,
        'email' => $faker->email,
        'address' => $faker->address,
    ];
});
