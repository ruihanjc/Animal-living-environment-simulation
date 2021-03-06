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
    /**
     * Create a new actor at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Actor(Field field, Location location){
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

    abstract protected int getMaxAge();

    abstract protected int breed(Class actorType);

    abstract protected boolean canBreed(Class actorType);

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
            field.clearAnimal(location);
        }
    }

    /**
     * Return the actor's location.
     * @return The actor's location.
     */
    abstract protected Location getLocation();
    
    /**
     * Place the actor at the new location in the given field.
     * @param newLocation The actor's new location.
     */
    abstract protected void setLocation(Location newLocation);
    
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