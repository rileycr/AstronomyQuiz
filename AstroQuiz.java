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
    private static Question[] quizQs = new Question[10];
    
    /***************************************************************************/
    
    /**
     * Main Constructor
     *
     * Creates and launches the AstroQuiz application
     */
    public AstroQuiz() {
        
        getPlayerName();
        dbConnect();
        createQuestions();
        guiStart();
        sendQuestion();
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
       Currently in testing phase, creates questions to send to the GUI
    */
    private void createQuestions() {

        String[] options1 = {"Huh?", "Pfffft..", "I Dunno", "OK"};
        //String[] options2 = {};
        
        quizQs[0] = new MCQuestion("What?", options1, "B");
        quizQs[1] = new ResponseQuestion("What is the mass of Earth?", "100");
        //quizQs[2] = new MCQuestion("What planet has the most moons orbiting it?", options2);

    }

    /**
       Sends a question to the GUI to be displayed
    */
    public void sendQuestion() {
        guiFrame.editQuestion(quizQs[qCount]);
        qCount ++;
    }

    /**
       Called by the GUI to pull the user's response and process it.
    */
    public static boolean processResponse(String response, int qNumber) {
        
        return quizQs[qNumber].isCorrect(response);
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
        guiFrame = new QuizFrame(playerName, this);
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
