package farmcity.dao;

import java.util.*;
import java.io.*;
import java.text.*;
import farmcity.entity.*;
import farmcity.exception.*;


/**
 * This class contains methods to retrieve gift from the GiftData CSV File
 * @author G3-T12
 */
public class GiftDAO {
    private static GiftDAO giftDAO;
    private HashMap<String, ArrayList<Player>> giftMap;
    private String GIFT_DATA_DIRECTORY = "./data/GiftData.csv";
	
	/**
	 * This constructor extracts all the data from gift database to a HashMap of Usernames and ArrayList of Players.
	 * @throws CityException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */
    private GiftDAO() throws CityException{
        load();
    }
	
	
    /**
	 * This method initializes the gift Data Access Object
	 * @return Data Access Object for gift
	 * @throws CityException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */
    public static GiftDAO getInstance() throws CityException{
        if(giftDAO == null) {
            giftDAO = new GiftDAO();
        }
        
        return giftDAO;
    }
    
    /**
	 * This method loads the Rank Data from the Gift CSV File
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */
    public void load() throws CityException{
        
        giftMap = new HashMap<String, ArrayList<Player>>();
        
        HashMap<String,Player> playerMap = PlayerDAO.getInstance().getAllPlayers();
        
        Iterator<String> playerMapIter = playerMap.keySet().iterator();
        
        while(playerMapIter.hasNext()) {
            String username = playerMapIter.next();
            giftMap.put(username, new ArrayList<Player>());
        }
        
        File file = new File(GIFT_DATA_DIRECTORY);
        
        Scanner fileReader = null;
        
        try {
            fileReader = new Scanner(file);
            
            fileReader.nextLine();

            while(fileReader.hasNext()) {
                String currentLine = fileReader.nextLine();
                
                String[] currentLineSplit = currentLine.split(",");
                
                String username = currentLineSplit[0];
                String friendUsername = currentLineSplit[1];
                
                String[] dateData = currentLineSplit[2].split(" ");

                String[] date = dateData[0].split("/");
                String[] time = dateData[1].split(":");

                int day = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]) - 1;
                int year = Integer.parseInt(date[2]);

                int hour = Integer.parseInt(time[0]);
                int min = Integer.parseInt(time[1]);
                int sec = Integer.parseInt(time[2]);

                Calendar dateSent = new GregorianCalendar(year, month, day, hour, min, sec);
                
                ArrayList<Player> giftSent = giftMap.get(username);
                giftSent.add(new Player(friendUsername,dateSent));

            }
            
        }catch (FileNotFoundException e) {
            throw new CityException("InvalidFile");
        } finally {
            if(fileReader != null) {
                fileReader.close();
            }
        }
    }
    
	/**
	 * This method allows player to save his/ her plots' current progress to the database file.
	 * @throws CityException if there is something wrong with writing to the file
	 */
    public void save() throws CityException{
        
        File file = new File(GIFT_DATA_DIRECTORY);
        
        PrintStream fileWriter = null;
        
        try {
            fileWriter = new PrintStream(new FileOutputStream(file));
            
            fileWriter.println("Username,FriendUsername,GiftTime");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
             
            Iterator<String> giftMapIter = giftMap.keySet().iterator();
            
            while(giftMapIter.hasNext()) {
                String username = giftMapIter.next();
                
                ArrayList<Player> giftList = giftMap.get(username);
                
                for(Player receiver: giftList) {
                    fileWriter.print(username);
                    fileWriter.print(",");
                    fileWriter.print(receiver.getUsername());
                    fileWriter.print(",");
                    fileWriter.print(sdf.format(receiver.getGiftDate().getTime()));
                    fileWriter.println();
                }
            }
        } catch(IOException e) {
            throw new CityException("UnableWrite");
        } finally {
            if(fileWriter != null) {
                fileWriter.close();
            }
        }
    }
    
	/*
	 *This method returns a list of Player objects that the current player has sent gifts to
	 *on the previous day before 00:00
	 *@param username username of the current player
	 *@returns ArrayList of Player objects
	 */
    public ArrayList<Player> getGiftList(String username) {
        return giftMap.get(username);
    }
    
	
	/*
	 *Adds a gift object to the current player's arrayList of giftList
	 *@param username username of the current player
	 */
    public void addGift(String username, Player receiver) {
        ArrayList<Player> giftList = giftMap.get(username);
        giftList.add(receiver);
    }
}
