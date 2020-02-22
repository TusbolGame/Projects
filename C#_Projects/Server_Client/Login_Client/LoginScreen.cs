using System;
using System.Data;
using System.Linq;
using System.Windows.Forms;
using System.Management;
using System.IO;
using Microsoft.Win32;
using System.Reflection;
using SharpUpdate;
using System.Diagnostics;
using Common;
using System.Net.Sockets;
using System.Threading;
using System.Globalization;

namespace UnKnownFamilyBypassPrv8
{
    public partial class LoginScreen : Form
    {
        #region [ Static Data Varibales ]
        private static SharpUpdater updater;
        private delegate void SafeTextDelegate(string text);
        private delegate void SafeColorDelegate(System.Drawing.Color color);
        private delegate void SafeVisibility(bool value);
        private delegate void SafeWindowHide();
        #endregion
        #region [ LOAD LOGIN SCREEN ]
        public LoginScreen()
        {
            
            InitializeComponent();
            updater = new SharpUpdater(Assembly.GetExecutingAssembly(), this, new Uri("https://unknown-shop.com/myapp/bypass.xml"));
            //currentversion = "2";
        }
        #endregion
        #region [ HDDSerialNumber ]
        public string GetHDDSerialNumber(string drive)
        {
            if ((drive == "" ? true : drive == null))
            {
                drive = "C";
            }
            ManagementObject managementObject = new ManagementObject(string.Concat("win32_logicaldisk.deviceid=\"", drive, ":\""));
            managementObject.Get();
            return managementObject["VolumeSerialNumber"].ToString();
        }
        #endregion
        
        #region [ LOGIN CLICK ]
        private void login_Click(object sender, EventArgs e)
        {
            wrongm.Visible = false;
            wrongm.Visible = true;
            wrongm.Text = "CHECKING DATA PLEASE WAIT !";
            DateTime nistTime;
            nistTime = DateTime.Now;
            if (this.KeyPlace.Text == "")
            {
                wrongm.Visible = true;
                wrongm.Text = "Key Cannot Be Empty !";
                wrongm.ForeColor = System.Drawing.Color.Red;
            }
            else if (this.KeyPlace.Text.Length < 32)
            {
                wrongm.Visible = true;
                wrongm.Text = "BAD FORMAT , NOT OUR KEY !";
                wrongm.ForeColor = System.Drawing.Color.Yellow;
            }
            else
            {
                wrongm.Visible = false;
                wrongm.Visible = true;
                wrongm.Text = "CHECKING DATA PLEASE WAIT !";
                wrongm.ForeColor = System.Drawing.Color.Lime;

                //MessageBox.Show(this.GetHDDSerialNumber("C").ToString());

                
                Connect(this.GetHDDSerialNumber("C"), this.KeyPlace.Text, nistTime.ToString("yyyy-MM-dd HH:mm:ss"), this);

                wrongm.Visible = false;
                wrongm.Visible = true;
                wrongm.Text = "CHECKING VAILDATION !";

            }
        }
        #endregion
        #region [ CONNECT SERVER ]
        private static void Connect(string HDDSerial, string key, string datetime, LoginScreen parent)
        {
            ConnectedObject client = new ConnectedObject();
            // Create a new socket
            client.Socket = ConnectionManager.CreateSocket();
            int attempts = 0;

            // Loop until we connect (server could be down)
            while (!client.Socket.Connected)
            {
                try
                {
                    attempts++;
                    Console.WriteLine("Connection attempt " + attempts);

                    // Attempt to connect
                    client.Socket.Connect(ConnectionManager.ServerPoint);
                }
                catch (SocketException)
                {
                    //Console.Clear();
                    continue;
                }
            }

            // Display connected status
            PrintConnectionState($"Socket connected to {client.Socket.RemoteEndPoint.ToString()}");

            // Start sending & receiving
            Thread sendThread = new Thread(() => Send(client, HDDSerial, key, datetime));
            Thread receiveThread = new Thread(() => Receive(client, parent));

            sendThread.Start();
            receiveThread.Start();

            // Listen for threads to be aborted (occurs when socket looses it's connection with the server)
            while (sendThread.IsAlive && receiveThread.IsAlive) { }

            // Attempt to reconnect
            //Connect(HDDSerial, key, datetime);
        }
        
        /// <summary>
        /// Sends a message to the server
        /// </summary>
        /// <param name="client"></param>
        private static void Send(ConnectedObject client, string HDDSerial, string Key, string dateTime)
        {
            // Build message

            string msg = HDDSerial + "," + Key + "," + dateTime;

            client.CreateOutgoingMessage(msg);
            byte[] data = client.OutgoingMessageToBytes();

            // Send it on a 1 second interval

            try
            {
                client.Socket.BeginSend(data, 0, data.Length, SocketFlags.None, new AsyncCallback(SendCallback), client);
            }
            catch (SocketException)
            {
                Console.WriteLine("Server Closed");
                client.Close();
                Thread.CurrentThread.Abort();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                Thread.CurrentThread.Abort();
            }

        }

