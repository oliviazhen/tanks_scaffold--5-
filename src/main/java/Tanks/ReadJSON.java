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

        app.movingAvg = smoothedTerrainLine(app, app.foregroundColourRBG, charTiles);
        double[] centerValues = getCenterValues(app.movingAvg);
        app.movingAvgWithCELLSIZE = getMovingAverage(app.movingAvg);
        System.out.print(app.movingAvgWithCELLSIZE[0]);

        //Set the trees if the txt file has 'em
        if (level.getString("trees") != null) setTrees(app, level, centerValues);

        initializePlayers(app, charTiles);

        setPlayerColors(app);

        updateTankPositionsAfterSmoothing(app, centerValues);

    }

    public static double[] smoothedTerrainLine(App app, int[] foregroundColourRBG, char[][] charTiles) {

        int[] heights = Terrain.heightTerrainElement(app.tiles);
        double[] micro = Terrain.getMicroscopicArray(heights);
        double[] movingAvg = Terrain.movingAverage(micro, app.CELLSIZE);
        double[] secondMovingAvg = Terrain.movingAverage(movingAvg, app.CELLSIZE);

        return secondMovingAvg;
    }

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

    public static double[] getMovingAverage(double[] movingAvg){

        double[] movingAvgWithCELLSIZE = new double[movingAvg.length];
        for (int i = 0; i < movingAvgWithCELLSIZE.length; i ++){
            movingAvgWithCELLSIZE[i] = movingAvg[i] * App.CELLSIZE;
        }

        return movingAvgWithCELLSIZE;
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
           // Get the updated positions
           app.tankPositions = app.newTankPositions(app.tiles, centerValues);

           for (Map.Entry<Double, Double> tankPos : app.tankPositions.entrySet()){
               Double x = tankPos.getKey();
               Double y = tankPos.getValue(); 
           }
   
           // Update every player's position with the new tank positions
           for (Map.Entry<Character, Tank> entry : app.players.entrySet()) {
               Character c = entry.getKey();
               Tank current_tank = entry.getValue(); 
               
               Double before_x = current_tank.getRow() / app.CELLSIZE;
               Double before_y = current_tank.getColumn() / app.CELLSIZE;
   
               Double new_y = 0.0;
               if (app.tankPositions.containsKey(before_x)){
                   new_y = app.tankPositions.get(before_x);
                   current_tank.setPosition(new_y, before_x); // Update player's position
               }
           }
    }
}