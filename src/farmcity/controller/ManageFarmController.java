package farmcity.controller;

import java.util.*;
import farmcity.entity.Crop;
import farmcity.dao.*;
import farmcity.entity.*;
import farmcity.exception.*;

/**
 * This class manages harvest crop, plant crop functions as well as player's process of leveling up  
 * @author G3-T12
 */

public class ManageFarmController {

    private PlotDAO plotDAO;
    private InventoryDAO inventoryDAO;
    private RankDAO rankDAO;
    private PlayerDAO playerDAO;
    private CropDAO cropDAO;

	/**
	 * This controller initializes cropDAO, playerDAO, plotDAO, inventoryDAO and rankDAO as well as set the 
	 * @throws CityException if the necessary files to initialise the application cannot be found
	 */
    public ManageFarmController() throws CityException{
		
        cropDAO = CropDAO.getInstance();
        playerDAO = PlayerDAO.getInstance();

        plotDAO = PlotDAO.getInstance();
        inventoryDAO = InventoryDAO.getInstance();
        rankDAO = RankDAO.getInstance();
        
    }
	
	/**
	 * This method fetches all the plots of the current player from the database
	 * @param username Username of the current player
	 * @return An ArrayList of Plots that the player has
	 */
    public ArrayList<Plot> getAllPlots(String username) {
        return plotDAO.getAllPlots(username);
    }
	
	/**
	 * This method updates the growth percentage of the crop in player's farm as well as set the status of the plot based on the timePlanted of the Crop on that Plot.
	 * The possible status are w (wilted) when the growth percentage is 100% and more than twice the maturity time of the crop has passed since it was planted,
	 * e (empty) when there are no crops on the plot, p (planted) when the growth percentage is less than 100% and a crop is planted on the plot
	 * or h (ready to harvest) when the growth percentage is 100% and less than twice the maturity time of the crop has passed since it was planted
	 * @param player The current player's object
	 */
	
    public void updatePercentages(Player player) {
        ArrayList<Plot> allPlots = player.getPlotList();

        for (int i = 0; i < allPlots.size(); i++) {
            Plot plot = allPlots.get(i);
            char status = 'e';
            int percentage = 0;

            if (plot.getTimePlanted() != null) {
                Date currentTimeDate = new Date();
                Date plantedTimeDate = plot.getTimePlanted().getTime();
                String plotCropName = plot.getCrop().getCropName();

                long timeDifference = (currentTimeDate.getTime() - plantedTimeDate.getTime()) / 1000; //Calculate time difference between time planted and current time

                double timeNeeded = cropDAO.getCropByName(plotCropName).getMaturityTime() * 60; //Get crop information 

                percentage = (int) (timeDifference / timeNeeded * 100);

                if (percentage > 200) { //Maximum percentage allowed. 2 * maturity time
                    status = 'w'; //set status to wilted
                } else if (percentage >= 100) { // Plot is ripe for harvest
                    status = 'h';	//set status to harvest
                } else {
                    status = 'p';	//plot is planted on
                }
            }
            plot.setStatus(status);
            plot.setPercentage(percentage);
        }
    }
	
	/**
	 * This method allows a user to plant a Crop in his Inventory on the specified Plot in his farm. The time that the plot that player wishes to plant to crop on
	 * @return Return true if the player succeeded in planting the crop, return false otherwise
	 * @throws CityException if the file to be saved to cannot be found
	 */
    
    public boolean plantCrop(String username, InventoryCrop plantedCrop, Plot plot) throws CityException{
        
		Crop crop = plantedCrop.getCrop();
        plot.setCrop(crop);
		
        inventoryDAO.updateInventory(username, new InventoryCrop(crop, -1));
        plot.setStatus('p');
        plot.setTimePlanted(new GregorianCalendar());
        plotDAO.save();

        return true;
    }
	
	
	/**
	 * This method allows player to return all of the harvestable Crops on all of his Crops,
	 * calculates the yields of each crop and sells them to gain money and experience.
	 * The plot(s) which had the harvested Crop are set back to empty (e). 
	 * If the experience combined with the existing experience points exceed the requirement for the next level, promote the player to the corresponding Rank. 
	 * The player then have additional plot(s) according the his new Rank
	 * @param player The current player's object 
	 * @return The details of harvest session (e.g. How much money and experience the player gained from harvesting the crop(s))
	 * @throws CityException if the file to be saved to cannot be found
	 */
    
