package astronomy;

/*
  Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
  Class: CSE 385
  Assignment: Astronomy Quiz
  Instructor: Dr. Inclezan
*/

public abstract class Question {
    private String text;

    /*
      Constructor
    */
    public Question(String text){
        this.text = text;
    }

    /*
      @return the text of the question
    */
    public String toString(){
        return text;
    }

    /*
      Determines if the response is correct
      @param response the user's guess
      @return if the guess is correct
    */
    public abstract boolean isCorrect(String response);
}
