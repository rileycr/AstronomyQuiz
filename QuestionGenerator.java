import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.*;
import java.lang.*;

public class QuestionGenerator{
  public static Connection connection;
  public static Random random = new Random();
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
  
  private Question makeOrbitQuestion(String table, String orbitType){
    ResultSet bodies = execQuery("SELECT Name, Orbits FROM " + table);
    ArrayList<String[]> planetNames = new ArrayList<String[]>();
    do{
      String[] bodyInfo = new String[2];
      bodyInfo[0] = bodies.getString("Name");
      bodyInfo[1] = bodies.getString("Orbits");
      bodyNames.add(bodyInfo);
    } while (bodies.next());
    String[] correctBodyInfo = bodyNames.get(random.nextInt(bodyNames.size()));
    String correctBody = correctBodyInfo[0];
    String correctBodyOrbit = correctBodyInfo[1];
    
    String[] questionInfo = new String[4];
    questionInfo[0] = correctBody;

    int count = 1;
    while(count < 4){
      String wrongBody = bodyNames.get(random.nextInt(bodyNames.size()))[0];
      String wrongodyOrbit = bodyNames.get(random.nextInt(bodyNames.size()))[1];
      //System.out.println("Count: " + count + " \nWrong Planet: " + wrongPlanet);
      if(!(wrongBody.equals(correctBody)) && !((wrongBody.equals(questionInfo[1])) || wrongBody.equals(questionInfo[2]) || wrongBody.equals(questionInfo[3])) && !(wrongBodyOrbit.equals(correctBodyOrbit))){
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
}