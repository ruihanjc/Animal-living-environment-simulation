import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @edited Chi Bene Chen and Rui Han Ji Chen
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
     * @param boolean Whether it has an random age or not
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

    /**
     * Returns the infection rate of each animal
     */
    abstract protected double getInfectionRate();

    /**
     * Returns the foodvalue for each animal
     */
    abstract protected int getFoodValue();

    /**
     * Returns the food level of each animal
     */
    abstract protected int getFoodLevel();

    /**
     * Increments the hunger of each animal
     */
    abstract protected void incrementHunger();

    /**
     * Adds the foodlevel of the animal
     */        
    abstract protected void addFoodLevel(int foodValue);

    /**
     * Returns the gender of an animal
     */
    protected boolean getIsFemale(){
        return isFemale;
    }

    /**
     * Returns if an animal has disease or not
     */
    protected boolean getHaveDisease(){
        return haveDisease;
    }

    abstract protected boolean isCarnivore();

    /**
     * Sets if the animal has disease or not
     */
    protected void setHaveDisease(){
        haveDisease = !haveDisease;
    }

    /**
     * Multipy an animal depending of which type of animal is having the coitus.
     * @param List<Actors> The list of actors in the field
     * @param Class The class of animal that is breeding
     */
    protected void multiplyActor(List<Actor> actors, Class animalClass){
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = multiply(animalClass);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Actor young;
            try{
                young = (Animal)animalClass
                .getDeclaredConstructor(new Class[]{boolean.class, Field.class, Location.class})
                .newInstance(false, getField(),loc);
                actors.add(young);
            }
            catch(Exception e){
                System.out.println("there is an error when create new animal");
            }                        
        }
    }

    /**
     * Searching the food depending on the type of animal it is
     * @parm Class preyClass
     * @param String herbivore and carnivore
     * @param int The foodvalue for each animal
     */
    protected Location findFood(int foodValue) {
        Field field = super.getField();
        List<Location> adjacent;
        if(isCarnivore()){
            adjacent = field.adjacentLocations(getLocation()); 
        }else{
            Location down = new Location(getLocation().getRow(), getLocation().getCol(), 0);
            adjacent = field.adjacentLocations(down);
        }
        
        // Since the plants and the animals are in different layers, we have to make sure that the animals doesn't
        //go down the layer since that would make a huge problem.
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location foodLoc = it.next();
            Object actor = field.getObjectAt(foodLoc);
            if(actor instanceof Actor ){
                Actor prey = (Actor)actor;
                if(FoodChain.isRelationExist(this, prey)){
                    if(prey.isAlive()){
                        prey.setDead();
                        addFoodLevel(getFoodValue());
                        Location where  = new Location(foodLoc.getRow(),foodLoc.getCol(),1);
                        return where;
                    }
                }
            }            
        } 
        return null;
    }

    /**
     * The number of births it will have
     */
    protected int multiply(Class animalType){
        int births = 0;
        if(canMultiply() && possibleToMultiply(animalType) && getRandom().nextDouble() <= getMultiplyProbability()) {
            births = getRandom().nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    /**
     * Whether the animal is old enough to breed
     */
    protected boolean canMultiply(){
        return (getAge() >= getMultiplyAge());
    }

    /**
     * Whether the animal is near an male gender animal of the same type
     */
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

    /**
     * Infecting the disease to other animals
     */
    public void infecting(){
        Field field = super.getField();
        if(super.getLocation() != null){
            List<Location> nearLocations = field.adjacentLocations(super.getLocation());
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
    }

    /**
     * Killing the animals with disease if the condiction is true
     */
    public void killing(){
        if(getAge() > getMaxAge()* 0.5){
            setDead();
        }
    }
}