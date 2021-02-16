import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a wolf.
 * Wolfs age, move, eat sheeps, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Wolf extends Animal
{
    // Characteristics shared by all wolfs (class variables).
    
    // The age at which a wolf can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a wolf can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a wolf breeding.
    private static final double BREEDING_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single sheep. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int SHEEP_FOOD_VALUE = 9;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The wolf's age.
    private int age;
    // The wolf's food level, which is increased by eating sheeps.
    private int foodLevel;

    /**
     * Create a wolf. A wolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the wolf will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location)
    {
        super(field, location, rand.nextBoolean());
        if (randomAge) {
            super.setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(SHEEP_FOOD_VALUE);
        } else {
            foodLevel = SHEEP_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the wolf does most of the time: it hunts for
     * sheeps. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newWolfs A list to return newly born wolfs.
     */
    public void act(List<Animal> newWolfs)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newWolfs);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Make this wolf more hungry. This could result in the wolf's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for sheeps adjacent to the current location.
     * Only the first live sheep is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Sheep) {
                Sheep sheep = (Sheep) animal;
                if(sheep.isAlive()) { 
                    sheep.setDead();
                    foodLevel = SHEEP_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this wolf is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newWolfs A list to return newly born wolfs.
     */
    private void giveBirth(List<Animal> newWolfs)
    {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(Wolf.class);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Wolf young = new Wolf(false, field, loc);
            newWolfs.add(young);
        }
    }
    
    protected int getMaxAge(){
        return MAX_AGE;
    }
    
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }
    
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
    
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
}
