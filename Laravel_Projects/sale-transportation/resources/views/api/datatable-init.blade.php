
<script>
$(document).ready(function(){
    // Setup - add a text input to each footer cell
    $('#homeTable thead tr').clone(true).appendTo( '#homeTable thead' );
    $('#homeTable thead tr:eq(1) th').each( function (i) {
        var title = $(this).text();
        $(this).html( '<input type="text" placeholder="Search '+title+'" />' );
        $( 'input', this ).on( 'keyup change', function () {
            if( table.column(i).search() !== this.value ) {
                table
                    .column(i)
                    .search( this.value )
                    .draw();
            }
        });
    });
 
    var table = $('#homeTable').DataTable( {
        orderCellsTop: true,
        fixedHeader: true,
        "columnDefs" : [
            // {"targets":3, "type":"date-eu"},
            // { "width": "10px", "targets": 0 },
            // { "width": "40px", "targets": 1 },
            // { "width": "100px", "targets": 2 },
            // { "width": "70px", "targets": 3 },
        ],
        "order": [[ 0, "desc" ]],
        dom: '<"top"Bf>rt<"bottom"lip><"clear">',
        // dom: 'Bfrtip',
        lengthMenu: [
            [ 10, 25, 50,100, -1 ],
            [ '10 rows', '25 rows', '50 rows','100 rows', 'Show all' ]
        ],
        buttons: [
           
            {
                extend: 'excelHtml5',
                text: '<i class="fa fa-file-excel"> </i> &nbsp;Excel',
                exportOptions: {
                    // columns: ':visible'
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7]
                }
            },
            {
                extend: 'pdfHtml5',
                text: '<i class="fa fa-file-pdf"> </i> &nbsp;PDF',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7]
                }
            },
            {
                extend: 'print',
                text: '<i class="fa fa-print"> </i> &nbsp;Print',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7]
                }
            },
        ]

    } );
    $('#hometable1').DataTable( {
        orderCellsTop: true,
        fixedHeader: true,
        "columnDefs" : [

        ],
        "order": [[ 0, "desc" ]],
        dom: '<"top"Bf>rt<"bottom"lip><"clear">',
        // dom: 'Bfrtip',
        lengthMenu: [
            [ 10, 25, 50,100, -1 ],
            [ '10 rows', '25 rows', '50 rows','100 rows', 'Show all' ]
        ],
        buttons: [
           
            {
                extend: 'excelHtml5',
                text: '<i class="fa fa-file-excel"> </i> &nbsp;Excel',
                exportOptions: {
                    // columns: ':visible'
                    columns: [ 0, 1, 2, 3, 4, 5, 6,7]
                }
            },
            {
                extend: 'pdfHtml5',
                text: '<i class="fa fa-file-pdf"> </i> &nbsp;PDF',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7]
                }
            },
            {
                extend: 'print',
                text: '<i class="fa fa-print"> </i> &nbsp;Stampa',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7]
                }
            },
        ]
    } );


    $('#invoiceTable').DataTable( {
            "columnDefs" : [{"targets":2, "type":"date-eu"}],
            "order": [[ 0, "desc" ]],
            dom: '<"top"Bf>rt<"bottom"lip><"clear">',
            // dom: 'Bfrtip',
            lengthMenu: [
                [ 10, 25, 50,100, -1 ],
                [ '10 rows', '25 rows', '50 rows','100 rows', 'Show all' ]
            ],
        buttons: [
            
            {
                extend: 'excelHtml5',
                text: '<i class="fa fa-file-excel"> </i> &nbsp;Excel',
                exportOptions: {
                    // columns: ':visible'
                    columns: [ 0, 1, 2, 3, 4, 5, 6]
                }
            },
            {
                extend: 'pdfHtml5',
                text: '<i class="fa fa-file-pdf"> </i> &nbsp;PDF',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6]
                }
            },
            {
                extend: 'print',
                text: '<i class="fa fa-print"> </i> &nbsp;Stampa',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6]
                }
            },
        ]
    } );




    $('#myTable').DataTable( {
            "columnDefs" : [{"targets":3, "type":"date-eu"}],
            "order": [[ 0, "desc" ]],
            dom: '<"top"Bf>rt<"bottom"lip><"clear">',
            // dom: 'Bfrtip',
            lengthMenu: [
                [ 10, 25, 50,100, -1 ],
                [ '10 rows', '25 rows', '50 rows','100 rows', 'Show all' ]
            ],
        buttons: [
           
            {
                extend: 'excelHtml5',
                text: '<i class="fa fa-file-excel"> </i> &nbsp;Excel',
                exportOptions: {
                    // columns: ':visible'
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
                }
            },
            {
                extend: 'pdfHtml5',
                text: '<i class="fa fa-file-pdf"> </i> &nbsp;PDF',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
                }
            },
            {
                extend: 'print',
                text: '<i class="fa fa-print"> </i> &nbsp;Stampa',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7, 8]
                }
            },
        ]
    } );

    $('#transporter_table').DataTable( {
            "columnDefs" : [{"targets":3, "type":"date-eu"}],
            "order": [[ 0, "desc" ]],
            dom: '<"top"Bf>rt<"bottom"lip><"clear">',
            // dom: 'Bfrtip',
            lengthMenu: [
                [ 10, 25, 50,100, -1 ],
                [ '10 rows', '25 rows', '50 rows','100 rows', 'Show all' ]
            ],
        buttons: [
           
            {
                extend: 'excelHtml5',
                text: '<i class="fa fa-file-excel"> </i> &nbsp;Excel',
                exportOptions: {
                    // columns: ':visible'
                    columns: [ 0, 1, 2, 3]
                }
            },
            {
                extend: 'pdfHtml5',
                text: '<i class="fa fa-file-pdf"> </i> &nbsp;PDF',
                exportOptions: {
                    columns: [ 0, 1, 2, 3]
                }
            },
            {
                extend: 'print',
                text: '<i class="fa fa-print"> </i> &nbsp;Stampa',
                exportOptions: {
                    columns: [ 0, 1, 2, 3]
                }
            },
        ]
    } );

    $('#produser_table').DataTable( {
            "columnDefs" : [{"targets":3, "type":"date-eu"}],
            "order": [[ 0, "desc" ]],
            dom: '<"top"Bf>rt<"bottom"lip><"clear">',
            // dom: 'Bfrtip',
            lengthMenu: [
                [ 10, 25, 50,100, -1 ],
                [ '10 rows', '25 rows', '50 rows','100 rows', 'Show all' ]
            ],
        buttons: [
           
            {
                extend: 'excelHtml5',
                text: '<i class="fa fa-file-excel"> </i> &nbsp;Excel',
                exportOptions: {
                    // columns: ':visible'
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7]
                }
            },
            {
                extend: 'pdfHtml5',
                text: '<i class="fa fa-file-pdf"> </i> &nbsp;PDF',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7]
                }
            },
            {
                extend: 'print',
                text: '<i class="fa fa-print"> </i> &nbsp;Stampa',
                exportOptions: {
                    columns: [ 0, 1, 2, 3, 4, 5, 6, 7]
                }
            },
        ]
    } );


});
</script>

