/**
 * This is a time class which calculates the time and returns whether if is daytime or not
 *
 * @author Chi Bene Chen and Rui Han Ji Chen
 * @version (a version number or a date)
 */
public class Time{
    private String day;
    
    private int time;
    
    private boolean isDay;

    /**
     * Constructor for objects of class Time
     */
    public Time(){
        isDay = false;
        time = 0;
    }

    /**
     * It updates the time and calculates whether if is day or night
     * @param int current steps,
     */
    public void updateTime(int step){
        time = step % 24;
        if(time > 20 || time < 8){
            day = "Night";
            isDay = false;
        } else {
            day = "Day";
            isDay = true;
        }
    }
    
    /**
     * Returns the time with the given steps
     * @param steps
     */
    public int getTime(int step){
        return step % 24;
    }
    
    /**
     * Returns if is day or not
     */
    public boolean getIsDay(){
        return isDay;
    }
    
    /**
     * Returns the text of day or night
     */
    public String getDay(){
        return day;
    }
}
