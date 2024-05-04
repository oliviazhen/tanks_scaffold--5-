package Tanks;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.*;
import java.util.*;


public class App extends PApplet{

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;

    //BOARD represents what board[][] in the checkers game would!
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    int x = 0;
    int y = 200;

    public static final int INITIAL_PARACHUTES = 1;

    //Since your FPS is 30, each frame lasts for 1/30th of a second
    //Since you have 30 frames per second, it needs to move (60 pixels / 30 frames) = 2 pixels per frame
    public static final int FPS = 30;
    float deltaTimeInSeconds = 1.0f / FPS;

    //All JSON variables
    JSONObject jsonData;
    PImage background;
    public int[] foregroundColourRBG;
    double[] movingAvg;

    //create an array to store the terrain based on the levels
    Tile[][] tiles;
    HashMap<Character, Tank> players = new HashMap<>();
    HashMap<Double, Double> tankPositions = new HashMap<>();
    Set<Tree> trees = new HashSet<>();
    Character currentPlayerKey;
    Tank current_player;
    boolean left, right, up, down;


    public App() {
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
        jsonData = loadJSONObject("config.json");
        ReadJSON.loadLevel(this, 0);

        /* 
        if (players.size() == 0){
            System.out.println("There are no players in the game");
        }
        else{
            currentPlayerKey = players.keySet().iterator().next();
            current_player = players.get(currentPlayerKey);

            draw.level(this);

        }
        */
        
        //Print testing to ensure intitialisation is not null
        /* 
        System.out.println(background);
        System.out.println(jsonData);
        System.out.println(smoothedTerrainLine.length);
        System.out.println(tiles.length);
        System.out.println(players.entrySet());
        System.out.println(tankPositions);
        System.out.println(trees);
        */
    }

    /**
     * Calculates the new positions of the trees according the the terrain heights
     * @param treeImagePath
     * @param tiles
     * @param smoothedValues
     * @return HashMap with each new position of the tree
     */
    public HashMap<Integer, Float> newTreePositions(String treeImagePath, Tile[][] tiles, double[] smoothedValues){
        PImage tree = loadImage(treeImagePath);

        //HashMap sorted by Vertical (column FIXED), Horizontal
        HashMap<Integer, Float> treeCoordinates = new HashMap<Integer, Float>();
        Set<Integer> treeVertical = new HashSet<>();

        for (int i = 0; i < tiles.length; i ++){
            for (int j = 0; j < tiles[i].length; j ++){
                if (tiles[i][j].getType() == "tree"){
                    //System.out.printf("There is a tree at row %d, column %d. %n", i, j);
                    treeVertical.add(j);
                }
            }
        }

        for (int i = 0; i < smoothedValues.length; i ++ ){
            if (treeVertical.contains(i)){
                //Plus one due to layering?
                float yPositon = (float) (BOARD_HEIGHT - (smoothedValues[i] + 1));
                int xPosition = i;
                treeCoordinates.put(xPosition, yPositon);
            }
        }
        return treeCoordinates;

    }
    /** The function saves the original column placement of the Tanks (via Tile[][])
     * Using these coordinates, we will find the new center positions
     * @param tiles
     * @param smoothedValues
     * @return
     */
    public HashMap<Double, Double> newTankYPositions(Tile[][] tiles, double[] centerValues){
        //HashMap sorted by Vertical (column FIXED), Horizontal
        HashMap<Double, Double> tankCoordinates = new HashMap<Double, Double>();
        Set<Double> tankX = new HashSet<>();

        //Save the tank's vertical position. We will use this to find the correct index for the centerValues
        for (int i = 0; i < tiles.length; i ++){
            for (int j = 0; j < tiles[i].length; j ++){
                if (tiles[i][j].getType() == "player"){
                    tankX.add((double) j * App.CELLSIZE);
                }
            }
        }
        //Using the X positions as indexes, find the corresponding centerValues to figure out the Y
        for (int i = 0; i < centerValues.length; i++){
            double newX = (double)i * App.CELLSIZE;
            if (tankX.contains(newX)){
                double xPosition = newX;
                double yPosition = centerValues[i];
                System.out.println(yPosition);
            }
        }
        
        return tankCoordinates;
    }

    public void moveToNextPlayer() {
        Object[] keys = players.keySet().toArray();

        int currentIndex = -1;
        for (int i = 0; i < keys.length; i++) {
            if ((char) keys[i] == currentPlayerKey) {
                currentIndex = i;
                break;
            }
        }
        int nextIndex = (currentIndex + 1) % keys.length;
        currentPlayerKey = (char) keys[nextIndex];
        current_player = players.get(currentPlayerKey);
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        //Tank movement
        if (keyCode == LEFT) {
            left = true;
            System.out.println("You are pressing left");
        }
        if (keyCode == RIGHT) {
            right = true;
            System.out.println("You are pressing right");
        }
        //Turret Movement
        if (keyCode == UP) {
            up = true;
            System.out.println("You are pressing up");
        }
        if (keyCode == DOWN) {
            down = true;
            System.out.println("You are pressing down");
        }
        if (keyCode == ' ') {
            moveToNextPlayer();
        }
        
    }
    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){

        //Tank Movement
        if (keyCode == LEFT ) {
            left = false;
            System.out.println("You are releasing left");
        }
        if (keyCode == RIGHT) {
            right = false;
            System.out.println("You are releasing right");
        }

        //Turret Movement
        if (keyCode == UP) {
            up = false;
            System.out.println("You are releasing up");
        }
        if (keyCode == DOWN) {
            down = false;
            System.out.println("You are releasing down");
        }


    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    
    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        /* 
        draw.level(this);
        current_player.display(this);
        current_player.update(this, deltaTimeInSeconds);


        //----------------------------------
        //display HUD:
        //----------------------------------
        //TODO

        //----------------------------------
        //display scoreboard: 
        draw.scoreboard(this,players);
        //----------------------------------
        //TODO
        
		//----------------------------------
        //----------------------------------
        
        */
    }
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    
    }

}