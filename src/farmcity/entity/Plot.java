package farmcity.entity;

import java.util.*;
import java.text.*;

/**
 * This class contains attributes, setter and getter methods for the Plot entity
 * @author G3-T12
 */
public class Plot {

    private int plotNumber;
    private char status;
    private Calendar timePlanted;
    private Crop crop;
    private int percentage;
	
	/**
	 * This constructor initializes the Plot object by passing in the Plot number as an int,
	 * setting the Plot status to empty (e), percentage of planted Crop to 0
	 * and time Planted as well as Crop planted on the Plot to null
	 * @param plotNumber Number of the Plot, based on how many Plots player currently have
	 */
    public Plot(int plotNumber) {
        this.plotNumber = plotNumber;
        status = 'e';
        timePlanted = null;
        crop = null;
    }

	/**
	 * This constructor initializes the Plot object by passing in the plot number (int), status of the plot (char),
	 * time Planted (Calendar), growth percentage (int) and the Crop planted on the Plot to the corresponding attributes
	 * @param plotNumber Number of the Plot, based on how many Plots player currently have, fixed
	 * @param status Status of the Plot, can be empty (e), planted (p), harvestable (h) or wilted (w)
	 * @param timePlanted The attribute which represents the moment the Crop is planted on the Plot
	 * @param percentage The growth percentage of the Crop, ranges from 0 to 100
	 * @param crop The type of Crop planted on the Plot
	 */
    public Plot(int plotNumber, char status, Calendar timePlanted, int percentage, Crop crop) {
        this.plotNumber = plotNumber;
        this.status = status;
        this.timePlanted = timePlanted;
        this.percentage = percentage;
        this.crop = crop;
    }
	
	/**
	 * Return Plot number
	 * @return return plot number in int
	 */
    public int getPlotNumber() {
        return plotNumber;
    }
	
	/**
	 * Return plots status
	 * @return return plot status in char
	 */
    public char getStatus() {
        return status;
    }
	
	/**
	 * Return the time the plot was planted
	 * @return return timePlanted in Calendar
	 */
    public Calendar getTimePlanted() {
        return timePlanted;
    }
	
	/**
	 * Set the timePlanted of the Crop on the Plot to the timePlanted passed in
	 * @param timePlanted the new timePlanted Calendar object to be passed in
	 */
    public void setTimePlanted(Calendar timePlanted) {
        this.timePlanted = timePlanted;
    }
	
	/**
	 * Return the Crop on the Plot
	 * @return Return the crop of type Crop
	 */
    public Crop getCrop() {
        return crop;
    }
	/**
	 * Set the status of the Plot to the status passed in
	 * @param status the new status in char to be passed in
	 */
    public void setStatus(char status) {
        this.status = status;
    }
	
	/**
	 * Set the Crop of the Plot to the Crop passed in
	 * @param crop the new crop to be passed in
	 */
    public void setCrop(Crop crop) {
        this.crop = crop;
    }
	
	/**
	 * Set the growth percentage of the Plot to the percentage passed in
	 * @param percentage the new percentage in int to be passed in
	 */
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
	
	/**
	 * Return the current growth percentage
	 * @return return the percentage in int
	 */
    public int getPercentage() {
        return percentage;
    }

}
