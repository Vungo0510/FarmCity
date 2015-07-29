/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmcity.exception;
import farmcity.dao.*;
/**
 * This class describes a general exception used for input validation 
 * @author G3-T12
 */
public class CityException extends Exception {
 
	private String key;
	/**
	* This constructor constructs a CityException object with the specified message
	* as the String passed in
	* @param key the message of the exception
	*/
    public CityException(String key) {
        super(ExceptionDAO.fetchKey(key));
		this.key = key;
    }
	/**
	* This method return the specified message for the exception
	* @return the name of the exception
	*/
	public String getKey() {
		return key;
	}
	
}
