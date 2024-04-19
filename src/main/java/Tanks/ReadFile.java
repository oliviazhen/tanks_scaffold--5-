package Tanks;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

// Parse JSON
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


public class ReadFile {

    public static char[][] loadArray(String filename){

        ArrayList<char[]> terrain = new ArrayList<>();
        char[][] terrainArray = new char[0][0];
        int maxCols = 0;

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            // Read the file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    terrain.add(new char[0]);
                    continue;
                }

                // Initial dirty array that is too short
                char[] lineChars = line.toCharArray();

                // Replace invalid characters with '.'
                for (int i = 0; i < lineChars.length; i++) {
                    if (lineChars[i] != 'X' && lineChars[i] != 'T') {
                        // If the current character is a space and the previous character was an 'X',
                        // replace it with '.'
                        if (lineChars[i] == ' ' && i > 0 && lineChars[i - 1] == 'X') {
                            lineChars[i] = '.';
                        }
                        // Otherwise, replace it with '.'
                        else {
                            lineChars[i] = '.';
                        }
                    }
                }

                // Add the dirty array into the terrain arrayList where every element represents a row 
                // We don't know how many rows we will have thus need for arrayList
                terrain.add(lineChars);


                // Update maxCols if necessary
                if (lineChars.length > maxCols) {
                    maxCols = lineChars.length;
                }
            }

            // Print the terrain
            /*
            for (char[] line : terrain) {
                System.out.println(line);
            }
            */

            terrainArray = new char[terrain.size()][maxCols];

            // Create the 2D array terrainArray with trailing "."
            int index_all_rows = 0;

            while (index_all_rows < terrainArray.length) {
                int index_rowElements = 0;

                // Copy elements from terrain.get(index_all_rows) to terrainArray[index_all_rows]
                for (char c : terrain.get(index_all_rows)) {
                    terrainArray[index_all_rows][index_rowElements] = c;
                    index_rowElements++;
                }

                // Fill the remaining elements in the row with '.'
                while (index_rowElements < terrainArray[index_all_rows].length) {
                    terrainArray[index_all_rows][index_rowElements] = '.';
                    index_rowElements++;
                }

                index_all_rows++;
            }


            // Traverse the terrainArray and create the snowy hills 
            for (int col = 0; col < terrainArray[0].length; col++) {
                // For each column, loop over each row
                for (int row = 0; row < terrainArray.length; row++) {
                    char element = terrainArray[row][col]; 

                    if (element == 'X') {
                        // If the current element is 'X', replace 'X' for all rows below it in the same column
                        for (int curr_row = row + 1; curr_row < terrainArray.length; curr_row++) {
                            terrainArray[curr_row][col] = 'X';
                        }
                    }
                }
            }

            // Print the 2D array terrainArray (if needed)
            /*  
            for (char[] row : terrainArray) {
                for (char cell : row) {
                    System.out.print(cell);
                }
                System.out.println();
            }
            */
        
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }

    return terrainArray; 
    }

    public static Tile[][] arrayToTiles(char[][] array, String colour, String treePath){

        Tile[][] tiles = new Tile[array.length][array[0].length];

        //learn to traverse the char array
        for (int row = 0; row < array.length; row ++){
            for (int col = 0; col < array[0].length; col ++){
                String type;
                //designate each element in char[][] to a tile
                if (array[row][col] == '.'){
                    type = "empty";
                }
                else if (array[row][col] == 'X'){
                    type = "floor";

                }
                else if (array[row][col] == 'T'){
                    type = "tree";
                }
                else {
                    type = "unknown";
                }
                //we don't need to create a tile for a character, they are handled with another class
                
                //create the tile
                tiles[row][col] = new Tile(type, row, col);

                if (tiles[row][col].getType() == "floor") tiles[row][col].setColour(colour);
                if (tiles[row][col].getType() == "tree") tiles[row][col].setTree(treePath);

            }
        }

    return tiles;
    }

    
}
