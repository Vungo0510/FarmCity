package farmcity.dao;

import farmcity.entity.Player;
import java.util.*;
import java.io.*;
import farmcity.entity.*;
import farmcity.dao.*;
import farmcity.exception.*;


/**
 * This class contains methods to retrieve and modify data from the Inventory database
 * @author G3-T12
 */
public class InventoryDAO {

    private static InventoryDAO inventoryDAO;
    private HashMap<String, ArrayList<InventoryCrop>> inventoryMap;
    final private String INVENTORY_DATA_DIRECTORY = "./data/InventoryData.csv";
	
	
	/**
	 * This constructor initializes a new HashMap for player's inventoryCrop objects and put all player's
	 * inventoryCrop data into the HashMap with his/ her username as key and an ArrayList of InventoryCrops as value
	 * @throws CityException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */
    private InventoryDAO() throws CityException{
        load();
    }
	
	
     
    public void load() throws CityException{
        inventoryMap = new HashMap<String, ArrayList<InventoryCrop>>();
        File file = new File(INVENTORY_DATA_DIRECTORY);
        Scanner fileReader = null;
        
        HashMap<String, Player> allPlayers = PlayerDAO.getInstance().getAllPlayers();

        Iterator<String> playerIter = allPlayers.keySet().iterator();

        while (playerIter.hasNext()) {
            String username = playerIter.next();
            Player player = allPlayers.get(username);
             ArrayList<InventoryCrop> inventoryList = new ArrayList<InventoryCrop>();
            
            inventoryMap.put(username, inventoryList);
            player.setInventoryCropList(inventoryList);
        }
        
        
        try {
            
            fileReader = new Scanner(file);
            String userData = null;
            
            fileReader.nextLine();
            
            while (fileReader.hasNext()) {
                String currentLine = fileReader.nextLine();
                
                
                String[] splitLine = currentLine.split(",");
                String username = splitLine[0];
                String cropName = splitLine[1];
                Crop crop = CropDAO.getInstance().getCropByName(cropName);
                int quantity = Integer.parseInt(splitLine[2]);
                
                

                ArrayList<InventoryCrop> inventoryList = inventoryMap.get(username);
                inventoryList.add(new InventoryCrop(crop,quantity));
            }
        } catch (FileNotFoundException e) {
           throw new CityException("InvalidFile");
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }
	
	/** This method initializes the Inventory Data Access Object
	 * @return Data Access Object for Inventory
	 * @throws CityException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */
    public static InventoryDAO getInstance() throws CityException{
        if (inventoryDAO == null) {
            inventoryDAO = new InventoryDAO();
        }

        return inventoryDAO;
    }
	
	/**
	 * This method set the current Player Data Access Object to null
	 */
    public static void destroyInstance() {
        inventoryDAO = null;
    }
	
	
	/**
	 * This method return an ArrayList of InventoryCrop of the Player who has the specified username 
	 * @param username username of the player whose inventory is to be returned
	 * @return ArrayList of InventoryCrop of the current player
	 */
    public ArrayList<InventoryCrop> getPlayerInventoryCrops(String username) {
        return inventoryMap.get(username);
    }
	
	
    /**
	 * This method adds new inventoryCrop to the ArrayList of inventoryCrop of the current Player and save player's current progress
	 * @param username Username of the current player
	 * @param addCrop Name of the inventoryCrop to be added to the player's inventory
	 */   
    public boolean updateInventory(String username, InventoryCrop addCrop) {
        
        ArrayList<InventoryCrop> inventoryList = inventoryMap.get(username);
		String addCropName = addCrop.getCrop().getCropName(); 
        
        boolean update = false;
		boolean cropFound = false;
        
       
        
		for(int i = 0; i < inventoryList.size(); i++) {
			
			InventoryCrop invenCrop = inventoryList.get(i);
			String currentCropName = invenCrop.getCrop().getCropName();
			
			if(currentCropName.equals(addCropName)) {
				cropFound = true;
				
                int currentQuantity = invenCrop.getQuantity();
                int addedQuantity = addCrop.getQuantity();
                int newQuantity = currentQuantity + addedQuantity;

                invenCrop.setQuantity(newQuantity);

                update = true;
            }
		}
		
		Iterator<InventoryCrop> inventoryListIter = inventoryList.iterator();
		
        while(inventoryListIter.hasNext()) {
            
            InventoryCrop invenCrop = inventoryListIter.next();
			
            if(invenCrop.getQuantity() == 0) {
                inventoryListIter.remove();
            }
        }
        
        if(!cropFound && addCrop.getQuantity() > 0) {
            inventoryList.add(addCrop);
			update = true;
        }
		
		return update;
    }
	
	/**
	 * This method allows player to save his/ her Players' current inventory progress to the database file.
	 * @throws CityException if there is something wrong with writing to the file
	 */    

    public void save() throws CityException{

        File file = new File(INVENTORY_DATA_DIRECTORY);
        Scanner lineReader = null;
        
        
        PrintStream lineWriter = null;

        try {
            lineWriter = new PrintStream(new FileOutputStream(file));

            lineWriter.println("Username,Crop,Quantity");
            
            Iterator<String> usernameIter = inventoryMap.keySet().iterator();
            
            while(usernameIter.hasNext()) {
                
                String username = usernameIter.next();
                
                ArrayList<InventoryCrop> inventoryList = inventoryMap.get(username);
                Iterator<InventoryCrop> inventoryListIter = inventoryList.iterator();
                
                while(inventoryListIter.hasNext()){

                    InventoryCrop inventoryCrop = inventoryListIter.next();
                    int quantity = inventoryCrop.getQuantity();

                    lineWriter.print(username);
                    lineWriter.print(",");
                    lineWriter.print(inventoryCrop.getCrop().getCropName());
                    lineWriter.print(",");
                    lineWriter.print(quantity);
                    lineWriter.println();
                }
            }
        } catch (IOException e) {
			throw new CityException("UnableWrite");
        } finally {
            lineWriter.close();
        }
    }
}
