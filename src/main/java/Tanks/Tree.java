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

    public void draw(App app) {
        PImage tree = app.loadImage(getTreePath());
        app.image(tree, column * App.CELLSIZE, row * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
    }
}