    public HashMap<Crop,Integer> harvestCrop(Player player) throws CityException{

        ArrayList<Plot> plotList = player.getPlotList();
        ArrayList<Plot> harvestPlotList = new ArrayList<Plot>();

        for (int i = 0; i < plotList.size(); i++) {
            Plot plot = plotList.get(i);

            if (plot.getStatus() == 'h') {
                harvestPlotList.add(plot);
            }
        }

        HashMap<String, Integer> harvestedCrop = new HashMap<String, Integer>(); //hashmap stores yield per crop

        //Gets a list of harvestable crops from plotDAO e.g plots with 'w' 
        int[] totalYieldPerCrop = new int[plotList.size()];

        int totalXP = 0; //totalXP from harvesting crops
        int totalGoldGained = 0; //total gold gained from harvesting crops

        String harvestedCropDetails = "You have harvested ";
        boolean foundCrop = false;

        for (int i = 0; i < harvestPlotList.size(); i++) { //Iterates through plotList for each plot that is ready for harvest

            Plot plot = harvestPlotList.get(i);
            Crop crop = plot.getCrop();
            String cropName = crop.getCropName();// get crop name

            int minYield = crop.getMinYield();
            int maxYield = crop.getMaxYield();
            Random rnd = new Random();

            int yield = minYield + rnd.nextInt(maxYield - minYield + 1);	//set yield with random number for the current crop

            if (!harvestedCrop.containsKey(cropName)) {//When the name of crops that have been harvested before are found in the harvestedCRop ArrayList
                harvestedCrop.put(cropName, yield);

            } else {
                int totalCropYield = harvestedCrop.get(cropName);
                totalCropYield += yield;
                harvestedCrop.put(cropName, totalCropYield);
            }

            totalGoldGained += yield * crop.getCost();
            totalXP += yield * crop.getXP();

            plot.setTimePlanted(null);//set harvested plots timePlanted attribute to null
            plot.setCrop(null);	//set crop back to null
            plot.setStatus('e');// set plot status back to empty

        }

		int currentXP = player.getXP();
		player.setXP((currentXP+totalXP));
		
		int currentGold = player.getGold();
		player.setGold((currentGold+totalGoldGained));

        
        ArrayList<Rank> rankList = rankDAO.getAllRanks();

        for (int j = rankList.size() - 1; j >= 0; j--) {

            if (player.getXP() >= rankList.get(j).getXP()) {//If player's rank has reached beyond a certain level of XP, promote player to next rank
                Rank rank = rankList.get(j);

                int currentPlotNo = player.getPlotList().size();

                player.setRank(rank);

                plotDAO.addPlot(player.getUsername(), rank.getPlotAvailable() - currentPlotNo);//add number of plots accordingly

                break;
            }
        }

        Iterator<String> harvestedCropIter = harvestedCrop.keySet().iterator();
		
		HashMap<Crop,Integer> harvestCropYield = new HashMap<Crop,Integer>();
		
        while (harvestedCropIter.hasNext()) {
            String cropName = harvestedCropIter.next();
			Crop crop = cropDAO.getCropByName(cropName);
            int yield = harvestedCrop.get(cropName);
			
			harvestCropYield.put(crop,yield);
        }

        playerDAO.save();
        plotDAO.save();

        return harvestCropYield;

    }
	
	
	/**
	 * This method allows player to clear a wilted crop on a chosen wilted plot 
	 * by using 5 golds from his/ her budget. The plot status is set to empty (e)
	 * If the player has less then 5 gold, he/ she will be able to clear all wilted plots free of charge.
	 * @param plot The plot that has wilted crop which the player want to clear
	 * @throws CityException if the file to be saved to cannot be found
	 */
    public ArrayList<Plot> clearCrop(Player player, Plot plot) throws CityException{
        
         ArrayList<Plot> plotsCleared = new ArrayList<Plot>();

        if(plot.getStatus() == 'w') {
            plot.setStatus('e');
            plot.setTimePlanted(null);
            plot.setCrop(null);

            int gold = player.getGold();

            if (gold >= 5) {
                gold -= 5;
                player.setGold(gold);
                plotsCleared.add(plot);
            } else {
				ArrayList<Plot> plotList = player.getPlotList();
				
				for(int i = 0; i < plotList.size(); i++) {
					Plot currentPlot = plotList.get(i);
					
					if(plotList.get(i).getStatus() == 'w'){
						currentPlot.setStatus('e');
						currentPlot.setTimePlanted(null);
						currentPlot.setCrop(null);
                        plotsCleared.add(currentPlot);
					}
				}
			}

            plotDAO.save();
            
            return plotsCleared;
        } else {
            return plotsCleared;
        }
    }

}
