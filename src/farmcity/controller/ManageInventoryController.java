package farmcity.controller;

import farmcity.entity.Player;
import java.util.*;
import java.text.*;
import farmcity.dao.*;
import farmcity.entity.*;
import farmcity.exception.*;

/**
 * This class manages Inventory functions such as buying seeds and gifting to other players
 * 
 */

public class ManageInventoryController {

    private InventoryDAO inventoryDAO;
    private CropDAO cropDAO;
    private GiftDAO giftDAO;
    private FriendDAO friendDAO;
    private PlayerDAO playerDAO;
	
	/**
	 * This class manages harvest crop, plant crop functions as well as player's process of leveling up  
	 * @author G3-T12
	 * @throws CityException if the necessary files to initialise the application cannot be found
	 */
	
    public ManageInventoryController() throws CityException{
			inventoryDAO = InventoryDAO.getInstance();
			cropDAO = CropDAO.getInstance();
			giftDAO = GiftDAO.getInstance();
			friendDAO = FriendDAO.getInstance();
            playerDAO = PlayerDAO.getInstance();
        
    }
    
	/**
     * This method allows a player to send bag(s) of seeds as gift(s) to his
     * friend(s). This will add one bag of seeds of the particular Crop that the
     * Player chose to his/ her friend(s)' inventory and deduct the same amount
     * of bag(s) of seeds of the Crop in his/ her own inventory
     *
     * @param username Username of the current player
     * @param selectedCrop The Crop that player wishes to send to his friend(s)
     * @param giftNames the HashMap that keeps track of list of friends that the
     * Player has sent gift(s) to
     * @return the updated HashMap giftNames after the gift(s) have been
     * successfully sent to his friend(s)
     * @throws CityException if the necessary files to initialise the
     * application cannot be found, throws CityException
     */
    public HashMap<String,Character> sendGifts(String username,Crop selectedCrop,HashMap<String,Character> giftNames) throws CityException {
        
        HashMap<Player, Character> friendListMap = friendDAO.retrieveAllFriends(username);

		Iterator<String> giftNamesIter = giftNames.keySet().iterator();
       
        while(giftNamesIter.hasNext()) {
            
            String giftUsername = giftNamesIter.next();

            Iterator<Player> friendListMapIter = friendListMap.keySet().iterator();
            
            while(friendListMapIter.hasNext()) {
                Player player = friendListMapIter.next();
                String friendUsername = player.getUsername();
                
                if(friendUsername.equals(giftUsername)) {
                    giftNames.put(giftUsername,'v');
                }
            }
        }

        ArrayList<Player> sentList = giftStatusUpdate(username);
		
        int giftCount = sentList.size();
        int giftLeft = 5-giftCount;
        int giftSent = 0;
        
        giftNamesIter = giftNames.keySet().iterator();
        
        while(giftLeft != 0 && giftNamesIter.hasNext()) {
            
            String giftUsername = giftNamesIter.next();
            char status = giftNames.get(giftUsername);
            
            if(status == 'v') {
                
                boolean gifted = false;
                
                for(Player receiver: sentList) {
                    if(receiver.getUsername().equals(giftUsername)) {
                        gifted = true;
                    }
                }
               
                if(gifted == false) {
                    
                    Player newReceiver = new Player(giftUsername, new GregorianCalendar());

					boolean success = inventoryDAO.updateInventory(username,new InventoryCrop(selectedCrop, -1));

					if(success) {

						inventoryDAO.updateInventory(giftUsername,new InventoryCrop(selectedCrop,1));
						giftNames.put(giftUsername, 's');

                        Calendar currentDate = new GregorianCalendar();

                        giftDAO.addGift(username, newReceiver);
						giftDAO.save();
					}
                }
            }
        }        
        
        inventoryDAO.save();
        return giftNames;
        
    }
    /**
     * This method checks the status of a player's gifting (whether he can send
     * gift to his friend(s)) at the time when the user choose the Gift function
     * in his Inventory
     *
     * @param username Username of the Player that chooses to send a Gift
     * @return The ArrayList of Player which represents friends that this Player
     * cannot send Gift to
     * @throws CityException if the necessary files to initialise the
     * application cannot be found, throws CityException
     */
    public ArrayList<Player> giftStatusUpdate(String username) throws CityException {
        ArrayList<Player> sentList = giftDAO.getGiftList(username);
        
        Date currentTime = new Date();
        
        Iterator<Player> sentListIter = sentList.iterator();

        while(sentListIter.hasNext()){
            
            Player receiver = sentListIter.next();
            
            Calendar dateSent = receiver.getGiftDate();
            
            int day = dateSent.get(Calendar.DAY_OF_MONTH);
            int month = dateSent.get(Calendar.MONTH);
            int year = dateSent.get(Calendar.YEAR);
            
            Calendar nextDay = new GregorianCalendar(year,month,day,0,0,0);
            nextDay.add(Calendar.DAY_OF_MONTH,1);
            
            Calendar currentDay = new GregorianCalendar();
            
            if(currentDay.after(nextDay) || currentDay.equals(nextDay)) {
                sentListIter.remove();
            }
        }
        giftDAO.save();
        return sentList;
		
    }
    
	
	/**
     * This method allows a user to purchase bag(s) of seeds of Crop(s) from the
     * Shop. If the purchase is successful, add the amount of bag(s) of seeds of
     * Crop(s) to the player's inventory and deduct (price of Crop * number of
     * bags of seeds purchased) Gold from the player's gold
     *
     * @param player The Player who chooses to Buy in Shop Menu
     * @param crop the Crop that player chose to purchase
     * @param quantity the number of bag(s) of seeds that the Player choose to
     * purchase
     * @throws CityException if the player doesn't have enough gold to purchase
     * the specified number of bag(s) of seeds of the particular Crop
     */
    public void purchaseCrop(Player player, Crop crop, int quantity) throws CityException{
        

        int totalCost = crop.getCost()*quantity;
        int playerGold = player.getGold();

        if(totalCost > playerGold) {
            throw new CityException("LackOfGold");
        }

        player.setGold(playerGold-totalCost);

        inventoryDAO.updateInventory(player.getUsername(), new InventoryCrop(crop,quantity));
		inventoryDAO.save();
        playerDAO.save();
    }
    
	/**
	* This method return all of the player's available Crop
	* @return The ArrayList of Crop that player has in his/ her inventory
	*/
    public ArrayList<Crop> getAllCrops() {
        return cropDAO.getAllCrops();
    }
   

}
