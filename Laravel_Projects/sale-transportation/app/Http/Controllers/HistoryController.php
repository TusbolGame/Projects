<?php

namespace App\Http\Controllers;
use App\Mail\Unpaid_email;
use App\Order;
use App\Client;
use App\Invoice;
use App\Producer;
use App\User;
use Carbon\Carbon;
use App\Transporter;
use App\Jobs\InvoiceJob;
use App\Mail\InvoiceMail;
use Illuminate\Support\Arr;
use Illuminate\Http\Request;
use App\Mail\InvoiceReminder;
use Illuminate\Support\Collection;
use Illuminate\Support\Facades\DB;
use PDF; // at the top of the file
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Mail;
use function Sodium\add;

class HistoryController extends Controller
{
    public function index(){
		$clients = Client::all(); // suppliers/customers
        $producers = Producer::all(); // producers
        $transporters = Transporter::all(); //transporters
        $invoices = Invoice::where(['payment_status'=> 1])->get();
        $orders = Order::all();
        for ($x=0;$x<count($invoices);$x++){
            for ($y=0;$y<count($orders);$y++){
                if($invoices[$x]->order_id == $orders[$y]->id){
                    $invoices[$x]->order=$orders[$y];
                }
            }
        }

        foreach ($invoices as $invoice){
            foreach ($clients as $client){
                if($invoice->customer_id === $client->id){
                    $invoice->customer = $client;

                }
            }

        }
        $customer_info = [];
        foreach ($invoices as $invoice){
            array_push($customer_info,$invoice->customer->business_name);
        }
        $customer_info = array_unique($customer_info);
        $pageTitle = 'Storico Fatture';
        return view('invoice.history',compact('pageTitle', 'customer_info','invoices','clients','transporters','producers','orders'));
	}
}
