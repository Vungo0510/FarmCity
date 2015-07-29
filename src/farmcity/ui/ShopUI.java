package farmcity.ui;

import java.util.*;
import farmcity.entity.*;
import farmcity.controller.*;
import farmcity.exception.*;

/**
 * This UI represents the Shop Menu of the game, which is displayed after the Player
 * chooses to buy additional seeds in the Inventory Menu
 * @author G3-T12
 */
public class ShopUI {
    private Player player;
    private ManageInventoryController manageInventoryController;
    private ManageFarmController manageFarmController;
    
	/**
	 * This constructor initializes a new ManageInventoryController and a
	 * new ManageFarmController by passing in the Player object which represents 
	 * the current player.
	 * @param player The current player
	 */   
    public ShopUI (Player player) {
        this.player = player;
		try {
			manageInventoryController = new ManageInventoryController();
			manageFarmController = new ManageFarmController();
		}
		catch (CityException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
    }
    
	/**
	  * This method executes the Shop Menu and prints out the Player's details
	  * as well as all of the seeds available for sale with their details (name,
	  * cost, maturity time and xp gained). The method then prompts for player's input
	  * for the crop he/ she wants to purchase as well as the quantity if chooses not to
	  * return to main menu. After that, the method proceed to update the player's inventory
	  * through ManageInventoryController's updateInventory method, update player's gold
	  * through Player's addGold method and display a success message then goes back 
	  * to the Shop Menu
	  */ 
    public void execute() {
        Scanner sc = new Scanner(System.in);
 
        int choice = 0;
        ArrayList<Crop> cropList;
        
        do {
            System.out.println("== Farm City :: Store ==");
            System.out.println("Welcome, " + player.getFullName());
            System.out.println("Rank : " + player.getRank().getRankName() + "\t" + "Gold : " + " " + player.getGold());
            System.out.println();
            System.out.println("Seeds available: ");
            cropList = manageInventoryController.getAllCrops();
            
            for (int i = 0; i < cropList.size(); i++) {
                Crop crop = cropList.get(i);
                System.out.println(i+1 + ". " + crop.getCropName() + " cost: " + crop.getCost() +
                    " gold");
                if (crop.getMaturityTime() > 60) {
                    System.out.println("Harvest in: " + crop.getMaturityTime()/60 + " hours");
                } else {
                    System.out.println("Harvest in: " + crop.getMaturityTime() +  " mins");
                }
                System.out.println("XP gained: " + crop.getXP());
            }
            
            System.out.println();
            System.out.print("[M]ain | Select choice > ");
            String userInput = sc.nextLine().toUpperCase();
			
			
			if(userInput.length() > 0) {
				choice = userInput.charAt(0);
			}
				
				
			if (userInput.length() > 0 && choice != 'M'){

			
				try {
				
					choice = Integer.parseInt(userInput) - 1;
					
					if(choice >= 0 && choice < cropList.size()){
					
						System.out.print("  Enter quantity > ");
						String inputQuantity = sc.nextLine();
					
					
					
						int quantity = Integer.parseInt(inputQuantity);

						Crop crop = cropList.get(choice);
						
						manageInventoryController.purchaseCrop(player,crop,quantity);
					
					
						System.out.println(quantity + " bags of seeds purchased for " + (quantity*crop.getCost()) + " gold.");   
					
					
					}else {
						System.out.println("Please enter a valid input!");
					}
				}
				catch(NumberFormatException e){
					System.out.println("Please enter a valid number");
				}				
				catch (CityException e) {
					System.out.println(e.getMessage());
				}
			}
			
			System.out.println();
        } while (choice != 'M');
    }
}
