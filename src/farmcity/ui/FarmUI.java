package farmcity.ui;

import farmcity.controller.ManageFarmController;
import java.util.*;
import farmcity.dao.*;
import farmcity.entity.*;
import farmcity.exception.*;

/**
 * This user interface (UI) represent the Farm Menu of the game, which is displayed after
 * the player chooses to visit his farm in the main menu. This UI consists of functions for the player
 * to plant a new Crop on one of his Plot, clear wilted crop(s) or harvest all of the harvestable crop(s)
 * @author G3-T12
 */
public class FarmUI {
	
    private ManageFarmController manageFarmController;
    private Player player;
	
	
	/**
	 * This constructor initializes the ManageFarmController object by passing in the player object
	 * which represents the current player
	 * @param player The current player
	 */
    public FarmUI(Player player) {
        this.player = player;
		try {
			manageFarmController = new ManageFarmController();
		} catch(CityException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
    }

	/**
	 * This method executes Farm Menu and prints out the UI to user's screen for
	 * him/ her to choose what he/ she wants to do. Then it takes in
	 * player's input and proceed to the corresponding method or exit to main menu.
	 */
    public void execute() {

        char choice = '0';
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("== Farm City :: My Farm ==");//UI Display
            System.out.println("Welcome, " + player.getFullName());
            System.out.println("Rank : " + player.getRank().getRankName() + " \t Gold: " + player.getGold());
            System.out.println();

            //Initialise UI with the information of each plot of land that the user has
            displayPercentage();

            //Initialise plot of lands that user has//
            //Display Menu options
            System.out.print("[M]ain | [P]lant | C[L]ear | [H]arvest > ");
            String userInput = sc.nextLine().toUpperCase();

            //Used to check for planting crop options
            if (userInput.length() > 0) {

                choice = userInput.charAt(0);

                int plotNum = -1;
				
				try {
				
					if(userInput.length() == 2 && (choice == 'L' || choice == 'P')) {
						plotNum = userInput.charAt(1) - '1';
					}
						
						
					switch (choice) {
						case 'L':
							clearPlotProcess(plotNum);
							break;
						case 'M':
							break;
						case 'H': 
							harvestCropProcess(player);//Call harvest Crop function
							break;
						case 'P':
							plantCropProcess(plotNum);
							break;

						default:
							System.out.println("Please enter a valid choice!");
							break;
							
					}
						
					
				} catch (CityException e) {
					System.out.println(e.getMessage());
				} catch (NumberFormatException e) {
					System.out.println("Please enter a valid input!");
				}
            } else {
                System.out.println("Please enter a valid choice!");
            }
			System.out.println();
			
        } while (choice != 'M');
    }
	
