package Tanks;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

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

            // Create the 2D array terrainArray
            terrainArray = new char[terrain.size()][maxCols];
            for (int i = 0; i < terrain.size(); i++) {
                char[] row = terrain.get(i);
                for (int j = 0; j < row.length; j++) {
                    terrainArray[i][j] = row[j];
                }
            }

            // Print the 2D array terrainArray (if needed)
            /* 
            for (char[] row : terrainArray) {
                for (char cell : row) {
                    System.out.print(cell + " ");
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
  

    public static Tile[][] arrayToTiles(char[][] array){

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
                    type = "white";
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
            }
        }

    return tiles;
    }

    
}
