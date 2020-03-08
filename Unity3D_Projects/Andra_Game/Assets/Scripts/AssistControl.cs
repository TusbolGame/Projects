using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AssistControl : MonoBehaviour {

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void startGame()
	{
		this.transform.parent.GetComponent<Canvas>().enabled = false;
		GameObject.Find("Fade_Background").GetComponent<Animator>().enabled = true;
	}
}
