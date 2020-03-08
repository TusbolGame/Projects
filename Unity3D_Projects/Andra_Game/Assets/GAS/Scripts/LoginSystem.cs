using System;
using UnityEngine;
using System.Collections;
using System.Diagnostics;
using System.IO;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using Debug = UnityEngine.Debug;
#if (UNITY_STANDALONE || UNITY_EDITOR_WIN || UNITY_EDITOR_OSX ) && (!UNITY_WEBPLAYER && !UNITY_WEBGL)
using System.Net.NetworkInformation;
#endif

public class LoginSystem : MonoBehaviour {
	[SerializeField] private InputField userName;
	[SerializeField] private InputField passwordName;
	[SerializeField] public string LoginUrl;
	[SerializeField] public string checkBannedUrl;
	private string info;
	[SerializeField] public string SecureKey;
	[SerializeField] private Text WarningMsg;
	private UiAccountManager GetLoginCanvas;
	public static ProcessStartInfo pythonInfo;
	public static Process python;

	public static string username;

	// Use this for initialization
	
	static Task<int> RunProcessAsync()
	{
		var tcs = new TaskCompletionSource<int>();

		pythonInfo = new ProcessStartInfo();
		pythonInfo.FileName =Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "real_time_video", @"real_time_video.exe");
		pythonInfo.CreateNoWindow = false;
		pythonInfo.ErrorDialog = false;
		pythonInfo.UseShellExecute = false;
		pythonInfo.RedirectStandardOutput = true;
		pythonInfo.RedirectStandardError = true;

		Console.WriteLine("Python Starting");
		python = Process.Start(pythonInfo);
		python.ErrorDataReceived += (sender, errorLine) => { if (errorLine.Data != null) Trace.WriteLine(errorLine.Data); };
		python.OutputDataReceived += (sender, outputLine) => { if (outputLine.Data != null) Trace.WriteLine(outputLine.Data); };
		python.BeginErrorReadLine();
		python.BeginOutputReadLine();
		python.WaitForExit();

		Console.WriteLine("Python Starting");
		python = Process.Start(pythonInfo);
		python.WaitForExit();

		return tcs.Task;
	}
	[DllImport("kernel32.dll", CharSet = CharSet.Auto)]
	public static extern void OutputDebugString(string message);
	void Start ()
	{

		//RunProcessAsync();
		OutputDebugString("started...");
		GetLoginCanvas = gameObject.GetComponent<UiAccountManager>();
		#if UNITY_STANDALONE || UNITY_EDITOR_WIN || UNITY_EDITOR_OSX
		ShowNetworkInterfaces ();
		#endif

	}



	// Update is called once per frame
	void Update () {

	}

	public void Login (){
		//StartCoroutine (Query_Account ());
		if (userName.text.Length < 1 || passwordName.text.Length < 1)
		{
			return;
		}
		var ds = new DataService ("GameDatabase.db");
		if (ds.CheckUser(userName.text, passwordName.text))
		{
			username = userName.text;
			SceneManager.LoadScene("1_Menu 3D");
		}
	}
	public void ShowNetworkInterfaces()
	{
		#if (UNITY_STANDALONE || UNITY_EDITOR_WIN || UNITY_EDITOR_OSX ) && (!UNITY_WEBPLAYER && !UNITY_WEBGL && !UNITY_IOS && !UNITY_ANDROID)

		IPGlobalProperties computerProperties = IPGlobalProperties.GetIPGlobalProperties();
		NetworkInterface[] nics = NetworkInterface.GetAllNetworkInterfaces();

		foreach (NetworkInterface adapter in nics)
		{
			PhysicalAddress address = adapter.GetPhysicalAddress();
			byte[] bytes = address.GetAddressBytes();
			string mac = null;
			for (int i = 0; i < bytes.Length; i++)
			{
				mac = string.Concat(mac +(string.Format("{0}", bytes[i].ToString("X2"))));
				if (i != bytes.Length - 1)
				{
					mac = string.Concat(mac + "-");
				}
			}
			info += mac ;

		}
		#endif

	}
	IEnumerator Query_Account (){
		#if (UNITY_STANDALONE || UNITY_EDITOR_WIN || UNITY_EDITOR_OSX || UNITY_ANDROID ) && (!UNITY_WEBPLAYER && !UNITY_WEBGL && !UNITY_ANDROID && !UNITY_IOS)

		WWW checkBanned = new WWW (checkBannedUrl + "?" + "mac=" + info.Trim() +"&secureid="+SecureKey);

		#endif

		#if UNITY_WEBGL || UNITY_WEBPLAYER || UNITY_ANDROID || UNITY_IOS
		WWW checkBanned = new WWW (checkBannedUrl + "?" + "ip=1&secureid="+SecureKey);

		#endif


		WarningMsg.text = "Check White User... ";
		yield return checkBanned;
		Debug.Log (checkBanned.text);
		if (checkBanned.text.Trim () == "1" || checkBanned.text.Trim () == "3") {
			WWW query = new WWW (LoginUrl + "?" + "username=" + userName.text + "&password=" + passwordName.text + "&secureid=" + SecureKey);
			WarningMsg.text = "Please Wait ... ";
			yield return query;
			string[] split = query.text.Split (',');
			if (split [0].Trim () == "1") {


				PlayerPrefs.SetInt ("userID", int.Parse (split [1].Trim ()));
				PlayerPrefs.SetString ("firstname", split [2].Trim ());
				PlayerPrefs.SetString ("lastname", split [3].Trim ());
				PlayerPrefs.SetString ("country", split [4].Trim ());
				PlayerPrefs.SetString ("username", split [5].Trim ());
				PlayerPrefs.SetInt ("age", int.Parse (split [6].Trim ()));
				PlayerPrefs.SetString ("email", split [7].Trim ());

				// After Login do what you want ex. load new scene ...
				Application.LoadLevel (1);


			} else if (split [0].Trim () == "2") {
				PlayerPrefs.SetString ("TempUser", userName.text.Trim ());
				GetLoginCanvas.ToggleCanvas ("active");
			} else {
				WarningMsg.text = split [0];


			}
		} else if (checkBanned.text.Trim () == "2") {

			WarningMsg.text = "BANNED";

		} else if (checkBanned.text.Trim () == "3") {
			WarningMsg.text = "Please Fill All Field";
		}

		else {
			WarningMsg.text = checkBanned.text;
		}

	}
}

