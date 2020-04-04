import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Flashcard extends Resource
{

	//the text kept on each side of the flashcard
	private String frontContent, backContent;

	//the ID for any other resources it is attached to
	private int dictionaryID, quizID;


	public Flashcard(String name)
	{

		super(name);

		//DUMMY DATA FOR FLASHCARDS

		frontContent = "What is the capital of Belgium?";
		backContent = "Brussels";

	}

	// getters and setter for frontContent, backContent and dictionaryID
	public String getFront() {
		return frontContent;
	}

	public void setFront(String content) {
		this.frontContent = content;
	}

	public String getBack() {
		return backContent;
	}

	public void setBack(String content) {
		this.backContent = content;
	}

	public int getDict() {
		return dictionaryID;
	}

	public void setDict(int ID) {
		this.dictionaryID = ID;
	}

	public static void main(String[] args)
	{

		Flashcard exFlashcard = new Flashcard("exFlashcard");

		exFlashcard.writeResource();

	}

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
				System.out.println("Flashcard File Created.");
			}
			else
			{
				System.out.println("Flashcard File Already Exists.");
			}

			//create a FileWriter object to write to the new
			FileWriter resourceWriter = new FileWriter(name, false);

			//write the meta information to the file seperated by scarabs
			resourceWriter.write(frontContent + "\n");
			resourceWriter.write(" ¤¤ " + "\n");
			resourceWriter.write(backContent);

			//close the writer once it's finished
			resourceWriter.close();


		}
		catch(IOException e)
		{

			e.printStackTrace();

		}

	}

}