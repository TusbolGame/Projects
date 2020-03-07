<?php

namespace App\Http\Controllers;

use App\Transporter;
use Illuminate\Http\Request;

class TransporterController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $transporters = Transporter::all();
        // dd($transporters);
        $pageTitle = 'transporterManagement';
        return view('transporter.index',compact('pageTitle','transporters'));
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
        // dump('trasporter store');

        // dd($request);
        $validators = $request->validate([
            'name'=> 'required'
        ]);

        $transporter = Transporter::create([
            'name' => $request->name,
            'vat' => $request->vat,
            'reg_number' => $request->reg_number,
            'type' => $request->type,
            'mobile' => $request->mobile,
            'email' => $request->email,
            'address' => $request->address,
        ]);

        if ($transporter) {
            return redirect(route('transporter.index'))->with('message','Record added successfully!');
        }else{
            return back()->withErrors($validators);
        }
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\transporter  $transporter
     * @return \Illuminate\Http\Response
     */
    public function show(transporter $transporter)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\transporter  $transporter
     * @return \Illuminate\Http\Response
     */
    public function edit(transporter $transporter)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\transporter  $transporter
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, transporter $transporter)
    {
        $validators = $request->validate([
            'name'=> 'required'
        ]);
        // dd($client->id);
        $transporter = transporter::where('id', $transporter->id)->first();
        // dd($transporter);
        $transporter->name = $request->name;
        $transporter->vat = $request->vat;
        $transporter->reg_number = $request->reg_number;
        $transporter->type = $request->type;
        $transporter->mobile = $request->mobile;
        $transporter->email = $request->email;
        $transporter->address = $request->address;

        if ($transporter->save()) {
            return redirect(route('transporter.index'))->with('message','Record updated successfully!');
        }else{
            return back()->withErrors($validators);
        }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\transporter  $transporter
     * @return \Illuminate\Http\Response
     */
    public function destroy(transporter $transporter)
    {
        // this method deletes the record from the database permanently
        if($transporter->forceDelete()){
            return back()->with('message','Record Successfully Deleted!');
        }else{
            return back()->with('message','Error Deleting Record');
        }
    }
}
