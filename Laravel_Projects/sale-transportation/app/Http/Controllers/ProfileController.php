<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;

class ProfileController extends Controller
{
    public function index(){

    // $password = Hash::make('secret');
    // $bpassword = bcrypt('secret');
    // if (Hash::check('secret', $password))
    // {
    //     dump($password);
    //     dd($bpassword);
    // }

        $user = Auth::user();
        // dd($user);
        $pageTitle = 'profileManagement';
        return view('admin.profile.index', compact('pageTitle','user'));
    }

    public function getmanager(){
        $user = User::where('user_role','manager')->first();
        // dd($user);
        $pageTitle = 'Manager Profile';
        return view('admin.profile.manager', compact('pageTitle','user'));
    }

    public function update_user(Request $request){
        if(@$request->user_role == 'manager'){
            $user = User::where('user_role','manager')->first();
        }else{
            $user = Auth::user();
        }

        $validators = $request->validate([
            'old_password'=>'required',
            'new_password'=>'required',
            'name'=>'required',
            'email'=>'required|email|unique:users,email,'.$user->email.',email',

        ]);
        // dump($user->password);
        if(Hash::check($request->old_password, $user->password)){
            $user->password = Hash::make($request->new_password);
        }else{
            return back()->withErrors('Old password incorrect, please enter correct password.');
        }

        $user->name = $request->name;
        $user->email = $request->email;

        if($user->save()){
            return back()->withMessage('User information updated successfully!, New Password is "'.$request->new_password.'"');
        }else{
            return back()->withErrors($validators);
        }
    }

    public function bank_details(Request $request){
        if(@$request->user_role == 'manager'){
            $user = User::where('user_role','manager')->first();
        }else{
            $user = Auth::user();
        }
        $user->bank_details = $request->bank_details;
        if($user->save()){
            return back()->with('message','Bank Details updated successfully!');
        }else{
            return back()->withErrors($validators);
        }
    }
    public function email_content(Request $request){
        if(@$request->user_role == 'manager'){
            $user = User::where('user_role','manager')->first();
        }else{
            $user = Auth::user();
        }
        $user->email_content = $request->email_content;
        if($user->save()){
            return back()->with('message','Email Content updated successfully!');
        }else{
            return back()->withErrors("Error!Dear Sir!


Buongiorno Bonjour / Guten Tag, 
Vi invitiamo a controllare le Fatture di Seguito INDICARE cato un provvedee al pagamento delle stesse 
Nous Vous invitons Il verificatore les fratture comme specifie et à les payer. 
Wir laden Sie ein, die Rechnungen wie zu angegeben ueberpruefen und zu bezhalen. 


Cordiali saluti / Cordialment / Mit freindlichen Gruessen  

Bertamino");
        }
    }
    public function invoice_content(Request $request){
        if(@$request->user_role == 'manager'){
            $user = User::where('user_role','manager')->first();
        }else{
            $user = Auth::user();
        }
        $user->invoice_content = $request->invoice_content;
        if($user->save()){
            return back()->with('message','Invoice Email Content updated successfully!');
        }else{
            return back()->withErrors("Error");
        }
    }
    public function missed_list_content(Request $request){
        if(@$request->user_role == 'manager'){
            $user = User::where('user_role','manager')->first();
        }else{
            $user = Auth::user();
        }
        $user->missed_list_content = $request->missed_list_content;
        if($user->save()){
            return back()->with('message','Missed List Content updated successfully!');
        }else{
            return back()->withErrors("Error!");
        }
    }
    public function reminder_email_content(Request $request){
        if(@$request->user_role == 'manager'){
            $user = User::where('user_role','manager')->first();
        }else{
            $user = Auth::user();
        }
//        dd($request);
        $user->reminder_email_content = $request->reminder_email_content;
        if($user->save()){
            return back()->with('message','Reminder Email Content updated successfully!');
        }else{
            return back()->withErrors('Error!');
        }
    }


}
