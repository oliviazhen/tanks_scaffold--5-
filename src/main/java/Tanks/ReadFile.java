package Tanks;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ReadFile{

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
        System.out.println("There is something wrong with your loading array");
        return null;
    }

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
    
    public static void printTerrain(char[][] terrain) {
        for (int i = 0; i < terrain.length; i++) {
            System.out.println(terrain[i]);
        }
    }

    public static Tile[][] arrayToTiles(char[][] array, int[] colours, String treePath){

        Tile[][] tiles = new Tile[array.length][array[0].length];
        //learn to traverse the char array

        for (int row = 0; row < array.length; row ++){
            for (int col = 0; col < array[row].length; col ++){
                String type;
                //designate each element in char[][] to a tile
                if (array[row][col] == 'X'){
                    type = "floor";

                }
                else if (array[row][col] == 'T'){
                    type = "tree";
                }
                else {
                    type = "empty";
                }
    
                //create the tile
                tiles[row][col] = new Tile(type, row, col);
                if (tiles[row][col].getType() == "floor") tiles[row][col].setColour(colours);
            }
        }
        return tiles;
    }
}

