/**
 * Represent a location in a rectangular grid.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class Location{
    // Row and column positions.
    private int row;
    private int col;
    private int ais;
    /**
     * Represent a row and column.
     * @param row The row.
     * @param col The column.
     */
    public Location(int row, int col, int ais){
        this.row = row;
        this.col = col;
        this.ais = ais;
    }
    
    /**
     * Implement content equality.
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol() && ais == other.getAis();
        }
        else {
            return false;
        }
    }
    
    /**
     * Return a string of the form row,column
     * @return A string representation of the location.
     */
    @Override
    public String toString(){
        return row + "," + col + "," + ais;
    }
    
    /**
     * Use the top 16 bits for the row value and the bottom for
     * the column. Except for very big grids, this should give a
     * unique hash code for each (row, col) pair.
     * @return A hashcode for the location.
     */
    @Override
    public int hashCode(){
        return (row << 16) + col;
    }
    
    /**
     * @return The row.
     */
    public int getRow(){
        return row;
    }
    /**
     * @return The column.
     */
    public int getCol(){
        return col;
    }

    public int getAis(){
        return ais;
    }
}