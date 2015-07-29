package farmcity.ui;

import farmcity.entity.*;
import farmcity.controller.*;
import farmcity.exception.*;
import java.util.*;

/**
 * This UI represents the Gift Menu of the game, which is displayed after the
 * Player choose to send a Gift to his/ her friend(s) in the Inventory Menu.
 *
 * @author G3-T12
 */
public class GiftUI {
    
    private Player player;
    private ManageInventoryController manageInventoryController;
    
    public GiftUI(Player player) {
        this.player = player;
		try {
			manageInventoryController = new ManageInventoryController();
		} catch (CityException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
    }
    
	/**
	 * This method executes the Gift Menu and prints out the gifts for the player to send to his/ her friend(s).
	 * The method then prompts for user's choice of Crop he/ she wants to send
	 * and proceed to ask for the username of the Player he/ she wants to send to Crop to
	 * If the gifting process is successful, this method adds one bag of seeds of the particular Crop to each of the player's inventory
	 * and deduct the number of bags sent successfully from the current player's inventory
	 */
    public void execute() {
        
        Scanner sc = new Scanner(System.in);
        
        
        System.out.println("== Farm City :: Send a Gift ==");
        System.out.println("Welcome, " + player.getFullName());
        System.out.println("Rank: " + player.getRank().getRankName() + "\t Gold:" + player.getGold());
        System.out.println();
        System.out.println("Gifts Available:");
        
        ArrayList<Crop> cropList = manageInventoryController.getAllCrops();
        
        for(int i = 0; i < cropList.size(); i++) {
            Crop crop = cropList.get(i);
            
            String cropName = crop.getCropName();
            
            System.out.println((i+1) + ". 1 Bag of " + cropName + "Seeds");
        }
        
        System.out.print("[M]ain | Select choice > ");
        int choice = sc.nextLine().charAt(0) - '0';
        
        if(!(choice <= cropList.size() && choice > 0)) {
            System.out.println("Please enter a valid choice!");
            return;
        }
        
		Crop selectedCrop = cropList.get(choice-1);
        
        System.out.print("Send to> ");
        
        String playerString = sc.nextLine();
        String[] splitPlayerString = playerString.split(",");
        
        HashMap<String,Character> giftNames = new HashMap<String,Character>();
        
        for(String playerName: splitPlayerString) {
            giftNames.put(playerName.trim(),'n');
        }
        
        
        try {
		
			HashMap<String, Character> giftMap = manageInventoryController.sendGifts(player.getUsername(),selectedCrop,giftNames);
		 
       
       
		   String successList = "";
		   String failList = "";
		   
		   Iterator<String> giftNamesIter = giftNames.keySet().iterator();
	 
		   
			while(giftNamesIter.hasNext()) {
			   String giftName = giftNamesIter.next();
			   char status = giftNames.get(giftName);

			   if(status == 's') {
				   successList += giftName + ",";
			   } else {
				   failList += giftName + ",";
			   }
			}
		   
			if(successList.length() > 0) {
			   System.out.println("Gift(s) successfully sent to " + successList.substring(0,successList.length()-1));
			}
		   
			if(failList.length() > 0) {
			   System.out.println("Gift(s) were not sent to " + failList.substring(0, failList.length()-1));
			}
		}
		catch (CityException e){
			System.out.println(e.getMessage());
			
		}
		System.out.println();
       
    }
}
