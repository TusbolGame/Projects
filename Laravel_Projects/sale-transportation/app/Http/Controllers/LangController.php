<?php

namespace App\Http\Controllers;

use App\LanguageSetting;

class LangController extends Controller
{
    public function __construct()
    {

    }

    public function index(){
        $this->languages = LanguageSetting::all();
        return view('admin.language-settings.index', $this->data);
    }
}
