import java.util.List;
import java.util.Random;
/**
 * This class simulate the weather.
 * object of this Class should only have one exist at the time.
 * every time when weather changed, new object with random weather and duration should be created.
 *
 * @author Chi Bene Chen and Rui Han Ji Chen
 * @version 2021/3/2
 */
public class Weather
{
    private final static double PROBABILITY_OF_RAIN = 0.4;
    private final static double PROBABILITY_OF_SUN = 0.6;
    private int duration;
    private final static int INITIAL_DURATION = 10;
    private final static boolean INITIAL_IsRain = false;

    /**
     * Constructor for objects of class WeatherSimulator
     */
    public Weather(){
        duration = duration();
    }
    
    public Weather(int duration){
        this.duration = duration;
    }

    /**
     * this method return a random boolean value that indicate whether next weather is sunny or rainny 
     */
    public static Class decideWeatherType()
    {
        Random rand = Randomizer.getRandom();
        if(rand.nextDouble() <= PROBABILITY_OF_RAIN){
            return Rain.class;
        }
        return Sun.class;
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
        if(duration <= 0){
            try{
                return (Weather)decideWeatherType().getDeclaredConstructor().newInstance();         
            }
            catch(Exception e){
                System.out.println("error occur when create new Weather" + e);
                return this;
            }
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
     * Returns the string of current Weather
     */
    public String getWeatherInText(){
        return this.getClass().getName();
    }
}