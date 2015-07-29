package farmcity.ui;


import farmcity.controller.ManageLoginController;
import farmcity.entity.Player;
import java.util.*;
import java.io.*;
import farmcity.exception.*;

/**
 * This UI represents the Login Menu of the game, which is displayed after the player choose to
 * Login to his/ her account at the Welcome UI. This method will authorize the player's access
 * to his/ her account.
 * @author G3-T12
 */
public class LoginUI {

    private ManageLoginController manageLoginController;
    private MainMenuUI mainMenuUI;
    private Player player;
	
	/**
	 * This constructor initializes a new ManageLoginController which is used to check user's input if 
	 * it matches with the username and password in the Player database.
	 */
    public LoginUI() {
		try {
			manageLoginController = new ManageLoginController();
		} catch (CityException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
    }
	
	/**
	 * This method executes the Login Menu and asks for the Player's username and
	 * password. The method then call the login function in manageLoginController
	 * to check the inputs and if they match, the Player object and the main menu UI
	 * is initialized with the current player and the method will proceed to execute
	 * Main Menu ingame.
	 */
    public void execute() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your username > ");
        String username = sc.nextLine();
        System.out.print("Enter your password > ");
        String password = sc.nextLine();
		
		try {
			player = manageLoginController.login(username, password);
			mainMenuUI = new MainMenuUI(player);
			System.out.println();
			mainMenuUI.execute();
			
		} catch (CityException e){
			System.out.println(e.getMessage());
			
			if(e.getKey().equals("InvalidFile")) {
				System.exit(0);
			}
		}

        System.out.println();
    }
}
