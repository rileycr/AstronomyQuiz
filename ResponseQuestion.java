/**
  Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
  Class: CSE 385
  Assignment: Astronomy Quiz
  Instructor: Dr. Inclezan
*/

public class ResponseQuestion extends Question {

    /**
      Constructor
    */
    public ResponseQuestion(String text, String answer){
        super(text, answer);
    }

    /**
      @return if the response is correct
    */
    public boolean isCorrect(String response){
    	return (this.getAnswer().contains(response) && !response.equals(""));
    }

    /**
       @return the text of the question
    */
    public String displayQuestion(){
        return getText();
    }
    
}
