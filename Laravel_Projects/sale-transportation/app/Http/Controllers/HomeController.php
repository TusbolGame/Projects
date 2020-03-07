<?php
  
  namespace App\Http\Controllers;
  
  use App\Order;
  use App\Client;
  use App\Invoice;
  use App\Producer;
  use App\User;
  use App\Transporter;
  use App\Mail\OrderEmail;
  use App\Mail\OrdersEmail;
  use Illuminate\Http\Request;
  use Illuminate\Support\Facades\DB;
  use PDF;
  use Illuminate\Support\Facades\Log;
  use Illuminate\Support\Facades\Mail;
  
  class HomeController extends Controller
  {
    public function __construct()
    {
      $this->middleware('auth');
    }
    
    public function index()
    {
      $clients = Client::all();
      $producers = Producer::all();
      $transporters = Transporter::all();
      $invoices = Invoice::where('customer_id', '=', DB::raw('seller_id'))->get();
      $orders = Order::where(['status' => 1])->get();
      $customer_info = '';
      $pageTitle = 'home';
      $mode=null;
      return view('admin.home', compact('pageTitle', 'customer_info', 'orders', 'clients', 'producers', 'transporters','mode'));
    }
    
    public function getreport(Request $request)
    {
      $customer_info = $request['id'];
      $mode = $request['mode'];
      $producers = Producer::all();
      $transporters = Transporter::all();
      $clients = Client::all();
      $pageTitle = 'home';
    
      if($mode=='0'){
        $orders = Order::where(['seller_id' => $customer_info, 'status' => 1])->get();
        return view('admin.home', compact('pageTitle', 'customer_info', 'orders', 'clients', 'producers', 'transporters','mode'));
  
      }elseif ($mode=='1'){
        $orders = Order::where(['buyer_id' => $customer_info, 'status' => 1])->get();
        return view('admin.home', compact('pageTitle', 'customer_info', 'orders', 'clients', 'producers', 'transporters','mode'));
  
      }else{
        $orders = Order::where(['seller_id' => $customer_info, 'status' => 1])->get();
        return view('admin.home', compact('pageTitle', 'customer_info', 'orders', 'clients', 'producers', 'transporters','mode'));
  
      }
    }
    
    public function order_show(Order $order)
    {
      $clients = Client::all();
      foreach ($clients as $client) {
        if ($order->seller_id === $client->id) {
          $order->seller = $client;
        } elseif ($order->buyer_id === $client->id) {
          $order->buyer = $client;
        }
      }
      $details = User::all();
      $pageTitle = 'Della mail di sollecito posizione';
      return view('admin.order_show', compact('order', 'details', 'pageTitle'));
    }
    
    public function order_email(Request $request, Order $order)
    {
      $content = $request['email_content'];
      $clients = Client::all();
      foreach ($clients as $client) {
        if ($order->seller_id === $client->id) {
          $order->seller = $client;
        } elseif ($order->buyer_id === $client->id) {
          $order->buyer = $client;
        }
      }
      $order['content'] = $content;
      session(['loading' => 'true']);
      Mail::to('djwuehwhrrj@gmail.com')
        ->send(new OrderEmail($order));
      Log::info("Invoice Email to supplier");
      session(['loading' => 'false']);
      
      return back()->with('success', 'Email success!');
    }
    
    public function orders_show($name)
    {
      $clients = Client::all(); // suppliers/customers
      $seller = Client::where(['business_name' => $name])->get(['id']);
      $seller_id = $seller[0]->id;
      $orders = Order::where(['seller_id' => $seller_id, 'status' => 1])->get();
      foreach ($orders as $order) {
        foreach ($clients as $client) {
          if ($order->seller_id === $client->id) {
            $order->seller = $client;
          }
        }
        foreach ($clients as $client) {
          if ($order->buyer_id === $client->id) {
            $order->buyer = $client;
          }
        }
      }
      $details = User::all();
      $pageTitle = 'Orders Email Show';
      return view('admin.orders_show', compact('pageTitle', 'orders', 'name', 'details'));
    }
    
    public function orders_email(Request $request, $name)
    {
      $details = $request['email_content'];
      $clients = Client::all(); // suppliers/customers
      $seller = Client::where(['business_name' => $name])->get(['id']);
      $seller_id = $seller[0]->id;
      $orders = Order::where(['seller_id' => $seller_id, 'status' => 1])->get();
      foreach ($orders as $order) {
        foreach ($clients as $client) {
          if ($order->seller_id === $client->id) {
            $order->seller = $client;
          }
        }
        foreach ($clients as $client) {
          if ($order->buyer_id === $client->id) {
            $order->buyer = $client;
          }
        }
        $order->content = $details;
      }
      session(['loading' => 'true']);
      Mail::to($orders[0]->seller->email_1)
        ->send(new OrdersEmail($orders));
      Log::info("Invoice Email to supplier");
      session(['loading' => 'false']);
      return back()->with('success', 'Email success!');
    }
    
    public function get_invoice(Invoice $invoice)
    {
      return $invoice;
    }
    
    public function invoice_show(Order $order, Request $request)
    {
      $condition = $request['condition'];
      if ($condition == 0) {
        $invoices = Invoice::where(['order_id' => $order->id, 'customer_id' => $order->seller_id])->get();
        $invoice = $this->get_invoice($invoices[0]);
      } elseif ($condition == 1) {
        $invoices = Invoice::where(['order_id' => $order->id, 'customer_id' => $order->buyer_id])->get();
        $invoice = $this->get_invoice($invoices[0]);
      }
      $pageTitle = 'viewInvoice';
      $details = User::all();
      return view('invoice.show', compact('pageTitle', 'invoice', 'details'));
    }
    
    public function order_delete(Order $order)
    {
      $invoices = Invoice::where(['order_id' => $order->id])->get();
      foreach ($invoices as $invoice) {
        $invoice->forceDelete();
      }
      if ($order->forceDelete()) {
        return back()->with('message', 'Record Successfully Deleted!');
      } else {
        return back()->with('message', 'Error Deleting Record');
      }
    }
    
    public function updatePaymentStatus(Request $request, $id)
    {
      $invoice = Invoice::where('id', $id)->first();
      $invoice->payment_status = $request->payment_status;
      if ($invoice->save()) {
        // return redirect(route('invoice.index'))->with('message','Invoice updated successfully!');
        return redirect('/home')->with('message', 'Status updated successfully!');
      } else {
        return redirect('/home')->withErrors($validators);
      }
    }
    
    public function updateStatus(Request $request, $id)
    {
      $order = Order::where('id', $id)->first();
      $order->status = $request->status;
      echo $order->status;
      if ($order->save()) {
        return redirect('/home')->with('message', 'Order status updated successfully!');
      } else {
        return redirect('/home')->withErrors($validators);
      }
    }
  
    public function updateComplete(Request $request, Order $order)
    {
      $request['status']=1;
      $newOrder = Order::where('id', $order->id)->first();
      $newOrder->fill($request->except(['lorry_reg_number']));
      if (!empty($request->lorry_reg_number)) {
        $new_lorry_reg_number = implode(', ', $request->lorry_reg_number);
        $newOrder->lorry_reg_number = $new_lorry_reg_number;
      }
      if ($newOrder->save()) {
        return redirect(url('/home'))->with('message', 'order has been Updated successfully!');
      } else {
        return back();
      }
    }
  }
