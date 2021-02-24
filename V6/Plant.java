import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Plant extends Actor{
    public Plant(boolean randomAge, Field field, Location location){
        super(randomAge,field, location);
        setAge(0);
    }

    protected void giveBirth(List<Actor> plants, Class plantClass){
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

    protected int multiply(Class actorType){
        int births = 0;
        if(canMultiply(actorType)) {
            births = getRandom().nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    protected boolean canMultiply(Class actorType){
        return (getAge() >= getMultiplyAge());
    }
}