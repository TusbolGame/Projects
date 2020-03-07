<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6 lt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7 lt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8 lt8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<head>
    <meta charset="UTF-8" />
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">  -->
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Login and Registration Form with HTML5 and CSS3" />
    <meta name="keywords" content="html5, css3, form, switch, animation, :target, pseudo-class" />
    <meta name="author" content="Codrops" />

    <link href="{{asset('material/css/style.css')}}" rel="stylesheet" media='all'>
    <link rel="stylesheet" type="text/css" href="{{asset('assets/log/css/demo.css')}}" />
    <link rel="stylesheet" type="text/css" href="{{asset('assets/log/css/style.css')}}" />
    <link rel="stylesheet" type="text/css" href="{{asset('assets/log/css/animate-custom.css')}}" />
</head>
<body>
<div class="container">

    <header >
        <h1>Accesso  <span>alla gestione </span></h1>

    </header>
    <section>
        <div id="container_demo"  style="margin-top: 100px">
            <!-- hidden anchor to stop jump http://www.css3create.com/Astuce-Empecher-le-scroll-avec-l-utilisation-de-target#wrap4  -->
            <a class="hiddenanchor" id="toregister"></a>
            <a class="hiddenanchor" id="tologin"></a>
            <div id="wrapper">
                <div id="login" class="animate form">
                    <form method="POST" action="{{ route('login') }}" id="loginform">
                        @csrf
                        <h1>Log in</h1>
                        <p>
                            <label for="username" class="uname" data-icon="u" > La tua mail o il tuo username </label>
                            <input id="username" value="{{ old('email') }}" class="{{ $errors->has('email') ? ' is-invalid' : '' }}"  name="email"  required autofocusrequired="required" type="text" placeholder="myusername or mymail@mail.com"/>
                            @if ($errors->has('email'))
                                <span class="invalid-feedback" role="alert">
                                        <strong>{{ $errors->first('email') }}</strong>
                                    </span>
                            @endif
                        </p>
                        <p>
                            <label for="password" class="youpasswd" data-icon="p"> La tua password</label>
                            <input id="password" name="password" required="required" class="{{ $errors->has('password') ? ' is-invalid' : '' }}" type="password" placeholder="eg. X8df!90EO" />
                            @if ($errors->has('password'))
                                <span class="invalid-feedback" role="alert">
                                        <strong>{{ $errors->first('password') }}</strong>
                                    </span>
                            @endif
                        </p>
                        <p class="keeplogin">
                            <input type="checkbox" name="loginkeeping" id="loginkeeping" value="loginkeeping"  {{ old('remember') ? 'checked' : '' }}>
                            <label for="loginkeeping">Ricordati di me</label>
                        </p>
                        <p class="login button">
                            <input type="submit" value="Login" />
                        </p>
                        <p class="change_link">

                        </p>
                    </form>
                    <form class="form-horizontal" id="recoverform" action="index.html">
                        <div class="form-group ">
                            <div class="col-xs-12">
                                <h3>Recover Password</h3>
                                <p class="text-muted">Enter your Email and instructions will be sent to you! </p>
                            </div>
                        </div>
                        <div class="form-group ">
                            <div class="col-xs-12">
                                <input class="form-control" type="text" required placeholder="Email">
                            </div>
                        </div>
                        <div class="form-group text-center m-t-20">
                            <div class="col-xs-12">
                                <button class="btn btn-primary btn-lg btn-block text-uppercase waves-effect waves-light" type="submit">Reset</button>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </section>
</div>
</body>
</html>