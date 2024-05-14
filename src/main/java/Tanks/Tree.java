package Tanks;

import processing.core.PImage;

public class Tree {
    private String treePath;
    PImage tree;
    private float row;
    private int column;

    /**
     * Contructor taking in the X as an int and a Y as a float
     * @param row
     * @param column
     */
    public Tree(float row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the row as a float
     * @return
     */
    public float getRow () {
        return row;
    }

    /**
     * Returns the column as an integer
     * @return
     */
    public int getColumn () {
        return column;
    }

    /**
     * Sets the row as a float
     * @return
     */
    public void setRow(float newRow) {
        this.row = newRow;
    }

    /**
     * Returns the column as an int
     * @return
     */
    public void setCol(int newCol) {
        this.column = newCol;
    }

    /**
     * Randomise the positon of the trees
     */
    public void randomisePosition(App app){
        float pixelX = this.column * App.CELLSIZE;
        float offsettedX = app.random(-15, 15);
        float newX = offsettedX / app.CELLSIZE;

        this.column += (int) (newX); // Convert pixels to cells
    }

    /**
     * Setter for the trees images
     * @param treePath
     */
    public void setTreeImage(String treePath) {
        this.treePath = treePath;
    }

    /**
     * Getter for the tree path
     * @return
     */
    public String getTreePath() {
        return this.treePath;
    }

    /**
     * Getter for the tree coordinates
     * @return String for debugging
     */
    public String getTreeCoordinates(){
        return "The tree is at row " + getRow() + " column " + getColumn();
    }

    /**
     * Display the tree's positions
     * @param app
     */
    public void display(App app) {
        PImage tree = app.loadImage(getTreePath());
        app.image(tree, column * App.CELLSIZE, row * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
    }
}