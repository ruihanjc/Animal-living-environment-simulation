/**
 * Write a description of class Time here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Time
{
    private String day;
    
    private int time;
    
    private boolean isDay;

    /**
     * Constructor for objects of class Time
     */
    public Time()
    {
        isDay = false;
        time = 0;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
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
    
    public int getTime(int step){
        return step % 24;
    }
    
    public boolean getIsDay(){
        return isDay;
    }
    
    public String getDay(){
        return day;
    }
}
