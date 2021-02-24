import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingWorker;
/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @edited Chi Bene Chen and Rui Han Ji Chen
 * @version 2016.02.29 (2)
 */
public class Simulator{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any giveBirthgiven grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.1;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.1;
    // The probability that a snake will be created in any given grid position.
    private static final double SNAKE_CREATION_PROBABILITY = 0.1;
    // The probability that a sheep will be created in any given grid position.
    private static final double SHEEP_CREATION_PROBABILITY = 0.1;
    // The probability that a rabbit will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.1;

    private static final double GRASS_CREATION_PROBABILITY = 0.02;

    // List of animals in the field.
    private final List<Actor> actors;
    // The current state of the field.
    private final Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;
    
    private int delayTime = 0;

    private final Time timeCount;

    private final Random rand;

    private boolean showRabbits = true, showFoxes = true, showWolves = true, showSheep = true, showSnakes = true, showGrass = true;
    
    private Weather currentWeather;
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
            depth = depth + 30;
            width = width + 30;
        }
        actors = new ArrayList<>();
        field = new Field(depth, width);
        rand = Randomizer.getRandom();
        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        setMenuBar();
        setButtonBar();
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Snake.class, Color.YELLOW);
        view.setColor(Wolf.class, Color.RED);
        view.setColor(Sheep.class, Color.PINK);
        view.setColor(Grass.class, Color.GREEN.darker());
        
        currentWeather = new Weather();
        

        timeCount = new Time();
        timeCount.updateTime(0);
        
        // Setup a valid starting point.
        reset();
        updateStatsDisplay();
    }

    public static void main(String[] args){
        Simulator simulator = new Simulator();
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
        for(int step = 1; step <= numSteps; step++){
            simulateOneStep();
            updateStatsDisplay();
            delay(delayTime);   // uncomment this to run more slowly
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
        currentWeather = currentWeather.weatherAct();
        if(showGrass && currentWeather.getIsRain()){
            List<Location> locations = field.getAllFreeLocations();
            for(Location l :locations){
                if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Plant grass = new Grass(true, field, l);
                    actors.add(grass);
                }
            }
        }
        
        if(timeCount.getIsDay()){
          // Provide space for newborn animals.
           List<Actor> newActors = new ArrayList<>();
           view.setEmptyColor(Color.white);
          // Let all rabbits act.
          for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ){
            Actor actor = it.next();
            if(actor instanceof Animal){
                Animal a = (Animal) actor;
                a.act(newActors);
                if(a.getHaveDisease()){
                    a.infecting();
                    a.killing();
                }
            }
            if(actor instanceof Plant){
                if(currentWeather.isRain()){
                    Plant p = (Plant) actor;
                    p.act(newActors);
                }
            }
            if (!actor.isAlive()) {
                it.remove();
            }
          }
            actors.addAll(newActors);
        }
        updateStatsDisplay();
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset(){
        step = 0;
        actors.clear();
        populate();
        // Show the starting state in the view.
        updateStatsDisplay();
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate(){
        field.clear();
        for(int row = 0; row < field.getDepth(); row++){
            for(int col = 0; col < field.getWidth(); col++){
                if(showFoxes && rand.nextDouble() <= FOX_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Fox fox = new Fox(false, field, location);
                    actors.add(fox);
                }else if(showRabbits && rand.nextDouble() <= RABBIT_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(false, field, location);
                    actors.add(rabbit);
                }else if(showSnakes && rand.nextDouble() <= SNAKE_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Snake snake = new Snake(false, field, location);
                    actors.add(snake);
                }else if(showSheep && rand.nextDouble() <= SHEEP_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Sheep sheep = new Sheep(false, field, location);
                    actors.add(sheep);
                }else if(showWolves && rand.nextDouble() <= WOLF_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Wolf wolf = new Wolf(false, field, location);
                    actors.add(wolf);
                } else if(showGrass && rand.nextDouble() <= GRASS_CREATION_PROBABILITY){
                    Location location = new Location(row, col);
                    Grass grass = new Grass(false,field, location);
                    actors.add(grass);
                }
            }
        }
    }

    private void setShowRabbits(boolean showRabbits){
        this.showRabbits = showRabbits;
    }

    private void setShowFoxes(boolean showFoxes){
        this.showFoxes = showFoxes;
    }

    private void setShowSheep(boolean showSheep){
        this.showSheep = showSheep;
    }

    private void setShowWolves(boolean showWolves){
        this.showWolves = showWolves;
    }

    private void setShowSnakes(boolean showSnakes){
        this.showSnakes = showSnakes;
    }
    
    private void setHasGrass(boolean showGrass){
        this.showGrass = showGrass;
    }
    
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }

    private void setMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu showMenu = new JMenu("Show");
        JCheckBoxMenuItem rabbitItem = new JCheckBoxMenuItem("Rabbits");
        rabbitItem.setSelected(true);
        rabbitItem.addItemListener(e ->{
            setShowRabbits(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateStatsDisplay();
        });
        JCheckBoxMenuItem foxItem = new JCheckBoxMenuItem("Foxes");
        foxItem.setSelected(true);
        foxItem.addItemListener(e ->{
            setShowFoxes(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateStatsDisplay();
        });
        JCheckBoxMenuItem wolfItem = new JCheckBoxMenuItem("Wolfs");
        wolfItem.setSelected(true);
        wolfItem.addItemListener(e ->{
            setShowWolves(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateStatsDisplay();
        });
        JCheckBoxMenuItem snakeItem = new JCheckBoxMenuItem("Snakes");
        snakeItem.setSelected(true);
        snakeItem.addItemListener(e ->{
            setShowSnakes(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateStatsDisplay();
        });
        JCheckBoxMenuItem sheepItem = new JCheckBoxMenuItem("Sheep");
        sheepItem.setSelected(true);
        sheepItem.addItemListener(e ->{
            setShowSheep(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateStatsDisplay();
        });
        JCheckBoxMenuItem grassItem = new JCheckBoxMenuItem("Grass");
        grassItem.setSelected(true);
        grassItem.addItemListener(e ->{
            setHasGrass(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateStatsDisplay();
        });
        
        
        showMenu.add(snakeItem);
        showMenu.add(foxItem);
        showMenu.add(rabbitItem);
        showMenu.add(wolfItem);
        showMenu.add(sheepItem);
        showMenu.add(grassItem);
        menuBar.add(showMenu);

        view.setJMenuBar(menuBar);
        view.pack();
    }

    private void setButtonBar(){
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        
        JButton nextStepButton = new JButton("Next");
        nextStepButton.addActionListener(e -> simulateOneStep());
        nextStepButton.addActionListener(e -> updateStatsDisplay());

        JButton runButton = new JButton("Run");
        runButton.addActionListener(this::runAction);
        
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> reset());
        resetButton.addActionListener(e -> updateStatsDisplay());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(event -> System.exit(0));

        subPanel.add(runButton);
        subPanel.add(resetButton);
        subPanel.add(nextStepButton);
        subPanel.add(quitButton);

        view.getContent().add(subPanel, BorderLayout.EAST);
        view.pack();
    }

    private void updateStatsDisplay(){
        if(!timeCount.getIsDay()){
            view.setEmptyColor(Color.black);
        }
        view.showStatus(step,timeCount.getTime(step), timeCount.getDay(), field, currentWeather.getWeatherInText());
    }
    
    private void runAction(ActionEvent event){
       SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
        protected Void doInBackground() throws Exception {
            delayTime = 60;
            runLongSimulation();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return null;
        }
       };
       worker.execute();
    }
}