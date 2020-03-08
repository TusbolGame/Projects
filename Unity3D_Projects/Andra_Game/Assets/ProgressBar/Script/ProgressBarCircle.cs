using System;
using UnityEngine;
using UnityEngine.UI;
using System.Threading;
using AsyncIO;
using NetMQ;
using NetMQ.Sockets;
using Unity.Jobs;

[ExecuteInEditMode]

public class ProgressBarCircle : MonoBehaviour {
    [Header("Title Setting")]
    public string Title;
    public Color TitleColor;
    public Font TitleFont;    

    [Header("Bar Setting")]
    public Color BarColor;
    public Color BarBackGroundColor;
    public Color MaskColor;
    public Sprite BarBackGroundSprite;
    [Range(1f, 100f)]
    public int Alert = 20;
    public Color BarAlertColor;

    [Header("Sound Alert")]
    public AudioClip sound;
    public bool repeat = false;
    public float RepearRate = 1f;

    private Image bar, barBackground,Mask;
    private float nextPlay;
    private AudioSource audiosource;
    private Text txtTitle;
    private float barValue;

    public float speed = 0f;

    public static float timeDelay;

    private Thread thread;
    private Thread miner_thread;
    private Thread picture_thread;
    private Thread color_thread;

    public static string emotion;
    public static string answer;
    
    private ManControll control;

    private int flag = 0;
    
    public float BarValue
    {
        get { return barValue; }

        set
        {
            value = Mathf.Clamp(value, 0, 100);
            barValue = 100;
            UpdateValue(barValue);

        }
    }

    private void Awake()
    {
        txtTitle = transform.Find("Text").GetComponent<Text>();
        barBackground = transform.Find("BarBackgroundCircle").GetComponent<Image>();
        bar = transform.Find("BarCircle").GetComponent<Image>();
        audiosource = GetComponent<AudioSource>();
        Mask= transform.Find("Mask").GetComponent<Image>();
    }

    private void Start()
    {
        txtTitle.text = Title;
        txtTitle.color = TitleColor;
        txtTitle.font = TitleFont;
       

        bar.color = BarColor;
        Mask.color = MaskColor;
        barBackground.color = BarBackGroundColor;
        barBackground.sprite = BarBackGroundSprite;
        GetComponent<Animator>().speed = 0f;
        
        GameObject thePlayer = GameObject.Find("Main Camera");
        control = thePlayer.GetComponent<ManControll>();

        UpdateValue(barValue);
    }

    void UpdateValue(float val)
    {
        bar.fillAmount = -(val / 100) + 1f;
        txtTitle.text = Title + " " + (100 - val).ToString() + "%";
    }


    public void AnimationComplete()
    {
        GetComponent<Animator>().speed = 0f;
        if (Application.loadedLevel == 2)
        {
            miner_thread.Join();
            GameObject.Find("Main Camera").GetComponent<ManControll>().CheckAnswer();
        }
        else if (Application.loadedLevel == 3)
        {
            picture_thread.Join();
            GameObject.Find("Main Camera").GetComponent<ImagePanelControl>().CheckAnswer();
            Debug.Log("Picture Joined~~~~~~~~~");
        }
        else if (Application.loadedLevel == 4)
        {
            color_thread.Join();
            GameObject.Find("Main Camera").GetComponent<ColorModelManage>().CheckAnswer();
        }

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
            }
            
            client.Disconnect("tcp://127.0.0.1:5555");
            client.Close();
        }
        NetMQConfig.Cleanup();
    }

    public void changeFlag()
    {
        flag = 1;
    }

    public void getAnswer(string msg, int port)
    {
        ForceDotNet.Force();
        flag = 0;
        using (RequestSocket client = new RequestSocket())
        {
            client.Connect("tcp://127.0.0.1:" + port.ToString());

            client.SendFrame(msg);
                
            string message = null;
            bool gotMessage = false;
            while (true)
            {
                gotMessage = client.TryReceiveFrameString(out message);
                
                if (gotMessage) break;
                
                if (flag == 1)
                {
                    message = "No Answer";
                    break;
                }
            }

            if (gotMessage)
            {
                Debug.Log("Received " + message);
                answer = message;
            }

            
            client.Disconnect("tcp://127.0.0.1:" + port.ToString());
            client.Close();
        }
        NetMQConfig.Cleanup();
    }

    public void sendControl(string msg, int port)
    {
        ForceDotNet.Force();
        
        using (RequestSocket client = new RequestSocket())
        {
            client.Connect("tcp://127.0.0.1:" + port.ToString());

            client.SendFrame(msg);
            client.Disconnect("tcp://127.0.0.1:" + port.ToString());
            client.Close();
        }
        NetMQConfig.Cleanup();
    }

    public string getResultAnswer()
    {
        if (answer == null || answer == "")
        {
            return "No Answer";
        }
        return answer;
    }


    public void startThread()
    {
    }

    public void stopThread()
    {
    }

    public void manUp()
    {
        if (Application.loadedLevel == 2)
        {
            int currentNumber = control.currentHoleNumber();
            Animation animation = GameObject.Find("Man_" + (currentNumber + 1).ToString()).GetComponent<Animation>();
            animation["Man" + (currentNumber + 1).ToString() + "_1"].speed = 3f;
            animation.Play("Man" + (currentNumber + 1).ToString() + "_1");    
        }

        
    }

    public void manDown()
    {
        if (Application.loadedLevel == 2)
        {
            int currentNumber = control.currentHoleNumber();
            Animation animation = GameObject.Find("Man_" + (currentNumber + 1).ToString()).GetComponent<Animation>();
            animation["Man" + (currentNumber + 1).ToString() + "_2"].speed = 3f;
            animation.Play("Man" + (currentNumber + 1).ToString() + "_2");    
        }

        
    }

    public void PlayAnimation(int level)
    {
        if (Application.loadedLevel == 2)
        {
            flag = 0;
            GetComponent<Animator>().speed = (3f - 0.3f) / (level * 2f) / 2f;
            
            timeDelay = 3f / ((3f - 0.3f) / (level * 2f) / 2f);
            Debug.Log("Emotion Start");
            miner_thread = new Thread(new ThreadStart(()=> getAnswer("start", 5556)));
            miner_thread.Start();
        }
        
        else if (Application.loadedLevel == 3)
        {
            flag = 0;
            GetComponent<Animator>().speed = 3f / (10f - level + 0.9f);
            Debug.Log("Start Sending~~~~~~~~~");
            picture_thread = new Thread(new ThreadStart(() => getAnswer("start", 5557)));
            picture_thread.Start();
        }
        else if (Application.loadedLevel == 4)
        {
            flag = 0;
            GetComponent<Animator>().speed = 3f / (5f - level + 0.9f);
            Debug.Log("Start Sending~~~~~~~~~");
            color_thread = new Thread(new ThreadStart(() => getAnswer("start", 5558)));
            color_thread.Start();
        }

    }
    
    private void Update()
    {
        UpdateValue((int)((1f - bar.fillAmount) * 100));
    }

}
