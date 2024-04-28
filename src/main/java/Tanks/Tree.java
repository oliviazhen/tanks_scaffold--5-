package Tanks;

import processing.core.PImage;

public class Tree {
    private String treePath;
    PImage tree;
    private double x;
    private double y;

    public Tree(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX () {
        return x;
    }

    public double getY () {
        return y;
    }

    public void setX (double newX) {
        this.x = newX;
    }

    public void setY (double newY) {
        this.y = newY;
    }

    public void setTreeImage(String treePath) {
        this.treePath = treePath;
    }

    public String getTreePath() {
        return this.treePath;
    }

    public String getTreeCoordinates(){
        return "The tree is at row " + getX() + " column " + getY();
        // We can use the row coordinate and adjust the Y coordinate according to the line
    }


    public void draw(App app) {
        PImage tree = app.loadImage(getTreePath());
        //app.image(tree, (int)(getY() * App.CELLSIZE), (int)(getX() * App.CELLSIZE), App.CELLSIZE, App.CELLSIZE);
    }
}
