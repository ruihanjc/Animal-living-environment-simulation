import java.util.List;

/**
 * A simple model of a Cow.
 * Cows age, move, breed, and die.
 * 
 * @author Chi Ben Chen and Rui Han Ji Chen
 * @version 2021/3/2
 */
public class Cow extends Animal{
    private static final int MULTIPLY_AGE = 4;
    // The age to which a cow can live.
    private static final int MAX_AGE = 30;
    // The likelihood of a cow breeding.
    private static final double MULTIPLY_PROBABILITY = 0.35;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single Cow. In effect, this is the
    // number of steps a Cow can go before it has to eat again.
    private static final int FOOD_VALUE = 9;
    // A shared random number generator to control breeding.    
    private static final double INFECTION_RATE = 0.07;
    
    private static final boolean IS_CORNIVORE = false;
    
    private int foodLevel;
    
    public Cow(boolean randomAge, Field field, Location location){
        super(randomAge,field, location);
        if (randomAge) {
            setAge(super.getRandom().nextInt(getMaxAge()));
            foodLevel = super.getRandom().nextInt(10);
        } else {
            foodLevel = 10;
        }
    }

    /**
     * This is what the Cow does most of the time - it runs
     * around and eats grass. Sometimes it will breed or die of old age.
     * @param newActors A list to return newly born Cows.
     */
    @Override
    public void act(List<Actor> newActors){
        super.incrementAge();
        incrementHunger();
        if(isAlive()){
            multiplyActor(newActors, this.getClass());
            Location newLocation = findFood(FOOD_VALUE);
            if(newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                super.setLocation(newLocation);
            }else{
                // Overcrowding.
                super.setDead();
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