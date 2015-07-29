package farmcity.dao;

import farmcity.entity.Rank;
import farmcity.exception.*;
import java.util.*;
import java.io.*;

/**
 * This class contains methods to retrieve data from the RankData CSV File
 * @author G3-T12
 */

public class RankDAO {

    private static RankDAO rankDAO;
    private ArrayList<Rank> allRanks;
    final private String RANK_DATA_DIRECTORY = "./data/RankData.csv";
	
	/**
	 * This constructor runs the load method to load the Rank Data
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
    private RankDAO() throws CityException{
        load();
    }
    
    /**
	 * This method extracts all the data from Rank database to an ArrayList of Ranks.
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
    public static RankDAO getInstance() throws CityException {
        if (rankDAO == null) {
            rankDAO = new RankDAO();
        }

        return rankDAO;
    }
	
	/**
	 * This method loads the Rank Data from the Rank CSV File
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
    public void load() throws CityException{
        
        allRanks = new ArrayList<Rank>();
        File file = new File(RANK_DATA_DIRECTORY);
        Scanner rankDataReader = null;
        Scanner lineReader = null;

        try {

            rankDataReader = new Scanner(file);

            ArrayList<String> rankDataArray = new ArrayList<String>();
            int count = 0;

            while (rankDataReader.hasNext()) {

                String rankDataLine = rankDataReader.nextLine();

                if (count > 0) {

                    String[] rankData = rankDataLine.split(",");

                    String rankName = rankData[0];
                    int xp = Integer.parseInt(rankData[1]);
                    int plotAvailable = Integer.parseInt(rankData[2]);

                    allRanks.add(new Rank(rankName, xp, plotAvailable));
                }

                count++;
            }

        } catch (FileNotFoundException e) {
           throw new CityException("InvalidFile");
        } finally {
            rankDataReader.close();
        }
    }
	
    /**
	 * This method retrieves an ArrayList of all Rank Objects
	 * @return ArrayList of ranks
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
	
    public ArrayList<Rank> getAllRanks() {
        return allRanks;
    }

	/**
	 * This method retrieves a Rank object by the rank name that is passed in
	 * @param name of the Rank
	 * @return a Rank object
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
    public Rank getRankByName(String rankName) {
        for (int i = 0; i < allRanks.size(); i++) {
            if (allRanks.get(i).getRankName().equals(rankName)) {
                return allRanks.get(i);
            }
        }

        return null;
    }
}
