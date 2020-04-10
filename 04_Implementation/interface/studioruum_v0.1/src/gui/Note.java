package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Note extends Resource
{
    private int noteID;

    //Replaced by Resource.resourceName
    //private String noteTitle;

	//This will be what the user writes
    private String noteContent;

	// Constructor now accepts note_id, resource_id, title and content as params
    public Note(int nID, int rID, String title, String content)
    {
    	super(rID, title);
		noteID = nID;
		noteContent = content;

        //example data 
        //noteContent = "Need to research x by date y";
    }



    /*public static void main(String[] args)
	{

		Note exNote = new Note("exNote");

		exNote.writeResource();

	}*/

    // Getter and setter for noteID
	public int getDict() {
		return noteID;
	}

	public void setDict(int ID) { this.noteID = ID; }

	// Getter and setter for noteContent
	public String getContent() {
		return noteContent;
	}

	public void setContent(String content) { this.noteContent = content; }

    //creating the local text file 
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
				System.out.println("Note File Created.");
			}
			else
			{
				System.out.println("Note File Already Exists.");
			}

			//create a FileWriter object to write to the new
			FileWriter resourceWriter = new FileWriter(name, false);

			//write the meta information to the file seperated by scarabs
			resourceWriter.write(noteContent);

			//close the writer once it's finished
			resourceWriter.close();


		}
		catch(IOException e)
		{

			e.printStackTrace();

		}

	}

}