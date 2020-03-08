using SQLite4Unity3d;

public class sentiment_table  {

	[PrimaryKey, AutoIncrement]
	public int id { get; set; }
	[NotNull, Unique]
	public string sentiment { get; set; }

	public override string ToString ()
	{
		return string.Format ("[Sentiment: id={0}, sentiment={1}", id, sentiment);
	}
}
