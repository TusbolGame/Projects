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


class InvoiceController extends Controller
{

    public function index(){
        $clients = Client::all(); // suppliers/customers
        $producers = Producer::all(); // producers
        $transporters = Transporter::all(); //transporters
        $invoices = Invoice::where(['payment_status'=> NULL])->get();
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
        $pageTitle = 'invoiceManagement';
        return view('invoice.index',compact('pageTitle', 'customer_info','invoices','clients','transporters','producers','orders'));
    }

    public function history(){
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
        $pageTitle = 'historicalInvoice';
        return view('invoice.History',compact('pageTitle', 'customer_info','invoices','clients','transporters','producers','orders'));
    }

    public function getreport(Request $request){
        $customer_name = $request->supplier_name;
        if($customer_name == 'all'){
            return redirect('invoice');
        }
		$customer = Client::where(['business_name'=> $customer_name])->get(['id']);
        $customer_id = $customer[0]->id;
		$invoices = Invoice::where(['customer_id' => $customer_id,'payment_status'=> NULL])->get();


        $clients = Client::all(); // suppliers/customers
        $producers = Producer::all(); // producers
        $transporters = Transporter::all(); //transporters
        $orders = Order::all();

        foreach ($invoices as $invoice){
            foreach ($orders as $order){
                if($invoice->order_id == $order->id){
                    $invoice->order=$order;
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

        $all = Invoice::all();
        foreach ($all as $in){
            foreach ($clients as $client){
                if($in->customer_id === $client->id){
                    $in->customer = $client;
                }
            }

        }
        $customer_info = [];
        foreach ($all as $invoice){
            array_push($customer_info,$invoice->customer->business_name);
        }
        $customer_info = array_unique($customer_info);
        $pageTitle = 'invoiceManagement';
//        echo $invoices;
                return view('invoice.index',compact('pageTitle','customer_name' , 'customer_info' ,'invoices','clients','transporters','producers','orders'));

    }

    public function create()
    {
        //
    }

    public function store(Request $request)
    {
        $validators = $request->validate([
            'order_id'=> 'required',
            'vat'=>'required',
            'date'=>'required',
            'place'=>'required'
        ]);

        $customer = Order::select('customer_id')->where('id',$request->order_id)->first();
        // dd($customer->customer_id);
        // dump($request->order_id);
        // $relatedOrder = Order::where('id',$request->order_id)->first();
        // dump($relatedOrder);
        // dd($request);

        $invoice = Invoice::create([
            'customer_id' => $customer->customer_id,
            'order_id' => $request->order_id,
            'vat' => $request->vat,
            'date' => $request->date,
            'place'=> $request->place
        ]);

        if ($invoice) {
            return redirect(route('invoice.index'))->with('message','Invoice added successfully!');
        }else{
            return back()->withErrors($validators);
        }
    }

    public function email_table(Request $request){
        $main_data = User::all();
        $supplier_name = $request->supplier_name;
        $payment_status = $request->payment_status;
        $supplier = Client::where(['business_name'=> $supplier_name])->get(['id']);
        $supplier_id = $supplier[0]->id;
        $invoices = Invoice::where(['customer_id' => $supplier_id,'payment_status'=> NULL])->get();
        if(count($invoices)>0){
            $clients = Client::all(); // suppliers/customers
            $producers = Producer::all(); // producers
            $transporters = Transporter::all(); //transporters
            $orders = Order::all();
            foreach ($invoices as $invoice){
                foreach ($orders as $order){
                    if($invoice->order_id === $order->id){
                        $invoice->order=$order;
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

            $pageTitle = 'Email di promemoria';
            return view('invoice.table_show',compact('pageTitle', 'main_data', 'payment_status' ,'invoices','clients','transporters','producers','orders'));
        }else{
            return redirect('invoice');
        }
    }


    public function unpaid_mail($customer_id, Request $request)
    {
        $content = $request['content'];
        $invoices = Invoice::where(['payment_status'=> null, 'customer_id' => $customer_id])->get();
        $clients = Client::all(); // suppliers/customers
        $producers = Producer::all(); // producers
        $transporters = Transporter::all(); //transporters
        $orders = Order::all();
        for ($x=0;$x<count($invoices);$x++){
            for ($y=0;$y<count($orders);$y++){
                if($invoices[$x]->order_id === $orders[$y]->id){
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
            $invoice->content = $content;

        }
       
        echo $invoices;

        Mail::to('$invoices[0]->customer->email_1')
  
            ->send(new unpaid_email($invoices));

        Log::info("Invoice Email to supplier");

        session(['loading' => 'false']);
        return redirect('invoice')->with('success','Email success!');
    


    }

    public function show(Invoice $invoice)
    {
        $pageTitle = 'viewInvoice';
        $details = User::all();
        return view('invoice.show',compact('pageTitle','invoice', 'details'));
    }

    public function email(Invoice $invoice, Request $request)
    {
        $content = $request['email_content'];
       $invoice->content = htmlspecialchars($content);
       //print_r(new InvoiceMail($invoice));
  
        session(['loading' => 'true']);
        // Mail::to($invoice->order->customer->email_1)
       Mail::to($invoice->order->seller->email_1)
  
            ->send(new InvoiceMail($invoice));

        Log::info("Invoice Email to supplier");

        session(['loading' => 'false']);
        return back()->with('success','Email success!');
    

    }


    public function reminderEmail(Invoice $invoice){
        // dd($invoice->order->customer->email_1);
        session(['loading' => 'true']);
        $reminder_email_content = Auth::user()->reminder_email_content;
        // dd($reminder_email_content);

        Mail::to($invoice->order->seller->email_1)
            // ->cc($moreUsers)
            // ->bcc($evenMoreUsers)
            ->send(new InvoiceReminder($invoice, $reminder_email_content));
        Log::info("Reminder Email to supplier".$invoice->order->customer->email_1);
        // Mail::send('email_temps.testemailContent',array('Author'=>'Ashwani Garg'),
        //     function($message){
        //         $message->to('akgarg007@gmail.com','Testing')->subject('Welcome! Mail is received.');
        //     });
        // echo "Mail sent ok.";

        session(['loading' => 'false']);
        return back()->with('success','Email sent successfully.');
    }


    public function edit(Invoice $invoice)
    {
        //
    }


    public function update(Request $request, Invoice $invoice)
    {
        $validators = $request->validate([
            'name'=> 'required',
            'vat'=>'required',
            'date'=>'required'
        ]);
        // dd($invoice->id);
        $invoice = Invoice::where('id', $invoice->id)->first();
        // dd($invoice);
        $invoice->name = $request->name;
        $invoice->vat = $request->vat;
        $invoice->date = $request->date;

        if ($invoice->save()) {
            return redirect(route('invoice.index'))->with('message','Invoice updated successfully!');
        }else{
            return back()->withErrors($validators);
        }
    }


    public function updatePaymentStatus(Request $request, $id){
        // dump($id);
        // dd($request);
        $invoice = Invoice::where('id', $id)->first();
        // dd($invoice);

        $invoice->payment_status = $request->payment_status;

        if ($invoice->save()) {
            // return redirect(route('invoice.index'))->with('message','Invoice updated successfully!');
            return redirect('/invoice')->with('message','Invoice updated successfully!');
        }else{
            return redirect('/invoice')->withErrors($validators);
        }

    }


    public function destroy(Invoice $invoice)
    {
        // this method deletes the record from the database permanently
        if($invoice->forceDelete()){
            return back()->with('message','Record Successfully Deleted!');
        }else{
            return back()->with('message','Error Deleting Record');
        }
    }
	
	public function reminderShow(Invoice $invoice)
	{
		//echo $invoice;
		$details = User::all();
		//echo $details;
		$pageTitle = 'Reminder Email Show';
		return view('invoice.reminderShow', compact('invoice', 'details', 'pageTitle'));	
	}
}
