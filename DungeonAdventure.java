/**
 * DungeonAdventure.java
 * 
 * TCSS 143- Spring 2021
 * Programming Assignment 5
 */
import java.util.*;
/**
 * Driver for the dungeon adventure game, mainly just gathers user inputs and 
 * maintains a gameloop until a win or loose condition is met, all while 
 * providing help and instructions
 * @author Kyler Robison
 * @version 10 June 2021
 */
public class DungeonAdventure 
{
    /**
     * Initialize win and loose condition variables so they can be used throughout the class
     */
    private static boolean myGameOver = false;
    private static boolean myGameWon = false;
    /**
     * Main method that displays the welcome message, creates a scanner, then hands the work off to gameLoop
     * @param args
     */
    public static void main(String args[])
    {
        System.out.println("Welcome to the Dungeon Adventure game!\nFind the crowns and exit the dungeon to win!");
        System.out.println("Type a name for your hero and press enter!");
        Scanner readInput = new Scanner(System.in);     
        String heroName = readInput.nextLine();
        gameLoop(readInput, heroName);
    }
    /**
     * Essentially just collects input and loops until game is over
     * @param readInput Scanner that reads the user input
     * @param theHeroName Name for the hero
     */
    public static void gameLoop(Scanner readInput, String theHeroName)
    {
        String userInput;
        Dungeon dungeon = new Dungeon(theHeroName);
        
        while(!myGameWon && !myGameOver)
        {
            System.out.println(dungeon.toString());
            //System.out.println(dungeon.getCurrentRoom().getInfo());
            System.out.println("Enter H for help");
            userInput = readInput.nextLine();
            if(userInput.trim().equalsIgnoreCase("H")){printHelp(); userInput = readInput.nextLine();}
            if(userInput.trim().equalsIgnoreCase("K")){printKey(); userInput = readInput.nextLine();}
            if(userInput.trim().equalsIgnoreCase("T")){System.out.println(dungeon.myPlayer.getStats()); }
            if(userInput.trim().equalsIgnoreCase("Y")){System.out.println(dungeon.myPlayer.toString()); }
            if(userInput.trim().equalsIgnoreCase("U")){dungeon.myPlayer.drinkPotion();}
            if(userInput.trim().equalsIgnoreCase("V")){dungeon.drinkVisionPotion();}
            if(userInput.trim().equalsIgnoreCase("R"))
            {
                dungeon.revealRooms(); 
                System.out.println(dungeon.toString()); 
                System.out.println("Rooms Revealed!");
                userInput = readInput.nextLine();
            }
            if(userInput.trim().equalsIgnoreCase("N") || userInput.trim().equalsIgnoreCase("s") || userInput.trim().equalsIgnoreCase("e") || userInput.trim().equalsIgnoreCase("w"))
            {
                dungeon.move(userInput);
                dungeon.hideUndiscoveredRooms();
            }
        }
        if(myGameOver)
            System.out.println("You died! Better luck next time");
        if(myGameWon)
            System.out.println("You exited the Dungeon and won!");
        System.out.println(dungeon.myPlayer.getStats());
        readInput.close();
    }
    /**
     * Sets the game over condition
     */
    public static void setGameOver()
    {
        myGameOver = true;
    }
    /**
     * Sets the game won condition
     */
    public static void setGameWon()
    {
        myGameWon = true;
    }
    /**
     * Prints help for commands when user requests
     */
    public static void printHelp()
    {
        System.out.println("Help Page:");
        System.out.println("Move by typing your intended direction, N, S, E, W, and pressing enter");
        System.out.println("To use a healing potion, type U");
        System.out.println("To display map legend, type K");
        System.out.println("To print player's current stats, type T");
        System.out.println("To print player's information, type Y");
        System.out.println("To reveal the entire dungeon, type R");
        System.out.println("\nPress enter or make a move to continue");
    }
    /**
     * Prints the symbol key when user requests
     */
    public static void printKey()
    {
        System.out.println("Key:");
        System.out.println("C- Hero Position");
        System.out.println("?- Undiscovered Room");
        System.out.println("I- Entrance(In)");
        System.out.println("O- Exit(Out)");
        System.out.println("M- Multiple Items");
        System.out.println("#- Crown Piece");
        System.out.println("X- Monster");
        System.out.println("P- Pit");
        System.out.println("V- Vision Potion");
        System.out.println("\nPress enter or make a move to continue");
    }
}
