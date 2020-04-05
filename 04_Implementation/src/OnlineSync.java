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


//Import Statements For JDBC and Etc.
import java.util.Properties;
import java.sql.*;

public class OnlineSync
{

	public static void main(String[] args)
	{
		
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
			
			if(studConnect != null)
			{
				
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
		finally
		{
			
			//Close The Connection When Finished
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
		
	}
	
	//////////////////////////
	/*
	
	
	
		USERS TABLE
	
	
	
	*/
	//////////////////////////
	
	public void uploadUsers(Connection studConnect)
	{
		
		System.out.println("Attempting to Add a Value to the Table ~users~.");
		
		//Declaring the Statement to Be Used
		PreparedStatement statement = null;
		
		try
		{
			
			String username = "slgjon13";
			String email = "l.jones44@student.liverpool.ac.uk";
			String password = "default";
			String time_created = "12:00:00";
			String time = "in april";
			
			//Creating a Prepared Statement and Placing the Table Value In
			statement = studConnect.prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?);");
			statement.setString(1, username);
			statement.setString(2, email);
			statement.setString(3, password);
			statement.setString(4, time_created);
			statement.setString(5, time);
			
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
				String time = rs.getString("time");
			
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
	
}