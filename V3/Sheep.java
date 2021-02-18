import java.util.List;
import java.util.Random;

/**
 * A simple model of a sheep.
 * Sheeps age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Sheep extends Animal
{
    // The age at which a sheep can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a sheep can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a sheep breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    
    // Individual characteristics (instance fields).


    /**
     * Create a new sheep. A sheep may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the sheep will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Sheep(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(super.getRandom().nextInt(MAX_AGE));
        }
    }
    
    /**
     * This is what the sheep does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newSheeps A list to return newly born sheeps.
     */
    @Override
    public void act(List<Animal> newSheeps)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newSheeps);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
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
     * Check whether or not this sheep is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newSheeps A list to return newly born sheeps.
     */
    private void giveBirth(List<Animal> newSheeps)
    {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed(Sheep.class);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Sheep young = new Sheep(false, field, loc);
            newSheeps.add(young);
        }
    }
    
    @Override
    protected int getMaxAge(){
        return MAX_AGE;
    }
    
    @Override
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }
    
    @Override
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
    
    @Override
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
}
