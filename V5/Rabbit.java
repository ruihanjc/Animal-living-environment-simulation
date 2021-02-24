import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Rabbit extends Animal{

    public Rabbit(boolean randomAge, Field field, Location location){
        super(field, location);
        setBreedingAge(5);
        setFoodValue(5);
        setMaxLitterSize(10);
        setMaxAge(40);
        setBreedingProbability(1);
        setInfectionRate(0.08);
        if(randomAge) {
            super.setAge(super.getRandom().nextInt(getMaxAge()));
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
        if(isAlive()) {
            giveBirth(newActors, Rabbit.class);
            Location newLocation = findFood(Grass.class, super.getFoodValue());
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                super.setLocation(newLocation);
            }else{
                // Overcrowding.
                super.setDead();
            }
        }
    }
}
