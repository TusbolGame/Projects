using System;
using System.Collections;
using System.Diagnostics;
using System.Net; 
using System.Net.Sockets; 
using System.Text; 
using System.Threading;
using UnityEngine;
using UnityEngine.UI;
using Debug = UnityEngine.Debug;

public class SocketServer : MonoBehaviour
{

	#region private members 		
	private TcpListener tcpListener; 	
	private Thread tcpListenerThread;  	
	private TcpClient connectedTcpClient; 	
	private GameObject progressBar;
	#endregion 	
	
	// Use this for initialization
	void Start () {
		// Start TcpServer background thread 		
		tcpListenerThread = new Thread (new ThreadStart(ListenForIncommingRequests)); 		
		tcpListenerThread.IsBackground = true; 		
		tcpListenerThread.Start();
		progressBar = GameObject.Find("LoadingProgressBar");
		
		ProcessStartInfo pythonInfo = new ProcessStartInfo();
		Process python;
		pythonInfo.FileName = @"real_time_video.exe";
		pythonInfo.CreateNoWindow = false;
		pythonInfo.UseShellExecute = true;

		Console.WriteLine("Python Starting");
		//python = Process.Start(pythonInfo);
		//python.WaitForExit();
		//python.Close();
	}

	
	
	// Update is called once per frame
	void Update () {
		if (Input.GetKeyDown(KeyCode.Space)) {             
			SendMessage();         
		}
		
	}
	private void ListenForIncommingRequests () { 		
		try { 			
			// Create listener on localhost port 8052. 			
			tcpListener = new TcpListener(IPAddress.Parse("127.0.0.1"), 8052); 			
			tcpListener.Start();              
			Debug.Log("Server is listening");              
			Byte[] bytes = new Byte[1024];  			
			while (true) { 				
				using (connectedTcpClient = tcpListener.AcceptTcpClient()) { 					
					// Get a stream object for reading 					
					using (NetworkStream stream = connectedTcpClient.GetStream()) { 						
						int length; 						
						// Read incomming stream into byte arrary. 						
						while ((length = stream.Read(bytes, 0, bytes.Length)) != 0) { 							
							var incommingData = new byte[length]; 							
							Array.Copy(bytes, 0, incommingData, 0, length);  							
							// Convert byte array to string message. 							
							string clientMessage = Encoding.ASCII.GetString(incommingData); 							
							Debug.Log("client message received as: " + clientMessage);
						} 					
					} 				
				} 			
			} 		
		} 		
		catch (SocketException socketException) { 			
			Debug.Log("SocketException " + socketException.ToString()); 		
		}     
	}  	private void SendMessage() { 		
		if (connectedTcpClient == null) {             
			return;         
		}  		
		
		try { 			
			// Get a stream object for writing. 			
			NetworkStream stream = connectedTcpClient.GetStream(); 			
			if (stream.CanWrite) {                 
				string serverMessage = "This is a message from your server."; 			
				// Convert string message to byte array.                 
				byte[] serverMessageAsByteArray = Encoding.ASCII.GetBytes(serverMessage); 				
				// Write byte array to socketConnection stream.               
				stream.Write(serverMessageAsByteArray, 0, serverMessageAsByteArray.Length);               
				Debug.Log("Server sent his message - should be received by client");           
			}       
		} 		
		catch (SocketException socketException) {             
			Debug.Log("Socket exception: " + socketException);         
		} 	
	} 
}
