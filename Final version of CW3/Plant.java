import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of plants.
 *
 * @author Chi Bene Chen and Rui Han Ji Chen
 * @version 2016.02.29 (2)
 */
public abstract class Plant extends Actor{
    /**
     * Create a new plant at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field
     * @param boolean If random age or not
     */
    public Plant(boolean randomAge, Field field, Location location){
        super(randomAge,field, location);
        setAge(0);
    }
    
    /**
     * Multiplies a plant.
     */
    protected void multiplyActor(List<Actor> plants, Class plantClass){
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = multiply(plantClass);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Actor young;
            if(plantClass == Grass.class) {
                young = new Grass(false,getField(), loc);
                plants.add(young);
            }
        }
    }
    
    /**
     * Calculates the number of plants it will multiply.
     */
    protected int multiply(Class actorType){
        int births = 0;
        if(canMultiply(actorType)) {
            births = getRandom().nextInt(getMaxLitterSize());
        }
        return births;
    }
    
    /**
     * Checks the condiction whether the plant is able to multiply or not;
     */
    protected boolean canMultiply(Class actorType){
        return (getAge() >= getMultiplyAge());
    }
}