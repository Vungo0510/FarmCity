package farmcity.dao;

import farmcity.exception.*;


import java.util.*;
import java.io.*;

/**
 * This class contains methods to retrieve data of customized exceptions from
 * the ExceptionData CSV file.
 * 
 * @author G3-T12
 */
public class ExceptionDAO {
	private static HashMap<String, String> exceptionMap;
	final private static String EXCEPTION_DATA_DIRECTORY = "./data/ExceptionData.csv";
	private static ExceptionDAO exceptionDAO;

	/**
     * This constructor extracts all the exceptions data from Exception database
     * to a HashMap of Exception names and displayed messages
	 * @throws FileNotFoundException if the data for exception is not found
     */
	public static void load() throws FileNotFoundException{
		exceptionMap = new HashMap<String,String>();
		
		Scanner fileReader = null;
		
		File file = new File(EXCEPTION_DATA_DIRECTORY);
		
		try {
			fileReader = new Scanner(file);
			
			fileReader.nextLine();
			while(fileReader.hasNext()) {
				String currentLine = fileReader.nextLine();

				String[] splitLine = currentLine.split("/");
				
				String key = splitLine[0];
				String message = splitLine[1];
				
				exceptionMap.put(key,message);
			}
		} catch(FileNotFoundException e) {
			throw new FileNotFoundException("Critical File missing. The application will now terminate.");
		} finally {
			if(fileReader != null) {
				fileReader.close();
			}
		}

	}
	
	/**
     * This method return the Exception message with the name of the Exception as the input
     * @param key the name of the Exception
     * @return the Exception message
     */
	public static String fetchKey(String key) {
		return exceptionMap.get(key);
	}
}