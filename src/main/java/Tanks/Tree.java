package Tanks;

import processing.core.PImage;

public class Tree {
    private String treePath;
    PImage tree;

    private float row;
    private int column;

    public Tree(float row, int column) {
        this.row = row;
        this.column = column;
    }

    public double getRow () {
        return row;
    }

    public double getColumn () {
        return column;
    }

    public void setRow(float newRow) {
        this.row = newRow;
    }

    public void setCol(int newCol) {
        this.column = newCol;
    }

    public void randomisePosition(App app){
        //Convert to pixel position first
        float pixelX = this.column * App.CELLSIZE;

        // Generate random offsets within the range of -15 to 15 pixels from the original position 
        float offsettedX = app.random(-15, 15);

        float newX = offsettedX / app.CELLSIZE;
        
        // Update the position of the tree
        this.column += (int) (newX); // Convert pixels to cells

    }

    public void setTreeImage(String treePath) {
        this.treePath = treePath;
    }

    public String getTreePath() {
        return this.treePath;
    }

    public String getTreeCoordinates(){
        return "The tree is at row " + getRow() + " column " + getColumn();
        // We can use the row coordinate and adjust the Y coordinate according to the line
    }

    public void display(App app) {
        PImage tree = app.loadImage(getTreePath());
        app.image(tree, column * App.CELLSIZE, row * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
    }
}