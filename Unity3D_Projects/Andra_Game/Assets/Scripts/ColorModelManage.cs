using System;
using System.Collections;
using System.Collections.Generic;
using System.Net.Mime;
using UnityEngine;
using UnityEngine.UI;
using Random = UnityEngine.Random;

public class ColorModelManage : MonoBehaviour
{

	private int item_count = 20;
	private GameObject[] colorObjects;
	private string[] objNames = {"Blue", "Brown", "Green", "Orange", "Pink", "Red", "Yellow"};
	private int radius = 170;
	private float step;
	private GameObject[] fires;
	private string[] answers;
	private int index = 0;
	public bool gameStart = false;

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
		
		colorObjects = new GameObject[item_count];
		answers = new string[item_count];
		step = 2f * Mathf.PI/ (float)(item_count);
		for (int i = 0; i < item_count; i++)
		{
			string objName = objNames[Random.Range(0, 7)];
			colorObjects[i] = Instantiate(Resources.Load(objName, typeof(GameObject))) as GameObject;
			colorObjects[i].transform.position = new Vector3( radius * Mathf.Sin(step * i), 0f, radius * Mathf.Cos(step * i) );
			colorObjects[i].transform.eulerAngles = new Vector3(-14f, 180f + 360f / item_count * i - 20f, 0f);
			colorObjects[i].transform.localScale = new Vector3(100f, 100f, 100f);
			string textureName = objNames[Random.Range(0, 7)];
			Renderer renderer = colorObjects[i].transform.GetChild(1).GetComponent<Renderer>();
			renderer.material.mainTexture = Resources.Load("Images/Colors/" + textureName) as Texture;
			answers[i] = textureName;
		}
		Debug.Log(answers[index]);

		level = ProgressBar.difficulty;
		GameObject.Find("Name").GetComponent<Text>().text = LoginSystem.username;
	}

	public void startGame()
	{
		GameObject.Find("UIProgressBar").GetComponent<ProgressBarCircle>().PlayAnimation(level);
		//InvokeRepeating("ChangeRotation", 6f - level + 1f, 6f - level + 1f);
	}
	
	public void CheckAnswer()
	{
		
		string str_answer = GameObject.Find("UIProgressBar").GetComponent<ProgressBarCircle>().getResultAnswer();
		if (str_answer == "RED")
		{
			_answer = "Red";
		}
		else if (str_answer == "BLUE")
		{
			_answer = "Blue";
		}
		else if (str_answer == "GREEN")
		{
			_answer = "Green";
		}
		else if (str_answer == "YELLOW")
		{
			_answer = "Yellow";
		}
		else if (str_answer == "PINK")
		{
			_answer = "Pink";
		}
		else if (str_answer == "BROWN")
		{
			_answer = "Brown";
		}
		else if (str_answer == "ORANGE")
		{
			_answer = "Orange";
		}
		
		if(answers[index] == _answer)
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
		_answer = "";
		ChangeRotation();
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
		
		if (e.isKey && e.keyCode == KeyCode.R)
		{
			_answer = "Red";
		}
		else if (e.isKey && e.keyCode == KeyCode.G)
		{
			_answer = "Green";
		}
		else if (e.isKey && e.keyCode == KeyCode.B)
		{
			_answer = "Blue";
		}
		else if (e.isKey && e.keyCode == KeyCode.P)
		{
			_answer = "Pink";
		}
		else if (e.isKey && e.keyCode == KeyCode.O)
		{
			_answer = "Orange";
		}
		else if (e.isKey && e.keyCode == KeyCode.Y)
		{
			_answer = "Yellow";
		}
		else if (e.isKey && e.keyCode == KeyCode.V)
		{
			_answer = "Brown";
		}
		else if (e.isKey)
		{
			_answer = "";
		}
	}

	public void backButton_press()
	{
		Application.LoadLevel(1);
	}
	public void ChangeRotation()
	{
		GameObject.Find("UIProgressBar").GetComponent<ProgressBarCircle>().PlayAnimation(level);
		if (!gameStart)
		{
			return;
		}
		if (index == item_count)
		{
			return;
		}
		Debug.Log(answers[index]);
		index++;
		
		Camera.main.transform.eulerAngles = new Vector3(0f, Camera.main.transform.eulerAngles.y + 18f, 0f);
		float t = Mathf.PingPong(Time.time, 3f) / 3f;
		Camera.main.backgroundColor = Color.Lerp(Color.red, Color.green, t);
	}
	
	private void OnDestroy()
	{
		//ApplicationManager.color_python.Close();
	}
	
	
}
