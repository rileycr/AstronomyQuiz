import java.awt.*;
import javax.swing.*;

/*
  Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
  Class: CSE 385
  Assignment: Astronomy Quiz
  Instructor: Dr. Inclezan
*/
public class QuizFrame extends JFrame {

    private Question question;
    
    public QuizFrame(String player){
        super(player + "'s Astronomy Quiz");

        setLayout(new BorderLayout(1,1));
        setPreferredSize(new Dimension(500,500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
                
        pack();
        setVisible(true);
    }

    public QuizFrame() {
        this("");
    }
    
    //TODO
    /*
      Change the question to be displayed
    */
    public void editQuestion(Question newQuestion){
        
    }
}