	/**
	 * This method displays the status of the player's plots (the Crop planted,
	 * the percentage growth or the status of the plot) and prints the graphical
	 * representation of the farm to user's screen
	 */
    public void displayPercentage() {

        manageFarmController.updatePercentages(player);

        ArrayList<Plot> plotData = player.getPlotList();

        System.out.println("You have plots of " + plotData.size() + " land.");

        for (int i = 0; i < plotData.size(); i++) {

            Plot plot = plotData.get(i);
            char status = plot.getStatus();
            int percentage = plot.getPercentage();

            System.out.print((i + 1) + ". ");

            if (plot.getCrop() != null) {
                System.out.print(plot.getCrop().getCropName() + "\t");

                System.out.print("[");

                if (status == 'h' || status == 'p') {

                    if (percentage > 100) {
                        percentage = 100;
                    }

                    int statusBar = percentage / 10;
                    int leftOver = 10 - statusBar;

                    for (int j = 0; j < statusBar; j++) {
                        System.out.print("#");
                    }
                    for (int k = 0; k < leftOver; k++) {
                        System.out.print(" ");
                    }

                    System.out.print("]");

                    System.out.print(percentage + "%");
                } else {
                    System.out.print("  Wilted  ");
                    System.out.print("]");
                }
            } else {
                System.out.print("<empty>");
            }
            System.out.println();

        }
    }
	
	
	/**
	 * This method allows a user to plant a Crop on one of his/ her Plot at his/ her choice.
	 * It takes in the plot Number which represents the Plot and ask the player for the type
	 * of Crop that he/ she wants to plant on that Plot if the Plot is valid. Then the method
	 * will proceed to plantCrop function in manageFarmController to plant the crop 
	 * or go back to the main menu depends on player's choice
	 * @param plotNum the plot number which represents the plot.
	 * @throws CityException If the plant Crop process is unsuccessful, throws CityException 
	 */
    public void plantCropProcess(int plotNum) throws CityException{
         //= manageInventoryController.getAllInventoryCrop(); //Plant crop function
        ArrayList<Plot> plotData = player.getPlotList();

        Scanner sc = new Scanner(System.in);
		
        char cropSelect = 0;
        System.out.println();

        if (plotData.size() < plotNum || plotNum < 0) {
            System.out.println("Please enter a valid plot");
            return;
        }

        if (plotData.get(plotNum).getCrop() != null) { //Validation check in case user enters number of a plot that already has something planted
            System.out.println("Please select an empty plot!");
            return;
        }

        ArrayList<InventoryCrop> inventoryCropList = player.getInventoryCropList();

        if (inventoryCropList.size() > 0) {//Validation check to ensure that user has something to plant

            do {
                System.out.println("Please select a crop");

                for (int i = 0; i < inventoryCropList.size(); i++) { //Iterate through user's list of invetory crops and displays it out
                    System.out.println((i + 1) + "." + inventoryCropList.get(i).getCrop().getCropName());
                }

                System.out.print("[M]ain | Select Choice > ");
                cropSelect = sc.next().charAt(0);
                sc.nextLine();

            } while (cropSelect != 'M' && ((cropSelect - '0') <= 0 || (cropSelect - '0') > inventoryCropList.size()));
                                                        //Validation check to ensure that user enters either 'M' or selects a number that is valid//

            //Run plantCrop within DAO if user chooses a valid option
            if (cropSelect != 'M') {
                InventoryCrop selectedCrop = inventoryCropList.get(cropSelect - '1');

                manageFarmController.plantCrop(player.getUsername(),selectedCrop, plotData.get(plotNum));
            }
        } else {
            System.out.println("No crops available!");
        }
    }
	
	/**
	 * This method allows a user to clear a wilted Crop on one of his/ her Plot at his/ her choice.
	 * It takes in the plot Number which represents the Plot and 
	 * will proceed to clearCrop function in manageFarmController to clear the crop 
	 * @param plotNum the plot number which represents the plot.
	 * @throws CityException If the clear Crop process is unsuccessful, throws CityException
	 */
    public void clearPlotProcess(int plotNum) throws CityException{
        ArrayList<Plot> plotData = player.getPlotList();

        int initialGold = player.getGold();

        if (plotData.size() < plotNum || plotNum < 0) {
            System.out.println("Please enter a valid plot number");
            return;
        }

        Plot plot = plotData.get(plotNum - 1);

        ArrayList<Plot> clearedPlots = manageFarmController.clearCrop(player,plot);
        
        if(initialGold < 5 && clearedPlots.size() > 0) {
            System.out.println("All wilted plot(s) have been cleared free of charge.");
        } else if (initialGold >= 5 && clearedPlots.size() == 1) {
            System.out.println("Plot has been cleared");
        }else {
            System.out.println("No plots to clear!");
        }
    }
    
	/**
	 * This method allows a user to harvest all his crops that are ready for harvest
	 * It passes in a player object to get the lsit of player objects and then returns a String
	 * will proceed to clearCrop function in manageFarmController to clear the crop 
	 * @param player Represet the Player object that will have its crops harvested
	 * @throws CityException If the harvest Crop process is unsuccessful, throws CityException
	 */
    public void harvestCropProcess(Player player) throws CityException{
        HashMap<Crop, Integer> harvestYield = manageFarmController.harvestCrop(player);
		
		if(harvestYield.size() == 0) {
			System.out.println("Nothing to harvest");
		} else {
		
			Iterator<Crop> harvestYieldIter = harvestYield.keySet().iterator();
			
			String harvestDetails = "You have harvested ";
				
			while(harvestYieldIter.hasNext()) {
				int totalXP = 0;
				int totalGold = 0;
				Crop crop = harvestYieldIter.next();
				int yield = harvestYield.get(crop);
				totalXP += crop.getXP() * yield;
				totalGold += crop.getSalePrice() *yield;
				System.out.print(yield + "units of " + crop.getCropName() + "harvested for " + totalXP + "XP and " + totalGold + " gold");	
			}

		}
    }

}
