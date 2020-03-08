using DanielLochner.Assets.SimpleScrollSnap;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class FadeControl : MonoBehaviour
{

	public Animator animator;
	private int level;
	
	// Update is called once per frame
	void Update () {
		
	}

	public void FadeOut(int levelIndex)
	{
		level = levelIndex;
		animator.SetTrigger("FadeOut");
	}

	public void OnFadeInComplete()
	{
		transform.parent.GetComponent<Canvas>().enabled = false;
		if (Application.loadedLevel > 1)
		{
			GameObject.Find("BackButton").GetComponent<Image>().enabled = true;	
		}

		if (Application.loadedLevel == 2)
		{
			GameObject.Find("Main Camera").GetComponent<ManControll>().gameStart = true;
			GameObject.Find("UIProgressBar").GetComponent<Animator>().enabled = true;
			GameObject.Find("UIProgressBar").GetComponent<ProgressBarCircle>().PlayAnimation(1);
		}
		else if (Application.loadedLevel == 3)
		{
			GameObject.Find("TrueImage").GetComponent<SimpleScrollSnap>().gameStart = true;
			GameObject.Find("FalseImage").GetComponent<SimpleScrollSnap>().gameStart = true;
			GameObject.Find("UIProgressBar").GetComponent<Animator>().enabled = true;
			GameObject.Find("TrueImage").GetComponent<SimpleScrollSnap>().AnimationPlay();
		}
		else if (Application.loadedLevel == 4)
		{
			GameObject.Find("Main Camera").GetComponent<ColorModelManage>().gameStart = true;
			GameObject.Find("Main Camera").GetComponent<ColorModelManage>().startGame();
			GameObject.Find("UIProgressBar").GetComponent<Animator>().enabled = true;
		}
	}

	public void OnFadeComplete()
	{
		if (Application.loadedLevel == 1)
		{
			GameObject.Find("LoadingProgressBar").GetComponent<ProgressBar>().LevelLoad();	
		}
	}
}
