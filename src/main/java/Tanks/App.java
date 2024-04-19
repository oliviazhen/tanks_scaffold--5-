package Tanks;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;


public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
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
    char[][] map;
    Tile[][] tiles;


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
        map = ReadFile.loadArray(tileType);

        //load the foreground colour
        String foregroundColour = level.getString("foreground-colour");
        this.tiles = ReadFile.arrayToTiles(map, foregroundColour, null);
        
        //load the trees if any
        if (level.getString("trees") != null){
            String treeType = level.getString("trees");
            String treePath = "src/main/resources/Tanks/" + treeType;
            this.tiles = ReadFile.arrayToTiles(map, foregroundColour, treePath);
        }
        
        //fix this idk where it goes
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                tiles[row][col].draw(this, CELLSIZE); // Pass the size of each tile
            }
        }

    }

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

        loadLevel(2);
    
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
        //ReadFile.loadArray("level1.txt");
        //ReadFile.readJsonFile("config.json");

    }

}