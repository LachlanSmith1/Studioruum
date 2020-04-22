package gui;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Flashcard extends Resource
{

	//the text kept on each side of the flashcard
	//changed to SimpleStringProperty in order to populate TableView
	private SimpleStringProperty frontContent;
	private SimpleStringProperty backContent;

	// Added flashcardID
	private int flashcardID, dictionaryID;
	private int quizID = 0;

	// Added new parameters
	public Flashcard(int fID, int rID, int dID, String front, String back)
	{
		super(rID);
		flashcardID = fID;
		dictionaryID = dID;
		frontContent = new SimpleStringProperty(front);
		backContent = new SimpleStringProperty(back);
	}

	// Overloaded constructor in case quizID passed as param
	public Flashcard(int fID, int rID, int dID, int qID, String front, String back)
	{
		super(rID);
		flashcardID = fID;
		dictionaryID = dID;
		quizID = qID;
		frontContent = new SimpleStringProperty(front);
		backContent = new SimpleStringProperty(back);
	}

	// getter and setter for flashcardID
	public int getFID() {
		return flashcardID;
	}

	public void setFID(int ID) {
		this.flashcardID = ID;
	}

	// getter and setter for frontContent
	public SimpleStringProperty frontProperty() {
		return frontContent;
	}

	public void setFront(String content) {
		this.frontContent = new SimpleStringProperty(content);
	}

	// getter and setter for backContent
	public SimpleStringProperty backProperty() {
		return backContent;
	}

	public void setBack(String content) { this.backContent = new SimpleStringProperty(content); }

	// getter and setter for dictionaryID
	public int getDict() {
		return dictionaryID;
	}

	public void setDict(int ID) {
		this.dictionaryID = ID;
	}

	// getter and setter for quizID
	public int getQuiz() {
		return quizID;
	}

	public void setQuiz(int ID) {
		this.quizID = ID;
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

			//write the meta information to the file separated by scarabs
			resourceWriter.write(frontContent + "\n");
			resourceWriter.write(" ¤¤ " + "\n");
			resourceWriter.write(backContent + "");

			//close the writer once it's finished
			resourceWriter.close();


		}
		catch(IOException e)
		{

			e.printStackTrace();

		}

	}

}
