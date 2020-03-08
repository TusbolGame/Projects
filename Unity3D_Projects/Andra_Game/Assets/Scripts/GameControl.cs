using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameControl : MonoBehaviour
{

	private GameObject pausePanel;
	
	void Start()
	{
		pausePanel = GameObject.Find("MainMenu");
	}
	void Update()
	{
		if(Input.GetKeyDown (KeyCode.Escape)) 
		{
			if (!pausePanel.activeInHierarchy) 
			{
				PauseGame();
			}
			if (pausePanel.activeInHierarchy) 
			{
				ContinueGame();   
			}
		} 
	}

	public void GoMainMenu()
	{
		Time.timeScale = 1;
		Application.LoadLevel(0);
	}

	public void PauseGame()
	{
		GameObject.Find("Fade_Background").transform.parent.GetComponent<Canvas>().enabled = true;
		GameObject.Find("Fade_Background").GetComponent<Animator>().SetTrigger("FadeOut");
		pausePanel.GetComponent<Animation>().Play("menu_open");
	}

	public void MinegameStart()
	{
		Application.LoadLevel(2);
	}

	public void PicturegameStart()
	{
		Application.LoadLevel(3);
	}

	public void ColorgameStart()
	{
		Application.LoadLevel(4);
	}

	public void Pause()
	{
		Time.timeScale = 0;
	}

	public void ContinueGame()
	{
		Time.timeScale = 1;
		pausePanel.GetComponent<Animation>().Play("menu_close");
		GameObject.Find("Fade_Background").GetComponent<Animator>().SetTrigger("FadeIn");
	}

	public void Continue()
	{
		
	}

	public void GameQuit()
	{
		Debug.Log("Quit");
		Application.Quit();
	}
}
