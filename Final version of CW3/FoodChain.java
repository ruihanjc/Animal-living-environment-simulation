import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
/**
 * Write a description of class FoodChain here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class FoodChain
{
    private static final HashMap<Class,Class[]> huntingRelation = new HashMap<>();
    /**
     * Constructor for objects of class FoodChain
     */
    public FoodChain()
    {
        huntingRelation.put(Lion.class, new Class[]{Cow.class});
        huntingRelation.put(Snake.class, new Class[]{Cow.class});
        huntingRelation.put(Cow.class, new Class[]{Grass.class});             
        huntingRelation.put(Wolf.class, new Class[]{Sheep.class});
        huntingRelation.put(Sheep.class, new Class[]{Grass.class});
    }

    public static boolean isRelationExist(Animal predator, Actor prey)
    {
        Class[] preys = huntingRelation.get(predator.getClass());
        if(preys == null){
            return false;
        }
        return Arrays.asList(preys).contains(prey.getClass());        
    }
}
