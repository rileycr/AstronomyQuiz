import java.sql.*;
import java.awt.*;
import javax.swing.*;

/**
 Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
 Class: CSE 385
 Assignment: Astronomy Quiz
 Instructor: Dr. Inclezan
 */
public class AstroQuiz {
	
    /******************		Member Variables	*************************/
	
    private String playerName;
    private QuizFrame guiFrame;
    public static Connection connection;
    private int qCount = 0;		// running count of the question
    Question[] quizQs = new Question[10];
    
    private String allText[] = new String[] {
    "Name two planets that orbit the Sun.",
    "What are the 3 different types of galaxies?",
    "What is the mass of ____?",
    "Which planet is more massive? (Multiple choice)",
    "Which star has the most planets orbiting it? (Multiple choice)",
    "What is the name of the supercluster Earth is in?",
    "True or false? Neptune is larger than Saturn. --- FALSE",
    "Phobos and Deimos are moons of what planet? --- MARS",
    "Triton is the largest moon of what planet? -- Neptune",
    "What stars are in Orion’s Belt?",
    "__(Star)__ is in __(Galaxy)__ (T/F)"
    };
    
    
    /***************************************************************************/
    
    /**
     * Main Constructor
     *
     * Creates and launches the AstroQuiz application
     */
    public AstroQuiz() {
        
        getPlayerName();
        dbConnect();
        guiStart();
        createQuestions();
        
    }
    
    /**
     * Create intro dialogue to retrieve player name
     */
    private void getPlayerName() {
    	playerName = JOptionPane.showInputDialog("Welcome! Please enter your name");
    }
    
    /**
     Connects to our database and allows for questions to be
     generated, queries made and results to be shown
     */
    private void dbConnect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = null;
            
            connection = DriverManager.getConnection("jdbc:sqlite:385Project.db");
        } catch (ClassNotFoundException e){
            System.err.println("Class Not found: org.sqlite.JDBC\n" + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQLException: \n" + e.getMessage());
        }
    }

    /**
       Currently for testing, creates questions to send to the GUI
    */
    private void createQuestions() {

        String[] options1 = {"Huh?", "Pfffft..", "I Dunno", "OK"};
        String[] options2 = {
        
        quizQs[0] = new MCQuestion("What?", options1);
        quizQs[1] = new ResponseQuestion("What is the mass of Earth?");
        quizQs[2] = new MCQuestion("What planet has the most moons orbiting it?", options2);

        sendQuestion();
        
    	/*
    	allQuestions[0] = new ResponseQuestion(allText[0]);
    	allQuestions[1] = new ResponseQuestion(allText[1]);
    	allQuestions[2] = new ResponseQuestion(allText[2]);
    	allQuestions[3] = new MCQuestion(allText[3]);
    	allQuestions[4] = new MCQuestion(allText[4]);
    	allQuestions[5] = new ResponseQuestion(allText[5]);
    	allQuestions[6] = new ResponseQuestion(allText[6]);
    	allQuestions[7] = new ResponseQuestion(allText[7]);
    	allQuestions[8] = new ResponseQuestion(allText[8]);
    	allQuestions[9] = new ResponseQuestion(allText[9]);
    	allQuestions[10] = new ResponseQuestion(allText[10]);
    	allQuestions[11] = new ResponseQuestion(allText[11]);
        */
    }

    /**
       Sends a question to the GUI to be displayed
    */
    public void sendQuestion() {
        guiFrame.editQuestion(quizQs[count]);
        qCount ++;
    }

    /**
       Called by the GUI to pull the user's response and process it.
    */
    public static boolean processResponse(String response) {
        
        return true;
    }
    
    /**
       Randomizes objects to be in questions,
    */
    private String randomObj() {
    	int randTable = (int)Math.random() * 100 % 6;
    	String name;
    	
    	switch(randTable){
        case 0:
            name = "Planet";
            break;
        case 1:
            name = "Moon";
            break;
        case 2:
            name = "Star";
            break;
        case 3:
            name = "Galaxy";
            break;
        case 4:
            name = "Asteroid";
            break;
        case 5:
            name = "Comet";
            break;
        default:
            name = "Star";
    	}
    	
    	String query = ("SELECT * FROM " + name);
    	
    	ResultSet rs = execQuery(query);
    	
    	int rows = 0;
    	try {
            if (rs.last()) {
                rows = rs.getRow();
                // Move to beginning
                rs.beforeFirst();
            } else {
                System.out.println("Error: Empty Set");
                System.exit(-1);
            }
    	} catch (Exception e) {
            e.printStackTrace();
    	}
    	
    	String randName = "";
    	
    	int rand = ((int)Math.random() * 100 % rows) + 1;
    	try {
            for(int i = 0; (i < rows && rs.next()); i++) {
                randName = rs.getString("Name");
            }
        } catch (SQLException e) {
            System.out.println("Error generating random query.");
            e.printStackTrace();
        }
    	return randName;
    	
    }

    /**
      @return Computes the result set for a query
    */
    private ResultSet execQuery(String query) {
    	ResultSet result = null;
    	Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.setQueryTimeout(10);
            result = stmt.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Error: execQuery\n");
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     Create main GUI frame and enclosed objects.
     */
    private void guiStart() {
        guiFrame = new QuizFrame(playerName);
    }
    
    /**
     * PROGRAM LAUNCH.
     */
    public static void main(String[] args){
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        new AstroQuiz();
    }
    
}
