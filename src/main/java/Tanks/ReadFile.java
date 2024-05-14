package Tanks;

import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ReadFile{

    /**
     * Loads a character array by parsing the txt file and then reading every value by line.
     * @param filename
     * @return char[][]
     */
    public static char[][] loadArray(String filename) {

        // Hard coded to allow for extra tile at the last position WOMP :(
        char[][] terrain = new char[App.BOARD_HEIGHT][App.BOARD_WIDTH + 1];
    
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
    
            // Read the file line by line
            int row = 0;
            while (row < 20 && scanner.hasNextLine()) {

                String line = scanner.nextLine();
                char[] terrainRow = new char[App.BOARD_WIDTH + 1];
                    
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    terrain[row] = terrainRow;
                    row ++;
                    continue;
                }

                // Convert line to char array of variable length
                char[] variableTerrainRow = line.toCharArray();
                
                //For each 27 characters in the terrainRow, append the variableTerrainRow characters inside
                for (int i = 0; i < terrainRow.length; i ++){
                    if (i >= variableTerrainRow.length) break;
                    else{
                        terrainRow[i] = variableTerrainRow[i];
                    }
                }
                terrain[row] = terrainRow;
                row ++;
            }

            scanner.close();

            fillBottomX(terrain);
            //printTerrain(terrain);
            return terrain;
            
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fills the bottom of the terrain where there are non-empty columns with X's to be parsed as valid terrain.
     * @param terrain
     * @return
     */
    public static char[][] fillBottomX(char[][] terrain) {

        for (int row = 0; row < terrain.length; row++) {
            for (int col = 0; col < terrain[row].length; col++) {

                if (terrain[row][col] == 'X') {
                    // If the current element is 'X', replace 'X' for all rows below it in the same column
                    for (int curr_row = row + 1; curr_row < terrain.length; curr_row++) {
                        terrain[curr_row][col] = 'X';
                    }
                }
            }
        }

        return terrain;
    }
    
    /**
     * Print function for debugging purposes
     * @param terrain
     */
    public static void printTerrain(char[][] terrain) {
        for (int i = 0; i < terrain.length; i++) {
            System.out.println(terrain[i]);
        }
    }

    /**
     * Converts my character array into a collection of tile objects.
     * Each tile has a colour and is separated by strings of "floor", "tree", "space", and "player"
     * @param array
     * @param colours
     * @return Tile[][]
     */
    public static Tile[][] arrayToTiles(char[][] array, int[] colours){

        Tile[][] tiles = new Tile[array.length][array[0].length];
        int count = 0;

        for (int row = 0; row < array.length; row ++){

            // Make empty lines into an empty character array
            String arrayString = new String(array[row]);
            if (arrayString.trim().isEmpty()){
                char[] emptyArray = new char[array[row].length]; 
                Arrays.fill(emptyArray, ' '); 
                array[row] = emptyArray;
            }

            // Designate each element in char[][] to a tile
            for (int col = 0; col < array[row].length; col ++){
                String type;
                if (array[row][col] == 'X') {
                    type = "floor";
                } else if (array[row][col] == 'T') {
                    type = "tree";
                } else if ((String.valueOf(array[row][col]).trim().isEmpty())) {
                    type = "empty";
                } else {
                    type = "player";
                }
    
                // Create the tile
                tiles[row][col] = new Tile(type, row, col);
                if (tiles[row][col].getType() == "floor") tiles[row][col].setColour(colours);
            }
        }
        return tiles;
    }
}

