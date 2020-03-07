<?php
  
  namespace App\Http\Controllers;
  
  use App\Client;
  use Illuminate\Http\Request;
  
  class ClientController extends Controller
  {
    public function index()
    {
      $clients = Client::all();
      // dd($clients);
      $pageTitle = 'mangeClient';
      return view('client.index', compact('pageTitle', 'clients'));
    }
    
    
    public function create()
    {
      
      $pageTitle = 'createClient';
      return view('client.create', compact('pageTitle'));
    }
    
   
    public function store(Request $request)
    {
      $validators = $request->validate([
        'business_name' => 'required'
      ]);
      
      $client = Client::create([
        'business_name' => $request->business_name,
        'code' => $request->code,
        'street' => $request->street,
        'hose_num' => $request->hose_num,
        'zip_code' => $request->zip_code,
        'town' => $request->town,
        'district' => $request->district,
        'nation_code' => $request->nation_code,
        'vat' => $request->vat,
        'telephone' => $request->telephone,
        'mobile' => $request->mobile,
        'private_phone_1' => $request->private_phone_1,
        'private_phone_person_name_1' => $request->private_phone_person_name_1,
        'private_phone_2' => $request->private_phone_2,
        'private_phone_person_name_2' => $request->private_phone_person_name_2,
        'email_1' => $request->email_1,
        'email_2' => $request->email_2,
      ]);
      
      
      if ($client) {
        return redirect(route('client.index'))->with('message', 'Client added successfully!');
      } else {
        return back()->withErrors($validators);
      }
      
    }
    
    
    public function edit(Client $client)
    {
      //
    }
    
    public function update(Request $request, Client $client)
    {
      $validators = $request->validate([
        'business_name' => 'required'
      ]);
      // dd($client->id);
      $client = Client::where('id', $client->id)->first();
      // dd($client);
      $client->business_name = $request->business_name;
      $client->code = $request->code;
      $client->street = $request->street;
      $client->hose_num = $request->hose_num;
      $client->zip_code = $request->zip_code;
      $client->town = $request->town;
      $client->district = $request->district;
      $client->vat = $request->vat;
      $client->telephone = $request->telephone;
      $client->mobile = $request->mobile;
      $client->private_phone_1 = $request->private_phone_1;
      $client->private_phone_person_name_1 = $request->private_phone_person_name_1;
      $client->private_phone_2 = $request->private_phone_2;
      $client->private_phone_person_name_2 = $request->private_phone_person_name_2;
      $client->email_1 = $request->email_1;
      $client->email_2 = $request->email_2;
      
      if ($client->save()) {
        return redirect(route('client.index'))->with('message', 'Client updated successfully!');
      } else {
        return back()->withErrors($validators);
      }
    }
    
    public function destroy(Client $client)
    {
      // this method deletes the record from the database permanently
      if ($client->forceDelete()) {
        return back()->with('message', 'Record Successfully Deleted!');
      } else {
        return back()->with('message', 'Error Deleting Record');
      }
    }
    
    public function getNumber(Request $request)
    {
      $number = Client::select('number')->where(['id'=>$request['id']])->first();
      return $number;
    }
  }
