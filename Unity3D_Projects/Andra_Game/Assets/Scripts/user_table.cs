using SQLite4Unity3d;

public class user_table  {

	[PrimaryKey, AutoIncrement]
	public int id { get; set; }
	[NotNull, Unique]
	public string username { get; set; }
	[NotNull]
	public string password { get; set; }
	[NotNull]
	public int age { get; set; }
	public override string ToString ()
	{
		return string.Format ("[User: id={0}, username={1}, password={2}, age={3}", id, username, password, age);
	}
}
