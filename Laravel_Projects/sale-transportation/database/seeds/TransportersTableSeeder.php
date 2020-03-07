<?php

use Illuminate\Database\Seeder;

class TransportersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        factory(App\Transporter::class, 40)->create();
    }
}
