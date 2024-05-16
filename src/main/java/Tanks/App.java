package Tanks;

import org.checkerframework.checker.units.qual.A;

import com.jogamp.opengl.util.packrect.Rect;

import processing.core.PApplet;
import processing.core.PImage;
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
    HashMap<Integer, Integer> tankPositions = new HashMap<>();
    Set<Tree> trees = new HashSet<>();
    Character currentPlayerKey;
    Tank currentPlayer;
    boolean waitingForProjectile = false;
    ArrayList<Projectile> projectileList;
    boolean left, right, up, down, w, s, space;
    Wind windObj;

    //Tank movement
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
        currentPlayer = players.get(currentPlayerKey);
        newTankPosition = currentPlayer.getX();
        newTurretPosition = currentPlayer.getTurret().getAngle();

        DrawObject.level(this);

        //Create a wind object
        this.windObj = new Wind();
        DrawObject.wind(this, windObj);
        
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
        currentPlayer = players.get(currentPlayerKey);
        newTankPosition = currentPlayer.getX();
        newTurretPosition = currentPlayer.getTurret().getAngle();
    }    

    /**
     * Receive key pressed signal from the keyboard.
     */
    boolean teleport;
	@Override
    public void keyPressed(KeyEvent event){

        int key = event.getKeyCode();

        //Tank movement
        if (key == 37) {
            //LEFT
            left = true;

            right = false;
            up = false;
            down = false;
            space = false;

            move = 1;
        }
        if (key == 39) {
            //RIGHT
            right = true;
            
            left = false;
            up = false;
            down = false;
            space = false;
            move = 1;
        }
        //Turret Movement
        if (key == 38) {
            up = true;

            left = false;
            right = false;
            down = false;
            space = false;
            move = 1;
        }
        if (key == 40) {
            down = true;

            left = false;
            right = false;
            up = false;
            space = false;
            move = 1;
        }
        

        //Turret Power
        if (keyCode == 87){
            w = true;
            s = false;
            currentPlayer.setPower(currentPlayer.getPower() + 36);
        }
        if (keyCode == 83){
            s = true;
            w = false;
            currentPlayer.setPower(currentPlayer.getPower() - 36);
        }

        //Projectile
        if (key == 32) {
            space = true;

            left = false;
            right = false;
            down = false;
            up = false;
            currentPlayer.addProjectile(currentPlayer.getTurret());
            projectileList = currentPlayer.getProjectiles();
        }

        // Stop all movement
        if (key == 65){
            space = false;
            left = false;
            right = false;
            down = false;
            up = false;
            w = false;
            s = false;
        }

        // Extension, Teleport
        if (key == 84){
            if (currentPlayer.getFuelAmount() >= 15) {
                teleport = true;
            }
            else{
                teleport = false;
            }
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){

        //Tank Movement
        if (key == 37) {
            move = 0;
        }
        else if (key == 39) {
            move = 0;
        }

        //Turret Movement
        if (key == 38) {
            move = 0;
        }
        if (key == 40) {
            move = 0;

        }

        if (key == 32) {
            move = 0;
            space = false;
        }

        //Turret Power
        if (keyCode == 87){
            w = false;
        }
        if (keyCode == 83){
            s = false;
        }
        if (keyCode == 84){
            teleport = false;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Find the x of the mouse 
        int x = e.getX();
        if ((movingAvgWithCELLSIZE[x] > 0 && movingAvgWithCELLSIZE[x] < 640) && (teleport)){
            if (teleport) {
                currentPlayer.setPosition((int)(HEIGHT - movingAvgWithCELLSIZE[x]), x);
                currentPlayer.setFuelAmount(currentPlayer.getFuelAmount() - 15);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    /**
     * This iterates through every player's position relative to the bullet fired to check for damage to the tank.
     * Changes tank health using the setter
     * @param Projectile
     */
    public void checkDamage(Projectile bullet) {
        int MAX_DAMAGE = 60;
        
        Iterator<Map.Entry<Character, Tank>> iterator = players.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Character, Tank> entry = iterator.next();
            Character tankIdentifier = entry.getKey();
            Tank tank = entry.getValue();
            
            double distance = Math.sqrt(Math.pow(tank.getX() - bullet.getX(), 2) + Math.pow(tank.getY() - bullet.getY(), 2));
            
            if (distance <= bullet.radius) {
                float damagePercentage = (float) (bullet.radius - distance) / bullet.radius;
                int damageAmount = Math.round(damagePercentage * MAX_DAMAGE);
                
                if (!currentPlayer.getColours().equals(tank.getColours())) {
                    tank.setHealth(damageAmount);
                    
                    if (tank.getHealth() <= 0) {
                        tank.explode(this);
                        iterator.remove(); 
                    }
                    
                    currentPlayer.setScore(damageAmount);
                    
                }
            }
        }
    
    }
    
    /** 
     * Draw all elements in the game by current frame.
     * There are two main checks
     * 1. A move conditional is set to allow the the tank (including turret) to move
     * 2. A bullet fired conditional to allow the bullet to be fired everytime we press space
     * 
     */
    public int newTankPosition;
    public float newTurretPosition;

    @Override
    public void draw() {
        
        // Draw the background and other static elements
        DrawObject.level(this);
        DrawObject.wind(this, windObj);
        DrawObject.scoreboard(this, players);

        for (Map.Entry<Character, Tank> player : players.entrySet()) {
            player.getValue().display(this);
        }


        // Move is triggered
        if (move > 0) {
            //Update the tank's position
            if (left) newTankPosition -= 2;
            else if (right) newTankPosition += 2;
            //Update the turret's position. 0.1 radians per frame or 5.72 degrees
            else if (up) newTurretPosition -= 5.72;
            else if (down) newTurretPosition += 5.72;

            currentPlayer.display(this);
            currentPlayer.move(this, newTankPosition);

            currentPlayer.moveTurret(newTurretPosition);

            move += 1;

            if (move == 61) {
                move = 0;
                left = false;
                right = false;
                up = false;
                down = false;
                space = false;
            }
        }

        //Concerning the projectile
        projectileList = currentPlayer.getProjectiles();    
        if (!projectileList.isEmpty()) {
            waitingForProjectile = true;
            bullet = projectileList.get(0);
    
            if (bullet.checkRemove(this)) 
            {
                if (bullet.willExplode){
                    //This controls the player switch for an explosion 
                    checkDamage(bullet);
                    moveToNextPlayer();
                }

                projectileList.clear();
                waitingForProjectile = false;

                if (!space){
                    //This controls the player switch for projectile moving out of bounds 
                    moveToNextPlayer();
                    windObj.changeWindForce();
                }
            }
            bullet.setWind(windObj);
            bullet.display(this);
            bullet.move();
        }
        DrawObject.scoreboard(this, players);
         
    }
    
    
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}