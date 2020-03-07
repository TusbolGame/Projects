<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Documento</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

</head>
<body>
<div class="card card-body">
    <div class="row">
        <div class="col-md-4">
            <div class="card card-body">
                {{-- {{$order}} --}}
                <h4>La nostra posizione</h4>
                <h4>Code - {{@$order->code_nr}}</h4>
                <h4>Numero - {{@$order->number}}</h4>
            </div>
        </div>
        <div class="col-md-4">
            <div class="text-center">
            <img src="http://www.joesfood.ch/wp-content/uploads/2016/02/logo-contatti.png" alt="logo" height="180px">
            </div>
        </div>
    </div>
</div>
</body>
</html>