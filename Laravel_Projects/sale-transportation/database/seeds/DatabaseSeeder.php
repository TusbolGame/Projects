<?php

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {
        // $this->call(UsersTableSeeder::class);
        $this->call(TransportersTableSeeder::class);
        $this->call(ProducersTableSeeder::class);

        DB::table('users')->insert([
            'name' => 'Admin',
            // 'email' => Str::random(10).'@gmail.com',
            'email' => 'xander.abshire@example.com',
            'password' => bcrypt('secret'),
        ]);

    }
}
