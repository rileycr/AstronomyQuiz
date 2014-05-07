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

    public Connection connection;
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

        
        
        /*
            Use randSet(int setSize) to generate a random String[] of planetary objects.
         
            ---- How to randomize the order of the set with the actual answer included...?
         */
/*
        String[] options1 = randSet(4, "Planet");
        options1 = randInsert(options1, "Jupiter");
        
        String[] options2 = randSet(4, "");
        options2 = randInsert(options2, "Eliptical, Cloud, Spiral");
        
        String[] options3 = randSet(4, "Star");
        options3 = randInsert(options3, "Jupiter");
        
        String[] options4 = randSet(4,"");
        options4[3]= "Saturn";
        options4[2] = "Mars";
        options4[1] = "Neptune";
        
        String[] options5 = randSet(4, "");
        options5[0] = "Mars";
        options5[2] = "Neptune";
*/

<<<<<<< HEAD
        int count = 0;
        while (count < 5) {
            Query q1 = new Query(0);
            while(q1.answer == null) {
                q1 = new Query(0);
            }
            quizQs[count] = new MCQuestion(q1.question, q1.options, q1.options[4]);
            count ++;
        }
        /*quizQs[1] = new MCQuestion("What are the three types of galaxies?", options2, options2[4]);
=======

        //quizQs[0] = new MCQuestion("Which planet orbits the sun?", options1, options1[4]);
        quizQs[0] = makeQuestion();
        quizQs[1] = new MCQuestion("What are the three types of galaxies?", options2, options2[4]);
>>>>>>> FETCH_HEAD
        quizQs[2] = new ResponseQuestion("What is the mass of the earth?", "5.9726e24");
        quizQs[3] = new MCQuestion("Which planet is more massive?", options3, options3[4]);
        quizQs[4] = new MCQuestion("Which planet has the most moons orbiting it?", options4, "D");
        quizQs[5] = new MCQuestion("Phobos and Deimos are moons of which planet?", options5, "A");
        quizQs[6] = new MCQuestion("Triton is the largest moon of what planet?", options5, "C");
        quizQs[7] = new ResponseQuestion("Sol is a star in which galaxy?", "The Milky Way");
        quizQs[8] = new MCQuestion("Which planet has the least moons orbiting it?", options4, "B");
        quizQs[9] = new MCQuestion("Which planet has two moons orbiting it?", options4, "C");
*/
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
       Randomizes objects to be in questions,
    */
    private String randomObj(String not) throws SQLException {
        
        ArrayList<String> tables = getTableNames();
        
     // Get random
        String table = "";
        do {
            int rTable = (int)(Math.random() * tables.size());
            table = tables.get(rTable);
        } while (table.equals(not));
     

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
    
    private String[] randSet(int num, String not) { /***** changed 'not'*****/
        String answerSet[] = new String[num];
        for(int i = 0; (i < num); i++) {
            String s = "";
            while(s == "") {
                try{
                    s = randomObj(not); /***** changed 'not' *****/
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
      System.out.println("Running");
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
