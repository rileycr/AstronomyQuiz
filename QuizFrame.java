import java.awt.*;
import javax.swing.*;

/*
  Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
  Class: CSE 385
  Assignment: Astronomy Quiz
  Instructor: Dr. Inclezan
*/
public class QuizFrame extends JFrame {

    //Make these ArrayLists? OR have the arraylists in AstroQuiz
    private Question question;
    private String response;
    
    public QuizFrame(String player){
        super(player + "'s Astronomy Quiz");

        setLayout(new BorderLayout(1,1));
        setPreferredSize(new Dimension(500,500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        setupGUI();
                        
        pack();
        setVisible(true);
    }

    public QuizFrame() {
        this("");
    }

    public void setupGUI(){
        JPanel questionDisplay = new JPanel();
        JPanel responseField = new JPanel();
        JPanel buttons = new JPanel();
        JPanel info = new JPanel();

        add(questionDisplay, BorderLayout.CENTER);
        add(responseField, BorderLayout.SOUTH);
        
    }
    
    //TODO
    /*
      Change the question to be displayed
    */
    public void editQuestion(Question newQuestion){
        
    }
}
