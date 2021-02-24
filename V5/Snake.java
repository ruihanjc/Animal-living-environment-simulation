import java.util.List;

/**
 * A simple model of a snake.
 * Snakes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Snake extends Animal{

    /**
     * Create a snake. A snake can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the snake will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Snake(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setBreedingAge(10);
        setFoodValue(9);
        setMaxLitterSize(8);
        setMaxAge(90);
        setBreedingProbability(1);
        setInfectionRate(0.08);
        if (randomAge) {
            setAge(super.getRandom().nextInt(getMaxAge()));
            setFoodLevel(super.getRandom().nextInt(getFoodLevel()));
        } else {
            setFoodLevel(10);
        }
    }
    
    /**
     * This is what the snake does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newSnakes A list to return newly born snakes.
     */
    @Override
    public void act(List<Actor> newSnakes){
        super.incrementAge();
        super.incrementHunger();
        if(isAlive()) {
            super.giveBirth(newSnakes, Snake.class);
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
