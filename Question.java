/**
 Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
 Class: CSE 385
 Assignment: Astronomy Quiz
 Instructor: Dr. Inclezan
 */
public abstract class Question {
    private String answer;
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
       Constructor
    */
    public Question(String text, String answer){
        this.text = text;
        this.answer = answer;
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
       @return the answer to a question
    */
    public String getAnswer(){
        return answer;
    }
    
    /**
       Allows acces to change the answer to a quesetion
    */
    public void setAnswer(String answer){
        this.answer = answer;
    }
    
    /**
     Determines if the response is correct
     @param response the user's guess
     @return if the guess is correct
     */
    public abstract boolean isCorrect(String response);
    public abstract String displayQuestion();
}
