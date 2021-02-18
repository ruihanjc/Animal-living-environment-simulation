import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any giveBirthgiven grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    // The probability that a snake will be created in any given grid position.
    private static final double SNAKE_CREATION_PROBABILITY = 0.02;
    // The probability that a sheep will be created in any given grid position.
    private static final double SHEEP_CREATION_PROBABILITY = 0.08;
    // The probability that a rabbit will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.03;

    // List of animals in the field.
    private final List<Animal> animals;
    // The current state of the field.
    private final Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;

    private final Time timeCount;

    private boolean showRabbits = true, showFoxes = true, showWolves = true, showSheep = true, showSnakes = true;


    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator(){
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width){
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        animals = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        setMenuBar();
        setButtonBar();
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Snake.class, Color.BLACK);
        view.setColor(Wolf.class, Color.RED);
        view.setColor(Sheep.class, Color.PINK);


        timeCount = new Time();
        timeCount.updateTime(0);
        
        // Setup a valid starting point.
        reset();
    }

    public static void main(String[] args){
        Simulator x = new Simulator();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation(){
        simulate(1000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps){
        for(int step = 1; step <= numSteps; step++) {
            simulateOneStep();
            updateDisplay();
            // delay(60);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep(){
        step++;
        timeCount.updateTime(step);
        if(timeCount.getIsDay()){
          // Provide space for newborn animals.
           List<Animal> newAnimals = new ArrayList<>();        
          // Let all rabbits act.
          for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if(! animal.isAlive()) {
                it.remove();
            }
          }    
          // Add the newly born foxes and rabbits to the main lists.
          animals.addAll(newAnimals);
        }
        updateDisplay();
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset(){
        step = 0;
        animals.clear();
        populate();
        // Show the starting state in the view.
        updateDisplay();
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate(){
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++){
            for(int col = 0; col < field.getWidth(); col++) {
                if(showFoxes && rand.nextDouble() <= FOX_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    animals.add(fox);
                }
                else if(showRabbits && rand.nextDouble() <= RABBIT_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    animals.add(rabbit);
                }
                else if(showSnakes && rand.nextDouble() <= SNAKE_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Snake snake = new Snake(true, field, location);
                    animals.add(snake);
                }
                else if(showSheep&& rand.nextDouble() <= SHEEP_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Sheep sheep = new Sheep(true, field, location);
                    animals.add(sheep);
                }
                else if(showWolves && rand.nextDouble() <= WOLF_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Wolf wolf = new Wolf(true, field, location);
                    animals.add(wolf);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }

    public void setShowRabbits(boolean showRabbits){
        this.showRabbits = showRabbits;
    }

    public void setShowFoxes(boolean showFoxes){
        this.showFoxes = showFoxes;
    }

    public void setShowSheep(boolean showSheep){
        this.showSheep = showSheep;
    }

    public void setShowWolves(boolean showWolves){
        this.showWolves = showWolves;
    }

    public void setShowSnakes(boolean showSnakes){
        this.showSnakes = showSnakes;
    }

    public void setMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu showMenu = new JMenu("Show");
        JCheckBoxMenuItem rabbitItem = new JCheckBoxMenuItem("Rabbits");
        rabbitItem.setSelected(true);
        rabbitItem.addItemListener(e -> {
            setShowRabbits(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateDisplay();
        });

        JCheckBoxMenuItem foxItem = new JCheckBoxMenuItem("Foxes");
        foxItem.setSelected(true);
        foxItem.addItemListener(e -> {
            setShowFoxes(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateDisplay();
        });
        JCheckBoxMenuItem wolfItem = new JCheckBoxMenuItem("Wolfs");
        wolfItem.setSelected(true);
        wolfItem.addItemListener(e -> {
            setShowWolves(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateDisplay();

        });
        JCheckBoxMenuItem snakeItem = new JCheckBoxMenuItem("Snakes");
        snakeItem.setSelected(true);
        snakeItem.addItemListener(e -> {
            setShowSnakes(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateDisplay();
        });
        JCheckBoxMenuItem sheepItem = new JCheckBoxMenuItem("Sheep");
        sheepItem.setSelected(true);
        sheepItem.addItemListener(e -> {
            setShowSheep(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateDisplay();
        });

        showMenu.add(snakeItem);
        showMenu.add(foxItem);
        showMenu.add(rabbitItem);
        showMenu.add(wolfItem);
        showMenu.add(sheepItem);
        menuBar.add(showMenu);

        view.setJMenuBar(menuBar);
        view.pack();
    }

    public void setButtonBar(){
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));

        JButton runButton = new JButton("50 steps");
        runButton.addActionListener(e -> simulate(50));
        runButton.addActionListener(e -> updateDisplay());

        JButton nextStepButton = new JButton("Next");
        nextStepButton.addActionListener(e -> simulateOneStep());
        nextStepButton.addActionListener(e -> updateDisplay());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> reset());
        resetButton.addActionListener(e -> updateDisplay());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(event -> System.exit(0));


        subPanel.add(runButton);
        subPanel.add(resetButton);
        subPanel.add(nextStepButton);
        subPanel.add(quitButton);

        view.getContent().add(subPanel, BorderLayout.EAST);
        view.pack();
    }

    public void updateDisplay(){
        view.showStatus(step,timeCount.getTime(step), timeCount.getDay(), field);
    }
}