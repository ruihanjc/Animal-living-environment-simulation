import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal extends Actor{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    // The animal's position in the field.
    private Location location;

    private final boolean isFemale;
    
    private final Random rand = Randomizer.getRandom();

    private final Disease disease = new Disease();

    private boolean haveDisease;

    private int foodLevel;

    private static int BREEDING_AGE;
    // The age to which a sheep can live.
    private static int MAX_AGE;
    // The likelihood of a sheep breeding.
    private static double BREEDING_PROBABILITY;
    private static int MAX_LITTER_SIZE;

    private static int FOOD_VALUE;

    private double INFECTION_RATE;
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location){
        super(field, location);
        isFemale = rand.nextBoolean();
        if(rand.nextDouble() <= getInfectionRate()){
            haveDisease = true;
        }
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    public abstract void act(List<Actor> newAnimals);

    protected int getMaxAge(){
        return MAX_AGE;
    }

    protected int getBreedingAge(){
        return BREEDING_AGE;
    }

    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }

    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }

    protected double getInfectionRate(){
        return INFECTION_RATE;
    }

    protected int getFoodValue(){
        return FOOD_VALUE;
    }

    protected int getFoodLevel(){
        return foodLevel;
    }

    protected boolean getIsFemale(){
        return isFemale;
    }

    protected Location getLocation(){
        return location;
    }

    protected void setLocation(Location newLocation){
        if(location != null){
            getField().clearAnimal(location);
        }
        location = newLocation;
        getField().place(this, newLocation, false);
    }

    protected static void setBreedingAge(int breedingAge){
        BREEDING_AGE = breedingAge;
    }

    protected static void setMaxAge(int maxAge){
        MAX_AGE = maxAge;
    }

    protected static void setBreedingProbability(double breedingProbability){
        BREEDING_PROBABILITY = breedingProbability;
    }

    protected static void setMaxLitterSize(int maxLitterSize){
        MAX_LITTER_SIZE = maxLitterSize;
    }

    protected static void setFoodValue(int foodValue){
        FOOD_VALUE = foodValue;
    }

    protected void setInfectionRate(double infectionRate){
        INFECTION_RATE = infectionRate;
    }

    protected void setFoodLevel(int foodLevel){
        this.foodLevel = foodLevel;
    }

    protected void setHaveDisease(){
        haveDisease = true;
    }

    protected void diseaseExpand(Animal animal){
        if(haveDisease == true){
            disease.infecting(animal);
            disease.killing(animal);
        }
    }

    protected void giveBirth(List<Actor> actors, Class animalClass){
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = breed(animalClass);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Actor young;
            if(animalClass == Rabbit.class){
                young = new Rabbit(false, getField(),loc);
                actors.add(young);
            }
            if (animalClass == Fox.class){
                young = new Fox(false, getField(),loc);
                actors.add(young);
            }
            if (animalClass == Wolf.class){
                young = new Wolf(false, getField(),loc);
                actors.add(young);
            }
            if (animalClass == Snake.class){
                young = new Snake(false, getField(),loc);
                actors.add(young);
            }
            if (animalClass == Sheep.class){
                young = new Sheep(false, getField(),loc);
                actors.add(young);
            }
        }
    }

    protected Location findFood(Class preyClass, int foodvalue){
        Field field = super.getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) {
                    rabbit.setDead();
                    foodLevel = foodvalue;
                    return where;
                }
            }
        }
        return null;
    }

    protected void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    protected int breed(Class animalType){
        int births = 0;
        if(canBreed(animalType) && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    protected boolean canBreed(Class animalType){
        return (getAge() >= getBreedingAge() && possibleToBreed(animalType));
    }

    protected boolean possibleToBreed(Class animalType){
        for(Location location : getField().adjacentLocations(getLocation())){
            Object locationObject = getField().getObjectAt(location);
            if(locationObject != null && animalType.isInstance(locationObject)){
                Animal a = (Animal)locationObject;
                if (getIsFemale() && !a.getIsFemale()) {
                    return true;
                }
            }
        }
        return false;
    }

}