import java.util.List;
/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @edited Chi Bene Chen and Rui Han Ji Chen
 * @version 2016.02.29 (2)
 */
public class Fox extends Animal{
    private static final int MULTIPLY_AGE = 8;
    // The age to which a snake can live.
    private static final int MAX_AGE = 20;
    // The likelihood of a snake breeding.
    private static final double MULTIPLY_PROBABILITY = 0.07;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a snake can go before it has to eat again.
    private static final int FOOD_VALUE = 4;
    // A shared random number generator to control breeding.    
    private static final double INFECTION_RATE = 0.2;
    
    private static final boolean IS_CORNIVORE = true;
    
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
     * @param newFoxes A list to return newly born foxes.
     */
    @Override
    public void act(List<Actor> newFoxes){
        super.incrementAge();
        incrementHunger();
        if(isAlive()) {
            multiplyActor(newFoxes, this.getClass());
            // Move towards a source of food if found.
            Location newLocation = findFood(FOOD_VALUE);
            if(newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
            }else{
                // Overcrowding.
                setDead();
            }
            if(getHaveDisease()){
               infecting();
               killing();
            }
        }
    }
    
    /**
     * Returns the maximum age of each kind of actor
     */
    @Override
    protected int getMaxAge(){
        return MAX_AGE;
    }

    /**
     * Returns the age the the animal can breed
     */
    @Override
    protected int getMultiplyAge(){
        return MULTIPLY_AGE;
    }

    /**
     * Returns the maximum size that can breed 
     */
    @Override
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }

    /**
     * Returns the probability of breeding
     */
    @Override
    protected double getMultiplyProbability(){
        return MULTIPLY_PROBABILITY;
    }
    
    /**
     * Returns the infection rate
     */
    @Override
    protected double getInfectionRate(){
        return INFECTION_RATE;
    }

    /**
     * Returns the food value
     */
    @Override
    protected int getFoodValue(){
        return FOOD_VALUE;
    }

    /**
     * Returns the food level
     */
    @Override
    protected int getFoodLevel(){
        return foodLevel;
    }
    
    /**
     * @return a boolean that indicate whether this animal is a carnivore
     */
    @Override
    protected boolean isCarnivore(){
        return IS_CORNIVORE;
    }
    
    /**
     * Adds the foodlevel of the animal
     */
    @Override
    protected void addFoodLevel(int foodValue){
        foodLevel += FOOD_VALUE;
    }
    
    /**
     * Returns the maximum age of each kind of actor
     */
    @Override
    protected void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
}