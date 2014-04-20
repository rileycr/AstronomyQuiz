package astronomy;
import java.awt.*;
import javax.swing.*;

/*
  Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
  Class: CSE 385
  Assignment: Astronomy Quiz
  Instructor: Dr. Inclezan
*/
public class QuizFrame extends JFrame {

    public QuizFrame() {
        super("Astronomy Quiz");
        setLayout(new BorderLayout(1,1));
        setPreferredSize(new Dimension(500,500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        pack();
        setVisible(true);
    }
}
