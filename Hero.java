/**
 * Hero.java
 * 
 * TCSS 143- Spring 2021
 * Programming Assignment 5
 */
import java.util.*;
/**
 * Class that represents the hero, contains stats and inventory info
 * @author Kyler Robison
 * @version 10 June 2021
 */
public class Hero 
{
    /**
     * Initialize fields to be used throughout the class
     */
    private String myName;

    private int myHitPoints, myHealingPotions, myVsionPotions, 
    myCrowns, myPotionsUsed, myTotalMoves, myTotalDamageTaken;
    
    private final int MAX_HITPOINTS = 100, MIN_STARTING_HITPOINTS = 70;

    /**
     * sets all the stats to 0 except for starting health which is randomly chosen within a range
     * @param theName name of the hero
     */
    public Hero(String theName)
    {
        myName = theName;
        setRandomHealth();
        myHealingPotions = 0;
        myVsionPotions = 0;
        myCrowns = 0;

        myTotalMoves = 0;
        myTotalDamageTaken = 0;
        myPotionsUsed = 0;
    }
    /**
     * Randomly sets health value within a range
     */
    private void setRandomHealth()
    {
        Random random = new Random();
        myHitPoints = MIN_STARTING_HITPOINTS + random.nextInt(MAX_HITPOINTS - MIN_STARTING_HITPOINTS);
    }
    /**
     * Highly crucial, non-redundant die method that simply tells DungeonAdventure that the game has ended
     */
    private void die()
    {
        DungeonAdventure.setGameOver();
    }
    /**
     * Handles the health increase and inventory update involved with drinking a potion, 
     * validates hero has a potion and also makes sure that the player can't be healed past max health
     */
    public void drinkPotion()
    {
        if(myHealingPotions <= 0)
        {
            System.out.println("No healing potions to drink!");
        }
        else
        {
            if(myHitPoints >= MAX_HITPOINTS)
                System.out.println("Max health, can't drink a healing potion!");
            else
            {
                Random rand = new Random();
                int value = 5 + rand.nextInt(10);
                if(myHitPoints + value > MAX_HITPOINTS)
                {
                    System.out.println("Healing potion added " + (MAX_HITPOINTS - myHitPoints) + " health\nYou now have " + MAX_HITPOINTS + " health");
                    myHitPoints = MAX_HITPOINTS;
                }
                else
                {
                    myHitPoints += value;
                    System.out.println("Healing potion added " + value + " health\nYou now have " + myHitPoints + " health" );
                }
                myPotionsUsed++;
                myHealingPotions--;
            }
        }
    }
    /**
     * Updates count of vision potion and potions used stat
     */
    public void drinkVisionPotion()
    {
        myVsionPotions--;
        myPotionsUsed++;
    }
    /**
     * Short but sweet method that randomly calculates how much damage the monster does to the hero during the fight
     * @return true if monster is killed
     */
    public boolean fightMonster()
    {
        Random rand = new Random();
        addDamage(20 + rand.nextInt(30));
        if(myHitPoints > 0)
            return true;
        else
            return false;     
    }
    /**
     * handles the falling in pit event, takes a damage parameter and determines if user survives or not
     * @param damage
     * @return
     */
    public boolean fallInPit(int theDamage)
    {
        addDamage(theDamage);
        if(myHitPoints > 0)
            return true;
        else
            return false;
    }
    /**
     * Does damage to the player and calls die method if health reaches 0
     * @param amount amount of damage done to the player
     */
    public void addDamage(int theAmount)
    {
        myHitPoints -= theAmount;
        myTotalDamageTaken += theAmount;
        if(myHitPoints <= 0)
        {
            myHitPoints = 0;
            die();
        }
    }
    /**
     * Increments the move count
     */
    public void addMove()
    {
        myTotalMoves++;
    }
    /**
     * Increments the crown count
     */
    public void addCrown()
    {
        myCrowns++;
    }
    /**
     * Increments the healing potion count
     */
    public void addHealingPotion()
    {
        myHealingPotions++;
    }
    /**
     * Increments the healing potion count
     */
    public void addVisionPotion()
    {
        myVsionPotions++;
    }  
    /**
     * 
     * @return the player myHitPoints
     */
    public int getHitPoints()
    {
        return myHitPoints; 
    }
    /**
     * 
     * @return # of myCrowns the player has
     */
    public int getCrowns()
    {
        return myCrowns;
    }
    /**
     * 
     * @return # of healing potions the player has
     */
    public int reportHealingPotions()
    {
        return myHealingPotions;
    }
    /**
     * 
     * @return # of vision potions the player has
     */
    public int reportVisionPotions()
    {
        return myVsionPotions;
    }
    /**
     * 
     * @return name of the hero
     */
    public String getName()
    {
        return myName;
    }
    /**
     * 
     * @return a detailed description of crown count to be displayed when a crown is obtained
     */
    public String reportCrowns()
    {
        if(myCrowns == 0)
            return "You need to find two more crown pieces to be able to escape the dungeon";
        if(myCrowns == 1)
            return "You need to find one more crown piece to be able to escape the dungeon";
        else    
            return "Both crown pieces found, find the exit to escape the dungeon and win the game!";
    }  
    /**
     * 
     * @return stats about the player, possible candidate for a little bit of extra credit
     */
    public String getStats()
    {
        String output = "";
        output += "~Hero Stats~\n";
        output += "Health: " + myHitPoints + "/" + MAX_HITPOINTS;
        output += "\nTotal damage taken: " + myTotalDamageTaken;
        output += "\nTotal moves made: " + myTotalMoves;
        output += "\nPotions used: " + myPotionsUsed;
        return output;
    }
    /**
     * Displays info about the player and their inventory
     */
    @Override
    public String toString()
    {
        String output = "";
        output += "Hero Name: " + myName;
        output += "\nHealth: " + myHitPoints + "/" + MAX_HITPOINTS;
        output += "\nHealing potions: " + myHealingPotions;
        output += "\nCrown pieces found: " + myCrowns;
        output += "\nVision potions: " + myVsionPotions;
        return output;
    }

}
