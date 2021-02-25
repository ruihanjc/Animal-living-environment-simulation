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
    private final boolean isFemale;

    private boolean haveDisease;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(boolean randomAge, Field field, Location location){
        super(randomAge,field, location);
        isFemale = getRandom().nextBoolean();
        if(getRandom().nextDouble() <= getInfectionRate()){
            haveDisease = true;
        }
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    public abstract void act(List<Actor> newAnimals);

    abstract protected double getInfectionRate();

    abstract protected int getFoodValue();

    abstract protected int getFoodLevel();
    
    abstract protected void incrementHunger();

    protected boolean getIsFemale(){
        return isFemale;
    }
    
    protected boolean getHaveDisease(){
        return haveDisease;
    }

    protected void setHaveDisease(){
        haveDisease = !haveDisease;
    }
    
    abstract protected void addFoodLevel(int foodValue);

    protected void giveBirth(List<Actor> actors, Class animalClass){
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = multiply(animalClass);
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

    protected Location findFood(Class preyClass, int foodValue, String eatingType) {
        Field field = super.getField();
        if (eatingType.equals("carnivore")) {
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while (it.hasNext()) {
                Location where = it.next();
                Object actor = field.getObjectAt(where);
                if (actor instanceof Rabbit && actor.getClass() == preyClass) {
                    Rabbit rabbit = (Rabbit) actor;
                    if (rabbit.isAlive()) {
                        rabbit.setDead();
                        addFoodLevel(getFoodValue());
                        return where;
                    }
                }
                if (actor instanceof Sheep && actor.getClass() == preyClass) {
                    Sheep sheep = (Sheep) actor;
                    if (sheep.isAlive()) {
                        sheep.setDead();
                        addFoodLevel(getFoodValue());
                        return where;
                    }
                }
            }
        } else if (eatingType.equals("herbivore")) {
            Location down = new Location(getLocation().getRow(), getLocation().getCol(), 0);
            List<Location> adjacent = field.adjacentLocations(down);
            Iterator<Location> it = adjacent.iterator();
            while (it.hasNext()) {
                Location where = it.next();
                Object actor = field.getObjectAt(where);
                if (actor instanceof Grass && actor.getClass() == preyClass) {
                    Grass grass = (Grass) actor;
                    if (grass.isAlive()) {
                        grass.setDead();
                        addFoodLevel(getFoodValue());
                        return where;
                    }
                }
            }
        }
        return null;
    }

    protected int multiply(Class animalType){
        int births = 0;
        if(canMultiply(animalType) && getRandom().nextDouble() <= getMultiplyProbability()) {
            births = getRandom().nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    protected boolean canMultiply(Class animalType){
        return (getAge() >= getMultiplyAge() && possibleToMultiply(animalType));
    }

    protected boolean possibleToMultiply(Class animalType){
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
    
    public void infecting(){
         Field field = super.getField();
         List<Location> nearLocations = field.adjacentLocations(getLocation());
         for(Location loc : nearLocations){
             Object locationObject = field.getObjectAt(loc);
             if(locationObject != null && locationObject instanceof Animal){
                 Animal a = (Animal)locationObject;
                 if(getRandom().nextDouble() <= a.getInfectionRate()) {
                     setHaveDisease();
                 }
             }
         }
    }

    public void killing(){
        if(getAge() > getMaxAge()* 0.5){
            setDead();
            System.out.println("Disease");
         }
    }
}