import java.util.List;

/**
 * A simple model of a sheep.
 * Sheep's age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Sheep extends Animal{
    // The age at which a sheep can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a sheep can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a sheep breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;

    private double infection_rate = 0.6;
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
    public Sheep(boolean randomAge, Field field, Location location){
        super(field, location);
        if(randomAge) {
            setAge(super.getRandom().nextInt(MAX_AGE));
        }
    }
    
    /**
     * This is what the sheep does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newSheep A list to return newly born sheep.
     */
    @Override
    public void act(List<Animal> newSheep){
        incrementAge();
        if(isAlive()) {
            giveBirth(newSheep, Sheep.class);
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

    @Override
    protected double getInfectionRate(){
        return infection_rate;
    }
}
