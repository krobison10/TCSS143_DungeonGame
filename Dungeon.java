/**
 * Dungeon.java
 * 
 * TCSS 143- Spring 2021
 * Programming Assignment 5
 */
import java.util.*;
/**
 * The workhorse of the dungeon adventure game, initializes a game board, creates rooms,
 * handles myPlayer movements, game interactions and also serves as a bridge of communication
 * between DungeonAdventure and the Hero and Room classes
 * @author Kyler Robison
 * @version 10 June 2021
 */
public class Dungeon 
{
    /**
     * Initialize these fields so that they can be used throughout the class
     */
    private Room[][] myDungeonBoard = new Room[6][6];
    private int[] myplayerPosition = new int[2];
    private Room myCurrentRoom;
    public Hero myPlayer;
    private boolean usedVisionPotion;
    /**
     * Constructs a dungeon object and creates a hero object, passes off the work to initializeGameboard
     * @param theHeroName name for the hero
     */
    public Dungeon(String theHeroName)
    {
        initializeGameBoard();
        myPlayer = new Hero(theHeroName);
    }
    /**
     * First calls a method that populates the dungeon with room objects,
     * then begins to generate random points for the entrance and exit, and 
     * the crown pieces, most importantly, it ensures none of those overlap.
     */
    private void initializeGameBoard()
    {   
        populateArray();
        //Pick Player starting location/entrance
        int[] entrancePoint, exitPoint;
        do
        {
            entrancePoint = generatePoint();
            exitPoint = generatePoint();
        }
        while(entrancePoint == exitPoint);
        //set myPlayer position to the points generated
        myplayerPosition = entrancePoint;
        myCurrentRoom = myDungeonBoard[entrancePoint[1]][entrancePoint[0]];
        //tell the room that it is an entrance
        myCurrentRoom.setAsEntrance();
        //tell the room that it is an exit
        myDungeonBoard[exitPoint[1]][exitPoint[0]].setAsExit();
        //tell the room that it has the Player
        myCurrentRoom.hasPlayer(true);
        //tell the room to show it's contents
        myCurrentRoom.discover();
        
        putCrowns(entrancePoint, exitPoint);
    }
    /**
     * Populates the 2D array with room objects
     */
    private void populateArray()
    {
    //Populate 2d array with room objects
        for(int i = 0; i < 6; i++)
        {
            for(int j = 0; j < 6; j++)
            {
                myDungeonBoard[i][j] = new Room(j, i);
            }
        }
    }
    /**
     * Places crowns in two non-overlapping, random places in the board. 
     * Takes the entrance and exit points as a parameter to ensure that
     * the crowns don't overlap with those.
     * @param entrancePoint
     * @param exitPoint
     */
    private void putCrowns(int[] theEntrancePoint, int[] theExitPoint)
    {
        int[] firstCrown, secondCrown;
        //Generate values until both crowns are in unique points and also not conflicting with entrance or exit
        do
        {
            firstCrown = generatePoint();
            secondCrown = generatePoint();
        }
        while(secondCrown == firstCrown || firstCrown == theEntrancePoint || firstCrown == theExitPoint || secondCrown == theEntrancePoint || secondCrown == theExitPoint);
        //Inform rooms that they contain crowns
        myDungeonBoard[firstCrown[1]][firstCrown[0]].setCrown();
        myDungeonBoard[secondCrown[1]][secondCrown[0]].setCrown();
    }
    /**
     * Generates a random coordinate point on the board
     * @return int array with two data values for coordinates
     */
    private int[] generatePoint()
    {
        Random random = new Random();
        int[] coordinates = new int[2];
        coordinates[0] = random.nextInt(5);
        coordinates[1] = random.nextInt(5);
        return coordinates;
    }  
    /**
     * Move method that also validates if a move is in bounds, if not, the method essentially does nothing 
     * and the game loop has to loop around again for another try. If the move is valid it updates the 
     * myPlayer position and then calls a method to interact with the new room.
     * @param dir movement direction to be taken in to account for moving
     */
    public void move(String theDir)
    {
        boolean validMove = false;
        //save the previous position in case it is needed again
        int[] previousPosition = myplayerPosition;
        //tell the departing room that it no longer contains the myPlayer
        myDungeonBoard[myplayerPosition[1]][myplayerPosition[0]].hasPlayer(false);
        if(theDir.trim().equalsIgnoreCase("n"))
            if( (myplayerPosition[1] - 1) >= 0)
                myplayerPosition[1]--;
                validMove = true; 
        if(theDir.trim().equalsIgnoreCase("s"))
            if( (myplayerPosition[1] + 1) <= 5)
                myplayerPosition[1]++;
                validMove = true;   
        if(theDir.trim().equalsIgnoreCase("w"))
            if( (myplayerPosition[0] - 1) >= 0)
                myplayerPosition[0]--;
                validMove = true;
        if(theDir.trim().equalsIgnoreCase("e"))
            if( (myplayerPosition[0] + 1) <= 5)
                myplayerPosition[0]++;
                validMove = true;
        //if move is out of bounds reset the position back to previous and try again
        if(!validMove)
        {
            myplayerPosition = previousPosition;
            System.out.println("Invalid move, try again.");  
        }
        else
        {
            //update current move
            myCurrentRoom = myDungeonBoard[myplayerPosition[1]][myplayerPosition[0]];

            myPlayer.addMove();
            interactWithRoom();
        }
    }
    /**
     * Method that is intended to check if vision potion was used last turn
     * and if so, rehide the undiscovered rooms
     */
    public void hideUndiscoveredRooms()
    {
        if(usedVisionPotion)
        {
            usedVisionPotion = false;
        for(int i = 0; i < 6; i++)
        {
            for(int j = 0; j < 6; j++)
            {
                if(!(myDungeonBoard[i][j].isDiscovered()))
                {
                    myDungeonBoard[i][j].makeInvisible();
                }
            }
        }
        }
    }
    /**
     * Unmasks all of the rooms by looping through the array and calling the makeVisible method
     */
    public void revealRooms()
    {
        for(int i = 0; i < 6; i++)
        {
            for(int j = 0; j < 6; j++)
            {
                myDungeonBoard[i][j].makeVisible();
            }
        }
    }
    /**
     * Important method that handles many game functions, basically figures out what is in the room
     * then does and tells user what happens in that room, and updates the room object accordingly.
     */
    private void interactWithRoom()
    {
        myCurrentRoom.hasPlayer(true);
        myCurrentRoom.discover();
        if(myCurrentRoom.isExit() && myPlayer.getCrowns() >= 2)
        {
            DungeonAdventure.setGameWon();
            return;
        }
        if(myCurrentRoom.isExit() && myPlayer.getCrowns() < 2)
        {
            System.out.println("You have found the exit, but need to find " + (2 - myPlayer.getCrowns()) + " more crown piece(s) to escape and win..");
        }
        if(myCurrentRoom.hasHealing())
        {
            System.out.println("You found a healing potion!");
            myPlayer.addHealingPotion();
            myCurrentRoom.takeHealingPotion();
            System.out.println("You now have " + myPlayer.reportHealingPotions() + " healing potion(s). Enter 'U' on any turn to use");
        }
        if(myCurrentRoom.hasVision())
        {
            System.out.println("You found a vision potion!");
            myPlayer.addVisionPotion();
            myCurrentRoom.takeVisionPotion();
            System.out.println("You now have " + myPlayer.reportVisionPotions() + " vision potion(s). Enter 'V' on any turn to use");

        }
        if(myCurrentRoom.hasPit())
        {
            Random rand = new Random();
            int damage = 1 + rand.nextInt(19);
            System.out.println("You fall in to a pit, taking " + damage + " damage");
            boolean win = myPlayer.fallInPit(damage);
            if(win) 
            {
                myCurrentRoom.removePit();
                System.out.println("you now have " + myPlayer.getHitPoints() + " health");
            }
        }
        if(myCurrentRoom.hasMonster())
        {
            System.out.println("You encounter a monster");
            boolean win = myPlayer.fightMonster();
            if(win) 
            {
                myCurrentRoom.killMonster();
                System.out.println("You defeat the monster and now have " + myPlayer.getHitPoints() + " hitpoints remaining");
            }
        }
        if(myCurrentRoom.hasCrown())
        {
            System.out.println("You have found a crown piece!");
            myPlayer.addCrown();
            myCurrentRoom.takeCrown();
            System.out.println(myPlayer.reportCrowns());
        }
    }
    /**
     * Method that reveals rooms surrounding the myPlayer, but doesn't mark them as
     * discovered so that they can be rehidden later, also validates that there is an actual 
     * room to hide to avoid an indexOutOfBounds exception if trying to access a room outside of the array
     */
    public void drinkVisionPotion()
    {
        if(myPlayer.reportVisionPotions() > 0)
        {
            System.out.println("You drink a vision potion, revealing all adjacent rooms");
            usedVisionPotion = true;
            myPlayer.drinkVisionPotion();
            int x = myplayerPosition[0];
            int y = myplayerPosition[1];
            //check north
            if((y - 1) >= 0)
            {
                myDungeonBoard[myplayerPosition[1]-1][myplayerPosition[0]].makeVisible();
                System.out.println(myDungeonBoard[myplayerPosition[1]-1][myplayerPosition[0]].getInfo());
                //check northeast
                if((x + 1) < 6)
                {
                    myDungeonBoard[myplayerPosition[1]-1][myplayerPosition[0] + 1].makeVisible();
                    System.out.println(myDungeonBoard[myplayerPosition[1]-1][myplayerPosition[0] + 1].getInfo());
                }
                //check northwest
                if((x - 1) >= 0)
                {
                    myDungeonBoard[myplayerPosition[1]-1][myplayerPosition[0] - 1].makeVisible();
                    System.out.println(myDungeonBoard[myplayerPosition[1]-1][myplayerPosition[0] - 1].getInfo());
                }
            }
            //Check south
            if((y + 1) < 6)
            {
                myDungeonBoard[myplayerPosition[1]+1][myplayerPosition[0]].makeVisible();
                System.out.println(myDungeonBoard[myplayerPosition[1]+1][myplayerPosition[0]].getInfo());
                //check southeast
                if((x + 1) < 6)
                {
                    myDungeonBoard[myplayerPosition[1]+1][myplayerPosition[0] + 1].makeVisible();
                    System.out.println(myDungeonBoard[myplayerPosition[1]+1][myplayerPosition[0]+1].getInfo());
                }
                //check southwest
                if((x - 1) >= 0)
                {
                    myDungeonBoard[myplayerPosition[1]+1][myplayerPosition[0] - 1].makeVisible();
                    System.out.println(myDungeonBoard[myplayerPosition[1]+1][myplayerPosition[0]-1].getInfo());
                }
            }
            //Check east
            if((x + 1) < 6)
            {
                myDungeonBoard[myplayerPosition[1]][myplayerPosition[0]+1].makeVisible();
                System.out.println(myDungeonBoard[myplayerPosition[1]][myplayerPosition[0]+1].getInfo());
            }
            //Check west
            if((x - 1) >= 0)
            {
                myDungeonBoard[myplayerPosition[1]][myplayerPosition[0]-1].makeVisible();
                System.out.println(myDungeonBoard[myplayerPosition[1]][myplayerPosition[0]-1].getInfo());
            }
        }
        else
        {
            System.out.println("No vision potion to drink!");
        }
    }
    /**
     * Tostring method that returns a beautiful characterized console gameboard for user viewing
     */
    @Override
    public String toString()
    {
        String board = "";
        //Top border
        board += "\n\n* * * * * * * * * * * * *\n";
        //print rows
        for(int i = 0; i < 6; i++)
        {
            for(int j = 0; j < 6; j++)
            {
                board += myDungeonBoard[i][j].toString();    
            }
            if(i < 5)
                board += "\n* - * - * - * - * - * - *\n";
            else
                board += "\n";
        }
        //bottom border
        board += "* * * * * * * * * * * * *\n\n";
        //Add on information for each round
        board += "Health: " + myPlayer.getHitPoints();
        board += "\nPosition (" + myplayerPosition[0] + ", " + myplayerPosition[1]+ ")\n";
        return board;
    }
}
