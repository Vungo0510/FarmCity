package farmcity.ui;

import farmcity.controller.ManageLoginController;
import java.util.*;
import java.io.*;
import farmcity.exception.*;

/**
 * This UI represents the Welcome Menu of the game, which is displayed
 * right after the user chooses to run the application. This UI allows user to
 * login his/ her current account, register for a new account or exit and close
 * the application.
 * @author G3-T12
 */
public class WelcomeUI {

    private ManageLoginController manageLoginController;
    private RegisterUI registerUI;
    private LoginUI loginUI;
	
	/**
 * This constructor initializes the ManageLoginController,
 * RegisterUI as well as LoginUI
 */
    public WelcomeUI() {
		try {
			manageLoginController = new ManageLoginController();
		} catch (CityException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
        registerUI = new RegisterUI();
        loginUI = new LoginUI();
    }
	
	/**
	 * This method executes the Welcome Menu and prints out the available options for
	 * user to choose (Register, Login or Exit). The method then prompts for user's choice
	 * and proceed to the corresponding function (logout, execute LoginUI or Register UI)
	 * based on user's input. 
	 */
    public void execute() {

        int choice = 0;

        Scanner sc = new Scanner(System.in);

        while (choice != 3) {
		
			try {
				
				
				System.out.println("== Farm City :: Welcome == ");
				System.out.println("Hi,");
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				System.out.print("Enter your choice > ");

				choice = Integer.parseInt(sc.nextLine()); //Validation purposes
				
				switch(choice) {
					case 1: registerUI.execute();
							break;
					case 2: loginUI.execute();
							break;
					case 3: break;
					default: System.out.println("Please enter a valid option!");
							break;
				}
			}catch (NumberFormatException e) {
				System.out.println("Please enter a valid input!");
			}
			
			System.out.println();
        }
    }
}
