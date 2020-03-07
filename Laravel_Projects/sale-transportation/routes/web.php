<?php

Route::get('/', function () {
  return view('welcome');
});

Route::get('locale/{locale}', function ($locale) {
  Session::put('locale', $locale);
  return redirect()->back();
})->name('locale');

Auth::routes(['register' => false]);

Route::group(['middleware' => ['auth']], function () {
  //-------------- show --------------//
  Route::resource('/home', 'HomeController');
  Route::resource('/order', 'OrderController');
  Route::resource('/client', 'ClientController');
  Route::resource('/card', 'CardController');
  Route::resource('/transporter', 'TransporterController');
  Route::resource('/producer', 'ProducerController');
  Route::resource('/invoice', 'InvoiceController');
  Route::get('/invoice_history', 'InvoiceController@history')->name('invoice.history');

  //-------------- home --------------//
  Route::post('/home/getreport', 'HomeController@getreport')->name('home.getreport');
  Route::post('/order_email/{order}', 'HomeController@order_email')->name('home.order_email');
  Route::get('/order_email_show/{order}', 'HomeController@order_show')->name('home.order_email_show');
  Route::post('/orders_email/{name}', 'HomeController@orders_email')->name('home.orders_email');
  Route::get('/orders_email_show/{name}', 'HomeController@orders_show')->name('home.orders_email_show');
  Route::get('/invoice_show/{order}', 'HomeController@invoice_show')->name('home.invoice_show');
  Route::post('/home_paymentstatus/{id}', 'HomeController@updatePaymentStatus')->name('home.invoice_payment');
  Route::post('/customerpaymentstatus/{id}', 'HomeController@getreport')->name('customer_payment_status');
  Route::get('/order_delete/{order}', 'HomeController@order_delete')->name('home.order_delete');
  Route::get('/order_preview/{order}', 'HomeController@order_preview')->name('home.order_preview');
  Route::post('/updateStatus/{id}', 'HomeController@updateStatus')->name('updateStatus');
  Route::post('/paymenstatus/{id}', 'HomeController@updatePaymentStatus')->name('home_payment');
  Route::post('/AtupdateComplete/{order}', 'HomeController@updateComplete')->name('home.updateComplete');


  //-------------- profile --------------//
  Route::PUT('/profile/invoice_content', 'ProfileController@invoice_content')->name('profile.invoice_content');
  Route::get('/managerprofile', 'ProfileController@getmanager')->name('managerprofile');
  Route::PUT('/managerprofile', 'ProfileController@update')->name('managerprofile.update');
  Route::PUT('/profile/update_user', 'ProfileController@update_user')->name('profile.update_user');
  Route::PUT('/profile/bank_details', 'ProfileController@bank_details')->name('profile.bank_details');
  Route::PUT('/profile/email_content', 'ProfileController@email_content')->name('profile.email_content');
  Route::PUT('/profile/missed_list_content', 'ProfileController@missed_list_content')->name('profile.missed_list_content');
  Route::PUT('/profile/reminder_email_content', 'ProfileController@reminder_email_content')->name('profile.reminder_email_content');
  Route::get('/profile', 'ProfileController@index')->name('profile');
  Route::PUT('/profile', 'ProfileController@update')->name('profile.update');
  Route::PUT('/profile/updatepassword', 'ProfileController@updatepassword')->name('profile.updatepassword');

  //-------------- invoice --------------//
  Route::get('/email/{invoice}', 'InvoiceController@email')->name('invoice.email');
  Route::post('/unpaid_email/{customer_id}', 'InvoiceController@unpaid_mail')->name('invoice.unpaid_mail');
  Route::get('/reminderEmail/{invoice}', 'InvoiceController@unpaid_mail')->name('invoice.reminderEmail');
  Route::post('/paymentstatus/{id}', 'InvoiceController@updatePaymentStatus')->name('invoice_payment');
  Route::post('/invoice/getreport', 'InvoiceController@getreport')->name('invoice.getreport');
  Route::post('/invoice/gethistory', 'InvoiceController@gethistory')->name('invoice.gethistory');
  Route::post('/invoice/email_table', 'InvoiceController@email_table')->name('invoice.email_table');
  Route::get('/reminderShow/{invoice}', 'InvoiceController@reminderShow')->name('invoice.reminderShow');
  Route::get('/reminder/{invoice}', 'InvoiceController@reminder')->name('invoice.reminder');

  //-------------- order --------------//
  Route::get('/order_pdf/{order}', 'OrderController@orderPdf')->name('order_pdf');
  Route::post('/postajax', 'OrderController@search');
  Route::get('/number', 'ClientController@getNumber');
  Route::post('/complete', 'OrderController@complete')->name('order.complete');
  Route::post('/updateComplete/{order}', 'OrderController@updateComplete')->name('order.updateComplete');
  Route::post('/update/{order}', 'OrderController@update')->name('order.update');
  Route::post('/getReport', 'OrderController@getReport')->name('order.getReport');

});


