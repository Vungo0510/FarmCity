package farmcity.controller;

import java.util.*;
import java.io.*;
import farmcity.entity.*;
import farmcity.dao.*;
import farmcity.exception.*;


/**
 * This class manages harvest crop, plant crop functions as well as player's process of leveling up  
 * @author G3-T12
 * 
 */
public class ManageFriendController {

    private FriendDAO friendDAO;
    private PlayerDAO playerDAO;
	
	/**
	 * This method initialises the friendDAO and playerDAO  
	 * @throws CityException if the necessary files to initialise the application cannot be found
	 */
    public ManageFriendController() throws CityException {
			friendDAO = FriendDAO.getInstance();
			playerDAO = PlayerDAO.getInstance();
		
    }

    
    public HashMap<Player,Character> retrievePlayerFriends(String username) {
        return friendDAO.retrieveAllRequestsFriends(username);
    }
	
	/**
	 * This method sends a friend requst to the requested username
	 * @param requestUsername Username of the current player
	 * @param player The current Player
	 * @throws CityException if the player is sending a request to someone who is already on
	 * his friendlist OR if the player OR if the file to be saved to cannot be found. 
	 */
    public void requestFriend(Player player,String requestUsername) throws CityException{
    
        boolean playerExists = playerDAO.playerExists(requestUsername);

        if(!playerExists) {
            throw new CityException("InvalidUser");
        }
        
        HashMap<Player,Character> friendListMap = friendDAO.retrieveAllRequestsFriends(player.getUsername());

        Iterator<Player> friendListMapIter = friendListMap.keySet().iterator();
        
        while(friendListMapIter.hasNext()) {

            Player friend = friendListMapIter.next();
            if (friend.getUsername().equals(requestUsername) && friendListMap.get(friend) == 'f') {
                throw new CityException("Friend");
            }
			if (friend.getUsername().equals(requestUsername) && friendListMap.get(friend) == 'r') {
                throw new CityException("Request");
            }
            if(player.getUsername().equals(requestUsername)) {
                throw new CityException("SameUser");
            }
        }
        
        friendDAO.addRequest(player, requestUsername);
        friendDAO.save();  
    }
	
	
	/**
	 * This method deletes a friend from the current player's friendlist and at the same time delete the current player's name	 
	 * from that specific friend's friendlist
	 * @param username username of the current player
	 * @param friendUsername username of the friend to be deleted
	 * @throws CityException If there are problems in accessing the data file, throws CityException
	 */
    public void unfriend(String username, String friendUsername) throws CityException{
       friendDAO.delete(username,friendUsername);
       friendDAO.delete(friendUsername, username);
	   friendDAO.save();
	   
    }
	
	/**
	 * This method accepts a friend request from the current player's friendlist and at the same time adds the current player's name	 
	 * to that specific friend's friendlist
	 * @param player Player object of the current player
	 * @param friendUsername username of the friend to be added
	 * @throws CityException If there are problems in accessing the data file, throws CityException
	 */
    public void acceptFriend(Player player,String friendUsername) throws CityException{
        friendDAO.addFriend(player,friendUsername);
		friendDAO.save();
    }
	
	/**
	 * This method rejects a friend request from the current player's requestlist 
	 * @param username username of the current player
	 * @param friendUsername username of the friend to be added
	 * @throws CityException If there are problems in accessing the data file, throws CityException
	 */
    public void rejectFriend(String username, String friendUsername) throws CityException{
        friendDAO.delete(username, friendUsername);
		friendDAO.save();
    }
}
