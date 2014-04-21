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
    
    private static String player;

    public static void main(String[] args){
        
        //GUI
        editPlayer(JOptionPane.showInputDialog("Welcome! Please enter your name"));
        guiStart();

        //Connecting to our DataBase
        try {
            dbConnection();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } 

        //Questions
        //Queries
    }

    /*
      Connects to our database and allows for questions to be
      generated, queries made and results to be shown
    */
    public static void dbConnection() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:385Project.db");
            
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT Name FROM PLANET");
            while(result.next()){
                System.out.println(result.getString("Name"));
            }
            
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }

    /*
      Changes the name of the current player
    */
    public static void editPlayer(String name){
        player = name;
    }

    /*
      Starts the GUI of the quiz
    */
    public static void guiStart(){
        QuizFrame test = new QuizFrame(player);
    }
}
