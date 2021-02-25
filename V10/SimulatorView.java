import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kölling
 * @edited Chi Bene Chen and Rui Han Ji Chen
 * @version 2016.02.29
 */
public class SimulatorView extends JFrame{
    // Colors used for empty locations.
    private static Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;
    
    //Setting up the labels for each stat
    private JLabel popLabel;
    private JLabel stepLabel;
    private JLabel timeLabel;
    private JLabel weatherLabel;

    //Setting up the prefixes of the stats labels
    private final String TIME_PREFIX = "Time: ";
    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final String WEATHER_PREFIX =  "Weather: ";
    
    //Setting up the main panel
    private final JPanel statsPanel;
    private final FieldView fieldView;
    private final Container contents;

    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorView(int height, int width){
        stats = new FieldStats();
        colors = new LinkedHashMap<>();

        setTitle("Animal living environment simulator");

        statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout());
        
        //Initializing the labels
        popLabel = new JLabel();
        stepLabel = new JLabel();
        timeLabel = new JLabel();
        weatherLabel = new JLabel();

        statsPanel.add(popLabel);
        statsPanel.add(stepLabel);
        statsPanel.add(timeLabel);
        statsPanel.add(weatherLabel);
        setLocation(100, 50);
        
        fieldView = new FieldView(height, width);
        
        //Add all the panels inside the main one.
        contents = getContentPane();
        contents.add(statsPanel, BorderLayout.SOUTH);
        contents.add(fieldView, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    /**
     * Returns the contents
    */
    public Container getContent(){
        return contents;
    }
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color){
        colors.put(animalClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass){
        Color col = colors.get(animalClass);
        if(col == null){
            // no color defined for this class
            return UNKNOWN_COLOR;
        }else{
            return col;
        }
    }
    
    /**
     * Setting the color of and empty spcae
     * @param: The color you wanna set
    */
    public void setEmptyColor(Color color){
        EMPTY_COLOR = color;
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     * @param weather The current weater
     */
    public void showStatus(int step,int time, String day ,Field field, String currentWeather){
        if(!isVisible()){
            setVisible(true);
        }
        popLabel.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        stepLabel.setText(STEP_PREFIX + step);
        timeLabel.setText(TIME_PREFIX + time );
        weatherLabel.setText(WEATHER_PREFIX + "  " + currentWeather);
        stats.reset();

        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++){
            for(int col = 0; col < field.getWidth(); col++){
                    if(field.getObjectAt(row, col, 1) != null){ //If and object's aisle is in 1
                        Object actor = field.getObjectAt(row, col, 1);
                        if (actor != null) {
                            stats.incrementCount(actor.getClass());
                            fieldView.drawMark(col, row, getColor(actor.getClass()));
                        }
                    }else if(field.getObjectAt(row, col, 0) != null) {    //If and object's aisle is in 0
                            Object actor = field.getObjectAt(row, col, 0);
                            if (actor != null) {
                                stats.incrementCount(actor.getClass());
                                fieldView.drawMark(col, row, getColor(actor.getClass()));
                            }
                    } else {
                        fieldView.drawMark(col, row, EMPTY_COLOR); //If is empty
                    }
            }
        }
        stats.countFinished();
        fieldView.repaint();
    }



    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field){
        return stats.isViable(field);
    }
    
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JPanel{
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale, zScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width){
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        @Override
        public Dimension getPreferredSize(){
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint(){
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }
        
        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color){
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        @Override
        public void paintComponent(Graphics g){
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
