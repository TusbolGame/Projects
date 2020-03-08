using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Threading;
using AsyncIO;
using NetMQ;
using NetMQ.Sockets;
using UnityEngine;
using UnityEngine.UI;

public class ResultFadeControl : MonoBehaviour
{
	
	public Animator animator;
	public static string gameName = "Miner Game";
	private string userName = "HaoDa";
	private Thread thread;
	private string start_emotion = "angry";
	private string emotion = "happy";
	private DateTime _dateTime;
	private int rightCount = 10;
	private int wrongCount = 10;

	private GameObject bar1;
	private GameObject bar2;
	private GameObject bar3;
	private GameObject bar4;
	private GameObject bar5;
	private GameObject bar6;
	
	private GameObject date1;
	private GameObject date2;
	private GameObject date3;

	private GameObject _gameName;

	private IEnumerable<score_table> data;
	
	[DllImport("kernel32.dll", CharSet = CharSet.Auto)]
	public static extern void OutputDebugString(string message);
	private void Start()
	{
		Time.timeScale = 1;
		bar1 = GameObject.Find("Bar1");
		bar2 = GameObject.Find("Bar2");
		bar3 = GameObject.Find("Bar3");
		bar4 = GameObject.Find("Bar4");
		bar5 = GameObject.Find("Bar5");
		bar6 = GameObject.Find("Bar6");

		date1 = GameObject.Find("Date_1");
		date1.GetComponent<Text>().text = "";
		date2 = GameObject.Find("Date_2");
		date2.GetComponent<Text>().text = "";
		date3 = GameObject.Find("Date_3");
		date3.GetComponent<Text>().text = "";

		_gameName = GameObject.Find("xAxis");
		_gameName.GetComponent<Text>().text = "";

		bar1.GetComponent<Slider>().value = 0;
		bar1.transform.Find("ValueText").GetComponent<Text>().enabled = false;
		bar2.GetComponent<Slider>().value = 0;
		bar2.transform.Find("ValueText").GetComponent<Text>().enabled = false;
		bar3.GetComponent<Slider>().value = 0;
		bar3.transform.Find("ValueText").GetComponent<Text>().enabled = false;
		bar4.GetComponent<Slider>().value = 0;
		bar4.transform.Find("ValueText").GetComponent<Text>().enabled = false;
		bar5.GetComponent<Slider>().value = 0;
		bar5.transform.Find("ValueText").GetComponent<Text>().enabled = false;
		bar6.GetComponent<Slider>().value = 0;
		bar6.transform.Find("ValueText").GetComponent<Text>().enabled = false;

		int level = ApplicationManager.level;
		if (level == 2)
		{
			gameName = "Miner Game";
			rightCount = ManControll.rightAnswer;
			wrongCount = ManControll.wrongAnswer;
		}
		else if (level == 3)
		{
			gameName = "Picture Game";
			rightCount = ImagePanelControl.rightAnswer;
			wrongCount = ImagePanelControl.wrongAnswer;
		}
		else if (level == 4)
		{
			gameName = "Color Game";
			rightCount = ColorModelManage.rightAnswer;
			wrongCount = ColorModelManage.wrongAnswer;
		}

		userName = LoginSystem.username;
		start_emotion = ProgressBar.emotion;
		thread = new Thread(new ThreadStart(() => getEmotion("start")) );
		thread.Start();
		_dateTime = DateTime.Now;

		Time.timeScale = 0;
	}

	public void backButton_press()
	{
		Application.LoadLevel(1);
	}

	public void restartGame()
	{
		Application.LoadLevel(ApplicationManager.level);
	}

