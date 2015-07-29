package farmcity.ui;

import farmcity.controller.ManageLoginController;
import farmcity.exception.*;
import java.util.*;
import java.io.*;

/**
 * This UI represents the Register Menu, which is displayed after the user chooses
 * to register for a new account in welcome menu. The account created will enable
 * user's access to the functions in the game after he/ she has logged in through
 * login menu
 * @author G3-T12
 */
public class RegisterUI {

    private ManageLoginController manageLoginController;
	
	/**
	 * This constructor initializes a new ManageLoginController which is used to record user's future 
	 * account to the Player database.
	 */
    public RegisterUI() {
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
	 * This method executes the Register Menu and prompts the user for the username,
	 * full name, passwords and makes him/ her confirm the password for his/ her future
	 * account. Then the method calls the ManageLoginController to validate the inputs.
	 * If there are no duplication, the method prints out a success message and return
	 * the user to welcome UI
	 */
    public void execute() {
        Scanner sc = new Scanner(System.in);

        System.out.println("== Farm City	:: Registration ==");

        System.out.print("Enter your username > ");
        String username = sc.nextLine();

        System.out.print("Enter your full name > ");
        String fullName = sc.nextLine();

        System.out.print("Enter your password > ");
        String password = sc.nextLine();

        System.out.print("Confirm your password > ");
        String confirmedPassword = sc.nextLine();
		
		try {
			if (manageLoginController.register(username, fullName, password, confirmedPassword)) {
				System.out.println("Hi, " + username + "! Your account is successfully created! ");
			} else {
				System.out.println("Your account has already been registered");
			}
		} catch(CityException e) {
			System.out.println(e.getMessage());
			
			if(e.getKey().equals("InvalidFile")) {
				System.exit(0);
			}
		}
		
		System.out.println();
    }

}
