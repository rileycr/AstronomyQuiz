import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
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
    public Connection connection;
    private int qCount = 0;		// running count of the question
    private static Question[] quizQs = new Question[11];
    
    /***************************************************************************/
    
    /**
     * Main Constructor
     *
     * Creates and launches the AstroQuiz application
     */

    public AstroQuiz() throws SQLException {

        try{
            getPlayerName();
            dbConnect();
            
            createQuestions();
            guiStart();
            sendQuestion();
        } finally {
                connection.close();
        }

    }
    
    /**
     * Create intro dialogue to retrieve player name, only takes the first
     * 30 characters.
     */
    private void getPlayerName() {
        String name = JOptionPane.showInputDialog("Welcome! Please enter your name"); 
    	playerName = name.substring(0, Math.min(name.length(), 30));
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
            e.printStackTrace();
        }
    }

    /**
       Currently in testing phase, creates questions to send to the GUI
       
       *******New protocol: make the item after the last question null*********
    */
    private void createQuestions() {

        
        
        /*
            Use randSet(int setSize) to generate a random String[] of planetary objects.
         
            ---- How to randomize the order of the set with the actual answer included...?
         */
        
        String[] options1 = {"Jupiter", "Pegasus", "Epsilon Eridani", "Aquarii A"};
        String[] options2 = {"Eliptical, Cloud, Octagonal", "Eliptical, Cloud, Spiral", "Spiral, Octagonal, Cloud", "Spiral, Eliptical, Octagonal"};
        String[] options3 = {"Mercury", "Venus", "Earth", "Mars"};
        String[] options4 = {"Neptune", "Earth", "Mars", "Saturn"};
        String[] options5 = {"Mars", "Earth", "Neptune", "Saturn"};
        
        
        quizQs[0] = new MCQuestion("Which planet orbits the sun?", options1, "A");
        quizQs[1] = new MCQuestion("What are the three types of galaxies?", options2, "B");
        quizQs[2] = new ResponseQuestion("What is the mass of the earth?", "5.9726e24");
        quizQs[3] = new MCQuestion("Which planet is more massive?", options3, "C");
        quizQs[4] = new MCQuestion("Which planet has the most moons orbiting it?", options4, "D");
        quizQs[5] = new MCQuestion("Phobos and Deimos are moons of which planet?", options5, "A");
        quizQs[6] = new MCQuestion("Triton is the largest moon of what planet?", options5, "C");
        quizQs[7] = new ResponseQuestion("Sol is a star in which galaxy?", "The Milky Way");
        quizQs[8] = new MCQuestion("Which planet has the least moons orbiting it?", options4, "B");
        quizQs[9] = new MCQuestion("Which planet has two moons orbiting it?", options4, "C");

        quizQs[10] = null;

    }

    /**
       Sends a question to the GUI to be displayed
    */
    public void sendQuestion() {
        guiFrame.editQuestion(quizQs[qCount], qCount);
        qCount ++;
    }

    /**
       Called by the GUI to pull the user's response and process it.
    */
    public static boolean processResponse(String response, int qNumber) {
        
        return quizQs[qNumber].isCorrect(response);
    }

    public String getCorrectAnswer(int qNumber){
        return quizQs[qNumber].getAnswer();
    }
    
    /**
       Randomizes objects to be in questions,
    */
    private String randomObj() throws SQLException {
        
        ArrayList<String> tables = getTableNames();
        
    	// Get random
        int rTable = (int)Math.random() * tables.size();
    	String table = tables.get(rTable);
    	

        // Find the number of rows in the upcoming result set.
    	String countQuery = "SELECT COUNT(*) FROM " + table;
        int rows = -1;
        
        try {
            ResultSet rs = execQuery(countQuery);
            rows = Integer.parseInt(rs.getString("COUNT(*)"));
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        // Prepare and execute query
        String query = ("SELECT * FROM " + table);
    	ResultSet rs = execQuery(query);
    	
    	String randName = "";
    	
        // Get object name from random row in result set
    	int rand = (int)(Math.random() * rows);
        
    	try {
            for(int i = 0; (i < rand && rs.next()); i++) {
                randName = rs.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
        }
    	return randName;
    }
    
    String[] randSet(int num) {
        String answerSet[] = new String[num];
        for(int i = 0; (i < num); i++) {
            String s = "";
            while(s == "") {
                try{
                    s = randomObj();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            answerSet[i] = s;
        }
        return answerSet;
    }
    
    
    
    /**
        Get list of names for all tables in database
     */
    private ArrayList<String> getTableNames() {
        ArrayList<String> tables = new ArrayList<String>();
        ResultSet rs = null;
        try {
            try {
                DatabaseMetaData md = connection.getMetaData();
                String[] types = {"TABLE"};
                rs = md.getTables(null, null, "%", types);
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            }
            finally {
                if(rs != null) {
                    rs.close();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tables;

    }

    /**
      @return Computes the result set for a query
    */
    private ResultSet execQuery(String query) {

        ResultSet result = null;
        try {
            Statement stmt = connection.createStatement();
            stmt.setQueryTimeout(10);
            
            try {
                result = stmt.executeQuery(query);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch(Exception e) {
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
            new AstroQuiz();
        }
        catch (SQLException e) {
            System.err.println("Error: main() -- SQLException -->  " + e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    
}
