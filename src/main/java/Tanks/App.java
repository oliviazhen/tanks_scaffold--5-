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

        loadLevel(1);
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
                float yPositon = (float) (BOARD_HEIGHT - (smoothedValues[i] + 1));
                int xPosition = i;
                treeCoordinates.put(xPosition, yPositon);
            }
        }
        return treeCoordinates;

    }

    /**
     * Calculates the moving average
     * Need to update fill with the foreground colour
     * @param movingAvg
     * @return double[] which holds the X coordinates (relative to processing) for where the center of each 27 columns is
     */
    public double[] drawSmoothLine(int[] rgb, double[] movingAvg) {
        stroke(rgb[0], rgb[1], rgb[2]);

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

    /**
     * Draw a line of specified colour from the line to the bottom of the floor
     * The starting point of the line is defined by the coordinates (x, y),
     * The ending point of the line is defined by the coordinates (x, HEIGHT). 
     * The HEIGHT variable represents the height of the window.
     * @param rgb
     * @param movingAverageHeights
     */
    public void drawFloor(int[] rgb, double[] movingAverageHeights) {
        stroke(rgb[0], rgb[1], rgb[2]);
    
        for (int i = 0; i < movingAverageHeights.length; i++) {
            float x = (float) i; //i is 864 for each "row" pixel
            float y = HEIGHT - (float) movingAverageHeights[i]; // 640 for each "height" pixel
            
            line(x, y, x, HEIGHT);
        }
    }

    public void drawPixel(int x, int y, int[] rgb) {
        // Set the stroke color using the RGB values
        stroke(rgb[0], rgb[1], rgb[2]);
        
        // Calculate the y-coordinate for the top of the window
        int topY = TOPBAR; // Assuming TOPBAR is the distance from the top of the window to the top of the line
        
        // Draw a line from the specified point to the top of the window
        line(x, y, x, topY);
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

        String[] ls = foregroundColour.split(",");
        int r = Integer.parseInt(ls[0]);
        int g = Integer.parseInt(ls[1]);
        int b = Integer.parseInt(ls[2]);

        int[] int_colours = {r,g,b};

        this.tiles = ReadFile.arrayToTiles(mapFloor, int_colours, null);

        //Print out the heights
        int[] heights = Terrain.heightTerrainElement(tiles);

        //MicroArray
        double[] micro = Terrain.getMicroscopicArray(heights);
        double[] movingAvg = Terrain.movingAverage(micro, CELLSIZE);
        double[] movingAvgAgain = Terrain.movingAverage(movingAvg, CELLSIZE);

        double[] centerValues = drawSmoothLine(int_colours, movingAvgAgain);

        //Set the trees if the txt file has 'em
        if (level.getString("trees") != null){
            //Traverse the tiles array again and create a tree object
            String path = "src/main/resources/Tanks/" + level.getString("trees");

            HashMap<Integer, Float> treePositions = newTreePositions(path, tiles, centerValues);

            for(Map.Entry<Integer,Float> entry: treePositions.entrySet()){
                Integer column = entry.getKey();
                Float row = entry.getValue();
                Tree tree = new Tree(row,column);
                tree.setTreeImage(path);
                tree.draw(this);
            }
        }

        //make movingAvg again extend by the CELLSIZE to draw the floor
        for (int i = 0; i < movingAvgAgain.length; i ++){
            movingAvgAgain[i] = movingAvgAgain[i] * CELLSIZE;
        }
        drawFloor(int_colours, movingAvgAgain);

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