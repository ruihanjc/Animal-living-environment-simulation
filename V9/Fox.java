import java.util.List;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Fox extends Animal{
    private static final int MULTIPLY_AGE = 10;
    // The age to which a snake can live.
    private static final int MAX_AGE = 130;
    // The likelihood of a snake breeding.
    private static final double MULTIPLY_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a snake can go before it has to eat again.
    private static final int FOOD_VALUE = 9;
    // A shared random number generator to control breeding.    
    private static final double INFECTION_RATE = 0.2;
    
    private int foodLevel;
    
    
    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location);
        if (randomAge) {
            setAge(super.getRandom().nextInt(getMaxAge()));
            foodLevel = super.getRandom().nextInt(40);
        } else {
            foodLevel = 40;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    @Override
    public void act(List<Actor> newFoxes){
        super.incrementAge();
        incrementHunger();
        if(isAlive()) {
            super.giveBirth(newFoxes, Fox.class);
            // Move towards a source of food if found.
            Location newLocation = findFood(Rabbit.class, FOOD_VALUE, "carnivore");
            if (newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                super.setLocation(newLocation);
            } else {
                // Overcrowding.
                super.setDead();
            }
            if(getHaveDisease()){
                infecting();
                killing();
            }
        }
    }
    
    protected int getMaxAge(){
        return MAX_AGE;
    }

    protected int getMultiplyAge(){
        return MULTIPLY_AGE;
    }

    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }

    protected double getMultiplyProbability(){
        return MULTIPLY_PROBABILITY;
    }
    
    protected double getInfectionRate(){
        return INFECTION_RATE;
    }

    protected int getFoodValue(){
        return FOOD_VALUE;
    }

    protected int getFoodLevel(){
        return foodLevel;
    }
    
    protected void addFoodLevel(int foodValue){
        foodLevel += FOOD_VALUE;
    }
    
    protected void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
}