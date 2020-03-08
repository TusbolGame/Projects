using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;
using Random = UnityEngine.Random;

public class ImagePanelControl : MonoBehaviour {

	// Use this for initialization
	public int[] dir_numbers;
	public int[] trueList;
	public int[] falseList;
	public String[] answer;
	public int level;
	
	private String _answer;
	public String trueAnswer;
	public static int rightAnswer = 0;
	public static int wrongAnswer = 0;
	private int answer_count = 0;
	
	void Start () {
		
		Time.timeScale = 1;
		GameObject.Find("Fade_Background").GetComponent<Animator>().enabled = false;
		GameObject.Find("UIProgressBar").GetComponent<Animator>().enabled = false;
		
		dir_numbers = new int[20];
		trueList = new int[20];
		falseList = new int[20];
		answer = new string[20];
		List<int> numbers = new List<int>() { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };

		for (int i = 0; i < 20; i++)
		{
			int index = Random.Range(0, numbers.Count - 1);
			dir_numbers[i] = numbers[index];
			numbers.RemoveAt(index);
			int trueNumber = Random.Range(1, 3);
			int falseNumber = Random.Range(1, 3);
			trueList[i] = trueNumber;
			falseList[i] = falseNumber;
			if (trueNumber == falseNumber)
			{
				answer[i] = "yes";
			}
			else
			{
				answer[i] = "no";
			}
		}

		level = ProgressBar.difficulty;
		GameObject.Find("Name").GetComponent<Text>().text = LoginSystem.username;
	}
	public void backButton_press()
	{
		Application.LoadLevel(1);
	}
	
	public void CheckAnswer()
	{
		
		string str_answer = GameObject.Find("UIProgressBar").GetComponent<ProgressBarCircle>().getResultAnswer();
		if (str_answer == "YES")
		{
			_answer = "yes";
		}
		else if (str_answer == "NO")
		{
			_answer = "no";
		}
		
		if (trueAnswer == _answer)
		{
			rightAnswer++;
			GameObject.Find("Right").GetComponent<Text>().text = "Right : " + rightAnswer.ToString();
			answer_count++;
			//GameObject.Find("RightAnswer").GetComponent<Text>().enabled = true;
		}
		else
		{
			wrongAnswer++;
			GameObject.Find("Wrong").GetComponent<Text>().text = "Wrong : " + wrongAnswer.ToString();
			answer_count++;
			//GameObject.Find("WrongAnswer").GetComponent<Text>().enabled = true;
		}
		_answer = "";
		if (answer_count == 20)
		{
			Application.LoadLevel(5);
		}
	}
	
	private void OnGUI()
	{
		Event e = Event.current;
		if (e.isKey && e.keyCode == KeyCode.Escape)
		{
			Application.LoadLevel(1);
		}
		if (e.isKey && e.keyCode == KeyCode.Y)
		{
			_answer = "yes";
		}
		else if (e.isKey && e.keyCode == KeyCode.N)
		{
			_answer = "no";
		}
		else if (e.isKey)
		{
			_answer = "";
		}
	}
	private void OnDestroy()
	{
		//ApplicationManager.picture_python.Close();
	}
}
