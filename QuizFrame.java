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
    private JPanel extraPanel;
    private JPanel qResultPanel;
    private JPanel queryPanel;
    private JPanel learnButtonPanel;
    private JTextField responseField;
    private JTextPane questionPane;
    private JTextPane qResultDisplay;
    private JTextPane infoQuestionProgress;
    private JTextPane infoAccuracy;

    private JScrollPane resultScroll;

    private JButton submitButton;
    private JButton quitButton;
    private JButton choiceA;
    private JButton choiceB;
    private JButton choiceC;
    private JButton choiceD;
    private JButton planetButton;
    private JButton starButton;
    private JButton moonButton;
    private JButton asteroidButton;
    private JButton cometButton;
    private JButton constellationButton;
    private JButton galaxyButton;
    private JButton resultGoBack;
    
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
        this.player = player;
        bListen = new ButtonListener();
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
       For the extra panel, changes what is displayed
    */
    private void changeExtraDisplay(boolean queryPanelShowing){
        if(queryPanelShowing){
            extraPanel.remove(queryPanel);
            extraPanel.add(qResultPanel, BorderLayout.CENTER);
        } else {
            extraPanel.remove(qResultPanel);
            extraPanel.add(queryPanel, BorderLayout.CENTER);
        }
        extraPanel.revalidate();
        extraPanel.repaint();
    }

    /**
       Displays the basic info on the right panel
    */
    public void editQResultDisplay(String text){
        qResultDisplay.setText(text);
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
            } else if (e.getSource().equals(planetButton)) {
                changeExtraDisplay(true);
                quiz.displayBasic("planet");
            } else if (e.getSource().equals(starButton)) {
                changeExtraDisplay(true);
                quiz.displayBasic("star");
            } else if (e.getSource().equals(moonButton)) {
                changeExtraDisplay(true);
                quiz.displayBasic("moon");
            } else if (e.getSource().equals(asteroidButton)) {
                changeExtraDisplay(true);
                quiz.displayBasic("asteroid");
            } else if (e.getSource().equals(cometButton)) {
                changeExtraDisplay(true);
                quiz.displayBasic("comet");
            } else if (e.getSource().equals(constellationButton)) {
                changeExtraDisplay(true);
                quiz.displayBasic("constellation");
            } else if (e.getSource().equals(galaxyButton)) {
                changeExtraDisplay(true);
                quiz.displayBasic("galaxy");
            } else if (e.getSource().equals(resultGoBack)) {
                changeExtraDisplay(false);
            } else if (e.getSource().equals(quitButton)) {
                endQuiz();
            }
        }
    }

    
    /**
       Remaining code draws the GUI
     */
    public void setupGUI(){
        setSize(new Dimension(600, 430));
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
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
        infoQuestionProgress = new JTextPane();
        infoQuestionProgress.setEditable(false);
        infoQuestionProgress.setFont(new Font("Source Code Pro", Font.PLAIN, 10));
        northPanel.add(infoQuestionProgress);
        
        infoAccuracy = new JTextPane();
        infoAccuracy.setFont(new Font("Source Code Pro", Font.PLAIN, 10));
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
        questionPane.setFont(new Font("Source Code Pro", Font.PLAIN, 10));
        centerLayout.putConstraint(SpringLayout.NORTH, questionPane, 0, SpringLayout.NORTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.WEST, questionPane, 0, SpringLayout.WEST, centerPanel);
        centerLayout.putConstraint(SpringLayout.SOUTH, questionPane, 264, SpringLayout.NORTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.EAST, questionPane, 250, SpringLayout.WEST, centerPanel);
        questionPane.setEditable(false);
        questionPane.setText("Test question?\nA. Apples\nB. Bananas");
        centerPanel.add(questionPane);

        //A panel to add extra items in the future
        extraPanel = new JPanel();
        centerLayout.putConstraint(SpringLayout.NORTH, extraPanel, 0, SpringLayout.NORTH, centerPanel);
        centerLayout.putConstraint(SpringLayout.WEST, extraPanel, 6, SpringLayout.EAST, questionPane);
        centerLayout.putConstraint(SpringLayout.EAST, extraPanel, 0, SpringLayout.EAST, centerPanel);
        centerPanel.add(extraPanel);
        extraPanel.setLayout(new BorderLayout(0, 0));
        JTextPane dbQueryLabel = new JTextPane();
        dbQueryLabel.setEditable(false);
        dbQueryLabel.setText("Database Query Tool");
        extraPanel.add(dbQueryLabel, BorderLayout.NORTH);
        
        queryPanel = new JPanel(new BorderLayout(0,0));
        qResultPanel = new JPanel(new BorderLayout(0,0));
        resultGoBack = new JButton("Go Back");
        resultGoBack.addActionListener(bListen);

        qResultDisplay = new JTextPane();
        qResultDisplay.setFont(new Font("Source Code Pro", Font.PLAIN, 10));
        resultScroll = new JScrollPane(qResultDisplay);
        
        qResultPanel.add(resultGoBack, BorderLayout.NORTH);
        qResultPanel.add(resultScroll, BorderLayout.CENTER);

        learnButtonPanel = new JPanel(new FlowLayout());
        queryPanel.add(learnButtonPanel, BorderLayout.CENTER);
        
        extraPanel.add(queryPanel, BorderLayout.CENTER);
        
        JTextPane queryTextPane = new JTextPane();
        queryTextPane.setEditable(false);
        queryTextPane.setText("What do you want to learn about?");
        
        planetButton = new JButton("Planets");
        planetButton.addActionListener(bListen);
        
        starButton = new JButton("Stars");
        starButton.addActionListener(bListen);
        
        moonButton = new JButton("Moons");
        moonButton.addActionListener(bListen);
        
        asteroidButton = new JButton("Asteroids");
        asteroidButton.addActionListener(bListen);
        
        cometButton = new JButton("Comets");
        cometButton.addActionListener(bListen);
        
        constellationButton = new JButton("Constellations");
        constellationButton.addActionListener(bListen);
        
        galaxyButton = new JButton("Galaxies");
        galaxyButton.addActionListener(bListen);

        queryPanel.add(queryTextPane, BorderLayout.NORTH);
        learnButtonPanel.add(planetButton);
        learnButtonPanel.add(starButton);
        learnButtonPanel.add(moonButton);
        learnButtonPanel.add(asteroidButton);
        learnButtonPanel.add(cometButton);
        learnButtonPanel.add(constellationButton);
        learnButtonPanel.add(galaxyButton);

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
