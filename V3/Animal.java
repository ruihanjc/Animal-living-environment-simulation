import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    
    private int age;

    private final boolean isFemale;
    
    private final Random rand = Randomizer.getRandom();
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        isFemale = rand.nextBoolean();
        age = 0 ;
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);
    
    abstract protected int getMaxAge();
    
    abstract protected int getBreedingAge();
    
    abstract protected int getMaxLitterSize();
    
    abstract protected double getBreedingProbability();

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    protected void setAge(int age){
        this.age = age;
    }
    
    protected void incrementAge()
    {
        age++;
        if(this.age > this.getMaxAge()) {
            setDead();
        }
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    protected Random getRandom(){
        return rand;
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }

    protected boolean getIsFemale(){
        return isFemale;
    }
    
    protected int breed(Class animalType){
        int births = 0;
        if(canBreed(animalType) && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    protected boolean canBreed(Class animalType)
    {
        return (age >= getBreedingAge() && possibleToBreed(animalType));
    }
    
    protected boolean possibleToBreed(Class animalType){
        for(Location location : getField().adjacentLocations(getLocation())){
           Object locationObject = getField().getObjectAt(location);
           if(locationObject != null && animalType.isInstance(locationObject)){
               Animal a = (Animal)locationObject;
               if (getIsFemale() && !a.getIsFemale()) {
                   return true;
               }
           }
        }
        return false;
    }

}
