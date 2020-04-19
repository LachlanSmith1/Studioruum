package com.example.studioruum;

public class Note extends Resource
{
    private int noteID;

    //Replaced by com.example.studioruum.Resource.resourceName
    //private String noteTitle;

    //This will be what the user writes
    private String noteContent;

    // Constructor now accepts note_id, resource_id, title and content as params
    public Note(int nID, int rID, String title, String content) {
        super(rID, title);
        noteID = nID;
        noteContent = content;

    }

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

    @Override
    public String toString() {
        return getTitle();
    }
}