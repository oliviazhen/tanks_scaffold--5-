package Tanks;
import processing.core.PImage;
import java.util.*;

public class Tile{
    private String type; //is the tile a tree or a hill etc?
    private int x, y;//row and column of tile

    private int[] rgb;
    private String treePath;
    
    public Tile(String type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
    }


    public String getType(){
        return this.type;
    }

    public void setTree(String tree){
        if (this.type != "tree"){
            System.out.println("You are trying to set a tree for a non-tree type");
        }
        this.treePath = tree;
    }

    public String getTree(){
        return this.treePath;
    }

    public void setColour(String colours){
        if (this.type != "floor"){
            System.out.println("You are trying to set a colour for a non-floor type");
        }
        
        // Make the string of 3 numbers into an integer array
        String[] ls = colours.split(",");
        int r = Integer.parseInt(ls[0]);
        int g = Integer.parseInt(ls[1]);
        int b = Integer.parseInt(ls[2]);

        int[] int_colours = {r,g,b};

        this.rgb = int_colours;
    }
    
    public int[] getColour(){
        return this.rgb;
    }

    void draw(App app, int tileSize) {
        int x = this.x * tileSize;
        int y = this.y * tileSize;

        if (this.type.equals("floor")) {
            app.noStroke();
            app.rectMode(app.CENTER);
            //Change the colour based on the foreground-colour
            app.fill(getColour()[0], getColour()[1], getColour()[2]);
            app.rect(this.y*App.CELLSIZE + App.CELLSIZE/2, this.x*App.CELLSIZE + App.CELLSIZE/2, tileSize, tileSize);
        } else if (this.type.equals("tree")) {
            PImage tree = app.loadImage(getTree());
            app.image(tree, this.y*App.CELLSIZE, this.x*App.CELLSIZE, tileSize, tileSize);
        }



    }
    
}
