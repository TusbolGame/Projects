using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

public class AnimationControl : MonoBehaviour {

	private bool isShowing = false;
	private ManControll control;
	void Start () {
		GameObject thePlayer = GameObject.Find("Main Camera");
		control = thePlayer.GetComponent<ManControll>();
		isShowing = control.getShowing();
	}
	
	public void upState()
	{
		int currentNumber = control.currentHoleNumber();
		Animation animation = control.currentAnimation();
		animation.Play("Man" + currentNumber.ToString() + "_1");
		control.setShowing(true);
	}

	public void downState()
	{
		return;
		int currentNumber = control.currentHoleNumber();
		GameObject.Find("UIProgressBar").GetComponent<ProgressBarCircle>().PlayAnimation(control.getLevel());
		StartCoroutine(change_Position(currentNumber));
		
	}

	public void downFinish()
	{
		newStart();
	}

	public void newStart()
	{
		control.setShowing(false);
	}

	IEnumerator  change_Position(int holeNumber)
	{
		int gameLevel = control.getLevel();
		yield return new WaitForSeconds((float)gameLevel * 2);
		Animation animation = this.GetComponent<Animation>();
		animation.Play("Man" + (control.currentHoleNumber()	 + 1).ToString() + "_2");
	}
}
