using System;
using System.IO;

namespace Infra.Bl
{
    public class ExceptionLog2
    {
        public static void WriteLog(string exception) {
            DateTime currentDate = DateTime.Now;
            string LogFileName = "Log/" + currentDate.ToString("yyyy_MM_dd") + ".log";

            using (StreamWriter w = File.AppendText(LogFileName))
            {
                Log(exception, w);
            }
        }
        public static void WriteLogDocumentation(string exception)
        {
            DateTime currentDate = DateTime.Now;
            string LogFileName = "Log/" + currentDate.ToString("yyyy_MM_dd") + ".log";

            using (StreamWriter w = File.AppendText(LogFileName))
            {
                Log(exception, w);
            }
        }

        private static void Log(string logMessage, TextWriter w)
        {
            w.Write("\r\nLog Entry : ");
            w.WriteLine($"{DateTime.Now.ToLongTimeString()} {DateTime.Now.ToLongDateString()}");
            w.WriteLine("  :");
            w.WriteLine($"  :{logMessage}");
            w.WriteLine("-------------------------------");
        }
    }
}
