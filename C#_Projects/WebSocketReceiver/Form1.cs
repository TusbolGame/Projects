using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using WebSocketSharp;

namespace WebSocketReceiver
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Button1_Click(object msender, EventArgs me)
        {
            using (var ws = new WebSocket("wss://www.bitmex.com/realtime"))
            {
                ws.OnMessage += (sender, e) =>
                  textBox1.AppendText("Laputa says: " + e.Data);

                ws.Connect();
                ws.Send("BALUS");
            }
        }
    }
}
