import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Note extends Resource
{
    //This will be whatt the user writes
    private String noteContent;


    public Note(String name)
    {
        super(name);

        //example data 
        noteContent = "Need to research x by date y";
    }



    public static void main(String[] args)
	{

		Note exNote = new Note("exNote");

		exNote.writeResource();

	}



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
			resourceWriter.write(notContent);

			//close the writer once it's finished
			resourceWriter.close();


		}
		catch(IOException e)
		{

			e.printStackTrace();

		}

	}

}