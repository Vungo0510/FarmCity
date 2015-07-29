package farmcity.entity;

import java.util.*;
import java.text.*;

/**
 * This class contains attributes, setter and getter methods for the Player entity
 * @author G3-T12
 */
public class Player {

    private String username;
    private String password;
    private String fullName;
    private Rank rank;
    private int xp;
    private int gold;
    private Calendar giftDate;
    private ArrayList<InventoryCrop> inventoryCropList;
    private ArrayList<Plot> plotList;
    private HashMap<Player,Character> friendsRequestsList;
	
	/**
	 * This constructor initializes a Player object by passing in the player's username (String),
	 * player's full name (String), player's password (String), player's current Rank as well as 
	 * his/ her current experience points (xp) and gold in int
	 * @param username Username of the player, fixed
	 * @param fullName Full name of the player, fixed
	 * @param password Password of the player, fixed
	 * @param rank Current Rank of the player
	 * @param xp current amount of XP the player has
	 * @param gold current amount of gold the player has
	 */
    public Player(String username, String fullName, String password, Rank rank, int xp, int gold) {
        this(username, fullName, password);
        this.rank = rank;
        this.xp = xp;
        this.gold = gold;

    }
	
	/**
	 * This constructor initializes a new Player object by passing in the player's username (String),
	 * player's full name (String), player's password (String), set the player 
	 * current experience points (xp) to 0 and gold to 1000 (in int)
	 * @param username Username of the player
	 * @param fullName Full name of the player
	 * @param password Password of the player
	 */
    public Player(String username, String fullName, String password) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.xp = 0;
        this.gold = 1000;

    }
	
	/**
	 * This constructor initializes a new Player object by passing in the player's username (String),giftDAte(Calendar)
	 * @param username Username of the player
	 * @param giftDate Date that a player was sent a gift
	 */
    public Player(String username, Calendar giftDate) {
        this.username = username;
        this.giftDate = giftDate;
    }
	
	/**
	 * Return Player's username
	 * @return return username of the Player in String
	 */
    public String getUsername() {
        return username;
    }
	
	/**
	 * Return Player's password
	 * @return return password of the Player in String
	 */
    public String getPassword() {
        return password;
    }
	
	/**
	 * Return Player's full name
	 * @return return full name of the Player in String
	 */
    public String getFullName() {
        return fullName;
    }
	
	/**
	 * Return Player's Rank
	 * @return return the Rank of the Player in String
	 */
    public Rank getRank() {
        return rank;
    }
	
	/**
	 * set the Player's current Rank to the Rank passed in
	 * @param rank the player's new Rank
	 */
    public void setRank(Rank rank) {
        this.rank = rank;
    }

	/**
	 * Return Player's xp
	 * @return return the xp of the Player in int
	 */
    public int getXP() {
        return xp;
    }
	
	/**
	 * set the Player's current xp to the xp passed in
	 * @param xp the player's new xp
	 */
    public void setXP(int xp) {
        this.xp = xp;
    }
	
	/**
	 * Return Player's gold amount
	 * @return return the gold of the Player in int
	 */
    public int getGold() {
        return gold;
    }
	
	/**
	 * set the Player's current gold to the gold passed in
	 * @param gold the player's new amount of gold
	 */
    public void setGold(int gold) {
        this.gold = gold;
    }
    
	/**
	 * set the Player's current list of inventory crop to the list passed in
	 * @param inventoryList the player's new ArrayList of InventoryCrop
	 */    
    public void setInventoryCropList(ArrayList<InventoryCrop> inventoryCropList) {
        this.inventoryCropList = inventoryCropList;
    }
    
	/**
	 * Return Player's list of available crop in his inventory
	 * @return return the list of crop in ArrayList of InventoryCrop
	 */  
    public ArrayList<InventoryCrop> getInventoryCropList() {
        return inventoryCropList;
    }
	
	/**
	 * set the Player's current list of Plots to the list of Plots passed in
	 * @param plotList the player's new ArrayList of Plots
	 */    
    public void setPlotList(ArrayList<Plot> plotList) {
        this.plotList = plotList;
    }
    
	/**
	 * Return Player's list of Plots
	 * @return return the list of Plots of the Player in ArrayList of Plot
	 */   
    public ArrayList<Plot> getPlotList() {
        return plotList;
    }
    
	/**
	 * Set the player's GiftDate to the date that the gift was sent to this player
	 * @param Calendar object that is the giftDate
	 */
    public void setGiftDate(Calendar giftDate) {
        this.giftDate = giftDate;
    }
	
	/**
	 * Return the player's GiftDate to the Calendar parameter that is passed in
	 * @returns the date that a gift was sent to this Player Object
	 */
    public Calendar getGiftDate() {
        return giftDate;
    }
	
	/**
	 * set the Player's current friend list to the list of friends passed in
	 * @param friendList of the player's HashMap of Friends/Requests
	 */   
    public void setFriendList(HashMap<Player,Character> friendsRequestList) {
        this.friendsRequestsList = friendsRequestsList;
    }
	
	 /**
	 * Return Player's Friend/Request HashMap
	 * @return return the friend list of the Player in HashMap
	 */ 
	public HashMap<Player,Character> getAllRequestsFriends() {
        return friendsRequestsList;
    }

}
