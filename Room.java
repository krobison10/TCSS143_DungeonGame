/**
 * Room.java
 * 
 * TCSS 143- Spring 2021
 * Programming Assignment 5
 */
import java.util.*;
/**
 * Room class that represents each room in the dungeon, contains variables about what it contains
 * @author Kyler Robison
 * @version 10 June 2021
 */
public class Room
{
    /**
     * Initialize fields that are used throughout the class
     */
    private boolean roomVisible = false, hasPlayer = false, hasHealing = false, hasVisionPotion = false, isDiscovered = false, 
    hasMonster = false, hasPit = false, hasCrown = false, isExit = false, isEntrance = false;

    private int myPosX, myPosY, contents;
    /**
     * constructs the room object and randomly decides if it will contain certain game objects
     * calls a method that counts the total contents of the room
     * @param x x coordinate of the room
     * @param y y coordinate of the room
     */
    public Room(int theX, int theY)
    {
        myPosX = theX;
        myPosY = theY;
        hasHealing = randomProb(10);
        hasPit = randomProb(10);
        hasMonster = randomProb(10);
        hasVisionPotion = randomProb(20);
        if(hasMonster)
            hasPit = false;
        countRoomContents();
    }
    /**
     * 
     * @param probability number used to set probablity, if 10, odds will be 1 in 10
     * @return true or false but based off of random probability
     */
    private boolean randomProb(int theProbability)
    {
        Random rand = new Random();
        if(rand.nextInt(theProbability) == 4)
            return true;
        else
            return false;

    }
    /**
     * Counts the room contents so that the toString method can know to return
     * M if there is more than one thing in the room
     */
    private void countRoomContents()
    {
        if(isEntrance)
            contents++;
        if(isExit)
            contents++;
        if(hasHealing)
            contents++;
        if(hasMonster)
            contents++; 
        if(hasVisionPotion)
            contents++;  
    }
    /**
     * Method that is called to remove other game objects if the room is set as
     * an entrance or exit
     */
    private void removeOthers()
    {
        hasHealing = false;
        hasMonster = false;
        hasVisionPotion = false;
    }
    /**
     * Sets the room as the entrance
     */
    public void setAsEntrance()
    {
        isEntrance = true;
        removeOthers();
    }
    /**
     * Sets the room as the exit
     */
    public void setAsExit()
    {
        isExit = true;
        removeOthers();
    }   
    /**
     * Adds a crown to the room
     */
    public void setCrown()
    {
        hasCrown = true;
    }
    /**
     * Tells the room if it has the player in it or not
     * @param theNew true if has player, false otherwise
     */
    public void hasPlayer(boolean theNew)
    {
        hasPlayer = theNew;
    }
    /**
     * Makes the room visible and sets the discovered field, 
     * this method enables the vision potion functionaility by
     * differentiating actually discovered and just visible rooms so 
     * during the next turn after a vision potion the rooms that havent been discovered
     * will be re-hidden.
     */
    public void discover()
    {
        this.makeVisible();
        isDiscovered = true;
    }
    /**
     * Tells the toString method to omit the question mark and display whatever is in the room
     */
    public void makeVisible()
    {
        roomVisible = true;
    }
    /**
     * Makes the room become invisible again
     */
    public void makeInvisible()
    {
        roomVisible = false;
    }
    /**
     * Removes the monster from the room and updates the contents count
     */
    public void killMonster()
    {
        hasMonster = false;
        contents--;
    }
    /**
     * Removes the pit after the player trips it
     */
    public void removePit()
    {
        hasPit = false;
        contents--;
    }
    /**
     * Removes the crown from the room when the hero picks it up
     */
    public void takeCrown()
    {
        hasCrown = false;
        contents--;
    }
    /**
     * Removes the healing potion from the room when the hero picks it up
     */
    public void takeHealingPotion()
    {
        hasHealing = false;
        contents--;
    }  
    /**
     * Removes the vision potion from the room when the player picks it up
     */
    public void takeVisionPotion()
    {
        hasVisionPotion = false;
        contents--;
    }
    /**
     * @return true if the room is an exit
     */
    public boolean isExit()
    {
        return isExit;
    }
    /**
     * @return true if room has healing potion
     */
    public boolean hasHealing()
    {
        return hasHealing;
    }
    /**
     * @return true if room has vision potion
     */
    public boolean hasVision()
    {
        return hasVisionPotion;
    }
    /**
     * @return true if room has a monster
     */
    public boolean hasMonster()
    {
        return hasMonster;
    }
    /**
     * @return true if room has a crown piece
     */
    public boolean hasCrown()
    {
        return hasCrown;
    }
    /**
     * @return true if room has a pit
     */
    public boolean hasPit()
    {
        return hasPit;
    }
    /**
     * @return true if room has been discovered by the player
     */
    public boolean isDiscovered()
    {
        return isDiscovered;
    }
    /**
     * @return letter to represent what is in the room
     */
    private String getLetter()
    {
        if(!roomVisible)
            return "? ";
        if(hasPlayer)
            return "C ";
        if(isEntrance)
            return "I ";
        if(isExit)
            return "O ";
        if(contents > 1)
            return "M ";
        if(hasCrown)
            return "# ";
        if(hasHealing)
            return "H ";
        if(hasPit)
            return "P ";
        if(hasMonster)
            return "X ";
        else
            return "  ";
    }
    /**
     * 
     * @return some information about the room
     */
    public String getInfo()
    {
        String output = "";
        output += "Room (" + myPosX + ", " + myPosY + ")\nAttributes\n";
        if(isExit)
            output += "The exit\n";
        if(isEntrance)
            output += "The entrance\n";
        if(hasCrown)
            output += "Has a crown piece\n";
        if(hasMonster)
            output += "Has a monster\n";
        if(hasPit)
            output += "Has a pit\n";
        if(hasHealing)
            output += "Has a healing potion\n";
        
        return output;
    }
    /**
     * Quite an odd toString method, it is designed with the gameboard in mind
     * and essentially takes in to account the position of each room and figures
     * out of it needs to display a wall or door and also adds spaces where needed.
     */
    @Override
    public String toString()
    {
        String output = "";
        //Form left side barrier
        if(myPosX == 0)
            output += "* "; 
        //if the room is hidden 
        output += getLetter();
        //put doors in between each room
        if(myPosX < 5)
            output += "| ";
        //form right side barrier
        if(myPosX == 5)
            output += "*";
        //return whole string
        return output;
    }
}