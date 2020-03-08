using System.Collections;
using System.Collections.Generic;
using System.Threading;
using AsyncIO;
using NetMQ;
using NetMQ.Sockets;
using UnityEngine;
using UnityEngine.UI;


[ExecuteInEditMode]

public class ProgressBar : MonoBehaviour
{

    [Header("Title Setting")]
    public string Title;
    public Color TitleColor;
    public Font TitleFont;
    public int TitleFontSize = 10;

    [Header("Bar Setting")]
    public Color BarColor;   
    public Color BarBackGroundColor;
    public Sprite BarBackGroundSprite;
    [Range(1f, 100f)]
    public int Alert = 20;
    public Color BarAlertColor;

    [Header("Sound Alert")]
    public AudioClip sound;
    public bool repeat = false;
    public float RepeatRate = 1f;

    private Image bar, barBackground;
    private float nextPlay;
    private AudioSource audiosource;
    private Text txtTitle;
    private float barValue;
    public bool loadGame = false;

    private int gameLevel;
    private Thread thread;
    public static string emotion;
    public static int difficulty = 1;
    
    public float BarValue
    {
        get { return barValue; }

        set
        {
            value = Mathf.Clamp(value, 0, 100);
            barValue = 1f;
            UpdateValue(barValue);

        }
    }

        

    private void Awake()
    {
        bar = transform.Find("Bar").GetComponent<Image>();
        barBackground = GetComponent<Image>();
        txtTitle = transform.Find("Text").GetComponent<Text>();
        barBackground = transform.Find("BarBackground").GetComponent<Image>();
        audiosource = GetComponent<AudioSource>();
    }

    private void Start()
    {
        txtTitle.text = Title;
        txtTitle.color = TitleColor;
        txtTitle.font = TitleFont;
        txtTitle.fontSize = TitleFontSize;

        bar.color = BarColor;
        barBackground.color = BarBackGroundColor; 
        barBackground.sprite = BarBackGroundSprite;

        loadGame = false;
        
        UpdateValue(barValue);
        thread = new Thread(new ThreadStart(() => getEmotion("start")) );
        thread.Start();

        
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

                if (message == "angry")
                {
                    difficulty = 1;
                }
                else if (message == "disgust")
                {
                    difficulty = 1;
                }
                else if (message == "scared")
                {
                    difficulty = 1;
                }
                else if (message == "sad")
                {
                    difficulty = 1;
                }
                else if (message == "surprised")
                {
                    difficulty = 1;
                }
                else if (message == "happy")
                {
                    difficulty = 1;
                }
                
            }
            
            client.Disconnect("tcp://127.0.0.1:5555");
            client.Close();
        }
        NetMQConfig.Cleanup(); // this line is needed to prevent unity freeze after one use, not sure why yet
    }

    void UpdateValue(float val)
    {
        bar.fillAmount = val / 100;
        txtTitle.text = Title + " " + val + "%";

        if (val == 100f)
        {
            loadGame = true;
        }
    }

    public void LoadingGame(int level)
    {
        this.gameLevel = level;
        this.GetComponent<Animation>().Play();
    }

    
    public void LoadGame()
    {
        GameObject.Find("Fade_Background").transform.parent.GetComponent<Canvas>().enabled = true;
        GameObject.Find("Fade_Background").GetComponent<Animator>().SetTrigger("FadeOut");
        
    }

    public void LevelLoad()
    {
        thread.Join();
        Application.LoadLevel(gameLevel);
    }


    private void Update()
    {
        txtTitle.text = Title + " " + (int)(bar.fillAmount * 100) + "%";
    }

}
