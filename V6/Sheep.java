import java.util.List;

/**
 * A simple model of a sheep.
 * Sheep's age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Sheep extends Animal {
    /**
     * Create a new sheep. A sheep may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the sheep will have a random age.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Sheep(boolean randomAge, Field field, Location location) {
        super(randomAge,field, location);
        setMultiplyAge(9);
        setFoodValue(6);
        setMaxLitterSize(4);
        setMaxAge(40);
        setMultiplyProbability(0.12);
        setInfectionRate(0.06);
        setFoodLevel(5);
        if (randomAge) {
            setAge(super.getRandom().nextInt(getMaxAge()));
            setFoodLevel(super.getRandom().nextInt(getFoodLevel()));
        } else {
            setFoodLevel(5);
        }
    }

    /**
     * This is what the sheep does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     *
     * @param newSheep A list to return newly born sheep.
     */
    @Override
    public void act(List<Actor> newSheep) {
        super.incrementAge();
        if (isAlive()) {
            giveBirth(newSheep, Sheep.class);
            Location newLocation = findFood(Grass.class, super.getFoodValue());
            if (newLocation == null) {
                newLocation = super.getField().freeAdjacentLocation(getLocation());
            }
            if (newLocation != null) {
                super.setLocation(newLocation);
            } else {
                // Overcrowding.
                super.setDead();
            }
        }
    }
}