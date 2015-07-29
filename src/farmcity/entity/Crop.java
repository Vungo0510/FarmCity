package farmcity.entity;


/**
 * This class contains attributes and getter methods for the Crop entity
 * @author G3-T12
 */
public class Crop {

    private String cropName;
    private int cost;
    private int maturityTime;
    private int xp;
    private int minYield;
    private int maxYield;
    private int salePrice;
	
	
	/**
	 * This constructor initializes the Crop object by passing in the corresponding name of the crop in String,
	 * the cost to purchase a bag of seeds for this crop, the crop's maturity time in int, the xp player gained from producing
	 * the crop in int, minimum and maximum yields of one bag of seeds of the crop in int and the price at which the player can
	 * sell the crop
	 * @param cropName The name of the Crop, fixed
	 * @param cost The cost to purchase one bag of seeds for the crop, fixed
	 * @param maturityTime The time in minutes it takes for the Crop to mature (to be harvestable), fixed
	 * @param xp The amount of experience points the player gained from producing one unit of this crop, fixed 
	 * @param minYield Minimum yield (in units) produced by planting one bag of seed of the given crop, fixed
	 * @param maxYield Maximum yield (in units) produced by planting one bag of seed of the given crop, fixed
	 * @param salePrice Number of gold coins one unit of produce gives the farmer, fixed 
	 */
    public Crop(String cropName, int cost, int maturityTime, int xp, int minYield, int maxYield, int salePrice) {
        this.cropName = cropName;
        this.cost = cost;
        this.maturityTime = maturityTime;
        this.xp = xp;
        this.minYield = minYield;
        this.maxYield = maxYield;
        this.salePrice = salePrice;
    }


	/**
		 * Return the name of the Crop
		 * @return return the name of the Crop in String
		 */    
    public String getCropName() {
        return cropName;
    }
	
	/**
	 * Return the cost for purchasing one bag of seeds
	 * @return return the cost of the purchasing one bag of seeds in int
	 */ 
    public int getCost() {
        return cost;
    }
	
	/**
	 * Return the Crop's maturity time
	 * @return return the time it takes for the Crop to mature in int
	 */ 
    public int getMaturityTime() {
        return maturityTime;
    }
	
	/**
	 * Return the XP
	 * @return return the XP gained for producing the crop in int
	 */    
    public int getXP() {
        return xp;
    }
	
	/**
	 * Return the minimum yield of the Crop
	 * @return return the minimum yield of the Crop in int
	 */    
    public int getMinYield() {
        return minYield;
    }
	
	/**
	 * Return the maximum yield of the Crop
	 * @return return the maximum yield of the Crop in int
	 */  
    public int getMaxYield() {
        return maxYield;
    }
	
	/**
	 * Return the price at which the Crop's produce is sold
	 * @return return the price of the Crop in int
	 */  
    public int getSalePrice() {
        return salePrice;
    }
}
