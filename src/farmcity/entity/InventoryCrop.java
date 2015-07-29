/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package farmcity.entity;


/**
 * This class contains attributes, setter and getter methods for the InventoryCrop entity
 * @author G3-T12
 */
public class InventoryCrop {
    private Crop crop;
    private int quantity;
    
	/** 
	 * This constructor initializes an InventoryCrop object with the Crop and quantity of Crop,
	 * which represents the number of a specific Crop a player has in his inventory
	 * @param crop the Crop object, fixed
	 * @param quantity quantity of Crop to be stored
	 */  
    public InventoryCrop(Crop crop, int quantity) {
        this.crop = crop;
        this.quantity = quantity;
    }
    
	/**
	 * Return the Crop
	 * @return return the crop stored in the inventory in Crop
	 */ 
    public Crop getCrop() {
        return crop;
    }
    
	/**
	 * set the Crop's current quantity to the quantity passed in
	 * @param quantity the crop's new quantity
	 */ 
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
	/**
	 * Return Crop's quantity
	 * @return return quantity of the Crop in int
	 */ 
    public int getQuantity() {
        return quantity;
    }
}
