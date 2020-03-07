<?php

namespace App\Mail;

use App\Invoice;
use Illuminate\Bus\Queueable;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;
use Illuminate\Contracts\Queue\ShouldQueue;

class InvoiceReminder extends Mailable
{
    use Queueable, SerializesModels;
    
    public $invoice;
    public $reminder_email_content;
    /**
     * Create a new message instance.
     *
     * @return void
     */
    public function __construct(Invoice $invoice, $reminder_email_content)
    {
        $this->invoice = $invoice;
        $this->reminder_email_content = $reminder_email_content;
        // dd($this->reminder_email_content);
    }

    /**
     * Build the message.
     *
     * @return $this
     */
    public function build()
    {
        // return $this->view('view.name');
        return $this->markdown('invoice.reminderEmail');
    }
}
