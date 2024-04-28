package Tanks;
import java.util.*;

public class Tile{
    private String type; //is the tile a tree or a hill etc?
    protected int x, y;//row and column of tile

    protected int[] rgb;
    protected String treePath;
    
    public Tile(String type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    
    public String getType(){
        return this.type;
    }

    public void setColour(int[] colours){
        this.rgb = colours;
    }

    public int[] getColour(){
        return this.rgb;
    }

    public String toString(){
        return "The tile is at row:" + getX() + ", col: " + getY();
    }

    void draw(App app) {
        int x = this.x * App.CELLSIZE;
        int y = this.y * App.CELLSIZE;

        if (this.type.equals("floor")) {
            //app.noStroke();
            app.rectMode(app.CENTER);
            //Change the colour based on the foreground-colour
            app.fill(getColour()[0], getColour()[1], getColour()[2]);
            app.rect(this.y*App.CELLSIZE + App.CELLSIZE/2, this.x*App.CELLSIZE + App.CELLSIZE/2, App.CELLSIZE, App.CELLSIZE);
        }

    }

}
