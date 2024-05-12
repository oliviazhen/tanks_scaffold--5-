package Tanks;

import org.checkerframework.checker.units.qual.A;

import com.jogamp.opengl.util.packrect.Rect;

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


public class App extends PApplet{

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;

    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    int x = 0;
    int y = 200;

    public static final int INITIAL_PARACHUTES = 3;

    public static final int FPS = 30;


    //All JSON variables
    JSONObject jsonData;
    PImage background;
    public int[] foregroundColourRBG;
    double[] movingAvg;
    double[] movingAvgWithCELLSIZE;
    public String configPath;

    //All draw vairables
    HashMap<Float,Float> coordinates;
    PImage wind;

    //create an array to store the terrain based on the levels
    Tile[][] tiles;
    HashMap<Character, Tank> players = new HashMap<>();
    HashMap<Double, Double> tankPositions = new HashMap<>();
    Set<Tree> trees = new HashSet<>();
    Character currentPlayerKey;
    Tank current_player;
    boolean waitingForProjectile = false;
    ArrayList<Projectile> p_ls;
    boolean left, right, up, down, w, s, space;
    Wind windObj;

    float prevTime;
    float elapsedTime;

    //Tank movement
    int dx = 0;
    int move;

    //Explosions
    Projectile bullet;


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
   
        jsonData = loadJSONObject(configPath);
        ReadJSON.loadLevel(this, 2);

        currentPlayerKey = players.keySet().iterator().next();
        current_player = players.get(currentPlayerKey);

        draw.level(this);

        //Create a wind object
        this.windObj = new Wind();
        draw.wind(this, windObj);
    }

    public void moveToNextPlayer() {
        Object[] keys = players.keySet().toArray();
    
        // Find the index of the current player in the array of keys
        int currentIndex = -1;
        for (int i = 0; i < keys.length; i++) {
            if (currentPlayerKey == (char) keys[i]) {
                currentIndex = i;
                break;
            }
        }
        // Handle the case where the current player is not found
        if (currentIndex == -1) {
            System.out.println("Current player not found.");
            return;
        }
    
        // Calculate the index of the next player
        int nextIndex = (currentIndex + 1) % keys.length;
    
        // Update the current player to the next player
        currentPlayerKey = (char) keys[nextIndex];
        current_player = players.get(currentPlayerKey);
    }    

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){

        int key = event.getKeyCode();

        //Tank movement
        if (key == 37) {
            left = true;
            move = 1;
            dx += 1;
        }
        else if (key == 39) {
            right = true;
            move = 1;
            dx += 1;
        }
        //Turret Movement
        else if (key == 38) {
            up = true;
            move += 1;
            dx += 1;
        }
        else if (key == 40) {
            down = true;
            move += 1;
            dx += 1;
        }
        

        //Turret Power
        if (keyCode == 'W'){
            w = true;
            move += 1;
            //dx += 1;
        }
        if (keyCode == 'S'){
            s = true;
            move += 1;
            //dx += 1;
        }

        //Projectile
        if (keyCode == ' ') {
            space = true;
            current_player.addProjectile(current_player.getTurret());
            p_ls = current_player.getProjectiles();
        }
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){

        //Tank Movement
        if (keyCode == LEFT) {
            left = false;
            move = 0;
        }
        if (keyCode == RIGHT) {
            right = false;
            move = 0;
        }

        //Turret Movement
        if (keyCode == UP) {
            up = false;
            move = 0;
        }
        if (keyCode == DOWN) {
            down = false;
            move = 0;

        }

        if (keyCode == ' ') {
            space = false;
            if (waitingForProjectile) {
            } else {
                // If not waiting for projectile, switch players
                moveToNextPlayer();
                windObj.changeWindForce();
            }
        }

        //Turret Power
        if (keyCode == 'W'){
            w = false;
            move = 0;
        }
        if (keyCode == 'S'){
            s = false;
            move = 0;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    public float timer(float prevTime){
        float deltaTime =  (float) ((millis() - prevTime) / 1000.0); // Convert milliseconds to seconds
        prevTime = millis(); // Update prevTime for the next frame

        return deltaTime;
    }

    public int checkDamage(Projectile bullet){

        int damageAmount = 0;
        // Loop through all the tanks and check if any are affected by the explosion
        for (Map.Entry<Character, Tank> entry : players.entrySet()){
            Character tankIdentifier = entry.getKey();
            Tank tank = entry.getValue();
    
            double distance = Math.sqrt(Math.pow(tank.getColumn() - bullet.getX(), 2) + Math.pow(tank.getRow() - bullet.getY(), 2));

            // Check if the tank is within the explosion radius
            if (distance <= bullet.radius){
                double damagePercentage = 1 - (distance / bullet.radius);
                damageAmount = (int) (tank.getHealth() * (1.0 - (distance / bullet.radius)));

                tank.setHealth(damageAmount);

                System.out.println(tankIdentifier + " takes " + damagePercentage + " percent damage from the explosion. Remaining health: " + tank.getHealth());
    
            }
        }

        return damageAmount;
    }
    
    
    /** 
     * Draw all elements in the game by current frame.
     * At 30 FPS, Every second we flip through 30 frames
     */
    @Override
    public void draw() {
        /* */
        draw.level(this);
        draw.wind(this, windObj);
    
        // Concerning all the players
        for (Map.Entry<Character, Tank> player : players.entrySet()) {
            player.getValue().display(this);
        }

        //Concerning the current player; Move 60 px/second
        if(move > 0){
 
            current_player.move(this, dx);
            current_player.changePower(this, dx);
            current_player.moveTurret(this, dx);

            dx += 1;
            move += 1;

            if (move == 60){
                move = 0;
            } 
        }

        fill(0);

        //Concerning the projectile
        p_ls = current_player.getProjectiles();    
        if (!p_ls.isEmpty()) {
            waitingForProjectile = true;
            bullet = p_ls.get(0);
    
            if (bullet.checkRemove(this)) 
            {
                if (bullet.willExplode){
                    //System.out.println("The draw processes the explosion");
                    checkDamage(bullet);
                }

                p_ls.clear();
                waitingForProjectile = false;

                if (!space){
                    moveToNextPlayer();
                    windObj.changeWindForce();
                }
            }

            bullet.setWind(windObj);
            bullet.display(this);
            bullet.move();

        }
        draw.scoreboard(this, players);
    }
    
    
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}