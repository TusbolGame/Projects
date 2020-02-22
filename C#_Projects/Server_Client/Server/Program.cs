using Common;
using System;
using System.Data;
using System.Linq;
using System.IO;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Threading;
using ServerConnection.Database;

namespace Server
{
    class Program
    {
        // Client Collection
        private static List<ConnectedObject> _clients;
        // Thread Signal
        private static ManualResetEvent _connected = new ManualResetEvent(false);
        // Server socket
        private static Socket _server = null;

        #region [STATIC VARIABLES]
        private static string databaseusername;
        private static string databasepassword;
        private static string databasehddserial;
        private static string databaseonline;
        private static string databaseexpired;
        private static DateTime databaseexpiredate;
        private static string databasehddbanned;
        private static string databasestarted;
        private static int databaseperiod;
        private static string databaseversion;
        private static string currentversion = "2";
        #endregion

        static void Main(string[] args)
        {
            Console.Title = "Server";
            ServerDatabase.Init();
            _clients = new List<ConnectedObject>();
            StartListening();
            Console.ReadLine();
            CloseAllSockets();
            currentversion = "2";
        }

        /// <summary>
        /// Listen for client connections
        /// </summary>
        private static void StartListening()
        {
            try
            {
                Console.WriteLine("Starting server");
                _server = ConnectionManager.CreateListener();
                Console.WriteLine($"Server Started, Waiting for a connection ...");

                while (true)
                {
                    // Set the event to nonsignaled state
                    _connected.Reset();

                    // Start an asynchronous socket to listen for connections
                    _server.BeginAccept(new AsyncCallback(AcceptCallback), _server);

                    // Wait until a connection is made before continuing
                    _connected.WaitOne();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        #region [ GET TIME ]
        public static DateTime GetNistTime()
        {
            var result = DateTime.MinValue;

            // Initialize the list of NIST time servers
            // http://tf.nist.gov/tf-cgi/servers.cgi
            string[] servers = new string[] {
                "nist1-ny.ustiming.org",
                "nist1-nj.ustiming.org",
                "nist1-pa.ustiming.org",
                "time-a.nist.gov",
                "time-b.nist.gov",
                "nist1.aol-va.symmetricom.com",
                "nist1.columbiacountyga.gov",
                "nist1-chi.ustiming.org",
                "nist.expertsmi.com",
                "nist.netservicesgroup.com"
            };

            // Try 5 servers in random order to spread the load
            Random rnd = new Random();
            foreach (string server in servers.OrderBy(s => rnd.NextDouble()).Take(5))
            {
                try
                {
                    // Connect to the server (at port 13) and get the response
                    string serverResponse = string.Empty;
                    using (var reader = new StreamReader(new System.Net.Sockets.TcpClient(server, 13).GetStream()))
                    {
                        serverResponse = reader.ReadToEnd();
                    }

                    // If a response was received
                    if (!string.IsNullOrEmpty(serverResponse))
                    {
                        // Split the response string ("55596 11-02-14 13:54:11 00 0 0 478.1 UTC(NIST) *")
                        string[] tokens = serverResponse.Split(' ');

                        // Check the number of tokens
                        if (tokens.Length >= 6)
                        {
                            // Check the health status
                            string health = tokens[5];
                            if (health == "0")
                            {
                                // Get date and time parts from the server response
                                string[] dateParts = tokens[1].Split('-');
                                string[] timeParts = tokens[2].Split(':');

                                // Create a DateTime instance
                                DateTime utcDateTime = new DateTime(
                                    Convert.ToInt32(dateParts[0]) + 2000,
                                    Convert.ToInt32(dateParts[1]), Convert.ToInt32(dateParts[2]),
                                    Convert.ToInt32(timeParts[0]), Convert.ToInt32(timeParts[1]),
                                    Convert.ToInt32(timeParts[2]));

                                // Convert received (UTC) DateTime value to the local timezone
                                result = utcDateTime.ToLocalTime();

                                return result;
                                // Response successfully received; exit the loop

                            }
                        }

                    }

                }
                catch
                {
                    // Ignore exception and try the next server
                }
            }
            return result;
        }
        #endregion


        /// <summary>
        /// Handler for new connections
        /// </summary>
        /// <param name="ar"></param>
        private static void AcceptCallback(IAsyncResult ar)
        {
            PrintConnectionState("Connection received");

            // Signal the main thread to continue accepting new connections
            _connected.Set();

            // Accept new client socket connection
            Socket socket = _server.EndAccept(ar);

            // Create a new client connection object and store the socket
            ConnectedObject client = new ConnectedObject();
            client.Socket = socket;

            // Store all clients
            _clients.Add(client);

            // Begin receiving messages from new connection
            try
            {
                client.Socket.BeginReceive(client.Buffer, 0, client.BufferSize, SocketFlags.None, new AsyncCallback(ReceiveCallback), client);
            }
            catch (SocketException)
            {
                // Client was forcebly closed on the client side
                CloseClient(client);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        /// <summary>
        /// Handler for received messages
        /// </summary>
        /// <param name="ar"></param>
        private static void ReceiveCallback(IAsyncResult ar)
        {
            string err;
            ConnectedObject client;
            int bytesRead;

            // Check for null values
            if (!CheckState(ar, out err, out client))
            {
                Console.WriteLine(err);
                return;
            }

            // Read message from the client socket
            try
            {
                bytesRead = client.Socket.EndReceive(ar);
            }
            catch (SocketException)
            {
                // Client was forcebly closed on the client side
                CloseClient(client);
                return;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
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
                    client.PrintMessage();

                    string incomming_message = client.getIncommingMessage().Replace("<END>", "");


                    string flag = client.getIncommingMessage().Split(',')[0];
                    if (flag == "Exit")
                    {
                        MySqlCommand mySqlCommand = new MySqlCommand(MySqlCommandType.UPDATE);
                        mySqlCommand.Update("logins").Set("online", (long)0).Where("username", client.getIncommingMessage().Split(',')[1]).Execute();
                        return;
                    }



                    string HDDSerial = incomming_message.Split(',')[0];
                    string Key = incomming_message.Split(',')[1];
                    string nistTime = incomming_message.Split(',')[2];

                    // Get Incomming message from Client

                    using (MySqlCommand mySqlCommand1 = (new MySqlCommand(MySqlCommandType.SELECT)).Select("logins").Where("password", Key))
                    {
                        using (MySqlReader mySqlReader1 = new MySqlReader(mySqlCommand1))
                        {
                            if (mySqlReader1.Read())
                            {
                                databaseusername = mySqlReader1.ReadString("username");
                                Console.WriteLine("DatabaseUsername:" + databaseusername);
                                databasepassword = mySqlReader1.ReadString("password");
                                Console.WriteLine("DatabasePassword:" + databasepassword);
                                databasehddserial = mySqlReader1.ReadString("hddserial");
                                Console.WriteLine("DatabaseHDD:" + databasehddserial);
                                databaseonline = mySqlReader1.ReadString("online");
                                Console.WriteLine("DatabaseOnline:" + databaseonline);
                                databaseexpired = mySqlReader1.ReadString("expired");
                                Console.WriteLine("DatabaseExpire:" + databaseexpired);
                                databaseexpiredate = mySqlReader1.ReadDates("expiredate");
                                Console.WriteLine("DatabaseExpireDate:" + databaseexpiredate);
                                databasehddbanned = mySqlReader1.ReadString("hddbanned");
                                Console.WriteLine("DatabaseHDDBanned:" + databasehddbanned);
                                databasestarted = mySqlReader1.ReadString("started");
                                Console.WriteLine("DatabaseStarted:" + databasestarted);
                                databaseperiod = mySqlReader1.ReadInt16("period");
                                Console.WriteLine("DatabasePeriod:" + databaseperiod);
                                mySqlReader1.Dispose();                                
                            }
                        }
                    }
                    if (HDDSerial == databasehddbanned)
                    {
                        // Reset message
                        client.ClearIncomingMessage();
                        // Acknowledge message - This Key Got Banned!
                        SendReply(client, 1);                        
                    }
                    else if (databasepassword != Key)
                    {
                        // Reset message
                        client.ClearIncomingMessage();
                        // Acknowledge message - Password Wrong!
                        SendReply(client, 2);
                    }
                    else if (databaseonline == "1")
                    {
                        // Reset message
                        client.ClearIncomingMessage();
                        // Acknowledge message - Key Already Online!
                        SendReply(client, 3);
                    }
                    else
                    {
                        if (databasestarted == "0")
                        {
                            nistTime = GetNistTime().AddDays(databaseperiod).ToString("yyyy-MM-dd HH:mm:ss");
                            Console.WriteLine("Nist Time:" + nistTime);
                            string str = nistTime;
                            new MySqlCommand(MySqlCommandType.UPDATE).Update("logins").Set("expiredate", str).Where("password", databasepassword).Execute();
                            new MySqlCommand(MySqlCommandType.UPDATE).Update("logins").Set("started", "1").Where("password", databasepassword).Execute();
                            //nistTime = GetNistTime();
                            DateTime NistTime = GetNistTime();
                            databaseexpiredate = NistTime.AddDays(databaseperiod);

                            // Reset message
                            client.ClearIncomingMessage();
                            // Acknowledge message - Key Binded Click Login Again!
                            SendReply(client, 4);
                        }
                        if (databaseexpired == "1")
                        {
                            // Reset message
                            client.ClearIncomingMessage();
                            // Acknowledge message - Key Deleted Because Expired!
                            SendReply(client, 5);
                        }
                        else if (databaseexpiredate <= GetNistTime())
                        {                            
                            new MySqlCommand(MySqlCommandType.UPDATE).Update("logins").Set("expired", "1").Where("password", databasepassword).Execute();

                            // Reset message
                            client.ClearIncomingMessage();
                            // Acknowledge message - Your Key Expired!
                            SendReply(client, 6);
                        }
                        else if (databasehddserial == "")
                        {
                            new MySqlCommand(MySqlCommandType.UPDATE).Update("logins").Set("hddserial", HDDSerial).Where("password", databasepassword).Execute();
                        }

                        else if (databasehddserial != HDDSerial)
                        {
                            // Reset message
                            client.ClearIncomingMessage();
                            // Acknowledge message - You Can't Use On This PC!
                            SendReply(client, 7);                            
                        }
                        else if (databasehddbanned == HDDSerial)
                        {
                            // Reset message
                            client.ClearIncomingMessage();
                            // Acknowledge message - You Have Been Banned!
                            SendReply(client, 8);
                        }
                        else if (databasestarted == "1" || databaseexpired != "1" || databasehddbanned != HDDSerial || databaseexpiredate >= GetNistTime())
                        {
                            using (MySqlCommand mySqlCommand = (new MySqlCommand(MySqlCommandType.SELECT)).Select("versionupdate").Where("id", (long)1))
                            {
                                using (MySqlReader mySqlReader = new MySqlReader(mySqlCommand))
                                {
                                    if (mySqlReader.Read())
                                    {
                                        databaseversion = mySqlReader.ReadString("version");
                                        Console.WriteLine("Database Version:" + databaseversion);
                                    }
                                }
                            }
                            if (currentversion != databaseversion)
                            {
                                // Reset message
                                client.ClearIncomingMessage();
                                // Acknowledge message - VERSION OUTDATED!
                                SendReply(client, 9);                                
                            }
                            else
                            {
                                new MySqlCommand(MySqlCommandType.UPDATE).Update("logins").Set("online", "1").Where("password", databasepassword).Execute();
                                // Reset message
                                client.ClearIncomingMessage();
                                // Acknowledge message - Main!
                                SendReply(client, 10, databaseusername + "," + databaseexpiredate.ToString("yyyy-MM-dd HH:mm:ss"));                                
                            }
                        }
                    }
                    
                }
            }

            // Listen for more incoming messages
            try
            {
                client.Socket.BeginReceive(client.Buffer, 0, client.BufferSize, SocketFlags.None, new AsyncCallback(ReceiveCallback), client);
            }
            catch (SocketException)
            {
                // Client was forcebly closed on the client side
                CloseClient(client);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        /// <summary>
        /// Sends a reply to client
        /// </summary>
        /// <param name="client"></param>
        private static void SendReply(ConnectedObject client, int flag, string data = "")
        {
            if (client == null)
            {
                Console.WriteLine("Unable to send reply: client null");
                return;
            }

            Console.Write("Sending Reply: ");

            // Create reply
            client.CreateOutgoingMessage(flag.ToString() + "," + data);
            var byteReply = client.OutgoingMessageToBytes();

            // Listen for more incoming messages
            try
            {
                client.Socket.BeginSend(byteReply, 0, byteReply.Length, SocketFlags.None, new AsyncCallback(SendReplyCallback), client);
            }
            catch (SocketException)
            {
                // Client was forcebly closed on the client side
                CloseClient(client);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        /// <summary>
        /// Handler after a reply has been sent
        /// </summary>
        /// <param name="ar"></param>
        private static void SendReplyCallback(IAsyncResult ar)
        {
            Console.WriteLine("Reply Sent");
        }

        /// <summary>
        /// Checks IAsyncResult for null value
        /// </summary>
        /// <param name="ar"></param>
        /// <param name="err"></param>
        /// <param name="client"></param>
        /// <returns></returns>
        private static bool CheckState(IAsyncResult ar, out string err, out ConnectedObject client)
        {
            // Initialise
            client = null;
            err = "";

            // Check ar
            if (ar == null)
            {
                err = "Async result null";
                return false;
            }

            // Check client
            client = (ConnectedObject)ar.AsyncState;
            if (client == null)
            {
                err = "Client null";
                return false;
            }

            return true;
        }

        /// <summary>
        /// Closes a client socket and removes it from the client list
        /// </summary>
        /// <param name="client"></param>
        private static void CloseClient(ConnectedObject client)
        {
            PrintConnectionState("Client disconnected");
            client.Close();
            if (_clients.Contains(client))
            {
                _clients.Remove(client);
            }
        }

        /// <summary>
        /// Closes all client and server connections
        /// </summary>
        private static void CloseAllSockets()
        {
            // Close all clients
            foreach (ConnectedObject connection in _clients)
            {
                connection.Close();
            }
            // Close server
            _server.Close();
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
    }
}
