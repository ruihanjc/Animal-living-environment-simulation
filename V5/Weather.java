import java.util.Random;
/**
 * Write a description of class WeatherSimulator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Weather
{
    private final static double PRABABILITY_OF_RAIN = 0.4;
    private final static double PRABABILITY_OF_SUN = 0.6;
    private int duration;
    private boolean isRain;
    private final static int INITUAL_DURATION = 10;
    private final static boolean INITUAL_IsRain = false;

    /**
     * Constructor for objects of class WeatherSimulator
     */
    public Weather(){
        duration = duration();
        isRain = isRain();
    }

    private static boolean isRain()
    {
        Random rand = Randomizer.getRandom();
        if(rand.nextDouble() <= PRABABILITY_OF_RAIN){
            return true;
        }
        return false;
    }

    private static int duration(){
        Random rand = Randomizer.getRandom();
        int duration = rand.nextInt(3) + 2;
        return duration;
    }

    public Weather weatherAct(){
        duration -- ;
        if(isRain()){

        }
        else{

        }

        if(duration == 0){
            return new Weather();         
        }
        return this;
    }

    public int getDuration(){
        return duration;
    }

    public boolean getIsRain(){
        return isRain;
    }

    public String getWeatherInText(){
        if(isRain){
            return "Rain";
        }
        return "Sunny";
    }

}
