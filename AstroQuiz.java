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
   /** Randomly selects a question type to generate */
    
    private Question makeQuestion(){

      String[] defaultInfo = {"Earth","Moon","Pegasus","Pluto"};
      Question question;
      try{
      switch(random.nextInt(2) + 1) {
        case 1: question = makeSunOrbitQuestion();
        break;
        //case 2: question = makePlanetOrbitQuestion();
        //break;
        default: question = new MCQuestion("Which planet orbits the sun?", defaultInfo, "Earth");
      }
      return question;
      } catch (Exception e){
        System.out.println(e.getMessage());
        System.out.println("Error creating question");
        question =  new MCQuestion("Which planet orbits the sun?", defaultInfo, "Earth");
      }
      return question;

    }
    
    /** Generates question by querying database 
      * This question is about planets orbiting a sun*/
    private Question makeSunOrbitQuestion() throws SQLException{
      ResultSet planets = execQuery("SELECT Name, Orbits FROM Planet");
      ArrayList<String[]> planetNames = new ArrayList<String[]>();
      do{
        String[] planetInfo = new String[2];
        planetInfo[0] = planets.getString("Name");
        planetInfo[1] = planets.getString("Orbits");
        planetNames.add(planetInfo);
      } while (planets.next());
      String[] correctPlanetInfo = planetNames.get(random.nextInt(planetNames.size()));
      String correctPlanet = correctPlanetInfo[0];
      String[] questionInfo = new String[4];
      questionInfo[0] = correctPlanet;
      //System.out.println("Correct Planet: " + correctPlanet);
      String correctPlanetOrbit = correctPlanetInfo[1];
      //System.out.println("Correct Planet Orbits: " + correctPlanetOrbit);
      int count = 1;
      while(count < 4){
        String wrongPlanet = planetNames.get(random.nextInt(planetNames.size()))[0];
        String wrongPlanetOrbit = planetNames.get(random.nextInt(planetNames.size()))[1];
        //System.out.println("Count: " + count + " \nWrong Planet: " + wrongPlanet);
        if(!(wrongPlanet.equals(correctPlanet)) && !((wrongPlanet.equals(questionInfo[1])) || wrongPlanet.equals(questionInfo[2]) || wrongPlanet.equals(questionInfo[3])) && !(wrongPlanetOrbit.equals(correctPlanetOrbit))){
          questionInfo[count] = wrongPlanet;
          count++;
        }
      }
      Collections.shuffle(Arrays.asList(questionInfo));
      return new MCQuestion("Which planet orbits the star " + correctPlanetOrbit, questionInfo, determineLetter(questionInfo, correctPlanet));
    }
    
    /** Convenience method to find correct option letter */
     private String determineLetter(String[] questionInfo, String correctAnswer){
      if(questionInfo[0].equals(correctAnswer)){
        return "A";
      } else if (questionInfo[1].equals(correctAnswer)){
        return "B";
      } else if (questionInfo[2].equals(correctAnswer)){
        return "C";
      }
      return  "D";
    }
      

    /**
       Currently in testing phase, creates questions to send to the GUI
    */
    private void createQuestions() {
        
        int count = 0;
        while (count < 10) {
            Query q1 = new Query(count % 2);
            
            // Small database frequently has insufficient information to populate answers
            while(!q1.valid ||
                  q1.options[0] == null ||
                  q1.options[1] == null ||
                  q1.options[2] == null ||
                  q1.options[3] == null) {
                
                q1 = new Query(count % 2);
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

        String displayQuery = generateQuery(table);
        String displayOutput = "";

        try{
            ResultSet test = execQuery(displayQuery);
            
            if(table.equals("planet")) {
                displayOutput += String.format("%-12s %-12s %-12s", "Planet", "Star", "#-Moons\n");
                while(test.next()){
                    displayOutput += String.format("%-12s %-12s %-12s", test.getString("name"), test.getString("orbits"), test.getInt("Moons"));
                    displayOutput += "\n";
                }
            } else if (table.equals("star")) {
                
            } else if (table.equals("moon")) {
                
            } else if (table.equals("asteroid")) {
                
            } else if (table.equals("comet")) {
                
            } else if (table.equals("constellation")) {
                
            } else {
                
            }
            
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        
        guiFrame.editQResultDisplay(displayOutput);
    }

    /**
       Generates the query for the extra display
    */
    private String generateQuery(String table){
        String qry = "SELECT ";

        if(table.equals("planet")) {
            qry += ("planet.name, planet.orbits, COUNT(*) AS Moons FROM planet JOIN moon ON planet.name = moon.orbits GROUP BY planet.name");
        } else if (table.equals("star")) {
            qry += ("* FROM star");
        } else if (table.equals("moon")) {
            qry += ("* FROM moon");
        } else if (table.equals("asteroid")) {
            qry += ("* FROM asteroid");
        } else if (table.equals("comet")) {
            qry += ("* FROM comet");
        } else if (table.equals("constellation")) {
            qry += ("* FROM constellation");
        } else {
            qry += ("* FROM galaxy");
        }
        return qry;
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
