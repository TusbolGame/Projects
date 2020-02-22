using MySql.Data.MySqlClient;
using System;

using MYSQLCONNECTION = MySql.Data.MySqlClient.MySqlConnection;
namespace ServerConnection.Database
{
    public class ServerDatabase
    {
        private static string ConnectionStrings;
        public static string ConnectionString2 = "Server=51.77.161.206;Port=3306;Database=unkbypass;Uid=bypass;Password=0110425195;Persist Security Info=True;Pooling=true; Min Pool Size = 5;  Max Pool Size = 100;";
        public static MYSQLCONNECTION MySqlConnection
        {
            get
            {
                MYSQLCONNECTION conn = new MYSQLCONNECTION();
                conn.ConnectionString = ConnectionString2;
                return conn;
            }
        }
        public static void CreateConnection()
        {
            ServerDatabase.ConnectionStrings = string.Concat(new string[] { "Server=51.77.161.206;Port=3306;Database=unkbypass;Uid=bypass;Password=0110425195;Persist Security Info=True;Pooling=true; Min Pool Size = 5;  Max Pool Size = 100;" });
        }
        public static void Init()
        {
            ServerDatabase.CreateConnection();
        }
    }
}