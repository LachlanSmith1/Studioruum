/*

	OnlineSync.java
	-----------------

	This is a Basic Class to Test the Functionality of the Online Datbase

	It Will Initially Just Have Read / Write / Edit Capabilities

	It Will Be Expanded to Sync With the Offline Database

	*** USE THE COMMAND BELOW TO RUN THE CLASS WHEN COMPILED ***

	java -cp ../lib/mysql-connector-java-8.0.19.jar;. OnlineSync

	*** USE THE COMMAND ABOVE TO RUN THE CLASS WHEN COMPILED ***

*/

package gui;

//Import Statements For JDBC and Etc.
import jdk.jfr.Label;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;

public class OnlineSync
{

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	//Acts as the Main Method of the Program
	public Connection Connect() {

		//Establishes a Connection
		Connection studConnect = null;

		//The Format of the Host Name is the JDCB Specifier, Then the Address to Connect, Before the Database Name
		String host = "jdbc:mysql://studioruum.c5iijqup9ms0.us-east-1.rds.amazonaws.com/studioruumOnline";

		//Default Master Username and Password From AWS
		String user = "group40";
		String password = "zitozito";

		//Attempting to Connect
		try
		{

			studConnect = DriverManager.getConnection(host, user, password);

			if (studConnect != null) {

				System.out.println("Connected to " + host + ".");

				//TEST FUNCTIONS BELOW - UNCOMMENT AS NECCESSARY

				OnlineSync syncObj = new OnlineSync();
				String tableName = "users";

				//syncObj.downloadUsers(studConnect);

				//syncObj.uploadUsers(studConnect);

			}

		}
		catch (SQLException ex)
		{

			System.out.println("An Error Occured When Connecting to the Database.");
			ex.printStackTrace();

		}

		return studConnect;

	}

	//Close The Connection When Finished
	public void Disconnect(Connection studConnect)
	{

		if (studConnect != null)
		{

			try
			{

				studConnect.close();

			}
			catch (SQLException ex)
			{

				ex.printStackTrace();

			}

		}

	}

	//////////////////////////
	/*



		PASSWORDS



	*/
	//////////////////////////

	public static String bytesToStringHex(byte[] bytes)
	{

		char[] hexChars = new char[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++)
		{

			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];

		}

