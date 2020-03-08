using System;
using UnityEngine;
using System.Collections;
using System.Diagnostics;
using System.IO;
using Debug = UnityEngine.Debug;

public class ApplicationManager : MonoBehaviour
{
	private GameObject Fade_Background;

	public static int level = 1;

	private GameObject loadingBar;
	public static ProcessStartInfo miner_pythonInfo;
	public static Process miner_python;
	
	public static ProcessStartInfo picture_pythonInfo;
	public static Process picture_python;
	
	public static ProcessStartInfo color_pythonInfo;
	public static Process color_python;
	
	private void Start()
	{
		Time.timeScale = 1;
		Fade_Background = GameObject.Find("Fade_Background");
		loadingBar = GameObject.Find("LoadingProgressBar");
		loadingBar.SetActive(false);
		miner_pythonInfo = new ProcessStartInfo();
		picture_pythonInfo = new ProcessStartInfo();
		color_pythonInfo = new ProcessStartInfo();
	}

	public void HoleGame()
	{
		
		miner_pythonInfo.FileName = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "miner_sr", @"miner_sr.exe");
		miner_pythonInfo.CreateNoWindow = true;
		miner_pythonInfo.UseShellExecute = true;
		
		level = 2;
		loadingBar.SetActive(true);
		LevelLoad();
	}

	public void PictureGame()
	{
		picture_pythonInfo.FileName = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "picture_sr", @"picture_sr.exe");
		picture_pythonInfo.CreateNoWindow = true;
		picture_pythonInfo.UseShellExecute = true;
		
		level = 3;
		loadingBar.SetActive(true);
		LevelLoad();
	}

	public void ColorGame()
	{
		color_pythonInfo.FileName = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "color_sr", @"color_sr.exe");
		color_pythonInfo.CreateNoWindow = true;
		color_pythonInfo.UseShellExecute = true;
		
		level = 4;
		loadingBar.SetActive(true);
		LevelLoad();
	}

	public void Quit () 
	{
		Fade_Background.GetComponent<Animator>().SetTrigger("FadeOut");
		
		#if UNITY_EDITOR
		UnityEditor.EditorApplication.isPlaying = false;
		#else
		Application.Quit();
		#endif
	}

	public void LevelLoad()
	{
		Debug.Log("Load Game");
		GameObject.Find("LoadingProgressBar").GetComponent<ProgressBar>().LoadingGame(level);
	}

	private void OnGUI()
	{
		Event e = Event.current;
		if (e.isKey && e.keyCode == KeyCode.Escape)
		{
			Application.LoadLevel(1);
		}
	}
}
