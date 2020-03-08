using System;
using SQLite4Unity3d;

public class score_table  {

	[PrimaryKey, AutoIncrement]
	public int id { get; set; }
	
	public string username { get; set; }
	
	public string gamename { get; set; }
	
	public string start_sentiment { get; set; }
	
	public string end_sentiment { get; set; }
	
	public int right { get; set; }
	
	public int wrong { get; set; }
	
	public string timestamp { get; set; }
	public override string ToString ()
	{
		return string.Format ("[Score: id={0}, username={1}, gamename={2} start_sentiment={3}, end_sentiment={4}, right={5}, wrong={6}, timestamp={7}", id, username, gamename, start_sentiment, end_sentiment, right, wrong, timestamp);
	}
}
