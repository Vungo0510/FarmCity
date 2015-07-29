package farmcity.entity;


/**
 * This class contains attributes and getter methods for the Rank entity
 * @author G3-T12
 */
public class Rank {

    private String rankName;
    private int xp;
    private int plotAvailable;
	
	/**
	 * this constructor initializes the Rank object by setting the rankName, xp and number of Plots available 
	 * through the corresponding data passed in 
	 * @param rankName the Rank's name, fixed
	 * @param xp Amount of Xp required for the Rank, fixed
	 * @param plotAvailable Total number of Plots a Player will have when he reaches this Rank, fixed
	 */
    public Rank(String rankName, int xp, int plotAvailable) {
        this.rankName = rankName;
        this.xp = xp;
        this.plotAvailable = plotAvailable;
    }
	
	/**
	 * Return the name of the Rank
	 * @return Return the Rank's name in String 
	 */
    public String getRankName() {
        return rankName;
    }
	
	/**
	 * Return XP required for the Rank
	 * @return Return the XP requirement for the Rank in int
	 */
    public int getXP() {
        return xp;
    }
	
	/**
	 * Return the number of Plots a player will have at the Rank
	 * @return Return number of Plots for the Rank in int
	 */
    public int getPlotAvailable() {
        return plotAvailable;
    }
}
