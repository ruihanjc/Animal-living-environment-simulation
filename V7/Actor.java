import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of actors.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Actor{
    // Whether the actor is alive or not.
    private boolean alive;
    // The actor's field.
    private Field field;
    // The actor's position in the field.
    private Location location;
    
    private int age;
    
    private final Random rand = Randomizer.getRandom();
    
    private static int MULTIPLY_AGE;
    
    private static int MAX_AGE;
    
    private static double MULTIPLY_PROBABILITY;
    
    private static int MAX_LITTER_SIZE;
    /**
     * Create a new actor at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Actor(boolean randomAge,Field field, Location location){
        alive = true;
        this.field = field;
        setLocation(location);
        age = 0 ;
    }

    /**
     * Make this actor act - that is: make it do
     * whatever it wants/needs to do.
     * @param newactors A list to receive newly born actors.
     * @param newActors
     */
    abstract public void act(List<Actor> newActors);

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
    
    protected static void setMultiplyAge(int multiplyAge){
        MULTIPLY_AGE = multiplyAge;
    }

    protected static void setMaxAge(int maxAge){
        MAX_AGE = maxAge;
    }

    protected static void setMultiplyProbability(double multiplyProbability){
        MULTIPLY_PROBABILITY = multiplyProbability;
    }

    protected static void setMaxLitterSize(int maxLitterSize){
        MAX_LITTER_SIZE = maxLitterSize;
    }

    /**
     * Check whether the actor is alive or not.
     * @return true if the actor is still alive.
     */
    protected boolean isAlive(){
        return alive;
    }
    
    protected void setAge(int age){
        this.age = age;
    }

    protected int getAge(){
        return age;
    }
    
    protected void incrementAge(){
        age++;
        if(this.age > this.getMaxAge()) {
            setDead();
        }
    }
    /**
     * Indicate that the actor is no longer alive.
     * It is removed from the field.
     */
    protected void setDead(){
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the actor's location.
     * @return The actor's location.
     */
    protected Location getLocation(){
        return location;
    }
    
    /**
     * Place the actor at the new location in the given field.
     * @param newLocation The actor's new location.
     */
    protected void setLocation(Location newLocation){
        if(location != null){
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    protected Random getRandom(){
        return rand;
    }
    
    /**
     * Return the actor's field.
     * @return The actor's field.
     */
    protected Field getField(){
        return field;
    }

}