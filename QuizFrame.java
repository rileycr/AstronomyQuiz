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

    private String displayedQuestion;
    private JTextField responseField;
    private static int screenWidth;
    private static int screenHeight;
    private JTextPane questionPane;
    private JPanel buttonPanel;
        
    //Make these ArrayLists? OR have the arraylists in AstroQuiz
    private Question question;
    private String response;
    
    public QuizFrame(String player){
        super(player + "'s Astronomy Quiz");
        setupGUI();
    }

    public QuizFrame() {
        this("");
    }

    /*
      Change the question to be displayed
    */
    public void editQuestion(Question newQuestion){
        questionPane.setText(newQuestion.getText());
        
        boolean mcQuestion = (newQuestion instanceof MCQuestion);
        responseField.setVisible(mcQuestion);
        buttonPanel.setVisible(!mcQuestion);

    }

    //For testing purposes
    public static void main(String[] args) {
        QuizFrame test = new QuizFrame("Cooper");
    }

    /*
      Draws the GUI
    */
    public void setupGUI(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
        setSize(new Dimension(500, 430));
        setLocation(screenWidth / 2 - getWidth() / 2, screenHeight / 2 - getHeight() / 2);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage("GUI icon.png"));
		
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //The bottom buttons
        JSplitPane southPanel = new JSplitPane();
        southPanel.setEnabled(false);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
        
        JButton submitButton = new JButton("Submit");
        southPanel.setRightComponent(submitButton);
        
        JButton quitButton = new JButton("Quit");
        southPanel.setLeftComponent(quitButton);

        //Info for the user (how far along they are)
        JPanel northPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) northPanel.getLayout();
        flowLayout.setHgap(0);
        getContentPane().add(northPanel, BorderLayout.NORTH);

        //Input box
        JTextArea txtrQuestionOf = new JTextArea();
        txtrQuestionOf.setText("Question _ of 10                   ");
        txtrQuestionOf.setEditable(false);
        northPanel.add(txtrQuestionOf);
        
        JTextArea txtrCorrectSo = new JTextArea();
        txtrCorrectSo.setEditable(false);
        txtrCorrectSo.setText("     __% Correct so far");
        northPanel.add(txtrCorrectSo);
        
        JPanel westPanel = new JPanel();
        getContentPane().add(westPanel, BorderLayout.WEST);
        
        JPanel eastPanel = new JPanel();
        getContentPane().add(eastPanel, BorderLayout.EAST);

        //Main info panel with question, options and a space for something else (suggestions?)
        JPanel centerPanel = new JPanel();
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        SpringLayout centerLayout = new SpringLayout();
        centerPanel.setLayout(centerLayout);
        
        questionPane = new JTextPane();
        centerLayout.putConstraint(SpringLayout.NORTH, questionPane, 0, SpringLayout.NORTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.WEST, questionPane, 0, SpringLayout.WEST, centerPanel);
        centerLayout.putConstraint(SpringLayout.SOUTH, questionPane, 264, SpringLayout.NORTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.EAST, questionPane, 250, SpringLayout.WEST, centerPanel);
        questionPane.setEditable(false);
        questionPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
        questionPane.setText("Test question?\nA. Apples\nB. Bananas");
        centerPanel.add(questionPane);
        
        JPanel extraPanel = new JPanel();
        centerLayout.putConstraint(SpringLayout.NORTH, extraPanel, 0, SpringLayout.NORTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.WEST, extraPanel, -223, SpringLayout.EAST, centerPanel);
        centerLayout.putConstraint(SpringLayout.EAST, extraPanel, 0, SpringLayout.EAST, centerPanel);
        centerPanel.add(extraPanel);
        
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        centerPanel.add(buttonPanel);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JButton choiceA = new JButton("A");
        buttonPanel.add(choiceA);
        
        JButton choiceB = new JButton("B");
        buttonPanel.add(choiceB);
        
        JButton choiceC = new JButton("C");
        buttonPanel.add(choiceC);
        
        JButton choiceD = new JButton("D");
        buttonPanel.add(choiceD);
        
        responseField = new JTextField();
        centerLayout.putConstraint(SpringLayout.NORTH, buttonPanel, 14, SpringLayout.NORTH, responseField);
        centerLayout.putConstraint(SpringLayout.WEST, buttonPanel, 25, SpringLayout.WEST, responseField);
        centerLayout.putConstraint(SpringLayout.SOUTH, buttonPanel, -14, SpringLayout.SOUTH, responseField);
        centerLayout.putConstraint(SpringLayout.EAST, buttonPanel, -25, SpringLayout.EAST, responseField);
        centerLayout.putConstraint(SpringLayout.SOUTH, extraPanel, 0, SpringLayout.SOUTH, responseField);
        centerLayout.putConstraint(SpringLayout.NORTH, responseField, -71, SpringLayout.SOUTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.WEST, responseField, 0, SpringLayout.WEST, centerPanel);
        centerLayout.putConstraint(SpringLayout.SOUTH, responseField, -10, SpringLayout.SOUTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.EAST, responseField, 250, SpringLayout.WEST, centerPanel);
        centerPanel.add(responseField);
        responseField.setColumns(10);
        setVisible(true);
    }
}
