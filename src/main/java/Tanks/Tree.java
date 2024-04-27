package Tanks;
import processing.core.PImage;

public class Tree extends Tile {
    private String treePath;
    PImage tree;

    public Tree(int x, int y) {
        super("tree", x, y);
    }

    public void adjustYCoordinate(int newY) {
        this.y = newY;
    }

    public void setTreeImage(String treePath) {
        this.treePath = treePath;
    }

    public String getTreePath() {
        return this.treePath;
    }

    public String getTreeCoordinates(){
        return "The tree is at row " + getX()+ " column " + getY();
        // We can use the row coordinate and adjust the Y coordinate according to the line
    }

    @Override
    public void draw(App app) {
        PImage tree = app.loadImage(getTreePath());
        //app.image(tree, getY() * App.CELLSIZE, getX() * App.CELLSIZE, App.CELLSIZE, App.CELLSIZE);
    }
}
