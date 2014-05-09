import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.*;
import java.lang.*;

public class QuestionGenerator{
  
   /******************  Member Variables *************************/
  
  public static Connection connection;
  public static Random random = new Random();
  
   /*************************************************************/
  
  /** Constructor creates database connection */
  public QuestionGenerator(){
    dbConnect();
    //connection = c;
  }
  
  /** Creates a batch of question objects */
  public Question[] makeQuestionBatch(int amount){
    Question[] questions = new Question[amount];
    for(int i = 0; i < amount; i++){
      System.out.println(i);
      questions[i] = makeQuestion();
    }
    return questions;
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
    int QUESTIONS = 4; // The amount of possible question types
    try{
      switch(random.nextInt(QUESTIONS) + 1) {
        case 1: question = makeOrbitQuestion("Planet", "Star");
        break;
        case 2: question = makeOrbitQuestion("Moon", "Planet");
        break;
        case 3: question = makeAggregateQuestion("Planet", "Moon"); 
        break;
        case 4: question = makeAggregateQuestion("Star", "Comet");
        break;
        default: question = new MCQuestion("Which planet orbits the sun?", defaultInfo, "Earth");
        /* Non Working questions
        case : question = makeMassQuestion("Planet");
        System.out.println("making Aggregate");
        break;
        Removed due to insufficient data causing the program to lock up
        case : question = makeOrbitQuestion("Comet", "Star");
        break;
        case : question = makeOrbitQuestion("Asteroid", "Star");
        break;*/
      }
      return question;
    } catch(SQLException e){
      e.printStackTrace(System.out);
      question =  new MCQuestion("Which planet orbits the sun?", defaultInfo, "Earth");
    }catch (Exception ee){
      System.err.println(ee.getMessage());
      System.err.println("Error creating question");
      question =  new MCQuestion("Which planet orbits the sun?", defaultInfo, "Earth");
    }
    return question;
  }
  
  private Question makeOrbitQuestion(String table, String orbitType) throws SQLException{
    ResultSet bodies = execQuery("SELECT Name, Orbits FROM " + table);
    ArrayList<String[]> bodyNames = new ArrayList<String[]>();
    // Pulls out "Name" and "Orbits" columns for a each row and enters them into
    // A 2 element string array [0] = name, [1] = orbits
    do{
      String[] bodyInfo = new String[2];
      bodyInfo[0] = bodies.getString("Name");
      bodyInfo[1] = bodies.getString("Orbits");
      bodyNames.add(bodyInfo);
    } while (bodies.next());
    String[] correctBodyInfo = bodyNames.get(random.nextInt(bodyNames.size())); // Selects the body to use as the correct answer

    String[] questionInfo = new String[4]; // Array of string to print as options
    questionInfo[0] = correctBodyInfo[0]; // First option is the correct option

    int count = 1;
    /** 
      * Randomly selects planets from the list and checks that they 
      * aren't already on the list and don't orbit the body the correct 
      * answer does */
    while(count < 4){
      String[] wrongBodyInfo = bodyNames.get(random.nextInt(bodyNames.size()));
      String wrongBody = wrongBodyInfo[0];
      String wrongBodyOrbit = wrongBodyInfo[1];
      // Excesively large if statement to make checks listed above
      if(!(wrongBody.equals(correctBodyInfo[0])) && !((wrongBody.equals(questionInfo[1])) || wrongBody.equals(questionInfo[2]) || wrongBody.equals(questionInfo[3])) && !(wrongBodyOrbit.equals(correctBodyInfo[1]))){
        questionInfo[count] = wrongBody;
        count++;
      }
    }
    Collections.shuffle(Arrays.asList(questionInfo)); // Randomize order of options
    return new MCQuestion("Which " + table + " orbits the " + orbitType + " " + correctBodyInfo[1], questionInfo, determineLetter(questionInfo, correctBodyInfo[0]));
  }
  
  private Question makeMassQuestion(String table) throws SQLException{
    ResultSet bodies = execQuery("SELECT Name, Mass " + table);
    ArrayList<String[]> bodyNames = new ArrayList<String[]>();
    // Pulls out "Name" and "Mass" columns for a each row and enters them into
    // A 2 element string array [0] = name, [1] = mass
    do{
      String[] bodyInfo = new String[2];
      bodyInfo[0] = bodies.getString("Name");
      bodyInfo[1] = bodies.getString("Mass");
      bodyNames.add(bodyInfo);
    } while (bodies.next());
    String[] correctBodyInfo = bodyNames.get(random.nextInt(bodyNames.size())); // Selects the body to use as the correct answer

    String[] questionInfo = new String[4];
    questionInfo[0] = correctBodyInfo[0];

    int count = 1;
    while(count < 4){
      String[] wrongBodyInfo = bodyNames.get(random.nextInt(bodyNames.size()));
      String wrongBody = wrongBodyInfo[0];
      String wrongBodyMass = wrongBodyInfo[1];
      //System.out.println("Count: " + count + " \nWrong Planet: " + wrongPlanet);
      if(!(wrongBody.equals(correctBodyInfo[0])) && !((wrongBody.equals(questionInfo[1])) || wrongBody.equals(questionInfo[2]) || wrongBody.equals(questionInfo[3]))){
        questionInfo[count] = wrongBody;
        count++;
      }
    }
    Collections.shuffle(Arrays.asList(questionInfo));
    return new MCQuestion("Which " + table + "has a mass of" + correctBodyInfo[1], questionInfo, determineLetter(questionInfo, correctBodyInfo[0]));
  }
  
  /** Asks which body has the most bodies orbiting it */
  
  private Question makeAggregateQuestion(String table, String orbitingBody) throws SQLException{
    
    ResultSet bodies = execQuery("SELECT Name FROM " + table);
    ArrayList<String> names = new ArrayList<String>();
    do {
      String name = bodies.getString("Name");
      names.add(name);
    } while (bodies.next());
    
    System.out.println(names.size());
    
    int count = 0;
    String [] data = new String[4]; // String to hold selected body and amount of orbiting bodies
    int most = 0;
    String mostBody = "";
    
    while(count < 4){
      String body = names.get(random.nextInt(names.size()));
      if(!(body.equals(data[0]) || body.equals(data[1]) || body.equals(data[2]) || body.equals(data[3]))){
        ResultSet orbiting = execQuery("SELECT count(*) from " + orbitingBody + " WHERE Orbits = \"" + body + "\"");
        int orbits = orbiting.getInt(1);
        System.out.println("Orbits = " + orbits);
        if(orbits != most){
          data[count] = body;
          if(orbits > most){
            most = orbits;
            mostBody = body;
          }
          count++;
        }
      }
    } 
    
    return new MCQuestion("Which " + table + " has the most " + orbitingBody + "s orbiting it?", data, determineLetter(data, mostBody));
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
  
  /* Convenience method for communication with the database */
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
  
  public static void main(String[] args){
    System.err.println("Error!  This in not a standalone application");
    System.err.println("Run the AstroQuiz file instead");
  }
}