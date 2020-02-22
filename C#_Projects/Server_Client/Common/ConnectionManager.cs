using System;
using System.Net;
using System.Net.Sockets;
using System.Net.NetworkInformation;
using System.Linq;


namespace Common
{
    public class ConnectionManager
    {
        public static IPAddress _IpAdress { get { return IPAddress.Any; } }
        //public static IPAddress _IpAdreess { get { return IPAddress.Parse("18.217.91.74"); } }
        public static int Port { get { return 42000; } }
        public static IPEndPoint EndPoint { get { return new IPEndPoint(_IpAdress, Port); } }

        public static IPAddress ServerAdress { get { return IPAddress.Parse("51.77.161.206"); } }
        public static IPEndPoint ServerPoint { get { return new IPEndPoint(ServerAdress, Port); } }

        

        public static Socket CreateListener()
        {
            Socket socket = null;
            try
            {
                // Create a TCP/IP socket.             
                socket = CreateServerSocket();
                socket.Bind(EndPoint);
                socket.Listen(10);
            }
            catch (Exception)
            {
                throw;
            }

            return socket;
        }
        public static Socket CreateServerSocket()
        {
            return new Socket(ServerAdress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
        }
        public static Socket CreateSocket()
        {
            return new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        }
    }
}
