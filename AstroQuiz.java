package astronomy;
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
        System.out.println("Hello");
        
        //GUI
        editPlayer(JOptionPane.showInputDialog("Welcome!! Please enter your name: "));
        guiStart();

        //Questions

        //Queries

        
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
        QuizFrame test = new QuizFrame();
    }
}
