import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;

/**
 Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
 Class: CSE 385
 Assignment: Astronomy Quiz
 Instructor: Dr. Inclezan
 */
public class QuizFrame extends JFrame {
    
    private static int screenWidth;
    private static int screenHeight;
    private static String player;

    private AstroQuiz quiz;
    
    private JPanel buttonPanel;
    private JPanel northPanel;
    private JTextPane questionPane;
    private JTextField responseField;
    private JTextArea infoQuestionProgress;
    private JTextArea infoAccuracy;
    
    private JButton submitButton;
    private JButton quitButton;
    private JButton choiceA;
    private JButton choiceB;
    private JButton choiceC;
    private JButton choiceD;
    
    private ButtonListener bListen;

    private DecimalFormat df = new DecimalFormat("#.##");
    private int questionNumber;
    private int numCorrect;
    
    /**
     Constructor
     */
    public QuizFrame(String player, AstroQuiz quiz){
        super(player + "'s Astronomy Quiz");
        this.quiz = quiz;
        this.numCorrect = 0;
        bListen = new ButtonListener();
        this.player = player;
        setupGUI();
    }
    
    /**
     Change the question to be displayed
     */
    public void editQuestion(Question newQuestion, int qCount){
        if(newQuestion == null){
            updateQAccuracy();
            endQuiz();
        } else {
            questionPane.setText(newQuestion.displayQuestion());
            this.questionNumber = qCount;
            updateQProgress();
            
            boolean mcQuestion = (newQuestion instanceof MCQuestion);
            responseField.setVisible(!mcQuestion);
            buttonPanel.setVisible(mcQuestion);
        }
    }

    /**
       Sends the response to the user's answer
    */
    private void sendResponse(String answer){
        if(AstroQuiz.processResponse(answer, questionNumber)){
            JOptionPane.showMessageDialog(this, "You rock "+player+"!!!", "You did something right!!!", JOptionPane.PLAIN_MESSAGE);
            updateNumCorrect();
        } else {
            JOptionPane.showMessageDialog(this, "You can do better "+player+"!\nThe answer was: "+quiz.getCorrectAnswer(questionNumber), "You did something WRONG!!!", JOptionPane.PLAIN_MESSAGE);
        }

        updateQAccuracy();
        quiz.sendQuestion();
    }

    /**
       Adds one if the answer is correct
    */
    private void updateNumCorrect(){
        numCorrect++;
        updateQAccuracy();
    }

    /**
       Updates the accuracy of the responses
    */
    private void updateQAccuracy(){
        infoAccuracy.setText("\t"+grade()+"% Correct so far");
    }

    /**
       Updates the question progress counter
    */
    private void updateQProgress(){
        infoQuestionProgress.setText("Question "+(questionNumber + 1)+" of 10\t     ");
    }

    /**
       @return the accuracy of the user in %
    */
    private String grade(){
        double percent = (numCorrect*1.0)/(questionNumber+1.0)*100.0;
        return df.format(percent);
    }

    private void endQuiz(){
        JOptionPane.showMessageDialog(this, "You've completed your quiz!! you got a\n\n"+grade()+"%", "OK "+player+"!", JOptionPane.PLAIN_MESSAGE);
        JOptionPane.showMessageDialog(this, "Goodbye!", "", JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }

    /**
       Subclass that listens to all the buttons in the GUI
    */
    public class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e){
            if (e.getSource().equals(submitButton)){
                sendResponse(responseField.getText());
                responseField.setText("");
            } else if(e.getSource().equals(choiceA)) {
                sendResponse("A");
            } else if(e.getSource().equals(choiceB)) {
                sendResponse("B");
            } else if(e.getSource().equals(choiceC)) {
                sendResponse("C");
            } else if(e.getSource().equals(choiceD)) {
                sendResponse("D");
            } else if (e.getSource().equals(quitButton)){
                endQuiz();
            }
        }
    }
    
    /**
       Remaining code draws the GUI
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
        
        submitButton = new JButton("Submit");
        southPanel.setRightComponent(submitButton);
        submitButton.addActionListener(bListen);
        
        quitButton = new JButton("Quit");
        southPanel.setLeftComponent(quitButton);
        quitButton.addActionListener(bListen);
        
        //Info for the user (how far along they are)
        northPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) northPanel.getLayout();
        flowLayout.setHgap(0);
        getContentPane().add(northPanel, BorderLayout.NORTH);
        
        //Input box
        infoQuestionProgress = new JTextArea();
        infoQuestionProgress.setFont(new Font("Tahoma", Font.PLAIN, 13));
        infoQuestionProgress.setEditable(false);
        northPanel.add(infoQuestionProgress);
        
        infoAccuracy = new JTextArea();
        infoAccuracy.setFont(new Font("Tahoma", Font.PLAIN, 13));
        infoAccuracy.setEditable(false);

        northPanel.add(infoAccuracy);
        updateQProgress();
        updateQAccuracy();
        
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

        //A panel to add extra items in the future
        JPanel extraPanel = new JPanel();
        centerLayout.putConstraint(SpringLayout.NORTH, extraPanel, 0, SpringLayout.NORTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.WEST, extraPanel, -223, SpringLayout.EAST, centerPanel);
        centerLayout.putConstraint(SpringLayout.EAST, extraPanel, 0, SpringLayout.EAST, centerPanel);
        centerPanel.add(extraPanel);

        //The panel to contain the multiple choice buttons
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        centerPanel.add(buttonPanel);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        choiceA = new JButton("A");
        buttonPanel.add(choiceA);
        choiceA.addActionListener(bListen);
        
        choiceB = new JButton("B");
        buttonPanel.add(choiceB);
        choiceB.addActionListener(bListen);
        
        choiceC = new JButton("C");
        buttonPanel.add(choiceC);
        choiceC.addActionListener(bListen);
        
        choiceD = new JButton("D");
        buttonPanel.add(choiceD);
        choiceD.addActionListener(bListen);

        //For the user to enter Response Question answers
        responseField = new JTextField();
        centerLayout.putConstraint(SpringLayout.NORTH, buttonPanel, 14, SpringLayout.NORTH, responseField);
        centerLayout.putConstraint(SpringLayout.WEST, buttonPanel, 0, SpringLayout.WEST, responseField);
        centerLayout.putConstraint(SpringLayout.SOUTH, buttonPanel, -14, SpringLayout.SOUTH, responseField);
        centerLayout.putConstraint(SpringLayout.EAST, buttonPanel, 0, SpringLayout.EAST, responseField);
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
