import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Grass extends Plant {
    private static final int MULTIPLY_AGE = 8;
    // The age to which a snake can live.
    private static final int MAX_AGE = 10;
    // The likelihood of a snake breeding.
    private static final double MULTIPLY_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;

    public Grass(boolean randomAge,Field field, Location location){
        super(randomAge,field,location);
    }

    @Override
    public void act(List<Actor> newActors) {
        incrementAge();
        if (isAlive()) {
            giveBirth(newActors, Grass.class);
        } else {
            setDead();
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
}