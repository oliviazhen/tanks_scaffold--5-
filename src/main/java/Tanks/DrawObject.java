package Tanks;

import java.util.*;

import processing.core.PImage;

public class DrawObject {

    /**
     * Displays all the trees that exist in app. Also randomises the positions
     * @param app
     * @param trees
     */
    public static void trees(App app, Set<Tree> trees){
        for (Tree tree : trees) {
            tree.display(app);
        }
    }

    /**
     * Calculates the moving average
     * Need to update fill with the foreground colour
     * @param movingAvgWithCELLSIZE
    */
    public static void smoothLine(App app, int[] rgb, double[] movingAvgWithCELLSIZE) {
        app.stroke(rgb[0], rgb[1], rgb[2]);

        HashMap<Float,Float> coordinates = new HashMap<Float, Float>();

        //Initialise a loop that iterates over each element of the movingAverage array of the terrain heights except for last
        for (int i = 0; i < movingAvgWithCELLSIZE.length - 1; i++) {
            float x1 = i; // Starting x-coordinate
            float y1 = app.HEIGHT - (float) movingAvgWithCELLSIZE[i];// Starting y-coordinate
            float x2 = (i + 1); // Ending x-coordinate
            float y2 = app.HEIGHT - (float) movingAvgWithCELLSIZE[i + 1]; // Ending y-coordinate

            coordinates.put(x1, y1);
            coordinates.put(x2, y2);
    
            app.line(x1, y1, x2, y2);
        }

        app.coordinates = coordinates;
    }

    /**
     * Draw a line of specified colour from the line to the bottom of the floor
     * The starting point of the line is defined by the coordinates (x, y),
     * The ending point of the line is defined by the coordinates (x, HEIGHT). 
     * The HEIGHT variable represents the height of the window.
     * @param rgb
     * @param movingAverageWithCELLSIZE
     * @param app
     */
    public static void floor(App app, int[] rgb, double[] movingAvgWithCELLSIZE) {
        app.stroke(rgb[0], rgb[1], rgb[2]);
    
        for (int i = 0; i < movingAvgWithCELLSIZE.length; i++) {
            float x = (float) i; //i is 864 for each "row" pixel
            float y = app.HEIGHT - (float) movingAvgWithCELLSIZE[i]; // 640 for each "height" pixel
            
            app.line(x, y, x, app.HEIGHT);
        }
    }

    /**
     * GUI: for current player's turn
     * @param app
     * @param currentPlayerKey
     */
    public static void playerTurn(App app, Character currentPlayerKey){
        app.fill(0);
        app.textSize(15);
        app.text("Player ", 10, 30);

        app.fill(0);
        app.textSize(15);
        app.text(currentPlayerKey, 60, 30);

        app.textSize(15);
        app.text("'s turn ", 70, 30);

    }

    /**
     * GUI: Displays the arrow above the current player for 2 seconds
     * @param app
     * @param currentPlayer
     */
    public static void arrow(App app, Tank currentPlayer){

        float x = (float) currentPlayer.getY() * App.CELLSIZE + 15;
        float y = (float) currentPlayer.getX() * App.CELLSIZE - 60;
        
        app.fill(0);
        app.rect(x - 5, y - 100, 10, 100);
        app.triangle(x - 20, y - 30, x, y + 20, x + 20, y - 30);
    
    }

    /**
     * GUI: Display's the player's fuel levels
     * @param app
     * @param tank
     */
    public static void playerFuel(App app, Tank tank){
        PImage fuel = app.loadImage("src/main/resources/Tanks/fuel.png");
        fuel.resize(25, 25);
        app.image(fuel, 150, 10);

        app.fill(0);
        app.textSize(15);
        app.text(tank.getFuelAmount(), 180, 30);
    }

    /**
     * GUI: Displays the number of parachutes remaining for the player
     * @param app
     * @param tank
     */
    public static void playerParachute(App app, Tank tank){
        PImage parachute = app.loadImage("src/main/resources/Tanks/parachute.png"); 
        parachute.resize(25, 25);
        app.image(parachute, 150, 40);

        app.fill(0);
        app.textSize(15);
        app.text(tank.getParachutes(), 180, 60);
    }


    /**
     * GUI: Health bar which updates based on the changes in health
     * @param app
     * @param t
     */
    public static void healthBar(App app, Tank t){
        app.noStroke();
        int MAGNITUDE = 2;

        app.textSize(15);

        float currentPower = Math.round(t.getPower());

        //Create the rectangle
        app.text("Health: ", 280, 30);
        app.fill(t.getColours()[0],t.getColours()[1], t.getColours()[2]);
        app.rect(350, 10, 100 * MAGNITUDE,25);

        app.fill(0);
        app.rect(350, 10, currentPower * MAGNITUDE ,25);

        //Black text
        app.fill(0);
        app.text(t.getHealth(), 550, 30);

        app.text("Power: ", 280, 60);
        app.text((int)currentPower, 350, 60);

    }

    /**
     * GUI: Scoreboard updates the current scores of all players
     * @param app
     * @param players
     */
    public static void scoreboard(App app, HashMap<Character, Tank> players) {

        int numberOfPlayers = players.size();

        //First Box
        app.fill(0);
        app.line(720, 50, 850, 50);
        app.textSize(15);
        app.text("Scores", 730,65);
        app.line(720, 70 , 850, 70);
        
        //Adjust length of vertical lines to the number of players
        app.line(720, 50, 720, 70 + numberOfPlayers * 20);
        app.line(850, 50, 850, 70 + numberOfPlayers * 20);

        //For each player, use the colour to fill in text.
        int i = 85;
        for (Map.Entry<Character,Tank> entry: players.entrySet()){
            Character c = entry.getKey();
            Tank t = entry.getValue();

            //Player name
            app.fill(t.getColours()[0],t.getColours()[1], t.getColours()[2]);
            app.textSize(15);
            app.text("Player " + Character.toString(c), 730, i );

            //Player Score
            app.fill(0);
            app.textSize(15);
            app.text(Integer.toString(t.getScore()), 820, i );


            i += 20;
        }

        app.fill(0);
        app.line(720, 70 + numberOfPlayers * 20,850, 70 + numberOfPlayers * 20);
    }

    /**
     * Displays the background, I placed this in a function to allow me to smoothly control where the background is patched.
     * This is important to move my player
     * @param app
     * @param background
     */
    public static void background(App app, PImage background){
        app.image(background, 0, 0);
    }

    /**
     * Display the wind as a wind object
     * @param app
     * @param wind
     */
    public static void wind(App app, Wind wind){

        if (wind.getWindForce() < 0){
            app.wind = app.loadImage("src/main/resources/Tanks/wind-1.png");      
        }
        else if (wind.getWindForce() > 0){
            app.wind = app.loadImage("src/main/resources/Tanks/wind.png");
        }
    
        if (app.wind != null){
            app.wind.resize(50, 50);
            app.image(app.wind, 730, 5);
            app.textSize(15);
            app.fill(0);
            app.text(Integer.toString(wind.getWindForce()), 790,35);
        }

    }

    /**
     * Driver function of Draw class which will load everything
     * @param app
     */
    public static void level(App app){

        background(app, app.background);
        smoothLine(app, app.foregroundColourRBG, app.movingAvgWithCELLSIZE);
        floor(app, app.foregroundColourRBG, app.movingAvgWithCELLSIZE);
        trees(app, app.trees);

        playerTurn(app, app.currentPlayerKey);
        playerFuel(app, app.currentPlayer);
        playerParachute(app, app.currentPlayer);
        healthBar(app,app.currentPlayer);
        arrow(app, app.currentPlayer);
    }
    


    
}