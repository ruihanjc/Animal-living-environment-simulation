import java.util.List;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Fox extends Animal{
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
        super(field, location);
        setBreedingAge(8);
        setFoodValue(9);
        setMaxLitterSize(5);
        setMaxAge(150);
        setBreedingProbability(1);
        setInfectionRate(0.12);
        if (randomAge) {
            setAge(super.getRandom().nextInt(getMaxAge()));
            setFoodLevel(super.getRandom().nextInt(getFoodLevel()));
        } else {
            setFoodLevel(10);
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    @Override
    public void act(List<Actor> newFoxes){
        super.incrementAge();
        super.incrementHunger();
        if(isAlive()) {
            super.giveBirth(newFoxes, Fox.class);
            // Move towards a source of food if found.
            Location newLocation = findFood(Rabbit.class, super.getFoodValue());
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                super.setLocation(newLocation);
            }
            else {
                // Overcrowding.
                super.setDead();
            }
        }
    }
}