import java.util.List;

/**
 * A simple model of a wolf.
 * Wolfs age, move, eat sheeps, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Wolf extends Animal{

    /**
     * Create a wolf. A wolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the wolf will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location){
        super(randomAge,field, location);
        setMultiplyAge(20);
        setFoodValue(12);
        setMaxLitterSize(5);
        setMaxAge(200);
        setMultiplyProbability(0.15);
        setInfectionRate(0.01);
        if (randomAge) {
            setAge(super.getRandom().nextInt(getMaxAge()));
            setFoodLevel(super.getRandom().nextInt(getFoodLevel()));
        } else {
            setFoodLevel(25);
        }
    }
    
    /**
     * This is what the wolf does most of the time: it hunts for
     * sheeps. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newWolfs A list to return newly born wolfs.
     */
    @Override
    public void act(List<Actor> newWolfs){
        super.incrementAge();

        if(isAlive()){
            super.giveBirth(newWolfs, Wolf.class);
            // Move towards a source of food if found.
            Location newLocation = findFood(Sheep.class, super.getFoodValue());
            if(newLocation == null){
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null){
                super.setLocation(newLocation);
            }else{
                // Overcrowding.
                super.setDead();
            }
        }
    }
}
