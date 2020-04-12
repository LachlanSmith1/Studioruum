package gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Dictionary extends Resource {

  // the collection of associated flashcards
  private Vector<Flashcard> deck;

  // the ID to identify a specific dictionary
  private int dictionaryID;

  public Dictionary(int dID, int rID, String name) {
    super(rID, name);
    dictionaryID = dID;
    //resourceType = "dictionary";
    //deck = new Vector<Flashcard>();
  }

  // Getter and setter for dictionaryID
  public int getDict() {
    return dictionaryID;
  }

  public void setDict(int ID) { this.dictionaryID = ID; }

  /*public static void main(String[] args) {
    Dictionary exDictionary = new Dictionary("exDictionary");

    exDictionary.writeResource();
  }*/

  // Creates and adds flashcard to deck
  public void addFlashcard(String name, String front, String back) {
    /*Flashcard cardToAdd = new Flashcard(name);

    cardToAdd.setDict(dictionaryID);
    cardToAdd.setFront(front);
    cardToAdd.setBack(back);
    deck.add(cardToAdd);*/
  }

  public void writeResource(){

    //the name of the file which will be made at the current directory
    String name = resourceName + ".txt";

    try {

      //create a file object at the current directory
      File resourceFile = new File(name);

      boolean fileExists = resourceFile.createNewFile();

      //if the file didn't exist then creation was successful
      if (fileExists) {
        System.out.println("Dictionary File Created.");
      }
      else {
        System.out.println("Dictionary File Already Exists.");
      }

      //create a FileWriter object to write to the new
      FileWriter resourceWriter = new FileWriter(name, false);

      //iterate deck and write to file IDs of flashcards seperated by scarabs
      for (int i = 0; i < deck.size(); i++) {
        resourceWriter.write(deck.get(i).resourceID + "\n");
        resourceWriter.write(" ¤¤ " + "\n");
      }

      //close the writer once it's finished
      resourceWriter.close();


    }
    catch(IOException e) {

      e.printStackTrace();

    }

  }
}
