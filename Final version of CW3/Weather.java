import java.util.List;
import java.util.Random;
/**
 * Write a description of class WeatherSimulator here.
 *
 * @author Chi Bene Chen and Rui Han Ji Chen
 * @version (a version number or a date)
 */
public class Weather
{
    private final static double PROBABILITY_OF_RAIN = 0.4;
    private final static double PROBABILITY_OF_SUN = 0.6;
    private int duration;
    private boolean isRain;
    private final static int INITIAL_DURATION = 10;
    private final static boolean INITIAL_IsRain = false;

    /**
     * Constructor for objects of class WeatherSimulator
     */
    public Weather(){
        duration = duration();
        isRain = isRain();
    }
    
    /**
     * this method return a random boolean value that indicate whether next weather is sunny or rainny 
     */
    public static boolean isRain()
    {
        Random rand = Randomizer.getRandom();
        if(rand.nextDouble() <= PROBABILITY_OF_RAIN){
            return true;
        }
        return false;
    }

    /**
     * The duration of the weather
     */
    private static int duration(){
        Random rand = Randomizer.getRandom();
        return rand.nextInt(3) + 2;
    }

    /**
     * Actions of the weather
     */
    public Weather weatherAct(){
        duration -- ;
        if(duration == 0){
            return new Weather();         
        }
        return this;
    }

    /**
     * Returns duration
     */
    public int getDuration(){
        return duration;
    }

    /**
     * Returns if is raining or not
     */
    public boolean getIsRain(){
        return isRain;
    }
    
    /**
     * Returns the string for the stats
     */
    public String getWeatherInText(){
        if(isRain){
            return "Rain";
        }
        return "Sunny";
    }
}