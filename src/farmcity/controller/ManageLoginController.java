package farmcity.controller;

import farmcity.dao.InventoryDAO;
import farmcity.entity.Player;
import farmcity.dao.*;
import farmcity.exception.*;
import farmcity.entity.*;
import java.util.*;
import java.io.*;

/**
 * This class manages the register, login, logout functions for the player 
 * @author G3-T12
 */
public class ManageLoginController {

    private PlayerDAO playerDAO;
    private PlotDAO plotDAO;
	
	
	/**
     * 
     * 
     * This constructor initializes playerDAO and plotDAO
	 *	@throws CityException if the necessary files to initialise the application cannot be found
	*/	
    public ManageLoginController() throws CityException,FileNotFoundException{
		ExceptionDAO.load();
        playerDAO = PlayerDAO.getInstance();
        plotDAO = PlotDAO.getInstance();
    }
	
	/**
     * This method allows unregistered user to register a new account to play Farm City
     * @param username The username that user wishes to have in game. Only allows alphanumeric characters. Cannot be changed.
     * @param fullName Full name of the user, only displayed to the user himself/ herself. Cannot be changed.
     * @param password Password for login authentication. Only allows alphanumeric characters. Cannot be changed.
     * @param confirmedPassword To confirm that this is the password user wants to set for his/her account
     * @return return true if the user succeeded in creating a new account, return false otherwise (player already exists, confirm password string doesn't match with password
     * @throws CityException if the necessary files to initialise the application cannot be found OR there is a password mismatch OR 
	 * the player's username has already been registered OR the input String by the user does not only contain alphanumeric characters
     */
    public boolean register(String username, String fullName, String password, String confirmedPassword) throws CityException{

        if (!confirmedPassword.equals(password)) {
             throw new CityException("PasswordMismatch");

        }

        boolean playerExists = playerDAO.playerExists(username);

        if(playerExists) {
			throw new CityException("PlayerRegistered");
        }
        
        boolean validUsername = stringValidation(username);
        boolean validPassword = stringValidation(password);
        boolean validFullName = fullNameValidation(fullName);
        
        
        if(!validUsername || !validPassword || !validFullName) {
           throw new CityException("InvalidString");  
        }
		
		Player newPlayer = playerDAO.createUser(username, fullName, password);
		plotDAO.addPlot(newPlayer.getUsername(),5);
		plotDAO.save();
		playerDAO.save();
		
		PlayerDAO.getInstance().destroyInstance();
		PlotDAO.getInstance().destroyInstance();
        return true;
    }
	/*
     *validates that the string passsed in contains only alphanumeric characters
	 *@param validateString String that is passed in
	 *@return boolean if String is valid
	 */
    public boolean stringValidation(String validateString) {
        
        if(validateString == null || validateString.equals("")) {
            return false;
        }
        String editedString = validateString.toUpperCase();
       
        for(int i = 0; i < editedString.length(); i++) {
           char currentChar = editedString.charAt(i);
            
            if(!((currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= '0' && currentChar <= '9'))) {
                
                return false;
            }
        }
        
        return true;
    }
	/*
     *validates that the string passsed in contains only alphanumeric characters and " "
	 *@param validateString String that is passed in
	 *@return boolean if String is valid
	 */
    
    public boolean fullNameValidation(String validateString) {
        
        if(validateString == null || validateString.equals("")) {
            return false;
        }
        
        String editedString = validateString.toUpperCase();
        
        for(int i = 0; i < editedString.length(); i++) {
           char currentChar = editedString.charAt(i);
            
            if(!((currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= '0' && currentChar <= '9') || (currentChar == ' '))) {
               
                return false;
            }
        }
        
        return true;
    }
	
	/**
     * This method allows a registered player to login to his account using the chosen username and password.
     * @param username The player's chosen username
     * @param password The player's chosen password
     * @return the Player object which corresponds to the given username and password
     * @throws CityException If the file(s) cannot be found at the specified directory OR if invalid user credentials have been entered
     */
    public Player login(String username, String password) throws CityException{
        playerDAO = PlayerDAO.getInstance();
        Player player = playerDAO.retrieveUser(username, password);
		
		
		if(player == null) {
			throw new CityException("InvalidUser");
		}
		
        FriendDAO.getInstance().load();
        InventoryDAO.getInstance().load();
		PlotDAO.getInstance().load();
		GiftDAO.getInstance().load();
		
		
        return player;
    }
	
	
	 /**
     * This method allows the player to save his current progress in game and logout of the game when he/she no longer wishes to play or are forced to sleep by his/her parents
     * @param player The Player object which corresponds to the player's account
     * @return return true when player succeeded in logging out of the game, return false otherwise
     * @throws CityException if the file(s) cannot be found at the specified directory
     */

    public boolean logout(Player player) throws CityException{
        playerDAO.save();
     
        InventoryDAO inventoryDAO = InventoryDAO.getInstance();
        inventoryDAO.save();
        
        PlotDAO plotDAO = PlotDAO.getInstance();
        plotDAO.save();

        
        return true;
    }

}
