using System;
using System.IO;
using System.Windows.Forms;
using System.Diagnostics;
using Microsoft.Win32;
using Common;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Globalization;
namespace UnKnownFamilyBypassPrv8
{
    public partial class BypassScreen : Form
    {
        #region [ Main ]
        public string databaseusername;
        public string UIDir;
        public string WorkingDir;
        public string QM;
        public string XposedAPKPath;
        public string BypassAPKPath;
        public BypassScreen()
        {
            InitializeComponent();
            actionmsg.Visible = false;
            actionmsg.Text = "PLEASE WAIT !";
            actionmsg.Visible = true;
            //SetVar();
            //GetWorkingDirs();
            //ExtractMyFiles();
            //EditHosts();
        }
        #endregion
        #region [ SETVAR ]
        public void SetVar()
        {
            RegistryKey registryKey = Registry.LocalMachine.OpenSubKey(@"SOFTWARE\WOW6432Node\Tencent\MobileGamePC\UI");
            object value = registryKey.GetValue("InstallPath");
            string path = System.Environment.GetEnvironmentVariable("Path");
            string newPathComponent = value.ToString();
            if (!path.Contains(newPathComponent))
            {
                if (!path.EndsWith(";"))
                    path = path + ';';
                path = path + newPathComponent;
                var target = EnvironmentVariableTarget.User; // Or EnvironmentVariableTarget.Machine
                Environment.SetEnvironmentVariable("Path", path, target);
            }
        }
        #endregion
        #region [ GETWORKING DIRS ]
        private void GetWorkingDirs()
        {
            RegistryKey registryKey = Registry.LocalMachine.OpenSubKey(@"SOFTWARE\WOW6432Node\Tencent\MobileGamePC\UI");
            object Value = registryKey.GetValue("InstallPath");
            string UIPath = Value.ToString();
            UIDir = UIPath;
            var pathdirwork = UIPath + @"\AndroidEmulator\Type";
            WorkingDir = pathdirwork;
            var str = UIPath + @"\QMNetworkMgr.ini";
            QM = str;
            XposedAPKPath = WorkingDir + "\\Xposed.apk";
            BypassAPKPath = WorkingDir + "\\Bypass.apk";
            RegistryKey registryKey2 = Registry.CurrentUser.OpenSubKey(@"Techcom");
            object Name = registryKey2.GetValue("Name");
            string dataname = Name.ToString();
            databaseusername = dataname;
        }
        #endregion
        #region [ EXTRACTMYFILES ]
        private void ExtractMyFiles()
        {
            if(!Directory.Exists(WorkingDir))
            {
                #region [ CREATE MY DIR ]
                Directory.CreateDirectory(WorkingDir);
                byte[] xposed = Properties.Resources.Xposed;
                File.WriteAllBytes(XposedAPKPath, xposed);
                byte[] bypass = Properties.Resources.Bypass;
                File.WriteAllBytes(BypassAPKPath, bypass);
                #endregion
            }
            else if(Directory.Exists(WorkingDir))
            {
                #region [ SHOW MY FILE ]
                File.SetAttributes(WorkingDir, FileAttributes.Normal);
                #endregion
                #region [ DELTE FILES IF EXISTS ]
                if (File.Exists(XposedAPKPath))
                {
                    File.Delete(XposedAPKPath);
                }
                if (File.Exists(BypassAPKPath))
                {
                    File.Delete(BypassAPKPath);
                }
                #endregion
                #region [ CREATE FILE ]
                byte[] xposed = Properties.Resources.Xposed;
                File.WriteAllBytes(XposedAPKPath, xposed);
                byte[] bypass = Properties.Resources.Bypass;
                File.WriteAllBytes(BypassAPKPath, bypass);
                #endregion
            }
        }
        #endregion
        #region [ EDITHOSTS ]
        private void EditHosts()
        {
            string hostpathorg = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.System), @"drivers/etc/hosts");
            string hostpathback = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.System), @"drivers/etc/bhosts");
            string orghurl = "https://cdn.discordapp.com/attachments/646620898396471296/652528055243374622/hosts";
            if (!File.Exists(hostpathorg))
            {
                using (WebClient webClient = new WebClient())
                {
                    webClient.DownloadFileAsync(new Uri(orghurl), hostpathorg);
                }
            }
            else if(File.Exists(hostpathorg))
            {
                if(File.Exists(hostpathback))
                {
                    File.Delete(hostpathback);
                    using (WebClient webClient = new WebClient())
                    {
                        webClient.DownloadFileAsync(new Uri(orghurl), hostpathorg);
                    }
                }
                else if (!File.Exists(hostpathback))
                {
                    File.Move(hostpathorg, hostpathback);
                    using (WebClient webClient = new WebClient())
                    {
                        webClient.DownloadFileAsync(new Uri(orghurl), hostpathorg);
                    }
                }
            }
            actionmsg.Visible = false;
            actionmsg.Text = "READY !";
            actionmsg.Visible = true;
        }
        #endregion
        #region [ START EMULATOR ]
        private void startemulator_Click(object sender, EventArgs e)
        {
            actionmsg.Visible = false;
            actionmsg.Text = "PLEASE WAIT";
            actionmsg.Visible = true;
            Process[] pname = Process.GetProcessesByName("AndroidEmulator");
            if (pname.Length > 0)
            {
                string message = "EMULATOR ALREADY WORKING PLEASE CLICK OK TO CLOSE IT\n\nTHEN RE CLICK START EMULATOR";
                string title = "! BYPASS WARNING !";
                MessageBoxButtons buttons = MessageBoxButtons.OKCancel;
                DialogResult result = MessageBox.Show(message, title, buttons, MessageBoxIcon.Warning);
                if (result == DialogResult.OK)
                {
                    foreach (Process proc in Process.GetProcessesByName("AppMarket"))
                    {
                        proc.Kill();
                    }
                    foreach (Process proc in Process.GetProcessesByName("AndroidEmulator"))
                    {
                        proc.Kill();
                    }
                    actionmsg.Visible = false;
                    actionmsg.Text = "EMULATOR CLOSED , YOU CAN START IT AGAIN NOW";
                    actionmsg.Visible = true;
                }
            }
            else
            {
                var TP3 = UIDir + @"\tp3helper.dat";
                if (File.Exists(TP3))
                {
                    File.Delete(TP3);
                }
                var TP3e = UIDir + @"\TP3Helper.exe";
                if (File.Exists(TP3e))
                {
                    File.Delete(TP3e);
                }
                var AE = UIDir + @"\AndroidEmulator.exe";
                Process.Start(AE);
                actionmsg.Visible = false;
                actionmsg.Text = "EMULATOR PROCCESS CREATED";
                actionmsg.Visible = true;
            }
        }
        #endregion
        #region [ INSTALL BYPASS ]
        private void installbypass_Click(object sender, EventArgs e)
        {
            Process[] pname = Process.GetProcessesByName("AndroidEmulator");
            if (pname.Length > 0)
            {
                actionmsg.Visible = false;
                actionmsg.Text = "PLEASE WAIT";
                actionmsg.Visible = true;
                Process process = new Process();
                process.StartInfo.FileName = "CMD.exe";
                process.StartInfo.Arguments = "/c adb kill-server & adb start-server & adb connect localhost:5555 & adb -s localhost:5555 shell pm uninstall de.robv.android.xposed.installer & adb -s localhost:5555 shell pm uninstall family.ibrahem.unknownproject & adb -s localhost:5555 shell pm uninstall uc.ciceron.cerberus & adb -s localhost:5555 push " + BypassAPKPath + " /mnt/shell/emulated/0/Download & adb -s localhost:5555 push " + XposedAPKPath + " /mnt/shell/emulated/0/Download & adb -s localhost:5555 shell pm install /mnt/shell/emulated/0/Download/Bypass.apk & adb -s localhost:5555 shell pm install /mnt/shell/emulated/0/Download/Xposed.apk & adb -s localhost:5555 shell rm -rf /mnt/shell/emulated/0/Download/Bypass.apk & adb -s localhost:5555 shell rm -rf /mnt/shell/emulated/0/Download/Xposed.apk";
                process.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
                process.Start();
                process.WaitForExit();
                actionmsg.Visible = false;
                actionmsg.Text = "INSTALLING BYPASS DONE";
                actionmsg.Visible = true;
            }
            else
            {
                string message = "Emulator Not Running";
                string title = "[ UnKnownBYPASS ]";
                MessageBoxButtons buttons = MessageBoxButtons.OK;
                DialogResult result = MessageBox.Show(message, title, buttons, MessageBoxIcon.Information);
            }
        }
        #endregion
        #region [ RESTART EMULATOR ]
        private void restartemulator_Click(object sender, EventArgs e)
        {
            Process[] pname = Process.GetProcessesByName("AndroidEmulator");
            if (pname.Length > 0)
            {
                    actionmsg.Visible = false;
                    actionmsg.Text = "PLEASE WAIT !";
                    actionmsg.Visible = true;
                    Process process2 = new Process();
                    process2.StartInfo.FileName = "CMD.exe";
                    process2.StartInfo.Arguments = "/c adb kill-server & adb start-server & adb connect localhost:5555 & adb -s localhost:5555 shell reboot";
                    process2.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
                    process2.Start();
                    process2.WaitForExit();
                    actionmsg.Visible = false;
                    actionmsg.Text = "REBOOTING THE EMUALTOR";
                    actionmsg.Visible = true;
            }
            else
            {
                string message = "EMULATOR NOT RUNING , PLEASE MAKE SURE TO FOLLOW STEPS TO AVOID ANY BANNED !";
                string title = "! BYPASS WARNING !";
                MessageBoxButtons buttons = MessageBoxButtons.OKCancel;
                DialogResult result = MessageBox.Show(message, title, buttons, MessageBoxIcon.Warning);
            }
        }
        #endregion
        #region [ START GAME ]
        private void startpubgm_Click(object sender, EventArgs e)
        {
                actionmsg.Visible = false;
                actionmsg.Text = "RUNNING PUBG MOBILE";
                actionmsg.Visible = true;
                Process process2 = new Process();
                process2.StartInfo.FileName = "CMD.exe";
                process2.StartInfo.Arguments = "/c adb kill-server & adb start-server & adb connect localhost:5555 & adb -s localhost:5555 shell am start -n com.tencent.ig/com.epicgames.ue4.SplashActivity";
                process2.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
                process2.Start();
                process2.WaitForExit();
                actionmsg.Visible = false;
                actionmsg.Text = "PUBG MOBILE STARTED";
                actionmsg.Visible = true;
        }
        #endregion
        #region [ CLOSE GAME ]
        private void closepubg_Click(object sender, EventArgs e)
        {
            actionmsg.Visible = false;
            actionmsg.Text = "CLOSING PUBG MOBILE";
            actionmsg.Visible = true;
            Process process2 = new Process();
            process2.StartInfo.FileName = "CMD.exe";
            process2.StartInfo.Arguments = "/c adb kill-server & adb start-server & adb connect localhost:5555 & adb -s localhost:5555 shell am force-stop com.tencent.ig & adb -s localhost:5555 shell rm -rf /system/bin/disable_houdini & adb -s localhost:5555 shell rm -rf /system/bin/enable_houdini & adb -s localhost:5555 shell rm -rf /system/bin/houdini & adb -s localhost:5555 shell rm -rf /data/data/com.tencent.ig/files & adb -s localhost:5555 shell rm -rf /data/data/com.google.android.gms/shared_prefs/adid_settings.xml & adb -s localhost:5555 shell rm -rf /data/data/com.tencent.ig/cache/* & adb -s localhost:5555 shell rm -rf /data/data/com.tencent.ig/app_bugly/* & adb -s localhost:5555 shell rm -rf /data/data/com.tencent.ig/app_crashrecord/* & adb -s localhost:5555 shell rm -rf /data/data/com.tencent.ig/app_webview/Cache/* & adb -s localhost:5555 shell rm -rf /system/lib/libldutils.so & adb -s localhost:5555 shell rm -rf /sdcard/Android/data/com.tencent.ig/cache/GCloud/* & adb -s localhost:5555 shell rm -rf /sdcard/MidasOversea & adb -s localhost:5555 shell rm -rf /sdcard/.backups & adb -s localhost:5555 shell rm -rf /sdcard/tencent & adb -s localhost:5555 shell rm -rf /sdcard/MidasOversea & adb -s localhost:5555 shell rm -rf /sdcard/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Logs/* & adb -s localhost:5555 shell rm -rf /sdcard/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferTmpDir/* & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/tbslog/* & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/ca-bundle.pem & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/cacheFile.txt & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/vmpcloudconfig.json & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/ProgramBinaryCache/* & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/puffer_temp/* & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Paks/puffer_res.eifs & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/UpdateInfo/* & adb -s localhost:5555 shell rm -rf /storage/emulated/0/tencent/* & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Pandora/* & adb -s localhost:5555 shell rm -rf /storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/RoleInfo/*";
            process2.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
            process2.Start();
            process2.WaitForExit();
            actionmsg.Visible = false;
            actionmsg.Text = "PUBG CLOSED NOW";
            actionmsg.Visible = true;
            string message1 = "TO CLOSE EMULATOR CLICK EXIT BYPASS\nGOOD LUCK !";
            string title1 = "! BYPASS WARNING !";
            MessageBoxButtons buttons1 = MessageBoxButtons.OKCancel;
            DialogResult result1 = MessageBox.Show(message1, title1, buttons1, MessageBoxIcon.Warning);
        }
        #endregion
        #region [ GUIDE ]
        private void guide_Click(object sender, EventArgs e)
        {
            actionmsg.Visible = false;
            actionmsg.Text = "GUIDE ABOUT HOW TO USE NOT FINISHED YET :( , FOLLOW OUR YOUTUBE VIDEOS";
            actionmsg.Visible = true;
        }
        #endregion

        #region [ CONNECT SERVER ]
        private static void Connect(string userinfo)
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
                    client.Socket.Connect(ConnectionManager.EndPoint);
                }
                catch (SocketException)
                {
                    Console.Clear();
                }
            }

            // Display connected status
            Console.Clear();
            PrintConnectionState($"Socket connected to {client.Socket.RemoteEndPoint.ToString()}");

            // Start sending & receiving
            Thread sendThread = new Thread(() => Send(client, userinfo));

            sendThread.Start();

            // Attempt to reconnect
            Connect(userinfo);
        }

        /// <summary>
        /// Sends a message to the server
        /// </summary>
        /// <param name="client"></param>
        private static void Send(ConnectedObject client, string userinfo)
        {
            // Build message

            string msg = userinfo;

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



        #region [ CLOSE BYPASSING ]
        private void closebypass_Click(object sender, EventArgs e)
        {
            Process[] pname = Process.GetProcessesByName("AppMarket");
            Process[] pname2 = Process.GetProcessesByName("AndroidEmulator");
            if (pname.Length > 0)
            {
                foreach (Process proc in Process.GetProcessesByName("AppMarket"))
                {
                    proc.Kill();
                }
                foreach (Process proc in Process.GetProcessesByName("AndroidEmulator"))
                {
                    proc.Kill();
                }
            }
            else if (pname2.Length > 0)
            {
                foreach (Process proc in Process.GetProcessesByName("AppMarket"))
                {
                    proc.Kill();
                }
                foreach (Process proc in Process.GetProcessesByName("AndroidEmulator"))
                {
                    proc.Kill();
                }
            }

            Connect("Exit" + "," + this.databaseusername);            
            Application.Exit();
        }
        #endregion
        #region [ FIX98% ]
        private void fix98_Click(object sender, EventArgs e)
        {
            FIX98START2();
        }
        void FIX98START2()
        {
            Process[] pname = Process.GetProcessesByName("AndroidEmulator");
            if (pname.Length > 0)
            {
                SetVar();
                bool first = false;
                bool second = false;
                bool third = false;
                bool fourth = false;
                bool fifth = false;
                bool sixth = false;
                string stmessage = "Please Click OK To Start Procces Of Fix\n\n\n#UnKnownFamily";
                string sttitle = "[ UnKnownFamily ROBOT ]";
                MessageBoxButtons stbuttons = MessageBoxButtons.OK;
                DialogResult stresult = MessageBox.Show(stmessage, sttitle, stbuttons, MessageBoxIcon.Information);
                if (stresult == DialogResult.OK)
                {
                    ProcessStartInfo KillServer = new ProcessStartInfo("cmd");
                    KillServer.Arguments = "/c adb kill-server";
                    KillServer.RedirectStandardOutput = true;
                    KillServer.UseShellExecute = false;
                    KillServer.CreateNoWindow = true;
                    Process DoTheKill = new Process();
                    DoTheKill.StartInfo = KillServer;
                    DoTheKill.Start();
                    DoTheKill.WaitForExit();
                    first = true;
                }
                if (first == true)
                {
                    ProcessStartInfo Connect = new ProcessStartInfo("cmd");
                    Connect.Arguments = "/c adb connect localhost:5555";
                    Connect.RedirectStandardOutput = true;
                    Connect.UseShellExecute = false;
                    Connect.CreateNoWindow = true;
                    Process DoConnect = new Process();
                    DoConnect.StartInfo = Connect;
                    DoConnect.Start();
                    DoConnect.WaitForExit();
                    second = true;
                }
                if (second == true)
                {
                    ProcessStartInfo Rename1ST = new ProcessStartInfo("cmd");
                    Rename1ST.Arguments = "/c adb -s localhost:5555 shell mv /system/lib/libhoudini_408p.txt /system/lib/libhoudini_408p.so";
                    Rename1ST.RedirectStandardOutput = true;
                    Rename1ST.UseShellExecute = false;
                    Rename1ST.CreateNoWindow = true;
                    Process DoRename1ST = new Process();
                    DoRename1ST.StartInfo = Rename1ST;
                    DoRename1ST.Start();
                    DoRename1ST.WaitForExit();
                    third = true;
                }
                if (third == true)
                {
                    ProcessStartInfo Rename2ND = new ProcessStartInfo("cmd");
                    Rename2ND.Arguments = "/c adb -s localhost:5555 shell mv /system/lib/libhoudini_415c.txt /system/lib/libhoudini_415c.so";
                    Rename2ND.RedirectStandardOutput = true;
                    Rename2ND.UseShellExecute = false;
                    Rename2ND.CreateNoWindow = true;
                    Process DoRename2ND = new Process();
                    DoRename2ND.StartInfo = Rename2ND;
                    DoRename2ND.Start();
                    DoRename2ND.WaitForExit();
                    fourth = true;
                }
                if (fourth == true)
                {
                    ProcessStartInfo Rename2ND = new ProcessStartInfo("cmd");
                    Rename2ND.Arguments = "/c adb -s localhost:5555 shell mv /init.vbox86.r0 /init.vbox86.rc";
                    Rename2ND.RedirectStandardOutput = true;
                    Rename2ND.UseShellExecute = false;
                    Rename2ND.CreateNoWindow = true;
                    Process DoRename2ND = new Process();
                    DoRename2ND.StartInfo = Rename2ND;
                    DoRename2ND.Start();
                    DoRename2ND.WaitForExit();
                    fifth = true;
                }
                if (fifth == true)
                {
                    ProcessStartInfo Rename2ND = new ProcessStartInfo("cmd");
                    Rename2ND.Arguments = "/c adb -s localhost:5555 shell reboot";
                    Rename2ND.RedirectStandardOutput = true;
                    Rename2ND.UseShellExecute = false;
                    Rename2ND.CreateNoWindow = true;
                    Process DoRename2ND = new Process();
                    DoRename2ND.StartInfo = Rename2ND;
                    DoRename2ND.Start();
                    DoRename2ND.WaitForExit();
                    sixth = true;
                }
                if (sixth == true)
                {
                    string message = "Your Emulator Stuck Fixed\n\n\n#UnKnownFamily";
                    string title = "[ UnKnownFamily ROBOT ]";
                    MessageBoxButtons buttons = MessageBoxButtons.OK;
                    DialogResult result = MessageBox.Show(message, title, buttons, MessageBoxIcon.Information);
                    if (result == DialogResult.OK)
                    {
                        first = false;
                        second = false;
                        third = false;
                        fourth = false;
                        fifth = false;
                        sixth = false;
                    }

                }
            }
            else
            {
                string message = "Emulator Not Running\n\n#UnKnownFamily";
                string title = "[ UnKnownFamily TASK MANAGER ]";
                MessageBoxButtons buttons = MessageBoxButtons.OK;
                DialogResult result = MessageBox.Show(message, title, buttons, MessageBoxIcon.Information);
            }
        }
        #endregion
        #region [ FORM CLOSING ]
        private void BypassScreen_FormClosed(object sender, FormClosedEventArgs es)
        {
            Connect("Exit" + "," + this.databaseusername);
            Application.Exit();
        }
        #endregion
    }
}
