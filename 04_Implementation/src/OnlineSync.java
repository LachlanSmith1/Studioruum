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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class OnlineSync
{

	public static void main(String[] args)
	{
		
		//Establishes a Connection
		Connection studConnect = null;		
		
		//The Format of the Host Name is the JDCB Specifier, Then the Adress to Connect, Before the Database Name
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
	
}