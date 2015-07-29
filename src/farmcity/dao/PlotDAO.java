package farmcity.dao;

import farmcity.entity.Crop;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import farmcity.entity.*;
import farmcity.exception.*;

/**
 * This class contains methods to retrieve and modify data from the PlotData CSV File
 * @author G3-T12
 */
public class PlotDAO {

    private static PlotDAO plotDAO;
    private HashMap<String, ArrayList<Plot>> plotMap;
    final private String PLOT_DATA_DIRECTORY = "./data/PlotData.csv";
    private CropDAO cropDAO;
    
	/**
	 * This constructor calls the load method to load all the player's plots.
	 * @throws CityException If the file(s) cannot be found at the specified directory
	 */
    private PlotDAO() throws CityException{
        load();
    }
	
	/** This method initializes the Plot Data Access Object
	 * @return Data Access Object for Plot
	 * @throws CityException If the file(s) cannot be found at the specified directory
	 */
    public static PlotDAO getInstance() throws CityException{
        if (plotDAO == null) {
            plotDAO = new PlotDAO();
        }
        return plotDAO;
    }
	
	/**
	 * This method set the current Plot Data Access Object to null
	 */
    public static void destroyInstance() {
        plotDAO = null;
    }
	
	/**
	 * This method loads all the player's relevant plot data into a HashMap
	 * @throws CityException if the file(s) cannot be found at the specified directory
	 */

    public void load() throws CityException{

        cropDAO = CropDAO.getInstance();

        plotMap = new HashMap<String, ArrayList<Plot>>();
        File file = new File(PLOT_DATA_DIRECTORY);

        Scanner fileReader = null;
        ArrayList<String> playerPlotData = new ArrayList<String>();

        try {

            fileReader = new Scanner(file);

            fileReader.nextLine();

            while (fileReader.hasNext()) {
                playerPlotData.add(fileReader.nextLine());

            }
        } catch (FileNotFoundException e) {
			throw new CityException("InvalidFile");
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }

        HashMap<String, Player> allPlayers = PlayerDAO.getInstance().getAllPlayers();

        Iterator<String> playerIter = allPlayers.keySet().iterator();

        while (playerIter.hasNext()) {
            String username = playerIter.next();
            Player player = allPlayers.get(username);
            ArrayList<Plot> plotList = new ArrayList<Plot>();

            plotMap.put(username, plotList);
            player.setPlotList(plotList);
        }

        for (int i = 0; i < playerPlotData.size(); i++) {

            String[] splitLine = playerPlotData.get(i).split(",");

            String username = splitLine[0];
            
            int plotNumber = Integer.parseInt(splitLine[1]);

            Crop crop = null;
            char status = 'e';
            String[] dateData;
            int percentage = 0;

            Calendar timePlanted = null;

            if (!splitLine[2].equals("null")) {
                
                crop = cropDAO.getCropByName(splitLine[2]);

                dateData = splitLine[3].split(" ");

                String[] date = dateData[0].split("/");
                String[] time = dateData[1].split(":");

                int day = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]) - 1;
                int year = Integer.parseInt(date[2]);

                int hour = Integer.parseInt(time[0]);
                int min = Integer.parseInt(time[1]);
                int sec = Integer.parseInt(time[2]);

                timePlanted = new GregorianCalendar(year, month, day, hour, min, sec);
                
            }

            ArrayList<Plot> playerPlots = plotMap.get(username);
            playerPlots.add(new Plot(plotNumber, status, timePlanted, percentage, crop));
        }

    }
	
	/**
	 * This method returns all the Plots extracted
	 * @return ArrayList of all the Plots
	 */
	
    public ArrayList<Plot> getAllPlots(String username) {
        return plotMap.get(username);
    }
    
	/**
	 * This method allows player to save his/ her plots' current progress to the database file.
	 * @throws CityException if there is something wrong with writing to the file
	 */

    public void save() throws CityException{	//write information to file

        File file = new File(PLOT_DATA_DIRECTORY);
        Scanner lineReader = null;
        ArrayList<String> copyLine = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        PrintStream lineWriter = null;

        try {
            lineWriter = new PrintStream(new FileOutputStream(file));

            lineWriter.println("Username,PlotID,CropName,DatePlanted");

            Iterator<String> plotMapIter = plotMap.keySet().iterator();

            while (plotMapIter.hasNext()) {

                String username = plotMapIter.next();
                ArrayList<Plot> allPlots = plotMap.get(username);

                for (Plot plot : allPlots) {
                    lineWriter.print(username);
                    lineWriter.print(",");
                    lineWriter.print(plot.getPlotNumber());
                    lineWriter.print(",");
                    if (plot.getCrop() != null && plot.getTimePlanted() != null) {
                        lineWriter.print(plot.getCrop().getCropName());
                        lineWriter.print(",");
                        lineWriter.print(sdf.format(plot.getTimePlanted().getTime()));
                    } else {
                        lineWriter.print("null");
                        lineWriter.print(",");
                        lineWriter.print("null");
                    }
                    lineWriter.println();
                }
            }
        } catch (IOException e) {
			throw new CityException("UnableWrite");
        } finally {
            lineWriter.close();
        }
    }

   

    public void addPlot(String username, int quantity) {

        ArrayList<Plot> plotList = plotMap.get(username);
        
        if (plotList == null) {
            plotMap.put(username, new ArrayList<Plot>());
            plotList = plotMap.get(username);
        }
        for (int i = 0; i < quantity; i++) {
            plotList.add(new Plot(plotList.size()+1));
        }
    }

}
