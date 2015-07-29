package farmcity.dao;

import java.util.*;
import java.io.*;
import java.text.ParseException;
import farmcity.dao.*;
import farmcity.entity.*;
import farmcity.exception.*;


/**
 * This class contains methods to retrieve and modify data from the Player database
 * @author G3-T12
 */
public class PlayerDAO {

    private static PlayerDAO playerDAO;
    final private String PLAYER_DATA_DIRECTORY = "./data/PlayerData.csv";
    private HashMap<String, Player> playerMap;

	/**
	 * This constructor calls the load method to load player data into the HashMap
	 * @throws FileNotFoundException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */	
    private PlayerDAO() throws CityException {
        load();
    }

	 /** This method initializes the Player Data Access Object
	 * @return Data Access Object for Player
	 * @throws CityException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */
    public static PlayerDAO getInstance() throws CityException{
        if (playerDAO == null) {
            playerDAO = new PlayerDAO();
        }

        return playerDAO;
    }
	
	/**
	 * This method set the current Player Data Access Object to null
	 */
    public static void destroyInstance() {
        playerDAO = null;
    }
	
	
	/**
	 * This method loads all the players' data into a HashMap
	 * @throws CityException
	 */
    public void load() throws CityException{
        File file = new File(PLAYER_DATA_DIRECTORY);
		playerMap = new HashMap<String, Player>();
        Scanner fileReader = null;
        ArrayList<String> playerListString = new ArrayList<String>();

        try {

            fileReader = new Scanner(file);

            fileReader.nextLine();

            while (fileReader.hasNext()) {
                String readLine = fileReader.nextLine();
                playerListString.add(readLine);
            }

        } catch (FileNotFoundException e) {
			throw new CityException("InvalidFile");
        } finally {
            fileReader.close();
        }

        for (int i = 0; i < playerListString.size(); i++) {
            String[] playerData = playerListString.get(i).split(",");

            String username = playerData[0];
            String fullName = playerData[1];
            String password = playerData[2];
            int xp = Integer.parseInt(playerData[3]);
            int gold = Integer.parseInt(playerData[4]);
            String rankName = playerData[5];

            
            Rank rank = null;

            ArrayList<Rank> rankList = RankDAO.getInstance().getAllRanks();

            for (int j = 0; j < rankList.size(); j++) {
                Rank currentRank = rankList.get(j);

                if (rankName.equals(currentRank.getRankName())) {
                    rank = currentRank;

                }
            }

            playerMap.put(username, new Player(username, fullName, password, rank, xp, gold));
            
        }
    }
	
	/**
	 * This method returns all the Players from the HashMap
	 * @return a Hashmap of all the Players
	 */
    public HashMap<String, Player> getAllPlayers() {
        return playerMap;
    }
	
	
	/**
	 * This method checks if there is a Player with the specified username in the player database
	 * @param username the username used to check for player's existence
	 * @return Return true if the player exists, false if the player doesn't exist
	 */
    public boolean playerExists(String username) {
        return playerMap.containsKey(username);
    }
	
	/**
	 * This method creates a new Player instance and record it in the Player database
	 * @param inputUsername Username of the new Player
	 * @param inputFullName Full name of the new Player
	 * @param inputPassword Password of the new Player
	 * @return The new Player object that is created using the input
	 * @throws FileNotFoundException If the file(s) cannot be found at the specified directory, throws FileNotFoundException
	 */
    public Player createUser(String inputUsername, String inputFullName, String inputPassword) throws CityException{

        Player newPlayer = new Player(inputUsername, inputFullName, inputPassword);
        playerMap.put(inputUsername, newPlayer);
        Rank rank = RankDAO.getInstance().getRankByName("Novice");
        newPlayer.setRank(rank);

        return newPlayer;

    }
	
	/**
	 * This method return the Player which has the specified username 
	 * @param inputUsername username of the player to be retrieved
	 * @param inputPassword the password used to authenticate user's access to his/ her account 
	 * @return the Player which has its username matched with the password
	 */
    public Player retrieveUser(String inputUsername, String inputPassword) {

        if (!(playerMap.containsKey(inputUsername))) {
            return null;
        }

        Player player = null;

        player = playerMap.get(inputUsername);

        if (!player.getPassword().equals(inputPassword)) {
            return null;
        }

        return player;
    }
	
	/**
	 * This method allows player to save his/ her plots' current progress to the database file.
	 * @throws CityException if there is something wrong with writing to the file
	 */
    public void save() throws CityException{

        File file = new File(PLAYER_DATA_DIRECTORY);

        PrintStream lineWriter = null;

        try {
            lineWriter = new PrintStream(new FileOutputStream(file));

            lineWriter.println("Username,FullName,Password,XP,Gold,Rank");

            Iterator<String> playerMapIter = playerMap.keySet().iterator();

            while (playerMapIter.hasNext()) {

                String username = playerMapIter.next();

                Player player = playerMap.get(username);

                lineWriter.print(player.getUsername());
                lineWriter.print(",");
                lineWriter.print(player.getFullName());
                lineWriter.print(",");
                lineWriter.print(player.getPassword());
                lineWriter.print(",");
                lineWriter.print(player.getXP());
                lineWriter.print(",");
                lineWriter.print(player.getGold());
                lineWriter.print(",");
                lineWriter.print(player.getRank().getRankName());
                lineWriter.println();
            }
        } catch (IOException e) {
			throw new CityException("UnableWrite");
        } finally {
            lineWriter.close();
        }
    }

}