        /// <summary>
        /// Message sent handler
        /// </summary>
        /// <param name="ar"></param>
        private static void SendCallback(IAsyncResult ar)
        {
            Console.WriteLine("Message Sent");
        }

        private static void Receive(ConnectedObject client, LoginScreen parent)
        {
            int bytesRead = 0;

            while (true)
            {
                // Read message from the server
                try
                {
                    bytesRead = client.Socket.Receive(client.Buffer, SocketFlags.None);
                }
                catch (SocketException)
                {
                    Console.WriteLine("Server Closed");
                    client.Close();
                    Thread.CurrentThread.Abort();
                }
                catch (Exception)
                {
                    Thread.CurrentThread.Abort();
                    return;
                }


                // Check message
                if (bytesRead > 0)
                {
                    // Build message as it comes in
                    client.BuildIncomingMessage(bytesRead);

                    // Check if we received the full message
                    if (client.MessageReceived())
                    {

                        
                        // Print message to the console
                        Console.WriteLine("Message Received");

                        Console.WriteLine(client.getIncommingMessage());

                        string response = client.getIncommingMessage().Replace("<END>", "");

                        if (int.Parse(response.Split(',')[0]) == 1)
                        {
                            parent.SetVisible(true);
                            parent.SetText("This Key Got Banned !");
                            parent.SetColor(System.Drawing.Color.Red);
                        }
                        else if (int.Parse(response.Split(',')[0]) == 2)
                        {
                            parent.SetVisible(true);
                            parent.SetText("Password Wrong !");
                            parent.SetColor(System.Drawing.Color.Red);
                        }
                        else if (int.Parse(response.Split(',')[0]) == 3)
                        {
                            parent.SetVisible(true);
                            parent.SetText("Key Already Online !");
                            parent.SetColor(System.Drawing.Color.Yellow);
                        }
                        else if (int.Parse(response.Split(',')[0]) == 4)
                        {
                            parent.SetVisible(true);
                            parent.SetText("Key Binded Click Login Again !");
                            parent.SetColor(System.Drawing.Color.Lime);
                        }
                        else if (int.Parse(response.Split(',')[0]) == 5)
                        {
                            parent.SetVisible(true);
                            parent.SetText("Key Deleted Because Expired !");
                            parent.SetColor(System.Drawing.Color.Red);
                        }
                        else if (int.Parse(response.Split(',')[0]) == 6)
                        {
                            parent.SetVisible(true);
                            parent.SetText("Your Key Expired !");
                            parent.SetColor(System.Drawing.Color.Red);
                        }
                        else if (int.Parse(response.Split(',')[0]) == 7)
                        {
                            parent.SetVisible(true);
                            parent.SetText("You Can't Use On This PC !");
                            parent.SetColor(System.Drawing.Color.Red);
                        }
                        else if (int.Parse(response.Split(',')[0]) == 8)
                        {
                            parent.SetVisible(true);
                            parent.SetText("You Have Been Banned !");
                            parent.SetColor(System.Drawing.Color.Red);
                        }
                        else if (int.Parse(response.Split(',')[0]) == 9)
                        {
                            string message1 = "VERSION OUTDATED , PLEASE CLICK OK TO UPDATE OR CANCEL TO SHUTDOWN !";
                            string title1 = "! BYPASS WARNING !";
                            MessageBoxButtons buttons1 = MessageBoxButtons.OKCancel;
                            DialogResult result1 = MessageBox.Show(message1, title1, buttons1, MessageBoxIcon.Warning);
                            if (result1 == DialogResult.OK)
                            {
                                updater.DoUpdate();
                            }
                            else if (result1 == DialogResult.Cancel)
                            {
                                Application.Exit();
                            }
                        }
                        else if (int.Parse(response.Split(',')[0]) == 10)
                        {

                            parent.SetVisible(false);
                            parent.SafeHide();

                            using (RegistryKey Key = Registry.CurrentUser.OpenSubKey(@"Techcom", true))
                                if (Key != null)
                                {
                                    string val = (string)Key.GetValue("Name");
                                    if (string.IsNullOrEmpty(val))
                                    {
                                        Key.SetValue("Name", "" + response.Split(',')[1] + "");
                                        Key.Close();
                                    }
                                    else
                                    {
                                        Key.SetValue("Name", "" + response.Split(',')[1] + "");
                                        Key.Close();
                                    }
                                }
                                else
                                {
                                    RegistryKey key;
                                    key = Microsoft.Win32.Registry.CurrentUser.CreateSubKey("Techcom");
                                    key.SetValue("Name", "" + response.Split(',')[1] + "");
                                    key.Close();
                                }
                            
                            BypassScreen bypassdialog = new BypassScreen();
                            TimeSpan timeSpan = new TimeSpan(DateTime.ParseExact(response.Split(',')[2], "yyyy-MM-dd HH:mm:ss", CultureInfo.InvariantCulture).Ticks);
                            DateTime nistTime = DateTime.Now;
                            TimeSpan timeSpan1 = new TimeSpan(nistTime.Ticks);
                            int totalDays = (int)(timeSpan.TotalDays - timeSpan1.TotalDays);
                            int totalHours = (int)(timeSpan.TotalHours - timeSpan1.TotalHours);
                            int totalMinutes = (int)(timeSpan.TotalMinutes - timeSpan1.TotalMinutes);
                            string str1 = "";
                            string str2 = "";
                            if (totalDays > 0)
                            {
                                str1 = string.Concat("Expire By Days : ", totalDays, " Days");
                                str2 = string.Concat("", totalDays, " Days");
                            }
                            else if (totalHours > 0)
                            {
                                str1 = string.Concat("Expire By Hours: ", totalHours, " Hours");
                                str2 = string.Concat("", totalHours, " Hours");
                            }
                            else if (totalMinutes > 0)
                            {
                                str1 = string.Concat("Expire By Minutes : ", totalMinutes, " Minutes");
                                str2 = string.Concat("", totalMinutes, " Minutes");
                            }
                            bypassdialog.Show();
                            Control[] ctrls = bypassdialog.Controls.Find("username", false);
                            if (ctrls.Length > 0)
                            {
                                Label lbl = (Label)ctrls[0];
                                lbl.Text += response.Split(',')[1];
                            }
                            Control[] ctrls2 = bypassdialog.Controls.Find("keyindays", false);
                            if (ctrls2.Length > 0)
                            {
                                Label lbl = (Label)ctrls2[0];
                                lbl.Text = str1;
                            }
                            Control[] ctrls3 = bypassdialog.Controls.Find("keyindate", false);
                            if (ctrls3.Length > 0)
                            {
                                Label lbl = (Label)ctrls3[0];
                                lbl.Text = "Expire By Date : " + response.Split(',')[2] + "";
                            }
                            bypassdialog.Text += str2;
                            parent.SafeHide();
                        }

                        // Reset message
                        client.ClearIncomingMessage();
                    }
                }
            }
        }

