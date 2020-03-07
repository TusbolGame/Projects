<?php

namespace App\Http\Controllers;

use App\Producer;
use Illuminate\Http\Request;

class ProducerController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $producers = Producer::all();
        // dd($producers);
        $pageTitle = 'producerManagement';
        return view('producer.index',compact('pageTitle','producers'));
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        // dump('producer store');

        // dd($request);
        $validators = $request->validate([
            'name'=> 'required'
        ]);
        // $order = Order::create($request->except(['lorry_reg_number']));
        // $order->lorry_reg_number = $new_lorry_reg_number;
        // $order = $order->save();
        $producer = Producer::create($request->all());
        $producer = $producer->save();
        if ($producer) {
            return redirect(route('producer.index'))->with('message','Record added successfully!');
        }else{
            return back()->withErrors($validators);
        }
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Producer  $producer
     * @return \Illuminate\Http\Response
     */
    public function show(Producer $producer)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Producer  $producer
     * @return \Illuminate\Http\Response
     */
    public function edit(Producer $producer)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Producer  $producer
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Producer $producer)
    {
        $validators = $request->validate([
            'name'=> 'required'
        ]);
        // dd($client->id);
        $producer = Producer::where('id', $producer->id)->first();
        // dd($producer);
        $producer->name = $request->name;
        $producer->vat = $request->vat;
        $producer->reg_number = $request->reg_number;
        $producer->mobile = $request->mobile;
        $producer->email = $request->email;
        $producer->street = $request->street;
        $producer->zip_code = $request->zip_code;
        $producer->city = $request->city;
        $producer->nation = $request->nation;

        if ($producer->save()) {
            return redirect(route('producer.index'))->with('message','Record updated successfully!');
        }else{
            return back()->withErrors($validators);
        }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Producer  $producer
     * @return \Illuminate\Http\Response
     */
    public function destroy(Producer $producer)
    {
        // this method deletes the record from the database permanently
        if($producer->forceDelete()){
            return back()->with('message','Record Successfully Deleted!');
        }else{
            return back()->with('message','Error Deleting Record');
        }
    }
}
