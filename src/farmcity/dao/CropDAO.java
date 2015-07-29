package farmcity.dao;

import java.io.*;
import java.util.*;
import farmcity.entity.Crop;
import farmcity.exception.*;

/**
 * This class contains methods to retrieve data from the CropData CSV File
 * @author G3-T12
 */
public class CropDAO {

    private static CropDAO cropDAO;
    private ArrayList<Crop> allCrops;
    final private String CROP_DATA_DIRECTORY = "./data/CropData.csv";
	
	
	/**
	 * This constructor extracts all the data from crop database to an ArrayList of crops.
	 * @throws CityException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */
    private CropDAO() throws CityException{ //Initialise CropDAO with crop data from crop.csv file
		load();
    }
	
	/**
	 * This method loads the Rank Data from the Crop CSV File
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
	public void load() throws CityException{
		allCrops = new ArrayList<Crop>();
        File file = new File(CROP_DATA_DIRECTORY);
        Scanner cropDataReader = null;
        Scanner lineReader = null;

        try {

            cropDataReader = new Scanner(file);

            ArrayList<String> cropDataArray = new ArrayList<String>();
            int count = 0;

            while (cropDataReader.hasNext()) {

                String cropDataLine = cropDataReader.nextLine();

                if (count > 0) {

                    String[] cropData = cropDataLine.split(",");

                    String cropName = cropData[0];
                    
                    int cost = Integer.parseInt(cropData[1]);
                    int time = Integer.parseInt(cropData[2]);
                    int xp = Integer.parseInt(cropData[3]);
                    int minYield = Integer.parseInt(cropData[4]);
                    int maxYield = Integer.parseInt(cropData[5]);
                    int salePrice = Integer.parseInt(cropData[6]);

                    allCrops.add(new Crop(cropName, cost, time, xp, minYield, maxYield, salePrice));
                }

                count++;
            }

        } catch (FileNotFoundException e) {
            throw new CityException("InvalidFile");
        } finally {
            cropDataReader.close();
        }
	}
	
	
	/**
	 * This method initializes the crop Data Access Object
	 * @return Data Access Object for crop
	 * @throws CityException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */
    public static CropDAO getInstance() throws CityException{
        if (cropDAO == null) {
            cropDAO = new CropDAO();
        }

        return cropDAO;
    }
	
	
	/**
	 * This method returns all the Crops extracted
	 * @return ArrayList of all the Crops
	 */
    public ArrayList<Crop> getAllCrops() {
        return allCrops;
    }
	
	/**
	 * This method return the Crop which has the specified Crop name 
	 * @param cropName Name of the Crop to be returned
	 * @return the Crop object which has its name matches with the cropName
	 */
    public Crop getCropByName(String cropName) { 
        Crop crop = null;
        for (int i = 0; i < allCrops.size(); i++) {
            crop = allCrops.get(i);
            
            if (cropName.equals(crop.getCropName())) {
                return crop;
            }
        }
        return crop;
    }
}