        private void SetText(string text)
        {
            WriteTextSafe(text);
        }
        private void WriteTextSafe(string text)
        {
            if (wrongm.InvokeRequired)
            {
                var d = new SafeTextDelegate(WriteTextSafe);
                wrongm.Invoke(d, new object[] { text });
            }
            else
            {
                wrongm.Text = text;
            }
        }
        private void SetColor(System.Drawing.Color color)
        {
            SetControlColor(color);
        }
        private void SetControlColor(System.Drawing.Color color)
        {
            if (wrongm.InvokeRequired)
            {
                var c = new SafeColorDelegate(SetControlColor);
                wrongm.Invoke(c, new object[] { color });
            }
            else
            {
                wrongm.ForeColor = color;
            }
        }
        private void SetVisible(bool value)
        {
            SetControlVisible(value);
        }
        private void SetControlVisible(bool value)
        {
            if (wrongm.InvokeRequired)
            {
                var v = new SafeVisibility(SetControlVisible);
                wrongm.Invoke(v, new object[] { value });
            }
            else
            {
                wrongm.Visible = value;
            }
        }
        private void SafeHide()
        {
            SetWindowHide();
        }
        private void SetWindowHide()
        {
            if (this.InvokeRequired)
            {
                var h = new SafeWindowHide(SetWindowHide);
                this.Invoke(h, new object[] { });
            }
            else
            {
                this.Hide();
            }
        }


        /// <summary>
        /// Prints connection 'connected' or 'disconnected' states
        /// </summary>
        /// <param name="msg"></param>
        public static void PrintConnectionState(string msg)
        {
            string divider = new String('*', 60);
            Console.WriteLine();
            Console.WriteLine(divider);
            Console.WriteLine(msg);
            Console.WriteLine(divider);
        }
        #endregion

        #region [ EXIT BUTTON ]
        private void close_Click_1(object sender, EventArgs e)
        {
            System.Windows.Forms.Application.ExitThread();
        }
        #endregion
        #region [ BUY KEY ]
        private void buykey_Click(object sender, EventArgs e)
        {
            Process.Start("https://www.unknown-shop.com/?page=donate");
        }
        #endregion
        #region [ FORM CLOSING ]
        private void LoginScreen_FormClosed(object sender, FormClosedEventArgs e)
        {
            Process.Start("https://discord.gg/PNMwKSP");
            Application.ExitThread();
        }
        #endregion
    }
}
