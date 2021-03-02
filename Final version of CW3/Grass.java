import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author Chi Bene Chen and Rui Han Ji Chen
 * @version 2021/3/2
 */
public class Grass extends Plant {
    private static final int MULTIPLY_AGE = 8;
    // The age to which a snake can live.
    private static final int MAX_AGE = 10;
    // The likelihood of a snake breeding.
    private static final double MULTIPLY_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;

    /**
     * Create a new grass at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param boolean Whether it has an random age or not
     */
    public Grass(boolean randomAge,Field field, Location location){
        super(randomAge,field,location);
    }

    /**
     * This is what the grass does, it grows, multiplies and dies, nothing special and nothing more
     * @param newGrass A list to return newly born wolfs.
     */
    @Override
    public void act(List<Actor> newGrass) {
        incrementAge();
        if (isAlive()) {
            multiplyActor(newGrass, this.getClass());
        } else {
            setDead();
        }
    }

    /**
     * Returns the maximum age
     */
    @Override
    protected int getMaxAge(){
        return MAX_AGE;
    }

    /**
     * Returns the age to multiply
     */
    @Override
    protected int getMultiplyAge(){
        return MULTIPLY_AGE;
    }

    /**
     * Returns the maximum number grass it will multiply
     */
    @Override
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }

    /**
     * Returns the probability of multiplying
     */
    @Override
    protected double getMultiplyProbability(){
        return MULTIPLY_PROBABILITY;
    }
}