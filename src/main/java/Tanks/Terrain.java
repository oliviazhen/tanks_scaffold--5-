package Tanks;

import java.util.Arrays;
import java.util.ArrayList;

public class Terrain {

    /**
     * This method will calculate the height of each tile in the terrain
     * @param tiles
     * @return int[]
     */
      
    public static int[] heightTerrainElement(Tile[][] tiles){
        
        //We have a new height for every column
        int total_cols = tiles[0].length;
        int[] height = new int[total_cols];

        for (int col = 0; col < total_cols; col ++){

            int total_rows = tiles.length;

            for (int row = 0; row < total_rows; row++) {

                Tile element = tiles[row][col];
                if (element.getType() == "floor"){

                    height[col] = total_rows - row;
                    row = total_rows;
                    
                }
        }
    }

    //Print test
    /*
        for (int i = 0; i < heights.length; i ++){
            System.out.printf("The height of col %d is %d", i, heights[i]);
            System.out.println();
        }
     */
    
    return height;
}

    /**
     * Calculate the intial 32-pixel array with each height of the tile.
     * E.g The left most tile of level1.txt has height of 19 so create a [19] * 32 array.
     * @param windowLength
     * @param heights
     * @param heightIndex
     * @return double[] that contains each 32 pixel window that will accumulate into a 1D array in getMicroscopicArray()
     */
    public static double[] createWindow(int windowLength, int[] heights, int heightIndex){

        // Window length corresponds to the number of pixels in each cell.
        double[] window = new double [windowLength];

        for (int i = 0; i < window.length; i ++){
            window[i] = (double) heights[heightIndex];
        }

        return window;
    }

    /**
     * @param heights
     * nested_heights represents a row of 32 elements for each height; e.g [7.0, 7.0, 7.0...] and then the next column is [9.0, 9.0...] onwards
     * @return The full 28 x 32 for each pixel, which combines the 2D nested_heights into a 1D array.
     */
    public static double[] getMicroscopicArray(int[] heights){

        int[] window = new int[App.CELLSIZE];

        double[][] nested_heights = new double[heights.length][window.length];
        double[] fullArray = new double[heights.length * window.length];
        
        for (int a = 0; a < nested_heights.length; a ++){
            nested_heights[a] = createWindow(window.length, heights, a);    
        }
        
        int index = 0;
        for (int i = 0; i < nested_heights.length; i++) {
            double[] curr_element = nested_heights[i];
            for (int j = 0; j < curr_element.length; j++) {
                fullArray[index++] = curr_element[j];
            }
        }
    
        // Insert print testing here    
        /* 
        for (double eachExcrutiatingPixel: fullArray){
            System.out.print(eachExcrutiatingPixel + " ");
        }
        */
        return fullArray;

    }

    /**
     * 
     * @param microArray
     * @param windowSize
     * @return Moving average double array; Size of the array decreases by (# - 32 + 1) at every turn
     */
    
    public static double[] movingAverage(double[] microArray, int windowSize) {

        //Make sure microArray is of 896 values
        if (microArray.length != 896){
            double[] microArrayExtended = new double[896];

            for (int a = 0; a < microArrayExtended.length; a ++){

                for (int i = 0; i < microArray.length; i ++){
                    microArrayExtended[i] = microArray[i];
                    a ++;
                }
                for (; a < microArrayExtended.length; a ++){
                    microArrayExtended[a] = microArray[microArray.length - 1];
                }
            }  
            microArray = microArrayExtended; 
        }

        ArrayList<Double> result = new ArrayList<>();
        double movingSum = 0;
    
        // Initialise the result array with the first moving average. This should be te same as the first column terrain height
        for (int i = 0; i < windowSize; i++) {
            movingSum += microArray[i];
        }
        result.add(movingSum / windowSize);
    
        // Add the element that enters the window 
        /* 
        * We start off with i = 1 and we use this to access (i + 32 - 1) which means the next
        element that enters the window is 32. This is consistant with the moving average since we start at i = 0
        * We then subtract the element that we accessed already at the previous iteration from the moving sum
        * The result is added to the resultArray
        */ 
        for (int i = 1; i <= microArray.length - windowSize; i++) {
            movingSum += (microArray[i + windowSize - 1] - microArray[i - 1]);
            result.add(movingSum / windowSize);
        }
    
        double[] resultArray = new double[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultArray[i] = result.get(i);
        }
    
        //Print testing
        /* 
        for (double avg : resultArray) {
            System.out.print(avg + " ");
        }
        */
        
        //System.out.println("The size of the averages array is " + resultArray.length);
    
        return resultArray;
    }

    /* 
    public static void main(String[] args){

        char[][] arr = ReadFile.loadArray("level1.txt");

        Tile[][] tiles = ReadFile.arrayToTiles(arr, "255,255,255", "src/main/resources/Tanks/tree1.png");

        //Print out the heights
        int[] heights = Terrain.heightTerrainElement(tiles);

        //MicroArray
        double[] micro = Terrain.getMicroscopicArray(heights);
        double[] movingAvg = Terrain.movingAverage(micro, 32);
        double[] movingAvgAgain = Terrain.movingAverage(movingAvg, 32);
        
        for(double microPixel: movingAvgAgain){
            System.out.print(microPixel + " ");
        }
        
    }
    */

    
}
