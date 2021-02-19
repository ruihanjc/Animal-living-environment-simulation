import java.util.List;

/**
 * Write a description of class Disease here.
 *
 * @author RuiHan Ji Chen
 * @version (a version number or a date)
 */
public class Disease{

    public Disease(){}

    public void infecting(Animal infectedAnimal){
        Field field = infectedAnimal.getField();
        List<Location> nearLocations = field.getFreeAdjacentLocations(infectedAnimal.getLocation());
        for(Location loc : nearLocations){
            Object locationObject = field.getObjectAt(loc);
            if(locationObject != null && infectedAnimal.getRandom().nextDouble() <= infectedAnimal.getInfectionRate()){
                Animal a = (Animal)locationObject;
                a.setHaveDisease();
            }
        }
    }

    public void killing(Animal infectedAnimal){
        if(infectedAnimal.getAge() > infectedAnimal.getMaxAge()* 0.5){
            infectedAnimal.setDead();
        }
    }
}
