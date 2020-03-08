using System;
using SQLite4Unity3d;
using UnityEngine;
#if !UNITY_EDITOR
using System.Collections;
using System.IO;
#endif
using System.Collections.Generic;
using System.IO;
using System.Runtime.InteropServices;
using System.Security.Cryptography.X509Certificates;

public class DataService  {

	private SQLiteConnection _connection;
	
	[DllImport("kernel32.dll", CharSet = CharSet.Auto)]
	public static extern void OutputDebugString(string message);

	public DataService(string DatabaseName){

#if UNITY_EDITOR
            var dbPath = string.Format(@"Assets/StreamingAssets/{0}", DatabaseName);
#else
        // check if file exists in Application.persistentDataPath
        var filepath = string.Format("{0}/{1}", Application.persistentDataPath, DatabaseName);

        if (!File.Exists(filepath))
        {
            Debug.Log("Database not in Persistent path");
            // if it doesn't ->
            // open StreamingAssets directory and load the db ->

#if UNITY_ANDROID 
            var loadDb = new WWW("jar:file://" + Application.dataPath + "!/assets/" + DatabaseName);  // this is the path to your StreamingAssets in android
            while (!loadDb.isDone) { }  // CAREFUL here, for safety reasons you shouldn't let this while loop unattended, place a timer and error check
            // then save to Application.persistentDataPath
            File.WriteAllBytes(filepath, loadDb.bytes);
#elif UNITY_IOS
                 var loadDb = Application.dataPath + "/Raw/" + DatabaseName;  // this is the path to your StreamingAssets in iOS
                // then save to Application.persistentDataPath
                File.Copy(loadDb, filepath);
#elif UNITY_WP8
                var loadDb = Application.dataPath + "/StreamingAssets/" + DatabaseName;  // this is the path to your StreamingAssets in iOS
                // then save to Application.persistentDataPath
                File.Copy(loadDb, filepath);

#elif UNITY_WINRT
		var loadDb = Application.dataPath + "/StreamingAssets/" + DatabaseName;  // this is the path to your StreamingAssets in iOS
		// then save to Application.persistentDataPath
		File.Copy(loadDb, filepath);
		
#elif UNITY_STANDALONE_OSX
		var loadDb = Application.dataPath + "/Resources/Data/StreamingAssets/" + DatabaseName;  // this is the path to your StreamingAssets in iOS
		// then save to Application.persistentDataPath
		File.Copy(loadDb, filepath);
#else
	var loadDb = Application.dataPath + "/StreamingAssets/" + DatabaseName;  // this is the path to your StreamingAssets in iOS
	// then save to Application.persistentDataPath
	File.Copy(loadDb, filepath);

#endif

            Debug.Log("Database written");
        }

        var dbPath = filepath;
#endif
		//dbPath = Application.streamingAssetsPath + "/" + DatabaseName;
		OutputDebugString("database_path~~~:" + dbPath);
        _connection = new SQLiteConnection(dbPath, SQLiteOpenFlags.ReadWrite | SQLiteOpenFlags.Create);
        Debug.Log("Final PATH: " + dbPath);  
		
		OutputDebugString("database_path:" + dbPath);
		
		

	}

	public void CreateDB(){
		_connection.DropTable<Person> ();
		_connection.CreateTable<Person> ();

		_connection.InsertAll (new[]{
			new Person{
				Id = 1,
				Name = "Tom",
				Surname = "Perez",
				Age = 56
			},
			new Person{
				Id = 2,
				Name = "Fred",
				Surname = "Arthurson",
				Age = 16
			},
			new Person{
				Id = 3,
				Name = "John",
				Surname = "Doe",
				Age = 25
			},
			new Person{
				Id = 4,
				Name = "Roberto",
				Surname = "Huertas",
				Age = 37
			}
		});
	}

	public IEnumerable<Person> GetPersons(){
		return _connection.Table<Person>();
	}

	public IEnumerable<Person> GetPersonsNamedRoberto(){
		return _connection.Table<Person>().Where(x => x.Name == "Roberto");
	}

	public IEnumerable<score_table> GetUserRecords(string username, string gamename)
	{
		return _connection.Table<score_table>().Where(x => x.username == username).Where(x => x.gamename == gamename);
	}
	
	public Person GetJohnny(){
		return _connection.Table<Person>().Where(x => x.Name == "Johnny").FirstOrDefault();
	}

	public Person CreatePerson(){
		var p = new Person{
				Name = "Johnny",
				Surname = "Mnemonic",
				Age = 21
		};
		_connection.Insert (p);
		return p;
	}


	public user_table createUser(string username, string password, int age)
	{
		var user = new user_table
		{
			username = username,
			password = password,
			age = age
		};
		_connection.Insert (user);
		_connection.Close();
		return user;
	}

	public score_table scoreStore(string username, string gamename, string start_sentiment, string end_sentiment,
		int right, int wrong, string date_time)
	{
		var score = new score_table
		{
			username = username,
			gamename = gamename,
			start_sentiment = start_sentiment,
			end_sentiment = end_sentiment,
			right = right,
			wrong = wrong,
			timestamp = date_time
		};
		_connection.Insert(score);
		_connection.Close();
		return score;
	}

	public bool CheckUser(string username, string password)
	{
		if (_connection.Table<user_table>().Where(x => x.username == username).Where(x => x.password == password)
			    .Count() >= 1)
		{
			_connection.Close();
			return true;
		}
		_connection.Close();
		return false;
	}
}
