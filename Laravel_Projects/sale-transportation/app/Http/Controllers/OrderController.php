<?php
  
  namespace App\Http\Controllers;
  
  use App\Order;
  use App\Client;
  use App\Invoice;
  use App\Producer;
  use App\Transporter;
  use Illuminate\Http\Request;
  use Illuminate\Support\Facades\DB;
  use Illuminate\Validation\Rules\In;
  use PDF; // at the top of the file
  
  class OrderController extends Controller
  {
    public function index()
    {
      $clients = Client::all();
      $producers = Producer::all();
      $transporters = Transporter::all();
      $orders = Order::where(['status'=>0])->with(['seller', 'customer'])->get();
      $data['logo_path'] = asset('assets/images/logo-contatti.png');
      $pageTitle = 'manageOrder';
      return view('order.index', compact('pageTitle', 'orders', 'clients', 'transporters', 'producers'))->with($data);
    }
    
    public function getReport(Request $request)
    {
      if($request['mode']==''){
        $clients = Client::all();
        $producers = Producer::all();
        $transporters = Transporter::all();
        $orders = Order::where(['status'=>0])->with(['seller', 'customer'])->get();
        $data['logo_path'] = asset('assets/images/logo-contatti.png');
        $pageTitle = 'manageOrder';
        return view('order.index', compact('pageTitle', 'orders', 'clients', 'transporters', 'producers'))->with($data);
      }elseif($request['mode']=='seller'){
        $clients = Client::all();
        $producers = Producer::all();
        $transporters = Transporter::all();
        $orders = Order::where(['status'=>0,'seller_id'=>$request['name']])->with(['seller', 'customer'])->get();
        $data['logo_path'] = asset('assets/images/logo-contatti.png');
        $pageTitle = 'manageOrder';
        return view('order.index', compact('pageTitle', 'orders', 'clients', 'transporters', 'producers'))->with($data);
      }else{
        $clients = Client::all();
        $producers = Producer::all();
        $transporters = Transporter::all();
        $orders = Order::where(['status'=>0,'buyer_id'=>$request['name']])->with(['seller', 'customer'])->get();
        $data['logo_path'] = asset('assets/images/logo-contatti.png');
        $pageTitle = 'manageOrder';
        return view('order.index', compact('pageTitle', 'orders', 'clients', 'transporters', 'producers'))->with($data);
      }
    }
    
    public function create()
    {
      $clients = Client::all();
      $producers = Producer::all();
      $transporters = Transporter::all();
      $pageTitle = 'createOrder';
      return view('order.create', compact('pageTitle', 'clients', 'transporters', 'producers'));
    }
    
    public function store(Request $request)
    {
      $request['status']=1;
      $newOrder = Order::create($request->except(['lorry_reg_number']));
      if (!empty($request->lorry_reg_number)) {
        $new_lorry_reg_number = implode(', ', $request->lorry_reg_number);
        $newOrder->lorry_reg_number = $new_lorry_reg_number;
      }
      $order = $newOrder->save();
      $client = Client::where(['id'=>$request['seller_id']])->first();
      $number = $client['number'];
      $client['number'] = $number+1;
      $client->save();
      
      Invoice::create([
        'order_id' => $newOrder->id,
        'customer_id' => $newOrder->seller->id,
        'seller_id' => $newOrder->seller->id,
        'buyer_id' => $newOrder->buyer->id
      ]);
      if ($order) {
        return redirect(route('order.create'))->with('message', 'Order added successfully!');
      } else {
        return back()->withErrors($validators);
      }
      
    }
    
    public function complete(Request $request)
    {
      $request['status']=0;
      $newOrder = Order::create($request->except(['lorry_reg_number']));
      if (!empty($request->lorry_reg_number)) {
        $new_lorry_reg_number = implode(', ', $request->lorry_reg_number);
        $newOrder->lorry_reg_number = $new_lorry_reg_number;
      }
      $order = $newOrder->save();
      if ($order) {
        return redirect(route('order.create'))->with('message', 'Order added successfully!');
      } else {
        return back()->withErrors($validators);
      }
    }
    
    public function show(Order $order)
    {
      $pageTitle = 'View Order';
      return view('order.show', compact('pageTitle', 'order'));
    }
    
    public function orderPdf(Order $order)
    {
      $pdf = PDF::loadView('order.show', $order);
      return $pdf->download('invoice.pdf');
    }
    
    
    public function update(Request $request, Order $order)
    {
      $request['status']=0;
      $order = Order::where('id', $order->id)->first();
      $order->fill($request->except(['lorry_reg_number']));
      if (!empty($request->lorry_reg_number)) {
        $new_lorry_reg_number = implode(', ', $request->lorry_reg_number);
        $order->lorry_reg_number = $new_lorry_reg_number;
      }
      if ($order->save()) {
        return redirect(route('order.index'))->with('message', 'order updated successfully!');
      } else {
        return back();
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
      $client = Client::where(['id'=>$request['seller_id']])->first();
      $number = $client['number'];
      $client['number'] = $number+1;
      $client->save();
      
      Invoice::create([
        'order_id' => $newOrder->id,
        'customer_id' => $newOrder->seller->id,
        'seller_id' => $newOrder->seller->id,
        'buyer_id' => $newOrder->buyer->id
      ]);
      if ($newOrder->save()) {
        return redirect(route('order.index'))->with('message', 'order Completed successfully!');
      } else {
        return back();
      }
    }
    
    public function destroy(Order $order)
    {
      $invoice = Invoice::Where('order_id', '=', $order->id)->delete();
      Order::Where('id', '=', $order->id)->delete();
      if ($invoice) {
        return back()->with('message', 'Record Successfully Deleted!');
      } else {
        return back()->with('message', 'Record Successfully Deleted!');
      }
    }
  }
