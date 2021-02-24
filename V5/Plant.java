import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Plant extends Actor{
    private boolean alive;
    private Field field;
    private Location location;
    private int age;
    private final Random rand = Randomizer.getRandom();

    public Plant(Field field, Location location){
        super(field, location);
        age = 0 ;
    }

    abstract protected int getMaxAge();

    abstract protected int getBreedingAge();

    abstract protected int getMaxLitterSize();

    @Override
    protected void setLocation(Location newLocation){
        if(location != null){
            getField().clearPlant(location);
        }
        location = newLocation;
        getField().place(this, newLocation, true);
    }

    @Override
    protected Location getLocation(){
        return location;
    }

    protected void giveBirth(List<Actor> plants, Class plantClass){
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(Plant.class);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Plant young;
            if(plantClass == Grass.class) {
                young = new Grass(getField(), loc);
                plants.add(young);
            }
        }
    }

    protected int breed(Class actorType){
        int births = 0;
        if(canBreed(actorType)) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    protected boolean canBreed(Class actorType){
        return (age >= getBreedingAge());
    }

}