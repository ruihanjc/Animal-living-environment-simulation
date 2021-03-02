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
 * containing Cow, Liones, sheep, wolfs and Tigers
 * There's also plants, in this case only grass
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
    // The probability that a Lion will be created in any giveBirthgiven grid position.
    private static final double LION_CREATION_PROBABILITY = 0.06;
    // The probability that a gazelle will be created in any given grid position.
    private static final double COW_CREATION_PROBABILITY = 0.2;
    // The probability that a Tiger will be created in any given grid position.
    private static final double TIGER_CREATION_PROBABILITY = 0.06;
    // The probability that a sheep will be created in any given grid position.
    private static final double SHEEP_CREATION_PROBABILITY = 0.15;
    // The probability that a wolf will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.06;
    // The probability that a grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.1;

    // List of animals in the field.
    private final List<Actor> actors;
    // The current state of the field.
    private final Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;
    // The delay you wannna input into this
    private int delayTime = 0;
    // The time inside the simulation
    private final Time timeCount;
    
    private final Random rand;
    // If the simulation shows or not the animals.
    private boolean showCows = true, showLions = true, showWolves = true, showSheep = true, showTigers = true, showGrass = true;
    //The current weather
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
        }
        actors = new ArrayList<>();
        field = new Field(depth, width);
        rand = Randomizer.getRandom();
        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        // Setting the menubar and the buttons
        setMenuBar();
        setButtonBar();
        // Setting the colors of each actor
        view.setColor(Cow.class, Color.GRAY);
        view.setColor(Lion.class, Color.YELLOW);
        view.setColor(Tiger.class, Color.ORANGE);
        view.setColor(Wolf.class, Color.RED);
        view.setColor(Sheep.class, Color.PINK);
        view.setColor(Grass.class, Color.GREEN.darker());
        
        //create a new food chain
        new FoodChain();
        
        //Weather and time
        currentWeather = new Weather(0);
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
        simulate(1000);     //Run a 1000 steps
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
     * Lion and gazelle.
     */
    public void simulateOneStep(){
        step++;
        timeCount.updateTime(step);
        currentWeather = currentWeather.weatherAct();
        stepAct();    //The actions performed in each act
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
     * The actions performed in each step, depending on the weather, the time and the type of animal, the actors in the simulation 
     * will perform different kind of actions.
     */
    private void stepAct(){
        if(showGrass && currentWeather instanceof Rain){     //If is rainning and there are grasses in the simulation
            List<Location> locations = field.getAllFreeLocations(0);
            for(Location l :locations){
                if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Plant grass = new Grass(true, field, l);
                    actors.add(grass);
                }
            }
        }
        if(timeCount.getIsDay()){      //If is day and depending on the kind of actor, it will perform different kind of actions.
            List<Actor> newActors = new ArrayList<>();
            view.setEmptyColor(Color.white);
            for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ){
                Actor actor = it.next();
                if(actor instanceof Animal){
                    Animal a = (Animal) actor;
                    a.act(newActors);
                }
                if(actor instanceof Plant){
                    if(currentWeather instanceof Rain){
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
    }
    
    /**
     * Randomly populate the field with Liones, Cow, Tigers, wolves, and grass.
     */
    private void populate(){
        field.clear();
        for(int row = 0; row < field.getDepth(); row++){
            for(int col = 0; col < field.getWidth(); col++){
                if(showLions && rand.nextDouble() <= LION_CREATION_PROBABILITY){
                    Location location = new Location(row, col,1);
                    Lion lion = new Lion(true, field, location);
                    actors.add(lion);
                }else if(showCows && rand.nextDouble() <= COW_CREATION_PROBABILITY){
                    Location location = new Location(row, col,1);
                    Cow cow = new Cow(true, field, location);
                    actors.add(cow);
                }else if(showTigers && rand.nextDouble() <= TIGER_CREATION_PROBABILITY){
                    Location location = new Location(row, col,1);
                    Tiger tiger = new Tiger(true, field, location);
                    actors.add(tiger);
                }else if(showSheep && rand.nextDouble() <= SHEEP_CREATION_PROBABILITY){
                    Location location = new Location(row, col,1);
                    Sheep sheep = new Sheep(true, field, location);
                    actors.add(sheep);
                }else if(showWolves && rand.nextDouble() <= WOLF_CREATION_PROBABILITY){
                    Location location = new Location(row, col,1);
                    Wolf wolf = new Wolf(true, field, location);
                    actors.add(wolf);
                } else if(showGrass && rand.nextDouble() <= GRASS_CREATION_PROBABILITY){
                    Location location = new Location(row, col,0);
                    Grass grass = new Grass(false,field, location);
                    actors.add(grass);
                }
            }
        }
    }
    
    /**
     * A toggle to show or not the actor
     * @param: If we wanna show the actor
     */
    private void setShowCows(boolean showCows){
        this.showCows = showCows;
        field.clear();
    }
    
    /**
     * A toggle to show or not the actor
     * @param: If we wanna show the actor
     */
    private void setShowLions(boolean showLions){
        this.showLions = showLions;
        field.clear();
    }
    
    /**
     * A toggle to show or not the actor
     * @param: If we wanna show the actor
     */
    private void setShowSheep(boolean showSheep){
        this.showSheep = showSheep;
        field.clear();
    }
    
    /**
     * A toggle to show or not the actor
     * @param: If we wanna show the actor
     */
    private void setShowWolves(boolean showWolves){
        this.showWolves = showWolves;
        field.clear();
    }
    
    /**
     * A toggle to show or not the actor
     * @param: If we wanna show the actor
     */
    private void setShowTigers(boolean showTigers){
        this.showTigers = showTigers;
        field.clear();
    }
    
    /**
     * A toggle to show or not the actor
     * @param: If we wanna show the actor
     */
    private void setHasGrass(boolean showGrass){
        this.showGrass = showGrass;
        field.clear();
    }
    
    /**
     * Slowing down the simulation speed
     * @param: int, the seconds to delay
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

    /**
     * Setting up the menu bar, and the items inside the menu. If you click it, the system resets and disables the selected actor.
     */
    private void setMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu showMenu = new JMenu("Show");
        JCheckBoxMenuItem cowItem = new JCheckBoxMenuItem("Cow");
        cowItem.setSelected(true);
        cowItem.addItemListener(e ->{
            setShowCows(e.getStateChange() == ItemEvent.SELECTED);
            reset();
            updateStatsDisplay();
        });
        JCheckBoxMenuItem lionItem = new JCheckBoxMenuItem("Lions");
        lionItem.setSelected(true);
        lionItem.addItemListener(e ->{
            setShowLions(e.getStateChange() == ItemEvent.SELECTED);
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
        JCheckBoxMenuItem tigerItem = new JCheckBoxMenuItem("Tigers");
        tigerItem.setSelected(true);
        tigerItem.addItemListener(e ->{
            setShowTigers(e.getStateChange() == ItemEvent.SELECTED);
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
        
        showMenu.add(tigerItem);
        showMenu.add(lionItem);
        showMenu.add(cowItem);
        showMenu.add(wolfItem);
        showMenu.add(sheepItem);
        showMenu.add(grassItem);
        menuBar.add(showMenu);

        view.setJMenuBar(menuBar);
        view.pack();
    }

    /**
     * Setting the buttons on the right of the window, each button has an actions, click it, you perform it.
     */
    private void setButtonBar(){
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        
        JButton nextStepButton = new JButton("Next");
        nextStepButton.addActionListener(e -> simulateOneStep());
        nextStepButton.addActionListener(e -> updateStatsDisplay());

        // This is a special button, since it performs a quite long method, we can use a Swing worker to show each step
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

    /**
     * Updates the stats display
     */
    private void updateStatsDisplay(){
        if(!timeCount.getIsDay()){
            view.setEmptyColor(Color.black);
        }
        view.showStatus(step,timeCount.getTime(step), timeCount.getDay(), field, currentWeather.getWeatherInText());
    }
    
    /**
     * Special method for long simulation to make a multireading process.
     */
    private void runAction(ActionEvent event){
       SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
        protected Void doInBackground() throws Exception {
            delayTime = 60;
            runLongSimulation();
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return null;
        }
       };
       worker.execute();
    }
}