using System.Collections;
using System.Collections.Generic;
using System.Net.Mime;
using System.Xml.Serialization;
using UnityEngine;
using UnityEngine.UI;

public class ManControll : MonoBehaviour {

	// Use this for initialization
	private GameObject one_man;
	private GameObject two_man;
	private GameObject three_man;
	private GameObject four_man;
	private GameObject five_man;
	private GameObject six_man;
	private GameObject seven_man;
	private GameObject eight_man;
	private GameObject nine_man;

	private int trueCount = 0;
	private int totalCount = 20;

	private bool isShowing = false;
	
	private GameObject[] mans;
	private Vector3[] positions;
	private Vector3[] rotations;

	private Animation animation;
	
	private int gameLevel = 1;

	private int currentNumber = 0;

	private Button backButton;
	public bool gameStart = false;

	private int answer;
	private int trueAnswer;
	public static int rightAnswer = 0;
	public static int wrongAnswer = 0;

	private int answer_count = 0;
	
	private void Start() {
		
		Time.timeScale = 1;
		GameObject.Find("Fade_Background").GetComponent<Animator>().enabled = false;
		
		mans = new GameObject[9];
		
		one_man = GameObject.Find("Man_1");
		two_man = GameObject.Find("Man_2");
		three_man = GameObject.Find("Man_3");
		four_man = GameObject.Find("Man_4");
		five_man = GameObject.Find("Man_5");
		six_man = GameObject.Find("Man_6");
		mans[0] = one_man;
		mans[1] = two_man;
		mans[2] = three_man;
		mans[3] = four_man;
		mans[4] = five_man;
		mans[5] = six_man;
		gameLevel = ProgressBar.difficulty;
		GameObject.Find("Name").GetComponent<Text>().text = LoginSystem.username;
		GameObject.Find("Right").GetComponent<Text>().text = "Right : " + rightAnswer.ToString();
		GameObject.Find("Wrong").GetComponent<Text>().text = "Wrong : " + wrongAnswer.ToString();
		
		int holeNumber = Random.Range(0, 6);
		currentNumber = holeNumber;
		trueAnswer = holeNumber + 1;
		
	}

	public void CheckAnswer(){
		
		string str_answer = GameObject.Find("UIProgressBar").GetComponent<ProgressBarCircle>().getResultAnswer();
		
		if (str_answer == "ONE")
		{
			answer = 1;
		}
		else if (str_answer == "TWO")
		{
			answer = 2;
		}
		else if (str_answer == "THREE")
		{
			answer = 3;
		}
		else if (str_answer == "FOUR")
		{
			answer = 4;
		}
		else if (str_answer == "FIVE")
		{
			answer = 5;
		}
		else if (str_answer == "SIX")
		{
			answer = 6;
		}
		
		if (trueAnswer == answer)
		{
			rightAnswer++;
			GameObject.Find("Right").GetComponent<Text>().text = "Right : " + rightAnswer.ToString();
			answer_count++;
		}
		else
		{
			wrongAnswer++;
			GameObject.Find("Wrong").GetComponent<Text>().text = "Wrong : " + wrongAnswer.ToString();
			answer_count++;
		}
		answer = 0;
		if (answer_count == 20)
		{
			Application.LoadLevel(5);
		}
		int holeNumber = Random.Range(0, 6);
		currentNumber = holeNumber;
		trueAnswer = holeNumber + 1;
		GameObject.Find("UIProgressBar").GetComponent<ProgressBarCircle>().PlayAnimation(1);
	}

	private void OnGUI()
	{
		
		Event e = Event.current;
		
		if (e.isKey && e.keyCode == KeyCode.Escape)
		{
			Application.LoadLevel(1);
		}
		if (e.isKey && e.keyCode == KeyCode.Alpha1)
		{
			answer = 1;
		}
		else if (e.isKey && e.keyCode == KeyCode.Alpha2)
		{
			answer = 2;
		}
		else if (e.isKey && e.keyCode == KeyCode.Alpha3)
		{
			answer = 3;
		}
		else if (e.isKey && e.keyCode == KeyCode.Alpha4)
		{
			answer = 4;
		}
		else if (e.isKey && e.keyCode == KeyCode.Alpha5)
		{
			answer = 5;
		}
		else if (e.isKey && e.keyCode == KeyCode.Alpha6)
		{
			answer = 6;
		}
		else if(e.isKey)
		{
			answer = 0;	
		}
	}
	public void backButton_press()
	{
		Application.LoadLevel(1);
	}

	public bool getShowing()
	{
		return isShowing;
	}

	public void setShowing(bool showing)
	{
		this.isShowing = showing;
	}
	 

	public Animation currentAnimation()
	{
		return animation;
	}

	public int currentHoleNumber()
	{
		return currentNumber;
	}

	public int getLevel()
	{
		return gameLevel;
	}

	private void OnDestroy()
	{
		//ApplicationManager.miner_python.Close();
	}
}
