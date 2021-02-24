import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Grass extends Plant {
    public Grass(boolean randomAge,Field field, Location location){
        super(randomAge,field,location);
        setMultiplyAge(8);
        setMaxLitterSize(1);
        setMaxAge(10);
        setMultiplyProbability(0.07);
        if(randomAge){
           setAge(super.getRandom().nextInt(getMaxAge()));
        }
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
}