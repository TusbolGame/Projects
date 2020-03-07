<?php



namespace App\Mail;



use App\Invoice;

use Illuminate\Bus\Queueable;

use Illuminate\Mail\Mailable;

use Illuminate\Queue\SerializesModels;

use Illuminate\Contracts\Queue\ShouldQueue;



class Unpaid_email extends Mailable

{

    use Queueable, SerializesModels;



    public $invoices;

    /**

     * Create a new message instance.

     *

     * @return void

     */

    public function __construct($invoices)

    {

        $this->invoices = $invoices;

    }



    /**

     * Build the message.

     *

     * @return $this

     */

    public function build()

    {
        $content =  'invoice.unpaid_email_temp';
        $result = htmlspecialchars_decode($content);
        return $this->markdown($result);
    }

}

