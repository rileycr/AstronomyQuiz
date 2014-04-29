/**
 Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
 Class: CSE 385
 Assignment: Astronomy Quiz
 Instructor: Dr. Inclezan
 */
public class MCQuestion extends Question {

    private String answer;
    private String[] choices = new String[4];

    /**
      Constructor with question text only
    */
    public MCQuestion(String text){
        super(text);
    }
    
    /**
       Constructor
     */
    public MCQuestion(String text, String[] choice){
        super(text);
        for(int i = 0; i < choices.length; i++){
            this.choices[i] = choice[i];
        }
    }
    
    /**
       Copy Constructor
     */
    public MCQuestion(MCQuestion quest){
        this(quest.getText(), quest.getChoices());
    }
    
    /**
     @return the choices array
     */
    public String[] getChoices(){
        return this.choices;
    }


    
    /**
      returns the requested choice for the multiple choice question
    */
    public String getChoice(int i){
        return choices[i];
    }
    
    /**
     Determines if a question was answered
     correctly.
     @return the result
     */
    public boolean isCorrect(String response){
        return answer.equals(response);
    }

    /**
       @return a string in human readable form to display the question
       and options to the user
    */
    public String displayQuestion(){
        return (getText()+"\n\nA. "+choices[0]+"\nB. "+choices[1]+"\nC. "+choices[2]+"\nD. "+choices[3]);
    }
}
