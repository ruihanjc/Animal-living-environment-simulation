import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal.
 * However, we added a new dimension in the location to able to store a grass below the animals, so an animal can step
 * on the grass without destroying it.
 * 
 * @author David J. Barnes and Michael Kölling
 * @edited Chi Bene Chen and Rui Han Ji Chen
 * @version 2016.02.29
 */
public class Field{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The depth and width of the field.
    private int depth, width, height;
    // Storage for the animals.
    private Object[][][] field; //3 dimension

    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width){
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width][2];
    }

    public Field(int depth, int width, int height){
        this.depth = depth;
        this.width = width;
        this.height = height;
        field = new Object[depth][width][height];
    }

    /**
     * Empty the field.
     */
    public void clear(){
        for(int row = 0; row < depth; row++){
            for(int col = 0; col < width; col++){
                field[row][col][1] = null;
                field[row][col][0] = null;
            }
        }
    }
    
    /**
     * Clear the given location.
     * @param location The location to clear.
     */
    public void clear(Location location){
        field[location.getRow()][location.getCol()][location.getAis()] = null;
    }

    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will
     * be lost.
     * @param animal The animal to be placed.
     * @param row Row coordinate of the location.
     * @param col Column coordinate of the location.
     * @param ais Aisle coordinate of the location.
     */
    public void place(Object actor, int row, int col, int ais){
        place(actor, new Location(row, col, ais));
    }
    
    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will
     * be lost.
     * @param animal The animal to be placed.
     * @param location Where to place the animal.
     * @param ais Aisle coordinate of the location.
     */
    public void place(Object actor, Location location){
        field[location.getRow()][location.getCol()][location.getAis()] = actor;
    }
    
    /**
     * Return the animal at the given location, if any.
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Object getObjectAt(Location location){
        return getObjectAt(location.getRow(), location.getCol(),location.getAis());
    }
    /**
     * Return the animal at the given location, if any.
     * @param row The desired row.
     * @param col The desired column.
     * @return The animal at the given location, or null if there is none.
     */
    public Object getObjectAt(int row, int col, int ver){
        return field[row][col][ver];
    }

    /**
     * Generate a random location that is adjacent to the
     * given location, or is the same location.
     * The returned location will be within the valid bounds
     * of the field.
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location randomAdjacentLocation(Location location){
        List<Location> adjacent = adjacentLocations(location);
        return adjacent.get(0);
    }
    
    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location){
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            if(getObjectAt(next) == null) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Try to find a free location that is adjacent to the
     * given location. If there is none, return null.
     * The returned location will be within the valid bounds
     * of the field.
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location freeAdjacentLocation(Location location){
        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location);
        if(free.size() > 0){
            return free.get(0);
        }else{
            return null;
        }
    }

    /**
     * Returns all free locations in the field from a layer
     * @int The layer means aisle number of the location
     */
    public List<Location> getAllFreeLocations(int layer){
        List<Location> free = new LinkedList<>();
        for(int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                if (getObjectAt(row, col, layer) == null) {
                    Location location = new Location(row, col, layer);
                    free.add(location);
                }
            }
        }
        return free;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * However, the adjacent locations only means the adjacent locations in one aisle
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location){
        assert location != null : "Null location passed to adjacentLocations";
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<>();
        if(location != null){
            int row = location.getRow();
            int col = location.getCol();
            for(int roffset = -1; roffset <= 1; roffset++){
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth){
                    for(int coffset = -1; coffset <= 1; coffset++){
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)){
                            locations.add(new Location(nextRow, nextCol, location.getAis())); //As you can see, the aisle is the same one
                        }
                    }
                }
            }
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth(){
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth(){
        return width;
    }
    
    /**
     * Return the height of the field.
     * @return The height of the field.
     */
    public int getHeight(){
        return height;
    }
}