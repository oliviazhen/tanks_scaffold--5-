package Tanks;

import java.util.*;

import processing.core.PImage;

public class draw {

    public static void trees(App app, Set<Tree> trees){
        for (Tree tree : trees) {
            tree.display(app);
        }
    }
      /**
     * Calculates the moving average
     * Need to update fill with the foreground colour
     * @param movingAvg
     * @return double[] which holds the X coordinates (relative to processing) for where the center of each 27 columns is
     */
    public static void smoothLine(App app, int[] rgb, double[] movingAvgWithCELLSIZE) {
        app.stroke(rgb[0], rgb[1], rgb[2]);

        //Initialise a loop that iterates over each element of the movingAverage array of the terrain heights except for last
        for (int i = 0; i < movingAvgWithCELLSIZE.length - 1; i++) {
            float x1 = i; // Starting x-coordinate
            float y1 = app.HEIGHT - (float) movingAvgWithCELLSIZE[i];// Starting y-coordinate
            float x2 = (i + 1); // Ending x-coordinate
            float y2 = app.HEIGHT - (float) movingAvgWithCELLSIZE[i + 1]; // Ending y-coordinate
    
            app.line(x1, y1, x2, y2);
        }
    }

    /**
     * Draw a line of specified colour from the line to the bottom of the floor
     * The starting point of the line is defined by the coordinates (x, y),
     * The ending point of the line is defined by the coordinates (x, HEIGHT). 
     * The HEIGHT variable represents the height of the window.
     * @param rgb
     * @param movingAverage
     */
    public static void floor(App app, int[] rgb, double[] movingAvgWithCELLSIZE) {
        app.stroke(rgb[0], rgb[1], rgb[2]);
    
        for (int i = 0; i < movingAvgWithCELLSIZE.length; i++) {
            float x = (float) i; //i is 864 for each "row" pixel
            float y = app.HEIGHT - (float) movingAvgWithCELLSIZE[i]; // 640 for each "height" pixel
            
            app.line(x, y, x, app.HEIGHT);
        }
    }

    public static void pixel(App app, int x, int y, int[] rgb) {
        // Set the stroke color using the RGB values
        app.stroke(rgb[0], rgb[1], rgb[2]);
        
        // Calculate the y-coordinate for the top of the window
        int topY = app.TOPBAR; // Assuming TOPBAR is the distance from the top of the window to the top of the line
        
        // Draw a line from the specified point to the top of the window
        app.line(x, y, x, topY);
    }

    public static void playerTurn(App app, Character currentPlayerKey){
        app.fill(0);
        app.textSize(15);
        app.text("Player ", 10, 30);

        app.fill(0);
        app.textSize(15);
        app.text(currentPlayerKey, 65, 30);

        app.textSize(15);
        app.text("'s turn ", 80, 30);

    }

    public static void arrow(App app, Tank currentPlayer){
        float x = (float) currentPlayer.getColumn() * App.CELLSIZE + 15;
        float y = (float) currentPlayer.getRow() * App.CELLSIZE - 60;
        
        app.fill(0);
        app.rect(x - 5, y - 100, 10, 100);
        app.triangle(x - 20, y - 30, x, y + 20, x + 20, y - 30);
    }

    public static void playerFuel(App app, Tank tank){
        app.fill(0);
        app.textSize(15);
        app.text(tank.getFuelAmount(), 180, 30);
    }

    public static void healthBar(App app, Tank t){
        app.textSize(15);
        float fullPower = 200;

        float currentPower = (2/50)*fullPower;
    
        //Rectangle
        //For the smaller rectangle, the power determines its length which means we need a ratio.

        app.text("Health: ", 280, 30);
        app.fill(t.getColours()[0],t.getColours()[1], t.getColours()[2]);
        app.rect(350, 10, fullPower,25);

        app.fill(2);
        app.rect(350, 10, currentPower ,25);


        //Black text
        app.fill(0);
        app.text(t.getHealth(), 550, 30);

        app.text("Power: ", 280, 60);
        app.text(t.getPower(), 350, 60);

    }

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

    public static void background(App app, PImage background){
        app.image(background, 0, 0);
    }

    
    public static void level(App app){


        background(app, app.background);
        smoothLine(app, app.foregroundColourRBG, app.movingAvgWithCELLSIZE);
        floor(app, app.foregroundColourRBG, app.movingAvgWithCELLSIZE);
        trees(app, app.trees);

        PImage wind = app.loadImage("src/main/resources/Tanks/wind.png");
        PImage fuel = app.loadImage("src/main/resources/Tanks/fuel.png");
        wind.resize(50, 50);
        fuel.resize(25, 25);
        app.image(wind, 730, 5);
        app.image(fuel, 150, 10);
        playerTurn(app, app.currentPlayerKey);
        playerFuel(app, app.current_player);
        healthBar(app,app.current_player);
        //arrow(app, app.current_player);
    }
    


    
}