		return new String(hexChars);

	}

	public byte[] generateSalt()
	{

		byte[] salt=new byte[32];
		SecureRandom rand = new SecureRandom();
		rand.nextBytes(salt);
		return salt;

	}

	public byte[] getSalt(Connection studConnect, String username)
	{

		PreparedStatement getSalt;
		byte[] salt=null;

		try
		{

			getSalt = studConnect.prepareStatement("SELECT salt FROM users WHERE username=?;");
			getSalt.setString(1, username);
			ResultSet rs = getSalt.executeQuery();
			rs.next();
			Blob blob = rs.getBlob(1);
			salt = blob.getBytes(1,(int)blob.length());
			getSalt.close();
			rs.close();

		}
		catch(SQLException ex)
		{

			System.out.println(ex);

		}

		return salt;

	}

	public String generateHash(byte[] salt, String password)throws NoSuchAlgorithmException
	{

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		digest.update(salt);
		byte[] hash = digest.digest(password.getBytes());
		String hashedPassword=bytesToStringHex(hash);
		return hashedPassword;
	}

	//////////////////////////
	/*



		USERS TABLE



	*/
	//////////////////////////

	public Boolean uploadUsers(Connection studConnect, String uname, String pword, byte[] salt)
	{

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime now;
		System.out.println("Attempting to Add a Value to the Table ~users~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			now=LocalDateTime.now();
			String username = uname;
			String email = "l.jones44@student.liverpool.ac.uk";
			String password = pword;
			String time_created = dtf.format(now).substring(10);
			String date = dtf.format(now).substring(0,10);
			PreparedStatement stmt = studConnect.prepareStatement("SELECT COUNT(username) FROM users WHERE username = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int unique = rs.getInt(1);
			rs.close();
			stmt.close();

			if(unique>0)
			{

				System.out.println("Username already exists");
				return false;

			}

			else
			{

				//Creating a Prepared Statement and Placing the Table Value In
				statement = studConnect.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?,?);");
				statement.setString(1, username);
				statement.setString(2, email);
				statement.setString(3, password);
				statement.setString(4, time_created);
				statement.setString(5, date);
				statement.setBytes(6, salt);

				statement.executeUpdate();
				statement.close();
				return true;

			}
		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);
			return false;

		}
	}

	public void downloadUsers(Connection studConnect)
	{

		System.out.println("Attempting to Show All Values in the Table ~users~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			//Creating a Prepared Statement
			statement = studConnect.prepareStatement("SELECT * FROM users;");

			//Gather the Results of the Select
			ResultSet rs = statement.executeQuery();

			//Print Out Each Result
			while(rs.next())
			{

				String username = rs.getString("username");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String time_created = rs.getString("time_created");
				String time = rs.getString("date");
				byte[] salt = rs.getBytes("date");

				System.out.println(username + "  /  " + email + "  /  " + password + "  /  " + time_created + "  /  " + time);

			}

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}

	}

	//////////////////////////
	/*



		SCHOLARS TABLE



	*/
	//////////////////////////

	public void uploadScholars(Connection studConnect)
	{

		System.out.println("Attempting to Add a Value to the Table ~scholars~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			String username = "slgjon13";
			int scholar_id = 1;

			//Creating a Prepared Statement and Placing the Table Value In
			statement = studConnect.prepareStatement("INSERT INTO scholar VALUES(?, ?);");
			statement.setInt(1, scholar_id);
			statement.setString(2, username);

			statement.executeUpdate();

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}

	}

	public void downloadScholars(Connection studConnect)
	{

		System.out.println("Attempting to Show All Values in the Table ~scholars~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			//Creating a Prepared Statement
			statement = studConnect.prepareStatement("SELECT * FROM users;");

			//Gather the Results of the Select
			ResultSet rs = statement.executeQuery();

			//Print Out Each Result
			while(rs.next())
			{

				int scholar_id = rs.getInt("scholar_id");
				String username = rs.getString("username");

				System.out.println(Integer.toString(scholar_id) + "  /  " + username);

			}

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}

	}

	public Boolean login(Connection studConnect, String username, String password)
	{

		PreparedStatement statement = null;

		try
		{

			//Creating a Prepared Statement
			statement = studConnect.prepareStatement("SELECT password FROM users WHERE username = ?;");
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			rs.next();

			String realPass = rs.getString(1);
			rs.close();
			statement.close();

			if(realPass.equals(password))
			{

				return true;

			}
			else
			{

				return false;

			}

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);
			return false;

		}
	}

	//////////////////////////
	/*



		EDUCATORS TABLE



	*/
	//////////////////////////

	public void uploadEducators(Connection studConnect)
	{

		System.out.println("Attempting to Add a Value to the Table ~educators~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			String username = "slgjon13";
			int educator_id = 1;

			//Creating a Prepared Statement and Placing the Table Value In
			statement = studConnect.prepareStatement("INSERT INTO educator VALUES(?, ?);");
			statement.setInt(1, educator_id);
			statement.setString(2, username);

			statement.executeUpdate();

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}

	}

	public void downloadEducators(Connection studConnect)
	{

		System.out.println("Attempting to Show All Values in the Table ~educators~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			//Creating a Prepared Statement
			statement = studConnect.prepareStatement("SELECT * FROM users;");

			//Gather the Results of the Select
			ResultSet rs = statement.executeQuery();

			//Print Out Each Result
			while(rs.next())
			{

				int educator_id = rs.getInt("educator_id");
				String username = rs.getString("username");

				System.out.println(Integer.toString(educator_id) + "  /  " + username);

			}

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}

	}

	//////////////////////////
	/*



		CLASSRUUMS TABLE



	*/
	//////////////////////////

	public void uploadClassruums(Connection studConnect, String class_name) throws SQLException {
		System.out.println("Attempting to Add a Value to the Table ~classruums~.");

		//The Format of the Host Name is the JDCB Specifier, Then the Address to Connect, Before the Database Name
		String host = "jdbc:mysql://studioruum.c5iijqup9ms0.us-east-1.rds.amazonaws.com/studioruumOnline";

		//Default Master Username and Password From AWS
		String user = "group40";
		String password = "zitozito";

		PreparedStatement statement = null;

		Connection connection = DriverManager.getConnection(host, user, password);
		try
		{
			//Creating a Prepared Statement and Placing the Table Value In
			statement = connection.prepareStatement("INSERT INTO classruums VALUES(?, ?, ?);");
			//	statement.setInt(1, class_id); //HOW DO I GET ID
			// statement.setInt(2, educator_id); //HOW DO I GET ID
			statement.setString(3, class_name);
			statement.executeUpdate();

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}

	}

	public void downloadClassruums(Connection studConnect)
	{
		System.out.println("Attempting to Show All Values in the Table ~classruums~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			//Creating a Prepared Statement
			statement = studConnect.prepareStatement("SELECT * FROM classruums;");

			//Gather the Results of the Select
			ResultSet rs = statement.executeQuery();

			//Print Out Each Result
			while(rs.next())
			{

				int class_id = rs.getInt("class_id");
				int educator_id = rs.getInt("educator_id");
				String class_name = rs.getString("class_name");

				System.out.println(Integer.toString(class_id) + Integer.toString(educator_id) + class_name);

			}

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

				statement = null;


			}

		}
	}

	public void uploadClassruumScholars(Connection studConnect)
	{
		System.out.println("Attempting to Add a Value to the Table ~classruum_scholars~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			int class_id = 1;
			int scholar_id = 1;

			//Creating a Prepared Statement and Placing the Table Value In
			statement = studConnect.prepareStatement("INSERT INTO classruum_scholars VALUES(?, ?);");
			statement.setInt(1, class_id);
			statement.setInt(2, scholar_id);

			statement.executeUpdate();

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}
	}

	public void downloadClassruumScholars(Connection studConnect)
	{
		System.out.println("Attempting to Show All Values in the Table ~classruums_scholars~.");

		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		try
		{

			//Creating a Prepared Statement
			statement = studConnect.prepareStatement("SELECT * FROM classruums_scholars;");

			//Gather the Results of the Select
			ResultSet rs = statement.executeQuery();

			//Print Out Each Result
			while(rs.next())
			{

				int class_id = rs.getInt("class_id");
				int scholar_id = rs.getInt("educator_id");

				System.out.println(Integer.toString(class_id) + Integer.toString(scholar_id));

			}

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

				statement = null;


			}

		}
	}

	//////////////////////////
	/*



		FORUUMS TABLE



	*/
	//////////////////////////

	public void uploadForuums(Integer forum_id, Integer class_id, String forum_title) throws SQLException {
		System.out.println("Attempting to Add a Value to the Table ~foruums~.");

		//The Format of the Host Name is the JDCB Specifier, Then the Address to Connect, Before the Database Name
		String host = "jdbc:mysql://studioruum.c5iijqup9ms0.us-east-1.rds.amazonaws.com/studioruumOnline";

		//Default Master Username and Password From AWS
		String user = "group40";
		String password = "zitozito";


		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		Connection connection = DriverManager.getConnection(host, user, password);

		try
		{

			//Creating a Prepared Statement and Placing the Table Value In
			statement = connection.prepareStatement("INSERT INTO classruum_scholars VALUES(?, ?, ?);");
			statement.setInt(1, forum_id);
			statement.setInt(2, class_id);
			statement.setString(3, forum_title);

			statement.executeUpdate();

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}
	}

	public void downloadForuum() throws SQLException 
	{
	System.out.println("Attempting to Show All Values in the Table ~foruums~.");

	//The Format of the Host Name is the JDCB Specifier, Then the Address to Connect, Before the Database Name
	String host = "jdbc:mysql://studioruum.c5iijqup9ms0.us-east-1.rds.amazonaws.com/studioruumOnline";

	//Default Master Username and Password From AWS
	String user = "group40";
	String password = "zitozito";


	//Declaring the Statement to Be Used
	PreparedStatement statement = null;

	Connection connection = DriverManager.getConnection(host, user, password);


		try {

			//Creating a Prepared Statement
			statement = connection.prepareStatement("SELECT * FROM foruums;");
			//Gather the Results of the Select
			ResultSet rs = statement.executeQuery();
			//Print Out Each Result
			while (rs.next()) {

				int forum_id = rs.getInt("forum_id");
				int class_id = rs.getInt("class_id");
				String forum_title = rs.getString("forum_title");

				//System.out.println("Foruums downloaded");

			}


		} catch (SQLException ex) {

			System.out.println("Error Connecting: " + ex);

		} finally {

			try {

				statement.close();

			} catch (SQLException ex) {

				System.out.println("Error Closing: " + ex);

				statement = null;


			}

		}
	}

	//////////////////////////
	/*



		FORUUMS TABLE



	*/
	//////////////////////////

	public void uploadComment(Integer comment_id, Integer forum_id, String comment_content, String username, String time_updated) throws SQLException {
		System.out.println("Attempting to Add a Value to the Table ~comments~.");

		//The Format of the Host Name is the JDCB Specifier, Then the Address to Connect, Before the Database Name
		String host = "jdbc:mysql://studioruum.c5iijqup9ms0.us-east-1.rds.amazonaws.com/studioruumOnline";

		//Default Master Username and Password From AWS
		String user = "group40";
		String password = "zitozito";


		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		Connection connection = DriverManager.getConnection(host, user, password);

		try
		{

			//Creating a Prepared Statement and Placing the Table Value In
			statement = connection.prepareStatement("INSERT INTO classruum_scholars VALUES(?, ?, ?, ?, ?);");
			statement.setInt(1, comment_id);
			statement.setInt(2, forum_id);
			statement.setString(3, comment_content);
			statement.setString(4, username);
			statement.setString(5, time_updated);

			statement.executeUpdate();

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

			}

		}
	}

	public void downloadComment() throws SQLException {
		System.out.println("Attempting to Show All Values in the Table ~comments~.");

		//The Format of the Host Name is the JDCB Specifier, Then the Address to Connect, Before the Database Name
		String host = "jdbc:mysql://studioruum.c5iijqup9ms0.us-east-1.rds.amazonaws.com/studioruumOnline";

		//Default Master Username and Password From AWS
		String user = "group40";
		String password = "zitozito";


		//Declaring the Statement to Be Used
		PreparedStatement statement = null;

		Connection connection = DriverManager.getConnection(host, user, password);

		try
		{

			//Creating a Prepared Statement
			statement = connection.prepareStatement("SELECT * FROM comments;");

			//Gather the Results of the Select
			ResultSet rs = statement.executeQuery();

			//Print Out Each Result
			while(rs.next())
			{

				int comment_id = rs.getInt("comment_id");
				int forum_id = rs.getInt("forum_id");
				String comment_content = rs.getString("comment_content");
				String username = rs.getString("username");
				String time_updated = rs.getString("time_updated");

				//System.out.println("Comments downloaded");

			}

		}
		catch (SQLException ex)
		{

			System.out.println("Error Connecting: " + ex);

		}
		finally
		{

			try
			{

				statement.close();

			}
			catch (SQLException ex)
			{

				System.out.println("Error Closing: " + ex);

				statement = null;


			}

		}
	}

}