<?php

namespace App\Http\Controllers;

use App\Card;
use App\Client;
use App\Producer;
use App\Transporter;
use Illuminate\Http\Request;

class CardController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $clients = Client::all(); // suppliers/customers
        $producers = Producer::all();
        $transporters = Transporter::all();
        // $cards = Card::with(['supplier','customer'])->get();; // suppliers/customers
        $cards = Card::all();; // suppliers/customers
        // dump($cards);
        // foreach ($cards as  $card) {
        //     dump(unserialize($card->lorry_reg_number));
        // }
        // dd('ok');
        $pageTitle = 'Manage Card';
        return view('card.index',compact('pageTitle','cards','clients','transporters','producers'));
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        // dump('card create');
        $clients = Client::all(); // suppliers/customers
        $producers = Producer::all();
        $transporters = Transporter::all();
        $pageTitle = 'Create card';
        return view('card.create',compact('pageTitle','clients','transporters','producers'));

    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        // dd($request);
        $validators = $request->validate([
            // 'business_name'=> 'required'
        ]);

        $card = Card::create([
            'business_name' => $request->business_name,
            'position_number' => $request->position_number,
            'contract_number' => $request->contract_number,
            'lorry_reg_number' => json_encode($request->lorry_reg_number),
            'supplier_invoice_number' => $request->supplier_invoice_number,
            'supplier_invoice_date' => $request->supplier_invoice_date,
            'product_name' => $request->product_name,
            'variety' => $request->variety,
            'caliper' => $request->caliper,
            'nature' => $request->nature,
            'packing' => $request->packing,
            'quality' => $request->quality,
            'price_on_departure' => $request->price_on_departure,
            'price_on_arrival' => $request->price_on_arrival,
            'weight' => $request->weight,
            'amount' => $request->amount,
            'amount_commission_CHF' => $request->amount_commission_CHF,
            'amount_commission_EURO' => $request->amount_commission_EURO,
        ]);

        if ($card) {
            return redirect(route('card.create'))->with('message','Card added successfully!');
        }else{
            return back()->withErrors($validators);
        }
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Card  $card
     * @return \Illuminate\Http\Response
     */
    public function show(Card $card)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Card  $card
     * @return \Illuminate\Http\Response
     */
    public function edit(Card $card)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Card  $card
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Card $card)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Card  $card
     * @return \Illuminate\Http\Response
     */
    public function destroy(Card $card)
    {
        //
    }
}