	public void PauseGame()
	{
		GameObject.Find("Fade_Background").transform.parent.GetComponent<Canvas>().enabled = true;
		GameObject.Find("Fade_Background").GetComponent<Animator>().SetTrigger("FadeOut");
		GameObject.Find("MainMenu").GetComponent<Animation>().Play("menu_open");
	}
	
	
	void Update()
	{
		if (data == null)
		{
			return;
		}
		Time.timeScale = 1;
		if (data.Count() >= 3)
		{
			IEnumerable<score_table> dispData = data.Skip(Math.Max(0, data.Count() - 3));
			List<score_table> dataList = dispData.ToList();
			bar1.GetComponent<Slider>().value = dataList[0].right * 5;
			bar1.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar1.transform.Find("ValueText").GetComponent<Text>().text = dataList[0].right.ToString();
			
			List<string> datetime = dataList[0].timestamp.Split(' ').ToList();
			date1.GetComponent<Text>().text = datetime[0] + "\n" + datetime[1] + "\n" + dataList[0].start_sentiment + "--" + dataList[0].end_sentiment;
			
			bar2.GetComponent<Slider>().value = dataList[0].wrong * 5;
			bar2.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar2.transform.Find("ValueText").GetComponent<Text>().text = dataList[0].wrong.ToString();
			
			
			bar3.GetComponent<Slider>().value = dataList[1].right * 5;
			bar3.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar3.transform.Find("ValueText").GetComponent<Text>().text = dataList[1].right.ToString();
			
			datetime = dataList[1].timestamp.Split(' ').ToList();
			date2.GetComponent<Text>().text = datetime[0] + "\n" + datetime[1] + "\n" + dataList[1].start_sentiment + "--" + dataList[1].end_sentiment;
			
			bar4.GetComponent<Slider>().value = dataList[1].wrong * 5;
			bar4.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar4.transform.Find("ValueText").GetComponent<Text>().text = dataList[1].wrong.ToString();
			
			bar5.GetComponent<Slider>().value = dataList[2].right * 5;
			bar5.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar5.transform.Find("ValueText").GetComponent<Text>().text = dataList[2].right.ToString();
			
			datetime = dataList[2].timestamp.Split(' ').ToList();
			date3.GetComponent<Text>().text = datetime[0] + "\n" + datetime[1] + "\n" + dataList[2].start_sentiment + "--" + dataList[2].end_sentiment;
			
			bar6.GetComponent<Slider>().value = dataList[2].wrong * 5;
			bar6.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar6.transform.Find("ValueText").GetComponent<Text>().text = dataList[2].wrong.ToString();
		}
		else if (data.Count() == 2)
		{
			IEnumerable<score_table> dispData = data.Skip(Math.Max(0, data.Count() - 2));
			List<score_table> dataList = dispData.ToList();
			
			bar1.GetComponent<Slider>().value = dataList[0].right * 5;
			bar1.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar1.transform.Find("ValueText").GetComponent<Text>().text = dataList[0].right.ToString();
			
			List<string> datetime = dataList[0].timestamp.Split(' ').ToList();
			date1.GetComponent<Text>().text = datetime[0] + "\n" + datetime[1] + "\n" + dataList[0].start_sentiment + "--" + dataList[0].end_sentiment;
			
			bar2.GetComponent<Slider>().value = dataList[0].wrong * 5;
			bar2.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar2.transform.Find("ValueText").GetComponent<Text>().text = dataList[0].wrong.ToString();
			
			bar5.GetComponent<Slider>().value = dataList[1].right * 5;
			bar5.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar5.transform.Find("ValueText").GetComponent<Text>().text = dataList[1].right.ToString();
			
			datetime = dataList[1].timestamp.Split(' ').ToList();
			date3.GetComponent<Text>().text = datetime[0] + "\n" + datetime[1] + "\n" + dataList[1].start_sentiment + "--" + dataList[1].end_sentiment;
			
			bar6.GetComponent<Slider>().value = dataList[1].wrong * 5;
			bar6.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar6.transform.Find("ValueText").GetComponent<Text>().text = dataList[1].wrong.ToString();
		}
		else if (data.Count() == 1)
		{
			List<score_table> dataList = data.ToList();
			
			bar3.GetComponent<Slider>().value = dataList[0].right * 5;
			bar3.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar3.transform.Find("ValueText").GetComponent<Text>().text = dataList[0].right.ToString();
			
			List<string> datetime = dataList[0].timestamp.Split(' ').ToList();
			date2.GetComponent<Text>().text = datetime[0] + "\n" + datetime[1] + "\n" + dataList[0].start_sentiment + "--" + dataList[0].end_sentiment;

			bar4.GetComponent<Slider>().value = dataList[0].wrong * 5;
			bar4.transform.Find("ValueText").GetComponent<Text>().enabled = true;
			bar4.transform.Find("ValueText").GetComponent<Text>().text = dataList[0].wrong.ToString();
		}

		_gameName.GetComponent<Text>().text = gameName;
	}

	public void getEmotion(string msg)
	{
		ForceDotNet.Force(); // this line is needed to prevent unity freeze after one use, not sure why yet
        
		using (RequestSocket client = new RequestSocket())
		{
			client.Connect("tcp://127.0.0.1:5555");

			client.SendFrame(msg);
                
			string message = null;
			bool gotMessage = false;
			while (true)
			{
				gotMessage = client.TryReceiveFrameString(out message); // this returns true if it's successful
				if (gotMessage) break;
			}

			if (gotMessage)
			{
				Debug.Log("Received " + message);
				emotion = message;
				Debug.Log("Emotion: " + emotion);
				
				OutputDebugString("username:" + userName);
				OutputDebugString("gameName:" + gameName);
				OutputDebugString("startEmotion:" + start_emotion);
				OutputDebugString("endEmotion:" + emotion);
				OutputDebugString("rightcount:" + rightCount.ToString());
				OutputDebugString("wrongcount:" + wrongCount.ToString());
				
				var ds = new DataService ("GameDatabase.db");
				ds.scoreStore(userName, gameName, start_emotion, emotion, rightCount, wrongCount, _dateTime.ToString("yyyy-MM-dd hh:mm:ss"));
				
				
				OutputDebugString("insertResult:" + "Success");
				
				ds = new DataService ("GameDatabase.db");
				data = ds.GetUserRecords(userName, gameName);
			}
            
			client.Disconnect("tcp://127.0.0.1:5555");
			client.Close();
		}
		NetMQConfig.Cleanup();
		thread.Join();
	}
	
	public void FadeOut(int levelIndex)
	{
		animator.SetTrigger("FadeOut");
	}

	public void OnFadeInComplete()
	{

	}

	public void OnFadeComplete()
	{
		
	}
	
}
