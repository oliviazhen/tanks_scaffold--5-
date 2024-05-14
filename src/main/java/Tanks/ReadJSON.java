package Tanks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import processing.data.JSONArray;
import processing.data.JSONObject;

public class ReadJSON {

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

        // Load the background
        String backgroundName = level.getString("background");
        String backgroundPath = "src/main/resources/Tanks/" + backgroundName;
        app.background = app.loadImage(backgroundPath);

        // Load the tile map
        String tileType = level.getString("layout");
        char[][] charTiles = ReadFile.loadArray(tileType);
        
        // Load the foreground colour
        String foregroundColour = level.getString("foreground-colour");
        app.foregroundColourRBG = UsefulFunctions.RBGToArray(foregroundColour);

        app.tiles = ReadFile.arrayToTiles(charTiles, app.foregroundColourRBG);

        app.movingAvg = smoothedTerrainLine(app, app.foregroundColourRBG, charTiles);
        double[] centerValues = getCenterValues(app.movingAvg);
        app.movingAvgWithCELLSIZE = getMovingAverage(app.movingAvg);

        // Load the trees
        if (level.getString("trees") != null) setTrees(app, level, centerValues);

        initializePlayers(app, charTiles);

        setPlayerColors(app);

        updateTankPositionsAfterSmoothing(app, centerValues);

    }

    /**
     * Provides an array of the values generated after the second round of smoothing.
     * @param app
     * @param foregroundColourRBG
     * @param charTiles
     * @return
     */
    public static double[] smoothedTerrainLine(App app, int[] foregroundColourRBG, char[][] charTiles) {

        int[] heights = Terrain.heightTerrainElement(app.tiles);
        double[] micro = Terrain.getMicroscopicArray(heights);
        double[] movingAvg = Terrain.movingAverage(micro, app.CELLSIZE);
        double[] secondMovingAvg = Terrain.movingAverage(movingAvg, app.CELLSIZE);

        return secondMovingAvg;
    }

    /**
     * Provides the Y value for each X at the middle of each column.
     * @param movingAverage
     * @return array of the center values
     */
    public static double[] getCenterValues(double[] movingAverage){

        double[] centerValues = new double[App.BOARD_WIDTH];
        int centerValueindex = 0;

        for (int i = 0; i < movingAverage.length; i ++){
            if ((i % 16 == 0) && !(i % 32 == 0) && (i != 0)){
                centerValues[centerValueindex] = movingAverage[i];
                centerValueindex ++;
            }
        }
        return centerValues;
    }

    /**
     * Getter for the moving average. Used for debugging purposes to ensure that I am working with an 864 with each value multiplied by app.CELLSIZE
     * @param movingAvg
     * @return
     */
    public static double[] getMovingAverage(double[] movingAvg){

        double[] movingAvgWithCELLSIZE = new double[movingAvg.length];
        for (int i = 0; i < movingAvgWithCELLSIZE.length; i ++){
            movingAvgWithCELLSIZE[i] = movingAvg[i] * App.CELLSIZE;
        }

        return movingAvgWithCELLSIZE;
    }  
    
    /**
     * Setter for the trees, useful for setting positions after smoothing
     * @param app
     * @param level
     * @param centerValues
     */
    public static void setTrees(App app, JSONObject level, double[] centerValues) {     
            String path = "src/main/resources/Tanks/" + level.getString("trees");

            // Find the X positions of the trees based on the Tile[][] array
            HashMap<Integer, Float> treeCoordinates = new HashMap<Integer, Float>();
            Set<Integer> treeVertical = new HashSet<>();

            for (int i = 0; i < app.tiles.length; i ++){
                for (int j = 0; j < app.tiles[i].length; j ++){
                    if (app.tiles[i][j].getType() == "tree"){
                        //System.out.printf("There is a tree at row %d, column %d. %n", i, j);
                        treeVertical.add(j);
                    }
                }
            }

            // Find the Y positions by only accounting for X's that are included
            for (int i = 0; i < centerValues.length; i ++ ){
                if (treeVertical.contains(i)){
                    float yPositon = (float) (App.BOARD_HEIGHT - (centerValues[i] + 1));
                    int xPosition = i ;
                    treeCoordinates.put(xPosition, yPositon);
                }
            }

            /**
             * For each coordinate:
             * 1. Create the tree object that should be attached
             * 2. Set the appropriate image
             * 3. Add the trees in a collection for tracking
             */
            for (Map.Entry<Integer, Float> entry : treeCoordinates.entrySet()) {
                Integer column = entry.getKey();
                Float row = entry.getValue();
                Tree tree = new Tree(row, column);
                tree.randomisePosition(app);
                tree.setTreeImage(path);
                app.trees.add(tree);
            }
    }

    /**
     * Initialise players and set them into a HashMap of <Character, Tank> entries.
     * @param app
     * @param charTiles
     */
    public static void initializePlayers(App app, char[][] charTiles) {

        //Set Characters
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
        
           // Get the updated positions
            HashMap<Integer, Integer> tankCoordinates = new HashMap<Integer, Integer>();
            Set<Integer> tankVertical = new HashSet<>();

            for (int i = 0; i < app.tiles.length; i ++){
                for (int j = 0; j < app.tiles[i].length; j ++){
                    if (app.tiles[i][j].getType() == "player"){
                        tankVertical.add(j);
                    }
                }
            }

            for (int i = 0; i < centerValues.length; i ++ ){
                if (tankVertical.contains(i)){
                    int yPositon = (int) (App.HEIGHT - (centerValues[i] * App.CELLSIZE));
                    int xPosition = i * App.CELLSIZE;
                    tankCoordinates.put(xPosition, yPositon);
                }
            }

           // Update every player's position with the new tank positions
           for (Map.Entry<Character, Tank> entry : app.players.entrySet()) {
               Character c = entry.getKey();
               Tank current_tank = entry.getValue(); 
               

               int before_x = current_tank.getRow();
               int before_y = current_tank.getColumn();
               
   
               int new_y = 0;
               if (tankCoordinates.containsKey(before_x)){
                   new_y = tankCoordinates.get(before_x);
                   current_tank.setPosition(new_y, before_x); // Update player's position
               }

               //System.out.println("The new tank postion is at X " + current_tank.getColumn() + " Y " + current_tank.getRow());
           }
    }
}