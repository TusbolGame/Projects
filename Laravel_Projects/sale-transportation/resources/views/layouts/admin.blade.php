<!DOCTYPE html>
<html lang="{{app()->getLocale()}}">

<head>
  <meta charset="utf-8">
  <!-- CSRF Token -->
  <meta name="csrf-token" content="{{ csrf_token() }}">

  <meta http-equiv="X-UA-Compatible" content="IE=edge">

  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Sale-transportation system by devpremier.com">
  <meta name="author" content="devpremier.com">
  <script src="{{asset('assets/plugins/jquery/jquery.min.js')}}"></script>
  <link rel="icon" type="image/png" sizes="16x16" href="{{asset('assets/images/favicon.png')}}">
  <title>{{ config('app.name') }}</title>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.7/css/select2.min.css" rel="stylesheet"/>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
        integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous"
        media='all'>
  {{-- <link href="{{asset('assets/plugins/bootstrap/css/bootstrap.min.css')}}" rel="stylesheet"> --}}
  {{-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-datetimepicker/2.7.1/css/bootstrap-material-datetimepicker.min.css"> --}}
  <link rel="stylesheet"
        href="{{asset('assets/plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css')}}"
        media='all'>

  <link rel="stylesheet" href="https://cdn.datatables.net/1.10.18/css/dataTables.bootstrap4.min.css">
  <link href="{{asset('material/css/style.css')}}" rel="stylesheet" media='all'>
  <!-- Toggle Switch -->
  <link rel="stylesheet" href="">

  <!-- Prism Syntax Highlighter -->
  <link href="{{asset('material/css/colors/blue.css')}}" id="theme" rel="stylesheet" media='all'>

  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
  <script src="{{asset('assets/plugins/jQuery-Mask-Plugin-master/src/jquery.mask.js')}}"></script>
  <![endif]-->
  <style>
    ::placeholder { /* Chrome, Firefox, Opera, Safari 10.1+ */
      color: silver !important;
      opacity: 0.5; /* Firefox */
      font-size: 15px;
      font-weight: 100%;
    }

    header {
      opacity: 0.8;
      background-image: linear-gradient(black, #1C252C, black) !important;
    }

    body {
      position: relative !important;
      min-height: 100% !important;
      top: 0px !important;
    }

    .sidebar-nav ul li a.active {
      color: #fff;
    }

    .sidebar-nav > ul > li.active > a, .sidebar-nav > ul > li.active:hover > a {
      color: #ffffff;
      background-image: linear-gradient(darkgreen, green, darkgreen) !important;
    }

    .bg-dark {
      background-image: linear-gradient(darkslategrey) !important;
    }

    .dt-buttons .dt-button {
      padding: 5px 15px;
      border-radius: 4px;
      background-image: linear-gradient(darkgreen, green, darkgreen) !important;
      color: #ffffff;
      margin: 5px !important;
    }

    .dt-buttons:hover .dt-button:hover {
      padding: 5px 15px;
      border-radius: 4px;
      background-image: linear-gradient(darkred, red, darkred) !important;
      color: #ffffff;
      box-shadow: 2px 2px 6px black;
      margin: 5px !important;
    }

    /*.btn-primary:hover, .btn-primary.disabled:hover {*/
    /*background-image: linear-gradient(navy, blue, navy)!important;*/
    /*}*/
    /*.danger{*/
    /*background-color: red!important;*/
    /*}*/
    /*.btn-danger:hover, .btn-danger.disabled:hover {*/
    /*background-image: linear-gradient(darkred, orangered, darkred)!important;*/
    /*}*/
    /*.btn-warning{*/
    /*background-image: linear-gradient(blue, blue, blue)!important;*/

    /*border: 1px solid darkmagenta;*/
    /*}*/
    /*.btn-warning:hover, .btn-warning.disabled:hover{*/
    /*background-image: linear-gradient(darkmagenta, darkmagenta, darkmagenta)!important;*/
    /*border: 1px solid deeppink;*/
    /*}*/
    /*.btn-success{*/
    /*background-color: green;*/
    /*}*/
    /*.btn-success:hover{*/
    /*background-image: linear-gradient(darkgreen, green, darkgreen)!important;*/
    /*}*/

    .page-wrapper {
      {{--background: url("{{asset('assets/images/login-register.jpg')}}");--}}
   background-image: linear-gradient(grey, grey, grey) !important;
      padding-bottom: 60px;

    }

    .page-wrapper-cover {
      position: fixed;
      width: 100%;
      height: 100vh;

    }

    .sidebar-nav {
      background: none !important;
      padding: 15px;
    }

    .form-group {
      margin-bottom: 15px;
    }

    .table td, .table th {
      padding: .35rem;
      vertical-align: top;
      border-top: 1px solid #dee2e6;
    }

    div.switch-toggle label::before,
    div.switch-toggle label::after {
      width: 0 !important;
      height: 0 !important;
      opacity: 0 !important;
      display: none !important;
    }

    div.switch-toggle input {
      width: 0 !important;
      height: 0 !important;
      opacity: 0 !important;
    }

    div.switch-toggle label {
      padding-left: 5px !important;
    }

    @media print {
      .noprint {
        display: none;
      }
    }

    /* Select2 adds class .select2. You can override what script does using css. Here I'm set select2 to have 100% width, using !important. If I would not do that select2 would have 24px width.

    You can further customize other classes that select2 generates using some principle.

     */
    .select2 {
      width: 100% !important;
    }

    hr {
      height: 1px;
      color: red;
      background-color: red;
      border: none;
    }

    .google-translate {
      display: inline-block;
      margin-top: 10px;
      margin-left: 20px;

    }

    .goog-te-combo {
      font-size: 16px;
      padding: 3px;
      border: none;
      border-radius: 3px;
    }

    .goog-te-combo::after {
      content: none;
    }

    .goog-logo-link {
      display: none !important;
    }

    .goog-te-gadget {
      color: transparent !important;
    }

    #google_translate_element {
      display: none;
    }

    .goog-te-banner-frame.skiptranslate {
      display: none !important;
    }

    .mdi-power {
      color: #26c6da;
    }

    .mdi-power:hover {
      color: #fff;
    }

    .logo {
      height: 50px;
      margin-left: 70px;
    }

  </style>


</head>


<body class="bodyClass fix-header fix-sidebar card-no-border">
<div class="preloader">
  <svg class="circular" viewBox="25 25 50 50">
    <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10"/>
  </svg>
</div>
<div id="main-wrapper">
  <header class="topbar">
    <nav class="navbar top-navbar navbar-expand-md navbar-light">

      <img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png" class="logo">
      <span id="title"
            style="font-size: 30px; margin-left: 100px; font-weight: bold; color: lightskyblue">@lang('main.'.$pageTitle)</span>
      <div class="navbar-collapse">
        <ul class="navbar-nav ml-auto mt-md-0">
          <!-- This is  -->
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" id="navbarDropdown" role="button" data-toggle="dropdown"
               aria-haspopup="true" aria-expanded="false"><img> @lang('main.language')</a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              @php
                $en = 'en';
              $it = 'it';
              $fr = 'fr';
              @endphp
              <li class="dropdown-item"><a href="{{route('locale',$en)}}"><img
                      src="{{asset('assets/images/flags/gb.png')}}" width="30px"> @lang('main.english')</a></li>
              <li class="dropdown-item"><a href="{{route('locale',$it)}}"><img
                      src="{{asset('assets/images/flags/it.png')}}" width="30px"> @lang('main.italian')</a></li>
              <li class="dropdown-item"><a href="{{route('locale',$fr)}}"><img
                      src="{{asset('assets/images/flags/fr.png')}}" w idth="30px"> @lang('main.french')</a></li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav my-lg-0">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-muted waves-effect waves-dark" href=""
               data-toggle="dropdown" aria-haspopup="true"
               aria-expanded="false">
              <i class="ti-settings"></i>
            </a>
            <div class="dropdown-menu dropdown-menu-right scale-up bg-light" style="opacity: 1">
              <ul class="dropdown-user">
                <li>
                  <a href="{{route('profile')}}">
                    <i class="ti-user">&nbsp;&nbsp;</i>@lang('main.profile')</a>

                </li>
                <li role="separator" class="divider"></li>
                <li>
                  <a href="{{ route('logout') }}"
                     onclick="event.preventDefault(); document.getElementById('logout-form').submit();">
                    <i class="fa fa-power-off">&nbsp;&nbsp;</i> @lang('main.logout')
                  </a>

                  <form id="logout-form" action="{{ route('logout') }}" method="POST"
                        style="display: none;">
                    @csrf
                  </form>
                </li>

              </ul>
            </div>
          </li>
        </ul>
      </div>
    </nav>
  </header>
  <div class="noprint">
    <aside class="left-sidebar bg-dark" style="margin-top: 70px; padding:0; opacity: 0.95">
      <!-- Sidebar scroll-->
      <div class="scroll-sidebar">
        <!-- Sidebar navigation-->
        <nav class="sidebar-nav">
          <ul id="sidebarnav">
            <li class="nav-small-cap">@lang('main.navigation')</li>
            {{-- keep the invoices on dashboard --}}
            <li id="dashboard"><a class="waves-effect waves-dark" href="{{route('home.index')}}"><i
                    class="fa fa-home"></i>&nbsp;&nbsp;
                @lang('main.home')</a></li>
            <li><a class="has-arrow waves-effect waves-dark" href="#"
                   data-toggle="tooltip" data-placement="right" title="Gestisci Ordinis"
                   aria-expanded="false"
              ><i class="fa fa-arrow-alt-circle-right"></i><span
                    class="hide-menu">@lang('main.order')</span></a>
              <ul aria-expanded="false" class="collapse">
                <li><a href="{{route('order.create')}}"><i class="fa fa-plus"></i>&nbsp;&nbsp;@lang('main.create')</a>
                </li>
                <li><a href="{{route('order.index')}}"><i class="fa fa-edit"></i> &nbsp;@lang('main.management')</a>
                </li>
              </ul>
            </li>

            <li><a class="has-arrow waves-effect waves-dark" href="#" aria-expanded="false"
                   data-toggle="tooltip" data-placement="right" title="Customers / Suppliers"
              ><i class="fa fa-american-sign-language-interpreting"></i><span
                    class="hide-menu"> &nbsp;@lang('main.client') </span></a>
              <ul aria-expanded="false" class="collapse">
                <li><a href="{{route('client.create')}}"><i class="fa fa-plus"></i> &nbsp;@lang('main.create')</a>
                </li>
                <li><a href="{{route('client.index')}}"><i class="fa fa-edit"></i> &nbsp;@lang('main.management')</a>
                </li>
              </ul>
            </li>

            <li>
              <a class="waves-effect waves-dark" href="{{route('transporter.index')}}"
                 data-toggle="tooltip" data-placement="right" title="Transporter / Exporter"
              ><i class="fa fa-truck-moving"></i> &nbsp;@lang('main.transporter') </a>
            </li>

            <li>
              <a class="waves-effect waves-dark" href="{{route('producer.index')}}"
                 data-toggle="tooltip" data-placement="right" title="Gestisci Produttori"
              ><i class="fa fa-seedling"></i> &nbsp;@lang('main.producer') </a>
            </li>
            <li id="invoice">
              <a class="waves-effect waves-dark" href="{{route('invoice.index')}}"
                 data-toggle="tooltip" data-placement="right" title="Manage Invoices"
              ><i class="fa fa-euro-sign"></i> &nbsp;@lang('main.invoice') </a>
            </li>
            <li id="invoice">
              <a class="waves-effect waves-dark" href="{{route('invoice.history')}}"
                 data-toggle="tooltip" data-placement="right" title="Manage Invoices"
              ><i class="fa fa-recycle"></i> &nbsp;@lang('main.history') </a>
            </li>

          </ul>


        </nav>

      </div>

      <!-- Bottom points-->
      <div class="sidebar-footer bg-dark">

        <a class="link" style="margin-left: 70px;" data-toggle="tooltip" title="Logout" href="{{ route('logout') }}"
           onclick="event.preventDefault(); document.getElementById('logout-form').submit();">
          <i class="mdi mdi-power"></i>
        </a>
        <form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
          @csrf
        </form>
      </div>

    </aside>
  </div>
  <div class="page-wrapper">
    <div class="container-fluid">

      <div class="row">
        <div class="col-sm-12">
          @if ($errors->any())
            <div class="alert alert-danger">
              <ul>
                @foreach ($errors->all() as $error)
                  <li>{{ $error }}</li>
                @endforeach
              </ul>
            </div>
          @endif
        </div>
        <div class="col-sm-12">
          @if (session()->has('message'))
            <div class="alert alert-success">
              {{session('message')}}
            </div>
          @endif
        </div>
      </div>

      <style>
        .load-image {

          /* animation: spin 2s linear infinite; */
          position: absolute;
          top: 40%;

        }

        .transparent {
          /* background-color: rgba(0,0,0,.0001) !important; */
          background-color: transparent !important;
          border: none;
        }
      </style>
      <div id="load-image" class="modal fade bs-emailProcessing-modal-lg" tabindex="-1" role="dialog"
           aria-labelledby="myLargeModalLabel" style="display: none;" aria-hidden="true">
        <div class="modal-dialog modal-xm">
          <div class="modal-content transparent">
            Invio e-mail {{@$email_receiver}}{{session('email_receiver')}}
            <span style="text-align:center"><img src="{{asset('assets/images/send-email.gif')}}" alt=""
                                                 width="70%"></span>
            {{-- <br>
            <span style="text-align:center"><img src="{{asset('assets/images/loading.gif')}}" alt="" width="40%"></span> --}}
          </div>
          <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
      </div>

      @yield('content')


    </div>
    <footer class="footer noprint" style="background: none">
      ©{{date('Y')}}  {{ config('app.name')}} Developed From <a href="https://laravel.com">Laravel</a>
    </footer>
  </div>
</div>
<!-- Bootstrap tether Core JavaScript -->
<script src="{{asset('assets/plugins/popper/popper.min.js')}}"></script>
<script src="{{asset('assets/plugins/bootstrap/js/bootstrap.min.js')}}"></script>
<!-- slimscrollbar scrollbar JavaScript -->
<script src="{{asset('material/js/jquery.slimscroll.js')}}"></script>
<script src="{{asset('material/js/sidebarmenu.js')}}"></script>
<!--stickey kit -->
<script src="{{asset('assets/plugins/sticky-kit-master/dist/sticky-kit.min.js')}}"></script>
<script src="{{asset('assets/plugins/sparkline/jquery.sparkline.min.js')}}"></script>
<!--Custom JavaScript -->
<script src="{{asset('material/js/custom.min.js')}}"></script>
<script src="{{asset('assets/plugins/toast-master/js/jquery.toast.js')}}"></script>
<script src="{{asset('material/js/toastr.js')}}"></script>

<!-- This is data table -->
<script src="https://cdn.datatables.net/1.10.18/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.18/js/dataTables.bootstrap4.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.flash.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
<script src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
<script src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.print.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.colVis.min.js"></script>
<!-- end - This is for export functionality only -->
<script src="https://cdn.datatables.net/fixedheader/3.1.5/js/dataTables.fixedHeader.min.js"></script>
<!-- ============================================================== -->
<!-- Plugins for this page -->
<!-- ============================================================== -->
<!-- Plugin JavaScript -->
<script src="{{asset('assets/plugins/moment/moment.js')}}"></script>
<script
    src="{{asset('assets/plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js')}}"></script>


@yield('scripts')
<script>
  $(document).ready(() => {
    var title = $('#title').text();

    $('.readonly').prop("readonly", true);
    if (title === 'Cruscotto') {
      $('#dashboard').addClass('active');
    }

  });
</script>
<script>
  // Script to show email processing gif image
  // Use this class for email button
  $('.email_processing').click(function () {
    // alert('email sending');
    // var $loadImage =
    $('#load-image').show(); // show loading image
    // $('#myLargeModalLabel').show();
    $('.bs-emailProcessing-modal-lg').modal('show');
    $('.bodyClass').unbind('click');
  })
</script>


<script>
  window.setTimeout(function () {
    $(".alert").fadeTo(500, 0).slideUp(500, function () {
      $(this).remove();
    });
  }, 5000);

</script>
@include('api.form_mask')
</body>

</html>
