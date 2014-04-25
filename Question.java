package astronomy;

import java.awt.Button;

/*
 Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
 Class: CSE 385
 Assignment: Astronomy Quiz
 Instructor: Dr. Inclezan
 */

public abstract class Question {
	
	/******************		Member Variables	********************************/
	
	private static int qCount = 0;		// running count of the question
	
	private String text;				// used to display the question text
    private ClickListener submitted;	// listen for submit button pressed
    private int qNum;					// identifies each unique question
    
    private static String allText[] = new String[] {
    "Name two planets that orbit the Sun.",
    "What are the 3 different types of galaxies?",
    "What is the mass of ____?",
    "Which planet is more massive? (Multiple choice)",
    "Which star has the most planets orbiting it? (Multiple choice)",
    "What is the name of the supercluster Earth is in?",
    "True or false? Neptune is larger than Saturn. --- FALSE",
    "Phobos and Deimos are moons of what planet? --- MARS",
    "Triton is the largest moon of what planet? -- Neptune",
    "What stars are in Orion’s Belt?",
    "__(Star)__ is in __(Galaxy)__ (T/F)"	//****
    };
    
    public final static int NUM_QUESTIONS= allText.length;
    /***************************************************************************/
    
    /**
     * Constructor
     *
     * Selects an unasked question and populates
     * the object with pertinent information
     */
    public Question() {
    	qNum = qCount;
    	qCount++;
    	if(qCount >= allText.length) {	// Can be used to end the quiz as well
    		qCount = 0;
    	}
    	text = allText[qNum];
    }
    
    /*
     Copy Constructor
     */
    public Question(Question quest){
        this.text = quest.text;
        this.qNum = quest.qNum;
    }
    
    /**
     @return the text of the question
     */
    public String getText(){
        return text;
    }
    
    /**
     Determines if the response is correct
     @param response the user's guess
     @return if the guess is correct
     */
    public abstract boolean isCorrect(String response);
}
