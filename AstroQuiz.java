import java.sql.*;
import java.awt.*;
import javax.swing.*;


/*
 Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
 Class: CSE 385
 Assignment: Astronomy Quiz
 Instructor: Dr. Inclezan
 */
public class AstroQuiz {
	
    /******************		Member Variables	********************************/
	
    private String playerName;
    private Connection connection;
    private Statement stmt;
    
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
    	showQuestions();
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
            stmt = null;
            
            connection = DriverManager.getConnection("jdbc:sqlite:385Project.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e){
            System.err.println("Class Not found: org.sqlite.JDBC\n" + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQLException: \n" + e.getMessage());
        }
    }
    
    /**
     Create main GUI frame and enclosed objects.
     */
    private void guiStart() {
        QuizFrame test = new QuizFrame(playerName);
    }
    
    private void showQuestions() {
        Query query = new Query();
        //Question question = new Question();
    }

    /**
     * PROGRAM LAUNCH.
     */
    public static void main(String[] args){
    	
        new AstroQuiz();
    }
    
}
