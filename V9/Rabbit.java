import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Rabbit extends Animal{
    private static final int MULTIPLY_AGE = 10;
    // The age to which a snake can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a snake breeding.
    private static final double MULTIPLY_PROBABILITY = 0.17;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a snake can go before it has to eat again.
    private static final int FOOD_VALUE = 9;
    // A shared random number generator to control breeding.    
    private static final double INFECTION_RATE = 0.2;
    
    private int foodLevel;
    
    
    public Rabbit(boolean randomAge, Field field, Location location){
        super(randomAge,field, location);
        if (randomAge) {
            setAge(super.getRandom().nextInt(getMaxAge()));
            foodLevel = super.getRandom().nextInt(20);
        } else {
            foodLevel = 20;
        }
    }

    /**
     * This is what the rabbit does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newActors A list to return newly born rabbits.
     */
    @Override
    public void act(List<Actor> newActors){
        super.incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newActors, Rabbit.class);
            Location newLocation = findFood(Grass.class, FOOD_VALUE,"herbivore");
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
