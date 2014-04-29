/**
 Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
 Class: CSE 385
 Assignment: Astronomy Quiz
 Instructor: Dr. Inclezan
 */
public abstract class Question {
    
    private String text;
    
    /**
     * Constructor
     */
    public Question() {
        text = "";
    }

    /**
      Constructor
    */
    public Question(String text){
        this.text = text;
    }
    
    /**
     Copy Constructor
     */
    public Question(Question quest){
        this.text = quest.text;
    }

    
    /**
     *@return the text of the question
     */
    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }
    /**
     Determines if the response is correct
     @param response the user's guess
     @return if the guess is correct
     */
    public abstract boolean isCorrect(String response);
    public abstract String displayQuestion();
}
