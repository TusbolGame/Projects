using SQLite4Unity3d;

public class game_table  {

	[PrimaryKey, AutoIncrement]
	public int id { get; set; }
	[NotNull, Unique]
	public string gamename { get; set; }

	public override string ToString ()
	{
		return string.Format ("[Game: id={0}, gamename={1}", id, gamename);
	}
}
