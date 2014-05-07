import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.*;
import java.lang.*;
/**
 Authors:  Gavin Golden, Mark Gudorf, Victoria McIe, Cooper Riley
 Class: CSE 385
 Assignment: Astronomy Quiz
 Instructor: Dr. Inclezan
 */
public class AstroQuiz {
 
    /******************  Member Variables *************************/

    private String playerName;
    private QuizFrame guiFrame;

    public static Connection connection;
    private int qCount = 0;  // running count of the question

    private static Question[] quizQs = new Question[11];
    Random random = new Random();
    
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
    /** Randomly selects a question type to generate -MFG*/
    
    private Question makeQuestion(){
        String[] defaultInfo = {"Earth","Moon","Pegasus","Pluto"};
        Question question;
        try{
            switch(random.nextInt(1) + 1) {
            case 1: question = makeSunOrbitQuestion();
                break;
            default: question = new MCQuestion("Which planet orbits the sun?", defaultInfo, "Earth");
            }
            return question;
        } catch (Exception e){
            System.out.println(e.getMessage());
            question =  new MCQuestion("Which planet orbits the sun?", defaultInfo, "Earth");
        }
        return question;
    }
    
    /** Generates question by querying database */
    private Question makeSunOrbitQuestion() throws SQLException{
        String[] defaultInfo = {"Earth","Moon","Pegasus","Pluto"};
        ResultSet planets = execQuery("SELECT Name FROM Planet");
        ArrayList<String> planetNames = new ArrayList<String>();
        do{
            planetNames.add(planets.getString("Name"));
        } while (planets.next());
        String correctPlanet = planetNames.get(random.nextInt(planetNames.size()));
        String[] questionInfo = new String[4];
        questionInfo[0] = correctPlanet;
        System.out.println("Correct Planet: " + correctPlanet);
        int count = 1;
        while(count < 4){
            String wrongPlanet = planetNames.get(random.nextInt(planetNames.size()));
            System.out.println("Count: " + count + " \nWrong Planet: " + wrongPlanet);
            if(!(wrongPlanet.equals(correctPlanet))){
                questionInfo[count] = wrongPlanet;
                count++;
            }
        }
        String query = "SELECT Orbits FROM Planet WHERE Name = \"";
        String finalQuery = query.concat(correctPlanet);
        finalQuery = finalQuery.concat("\";");
        System.out.println("Query: " + finalQuery);
        ResultSet starName = execQuery(finalQuery);
        Collections.shuffle(Arrays.asList(questionInfo));
        String answerLetter = "";
        if(questionInfo[0].equals(correctPlanet)){
            answerLetter = "A";
        } else if (questionInfo[1].equals(correctPlanet)){
            answerLetter = "B";
        } else if (questionInfo[2].equals(correctPlanet)){
            answerLetter = "C";
        } else {
            answerLetter = "D";
        }
        
        return new MCQuestion("Which planet orbits the star \"" + starName.getString("Orbits") + "\"?", questionInfo, answerLetter);
        //return new MCQuestion("Which planet orbits the sun?", defaultInfo, "Earth");
    }
    
    /**
       Currently in testing phase, creates questions to send to the GUI
    */
    private void createQuestions() {
        
        int count = 0;
        while (count < 5) {
            Query q1 = new Query(0);
            while(q1.answer == null) {
                q1 = new Query(0);
            }
            quizQs[count] = new MCQuestion(q1.question, q1.options, q1.options[4]);
            count ++;
        }
        
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

    /**
       @return the correct answer to a question
    */
    public String getCorrectAnswer(int qNumber){
        return quizQs[qNumber].getAnswer();
    }

    /**
       Called by the GUI to get basic info from the database
    */
    public void displayBasic(String table){
        System.out.println("Printing "+table);
        
        String testQ = ("SELECT * FROM "+table);
        ResultSet test = execQuery(testQ);

        
        
        guiFrame.editQResultDisplay(table);
    }

    /**
       Inserts correct answer into array
    */
    private String[] randInsert(String answers[], String correct) {
        String letters[] = {"A", "B", "C", "D"};
        
        String[] update = new String[letters.length+1];
        System.arraycopy(answers, 0, update, 0, answers.length);
        
        int rand = (int)(Math.random() * answers.length);
        update[rand] = correct;
        update[4] = letters[rand];
        
        return update;
        
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
    public ResultSet execQuery(String query) {

        ResultSet result = null;
        try {
            Statement stmt = connection.createStatement();
            
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
