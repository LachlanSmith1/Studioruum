package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Quiz extends Resource
{
	private int quizID;
	// arraylist to hold the flashcards in the quiz
	ArrayList<Flashcard> theQuiz = new ArrayList<Flashcard>();

	//the current question and answer for the selected flashcard in the quiz
	private String question, answer, topic;

	public Quiz(int rID, int qID, String name, String Topic)
	{

		super(rID, name);
		quizID = qID;

		topic = Topic;

	}

	// getter and setter for quizID
	public int getQuiz() {
		return quizID;
	}

	public void setQuiz(int ID) {
		this.quizID = ID;
	}

	//adds a flashcard to the quiz
	public void addFlashcard(Flashcard card)
	{

		theQuiz.add(card);

	}

	//writes the flashcards in the quiz to a file. requires at least one flashcard in the quiz
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
				System.out.println("Quiz File Created.");
			}
			else
			{
				System.out.println("Quiz File Already Exists.");
			}

			//create a FileWriter object to write to the new
			FileWriter resourceWriter = new FileWriter(name, false);

			//go through each flashcard in the quiz and write it to the file
			for(int i=0;i<theQuiz.size();i++){
				//write the meta information to the file seperated by scarabs
				resourceWriter.write(Integer.toString(i)+". "+theQuiz.get(i).frontProperty() + "\n");
				resourceWriter.write(" ¤¤ " + "\n");
				resourceWriter.write(Integer.toString(i)+". "+theQuiz.get(i).backProperty()+"\n");
			}
			if(theQuiz.size()==0){
				resourceWriter.write("Empty Quiz");
			}

			//close the writer once it's finished
			resourceWriter.close();


		}
		catch(IOException e)
		{

			e.printStackTrace();

		}

	}

}