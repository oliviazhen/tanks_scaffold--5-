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

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    public String configPath;

    public static Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.
    PImage imageType;
    JSONObject jsonData;

    //create an array to store the terrain based on the levels
    Tile[][] tiles;
    double[] movingAvg;

    public App() {
        this.configPath = "config.json";
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
		//See PApplet javadoc:
		//loadJSONObject(configPath);
		//loadImage(this.getClass().getResource("src/main/resources/basic.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));

        // Load the JSON object upon setup
        jsonData = loadJSONObject("config.json");

        loadLevel(0);
    }

    public void drawTree(PImage tree, int r, double c){
        float row = r * CELLSIZE; // Adjust for cell size
        float col = HEIGHT - (float) c * CELLSIZE; // Adjust for cell size
        image(tree, row, col, CELLSIZE, CELLSIZE);
    }

    public double[] drawSmoothLine(double[] movingAvg) {
        fill(123);

        double[] centerValue = new double[BOARD_WIDTH];
        int centerValueindex = 0;

        //Initialise a loop that iterates over each element of the movingAverage array of the terrain heights except for last
        for (int i = 0; i < movingAvg.length - 1; i++) {
            float x1 = i; // Starting x-coordinate
            float y1 = HEIGHT - (float) movingAvg[i] * CELLSIZE;// Starting y-coordinate
            float x2 = (i + 1); // Ending x-coordinate
            float y2 = HEIGHT - (float) movingAvg[i + 1] * CELLSIZE ; // Ending y-coordinate
    
            if ((i % 16 == 0) && !(i % 32 == 0) && (i != 0)){
                centerValue[centerValueindex] = (double) movingAvg[i];
                centerValueindex ++;
            }

            line(x1, y1, x2, y2);

        }
        // We should be saving every 16th (15th with 0 indexing) value to place the trees and tanks
        return centerValue;
    }

    public void loadLevel(int levelIndex){
        JSONArray levels = jsonData.getJSONArray("levels");
        JSONObject level = levels.getJSONObject(levelIndex);

        //load the background
        String backgroundName = level.getString("background");
        String backgroundPath = "src/main/resources/Tanks/" + backgroundName;
        
        imageType = loadImage(backgroundPath);
        image(imageType, 0, 0);

        //load the tile map
        String tileType = level.getString("layout");
        char[][] mapFloor = ReadFile.loadArray(tileType);
        
        //load the foreground colour
        String foregroundColour = level.getString("foreground-colour");
        this.tiles = ReadFile.arrayToTiles(mapFloor, foregroundColour, null);

        //Print out the heights
        int[] heights = Terrain.heightTerrainElement(tiles);

        //We don't need to smooth out the last 28th block - it remains blocky 
        System.out.println("The size of the heights are " + heights.length);

        //MicroArray
        double[] micro = Terrain.getMicroscopicArray(heights);
        double[] movingAvg = Terrain.movingAverage(micro, CELLSIZE);
        double[] movingAvgAgain = Terrain.movingAverage(movingAvg, CELLSIZE);

        drawSmoothLine(movingAvgAgain);

    }
     
        //Draw the tiles
        /* 
        for (int row = 0; row < this.tiles.length; row++) {
            for (int col = 0; col < this.tiles[0].length; col++) {
                tiles[row][col].draw(this); // Pass the size of each tile
                }
        }
        */

        

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        
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

    

        //----------------------------------
        //display HUD:
        //----------------------------------
        //TODO

        //----------------------------------
        //display scoreboard:
        //----------------------------------
        //TODO
        
		//----------------------------------
        //----------------------------------

        //TODO: Check user action
    }


    public static void main(String[] args) {
        PApplet.main("Tanks.App"); 
    }

}