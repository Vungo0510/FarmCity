package farmcity.dao;

import farmcity.entity.*;
import farmcity.exception.*;
import java.io.*;
import java.util.*;

/**
 * This class contains methods to retrieve and modify data from the FriendData CSV File
 * @author G3-T12
 */
public class FriendDAO {

    private final String FRIEND_DATA_DIRECTORY = "./data/FriendData.csv";
    private HashMap<String, HashMap<Player,Character>> friendMap;
    private static FriendDAO friendDAO;
	
	/**
	 * This constructor loads the players' friend lists and request lists.
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
	
    private FriendDAO() throws CityException{
        load();
    }
	
	
	/** This method initializes the Inventory Data Access Object
	 * @return Data Access Object for Inventory
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
    public static FriendDAO getInstance() throws CityException{
        if (friendDAO == null) {
            friendDAO = new FriendDAO();
        }

        return friendDAO;
    }
	
	/**
	 * This method set the current Player Data Access Object to null
	 */
    public static void destroyInstance() {
        if (friendDAO != null) {
            friendDAO = null;
        }
    }
	
	/**
	 * This method loads all pair of Friends and Requests into a HashMap
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
    public void load() throws CityException{

        friendMap = new HashMap<String,HashMap<Player,Character>>();
        
        HashMap<String, Player> allPlayers = PlayerDAO.getInstance().getAllPlayers();

        Iterator<String> playerIter = allPlayers.keySet().iterator();

        while (playerIter.hasNext()) {
            String username = playerIter.next();
            Player player = allPlayers.get(username);
            HashMap<Player,Character> friendListMap = new HashMap<Player,Character>();
            
            friendMap.put(username, friendListMap);
            player.setFriendList(friendListMap);
        }
        
        Scanner fileReader = null;

        try {
            fileReader = new Scanner(new File(FRIEND_DATA_DIRECTORY));
            
            fileReader.nextLine();
            
            while (fileReader.hasNext()) {

                String currentLine = fileReader.nextLine();
                String[] splitLine = currentLine.split(",");
                
                String username = splitLine[0];
                String friendUsername = splitLine[1];
                char status = splitLine[2].charAt(0);
                
                HashMap<Player,Character> friendListMap = friendMap.get(username);
                friendListMap.put(allPlayers.get(friendUsername), status);
            }
        } catch (FileNotFoundException e) {
			throw new CityException("InvalidFile");
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }
	
	/**
	 * This method return a HashMap of Friends and Requests of the Player who has the
     * specified username. The character represents the status of the relationship
     * (r for request and f for friend) 
	 * @param username username of the player whose friend list is to be returned
	 * @return the HashMap of the player's friend list
	 */
    public HashMap<Player,Character> retrieveAllRequestsFriends(String username) {
        return friendMap.get(username);
    }
	
	/**
     * This method return a HashMap of Friends of the Player who has the
     * specified username
     *
     * @param username username of the player whose friend list is to be
     * returned
     * @return the HashMap of the player's friend list
     */
    
    public HashMap<Player,Character> retrieveAllFriends(String username) {
	
        HashMap<Player,Character> friendsRequestMap = friendMap.get(username);
        HashMap<Player,Character> friendsListMap = new HashMap<Player,Character>();
        
		Iterator<Player> friendsRequestMapIter = friendsRequestMap.keySet().iterator();
        
		while(friendsRequestMapIter.hasNext()) {
			Player player = friendsRequestMapIter.next();
			
			char status = friendsRequestMap.get(player);
			
            if(status == 'f') {
                friendsListMap.put(player,status);
            }
        }
        
        return friendsListMap;
    }
    
   /**
	 * This method adds a new friend to the HashMaps of friends of the current Player 
	 * @param player Player object of the current player
	 * @param friendUsername Username of the Friend to be added
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */  
    public void addFriend(Player player,String friendUsername) throws CityException{
        
        HashMap<Player,Character> friendListMap = friendMap.get(player.getUsername());
        
        Iterator<Player> friendListMapIter = friendListMap.keySet().iterator();
		
		while(friendListMapIter.hasNext()) {
			Player currentPlayer = friendListMapIter.next();
			char status = friendListMap.get(currentPlayer);
			
            if(currentPlayer.getUsername().equals(friendUsername)) {
                friendListMap.put(currentPlayer,'f');
            }
        }
        
        HashMap<Player,Character> requestFriendListMap = friendMap.get(friendUsername);
        
        requestFriendListMap.put(player, 'f');

    }
    
	
	/**
	 * This method adds a new Friend request to the ArrayList of Friends of the current Player and save player's current progress
	 * @param player Player of the current player
	 * @param friendUsername Username of the player whose request to be added
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */ 
    public void addRequest(Player player,String friendUsername) throws CityException{
        
        HashMap<Player,Character> friendListMap = friendMap.get(friendUsername);
        
        friendListMap.put(player, 'r');

    }

	/**
	 * This method removes a friend to the ArrayList of Friends of the current Player and save player's current progress
	 * @param username Username of the current player
	 * @param friendUsername Username of the Friend to be removed
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */  
    public void delete(String username, String friendUsername) throws CityException{

        HashMap<Player,Character> friendListMap = friendMap.get(username);
        
        Iterator<Player> friendListMapIter = friendListMap.keySet().iterator();

        while (friendListMapIter.hasNext()) {
            String currentUsername = friendListMapIter.next().getUsername();
            
            if(currentUsername.equals(friendUsername)) {
                friendListMapIter.remove();
            }
        }

    }
    
	 /**
	 * This method allows player to save his/ her Players' current Friends & Requests progress to the database file.
	 * @throws CityException if there is something wrong with writing to the file
	 */       

    public void save() throws CityException {

        File file = new File(FRIEND_DATA_DIRECTORY);
       
        PrintStream fileWriter = null;

        try {
            
            fileWriter = new PrintStream(new FileOutputStream(file));
            fileWriter.println("Username,Friend,Status");
            
            Iterator<String> friendMapIter = friendMap.keySet().iterator();
            
            while(friendMapIter.hasNext()) {
                String username = friendMapIter.next();
                HashMap<Player,Character> friendListMap = friendMap.get(username);
                
                Iterator<Player> friendListMapIter = friendListMap.keySet().iterator();

                while(friendListMapIter.hasNext()) {
                    Player friend = friendListMapIter.next();
                    char status = friendListMap.get(friend);

                    fileWriter.print(username);
                    fileWriter.print(",");
                    fileWriter.print(friend.getUsername());
                    fileWriter.print(",");
                    fileWriter.print(status);   
                    fileWriter.println();
                }
            }
        } catch (IOException e) {
            throw new CityException("UnableWrite");
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }
}
