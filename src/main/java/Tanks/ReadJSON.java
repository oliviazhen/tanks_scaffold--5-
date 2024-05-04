package Tanks;

import java.util.HashMap;
import java.util.Map;

import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.core.PApplet;

public class ReadJSON {

    // I want this ReadJSON to initialise all of the JSON variables that are declared in my App.java

    /**
     * Updates:
     * PImage background
     * JSON Object JSONData
     * double[] smoothedTerrainLine
     * Tile[][] tiles;
     * HashMap<Character, Tank> players
     * HashMap<Double, Double> tankPositions
     * Set<Tree> trees
     * @param app
     * @param levelIndex
     */
     public static void loadLevel(App app, int levelIndex){

        app.jsonData = app.loadJSONObject("config.json");

        JSONArray levels = app.jsonData.getJSONArray("levels");        

        JSONObject level = levels.getJSONObject(levelIndex);

        //load the background
        String backgroundName = level.getString("background");
        String backgroundPath = "src/main/resources/Tanks/" + backgroundName;
        app.background = app.loadImage(backgroundPath);

        //load the tile map
        String tileType = level.getString("layout");
        char[][] charTiles = ReadFile.loadArray(tileType);
        
        //load the foreground colour
        String foregroundColour = level.getString("foreground-colour");
        app.foregroundColourRBG = UsefulFunctions.RBGToArray(foregroundColour);

        app.tiles = ReadFile.arrayToTiles(charTiles, app.foregroundColourRBG, null);

        app.movingAvg = getMovingAverage(app, app.foregroundColourRBG, charTiles);
        
        /* 
        int column = 1;
        for (int i = 0; i < app.movingAvg.length; i++) {
            if ((i % 16 == 0) && (i % 32 != 0) && (i != 0)) {
                System.out.println("Column " + column + " has a height of " + app.movingAvg[i]);
                app.fill(0);
                app.ellipse((float)i, (float)app.movingAvg[i] * app.CELLSIZE, 10,10);
                column++;
            }
        }
        */
        
        /* 
        double[] centerValues = getCenterValues(app.movingAvg);

        //Set the trees if the txt file has 'em
        if (level.getString("trees") != null) setTrees(app, level, centerValues);

        initializePlayers(app, charTiles);

        setPlayerColors(app);

        updateTankPositionsAfterSmoothing(app, centerValues);
        */
    }

    public static double[] getMovingAverage(App app, int[] foregroundColourRBG, char[][] charTiles) {

        int[] heights = Terrain.heightTerrainElement(app.tiles);

        double[] micro = Terrain.getMicroscopicArray(heights);

        //this is where it goes wrong :()
        double[] movingAvg = Terrain.movingAverage(micro, app.CELLSIZE);
        int column = 1;
        for (int i = 0; i < micro.length; i++) {
            if ((i % 16 == 0) && (i % 32 != 0)) {
                System.out.println("Column " + column + " has a height of " + micro[i]);
                app.fill(0);
                app.ellipse(i, app.HEIGHT -((float) micro[i] * app.CELLSIZE), 10, 10);
                column++;
            }
        }
        
    
       //double[] firstGetCenterValues = getCenterValues(movingAvg);

        /* 
        double[] secondMovingAvg = Terrain.movingAverage(movingAvg, app.CELLSIZE);

       // Make it so that the secondMovingAverage (this being the Y values) will account for the CELLSIZE
        for (int i = 0; i < secondMovingAvg.length; i ++){
        secondMovingAvg[i] = secondMovingAvg[i] * app.CELLSIZE;
        }

        return secondMovingAvg;
        */
        return null;
    }

    public static double[] getCenterValues(double[] movingAverage){

        double[] centerValues = new double[App.BOARD_WIDTH];
        int centerValueindex = 0;

        for (int i = 0; i < movingAverage.length; i ++){
            if ((i % 16 == 0) && !(i % 32 == 0) && (i != 0)){
                centerValues[centerValueindex] = movingAverage[i];
                System.out.println("At column " + (centerValueindex + 1) + " the Y coordinate is " + centerValues[centerValueindex] );
                centerValueindex ++;
            }
        }
        return centerValues;
    }

    public static void setTrees(App app, JSONObject level, double[] centerValues) {     
            String path = "src/main/resources/Tanks/" + level.getString("trees");

            HashMap<Integer, Float> treePositions = app.newTreePositions(path, app.tiles, centerValues);
            for (Map.Entry<Integer, Float> entry : treePositions.entrySet()) {
                Integer column = entry.getKey();
                Float row = entry.getValue();
                Tree tree = new Tree(row, column);
                tree.randomisePosition(app);
                tree.setTreeImage(path);
                app.trees.add(tree);
            }
        }

    public static void initializePlayers(App app, char[][] charTiles) {
            for (int i = 0; i < charTiles.length; i++) {
                for (int j = 0; j < charTiles[i].length; j++) {
                    if (app.tiles[i][j].getType().equals("player")) {
                        Tank newTank = new Tank(j * app.CELLSIZE, i * app.CELLSIZE);
                        app.players.put(charTiles[i][j], newTank);
                    }
                }
            }
        }
   
    public static void setPlayerColors(App app) {
        JSONObject playerColors = app.jsonData.getJSONObject("player_colours");
        for (Map.Entry<Character, Tank> entry : app.players.entrySet()) {
            Character c = entry.getKey();
            Tank currentTank = entry.getValue();
            if (playerColors.getString(String.valueOf(c)) != null) {
                String playerColour = playerColors.getString(String.valueOf(c));
                int[] tankColours = UsefulFunctions.RBGToArray(playerColour);
                currentTank.setColour(tankColours);
            } else {
                System.out.println("The player " + c + " does not have an assigned colour in JSON file");
            }
        }
    }

    public static void updateTankPositionsAfterSmoothing(App app, double[] centerValues){
           // Vertical, Horizontal tank positions based on the centerValues
           
           for (Map.Entry<Character, Tank> entry : app.players.entrySet()) {
            System.out.println("Before smoothing, the tank " + entry.getKey() + " is at column " + entry.getValue().getX() + " and row " + entry.getValue().getY());
           }

           app.tankPositions = app.newTankYPositions(app.tiles, centerValues);

           for (Map.Entry<Double, Double> tankPos : app.tankPositions.entrySet()){
            //Get the key relative to the window AKA accounting for CELLSIZE
               Double x = tankPos.getKey() * App.CELLSIZE;
               Double y = tankPos.getValue() * App.CELLSIZE; 
           }
   
           // Update every player's position with the new tank positions
           for (Map.Entry<Character, Tank> entry : app.players.entrySet()) {
               Character c = entry.getKey();
               Tank current_tank = entry.getValue(); 
               
               Double before_x = current_tank.getX();
               Double before_y = current_tank.getY();
   
               Double new_y = 0.0;
               if (app.tankPositions.containsKey(before_x)){
                   new_y = app.tankPositions.get(before_x);
                   current_tank.setPosition(new_y, before_x); // Update player's position
               }
           }
    }
}