import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Grass extends Plant {
    private static final int BREEDING_AGE = 40;
    private static final int MAX_AGE = 3000;
    private static final int MAX_LITTER_SIZE = 8;

    public Grass(Field field, Location location){
        super(field,location);

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

    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }

    protected int getBreedingAge(){
        return BREEDING_AGE;
    }
    
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
}