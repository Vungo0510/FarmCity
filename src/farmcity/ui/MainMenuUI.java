package farmcity.ui;

import farmcity.controller.ManageLoginController;
import farmcity.entity.Player;
import farmcity.ui.FarmUI;
import java.util.*;
import java.io.*;
import farmcity.exception.*;

/**
 * This user interface (UI) represents the Main Menu of the game, which is displayed
 * after the player has successfully logged in or wants to visit another place. 
 * The Menu consists of functions for the player to visit his farm, take a look at 
 * his/ her friend list, check his inventory or log out of his account. 
 * @author G3-T12
 */
public class MainMenuUI {

    private Player player;
    private FarmUI farmUI;
    private InventoryUI inventoryUI;
    private FriendUI friendUI;
    private ManageLoginController manageLoginController;
	
	
	/**
	 * This constructor takes in the current player and creates a MainMenuUI object which
	 * initializes a FarmUI, FriendUI and InventoryUI object to the Player object passed in
	 * @param player the current player 
	 */
    public MainMenuUI(Player player) {
        this.player = player;
        farmUI = new FarmUI(player);
        friendUI = new FriendUI(player);
        inventoryUI = new InventoryUI(player);
		try {
			manageLoginController = new ManageLoginController();
		} catch(CityException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
    }
	
	/**
	 * This method executes the main menu and prints out the UI to the screen for the user to choose the
	 * place he wants to visit and proceed to the corresponding UI or help the player logout of his/ her account
	 * after taking in the player's input
	 */
    public void execute() {

        int choice = 0;
        Scanner sc = new Scanner(System.in);

        while (choice != 4) {
            System.out.println("== Farm City	:: Main Menu ==");
            System.out.println("Welcome, " + player.getFullName());
            System.out.println();
            System.out.println("1. My Friends");
            System.out.println("2. My Farm");
            System.out.println("3. My Inventory");
            System.out.println("4. Logout ");
            System.out.print("Enter your choice > ");
			
			try {
				choice = Integer.parseInt(sc.nextLine());

				switch (choice) {
					case 1: friendUI.execute();
						break;
					case 2:
						farmUI.execute();
						break;
					case 3:
						inventoryUI.execute();
						break;
					case 4: 
						break;
					default:
						System.out.println("Please choose a valid choice!!");
						break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid input!");
			}
			System.out.println();
        }
		
		try {
			manageLoginController.logout(player);
		} catch (CityException e) {
			System.out.println(e.getMessage());
		}
		
		
    }
}
