package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Resource
{
	
	//unique identifiers for the resource and attached user
	public int resourceID, userID;
	
	//the type of resource, being the subclass of this
	public String resourceType;
	
	//the name given to the resource (not necessary)
	public String resourceName;

	//access level, which is private by default
	public String privacyLevel;
	
	// added id and name as constructor params
	public Resource(int id, String name)
	{
		resourceName = name;
		resourceID = id;
	}

	// Overloaded constructor used by Flashcard (no need for title)
	public Resource(int id)
	{
		resourceID = id;
	}

	// Getter and setter for resourceName
	public String getTitle() {
		return resourceName;
	}

	public void setTitle(String title) { this.resourceName = title; }

	public void writeResource()
	{
		
		//the name of the file which will be made at the current directory
		String name = resourceName + ".txt"; 
		
		try
		{
			
			//create a file object at the current directory
			File resourceFile = new File(name);
			
			boolean fileExists = resourceFile.createNewFile();
			
			//if the file didn't exist then creation was successful
			if (fileExists)
			{
				System.out.println("Resource File Created.");
			}
			else
			{
				System.out.println("Resource File Already Exists.");
			}
			
			//create a FileWriter object to write to the new 
			FileWriter resourceWriter = new FileWriter(name, false);
			
			//write the meta information to the file seperated by scarabs
			resourceWriter.write(" ¤¤ " + Integer.toString(resourceID) + " ¤ " + Integer.toString(userID) + " ¤ " + resourceType + " ¤ " + privacyLevel + " ¤¤ ");
			
			//close the writer once it's finished
			resourceWriter.close();
			
			
		}
		catch(IOException e)
		{
			
			e.printStackTrace();
			
		}
		
	}
	
}