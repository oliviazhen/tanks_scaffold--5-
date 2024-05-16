package Tanks;

public class Tile implements Location{
    private String type; // Is the tile a tree or a hill etc?
    private int X, Y; //Column and Row respectively
    private int[] rgb;
    private String treePath;
    
    /**
     * Constructer that takes in a string value identifying the type
     * Integer X and Y positions
     */
    public Tile(String type, int x, int y){
        this.type = type;
        this.X = x;
        this.Y = y;
    }

    /**
     * Getter for X
     * @return int
     */
    public int getX(){
        return this.X;
    }

    /**
     * Getter for Y
     * @return int
     */
    public int getY(){
        return this.Y;
    }
    
    /**
     * Individual setter for X
     */
    public void setX(int X){
        this.X = X;
    }

    /**
     * Individual setter for Y
     */
    public void setY(int Y){
        this.Y = Y;
    }
    /**
     * Getter for the type
     * @return string
     */
    public String getType(){
        return this.type;
    }

    /**
     * Getter for colours 
     * @return int[]
     */
    public int[] getColour(){
        return this.rgb;
    }

    /**
     * Setter for the colours in an rbg value
     * @param colours
     */
    public void setColour(int[] colours){
        this.rgb = colours;
    }
    
    /**
     * Print debugging purposes
     */
    public String toString(){
        return "The tile is at row:" + getX() + ", col: " + getY();
    }
    
}
