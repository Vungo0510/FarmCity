package farmcity.ui;

import farmcity.entity.*;
import farmcity.controller.*;
import farmcity.ui.*;
import farmcity.exception.*;
import java.util.*;

/**
 * This UI represents the Inventory Menu of the game, which is displayed after 
 * the player choose to visit his/her inventory in the main menu. This UI consists
 * of functions for the player to manage his/her inventory (Buy additional bags of seeds,
 * send bag(s) of seeds as gift(s) to his/ her friend(s) or go back to the main menu
 * according to player's input
 * @author G3-T12
 */
public class InventoryUI {
    private ManageInventoryController inventoryController;
    private Player player;
    private ShopUI shopUI;
    private GiftUI giftUI;
	
	/**
	 * This constructor initializes a new InventoryController object which takes in a
	 * Player object which represents the current Player.
	 * @param player The current Player
	 */        
    public InventoryUI(Player player) {
        try {
			this.player = player;
			inventoryController = new ManageInventoryController();
			shopUI = new ShopUI(player);
			giftUI = new GiftUI(player);
		} catch (CityException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
    }
    
	/** 
	 * This method executes the Inventory Menu and prints out the Player's details as
	 * well as his/ her current stocks of bags of seeds so he/ she can choose what to do
	 * with his/ her seeds. The method then proceed to the corresponding function or go
	 * back to the main menu according to user's input.
	 */   
    public void execute () {
        Scanner sc = new Scanner(System.in);
        String username = player.getUsername();

        String userInput = " ";
        char option = ' ';
        
        do {
            
                    
            System.out.println("== Farm City :: My Inventory ==");
            System.out.println("Welcome, " + player.getFullName());
            System.out.println("Rank : " + player.getRank().getRankName() + "\t" + "Gold : " + " " + player.getGold());
            System.out.println();
            
            System.out.println("My seeds: ");
            ArrayList<InventoryCrop> inventoryCropList = player.getInventoryCropList();
            

            int count = 1;

            for(InventoryCrop inventoryCrop: inventoryCropList){
                Crop crop = inventoryCrop.getCrop();
                int quantity = inventoryCrop.getQuantity();
                System.out.println(count + "." + quantity + " Bags of " + crop.getCropName());
                count++;
            }
            System.out.println();

            System.out.print("[M]ain | [B]uy | [G]ift | Select choice > ");
            userInput = sc.next();
            option = userInput.toUpperCase().charAt(0);
			
			if(userInput.length() == 1) {
				switch (option) {
					case 'B':
						processBuySeeds();
						break;
					case 'G':
						processGift();
						break;
					default:
						System.out.println("Please enter a valid input!");
						break;
				}
			}else {
				System.out.println("Please enter a valid input!");
			}
			
			System.out.println();
			
        } while (option != 'M');    
    }
	
	/**
	 * This method allows user to buy additional seeds by calling the execute method 
	 * of shopUI. 
	 */ 
    public void processBuySeeds() {
        shopUI.execute();
    }
	
	/**
	 * This method allows user to send seeds as gift(s) to his/ her friend(s)
	 * by calling the execute method of giftUI. 
	 */   
    public void processGift() {
        giftUI.execute();
    }
}
    
