/*
  Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
  Class: CSE 385
  Assignment: Astronomy Quiz
  Instructor: Dr. Inclezan
*/

public class MCQuestion extends Question {

    private String[] choice = new String[4];
    
    /*
      Constructor
    */
    public MCQuestion(String text, String[] choice){
        super(text);
        for(int i = 0; i < choice.length; i++){
            this.choice[i] = choice[i];
        }
    }

    /*
      Copy Constructor
    */
    public MCQuestion(MCQuestion quest){
        this(quest.getText(), quest.getChoice());
    }

    /*
      @return the choice array
    */
    public String[] getChoice(){
        return this.choice;
    }

    /*
      Determines if a question was answered
      correctly.
      @return the result
    */
    public boolean isCorrect(String response){
        return true;
    }
}
