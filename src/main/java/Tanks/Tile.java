package Tanks;
import processing.core.PImage;

public class Tile{
    String type; //is the tile a tree or a hill etc?
    int x, y;//row and column of tile
    
    public Tile(String type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
    }

    void draw(App app, int tileSize) {
        int x = this.x * tileSize;
        int y = this.y * tileSize;

        if (this.type.equals("white")) {
            app.noStroke();
            app.rectMode(app.CENTER);
            app.fill(255, 255, 255);
            app.rect(this.y*App.CELLSIZE + App.CELLSIZE/2, this.x*App.CELLSIZE + App.CELLSIZE/2, tileSize, tileSize);
        } else if (this.type.equals("tree")) {
            PImage tree = app.loadImage("src/main/resources/Tanks/tree1.png");
            app.image(tree, this.y*App.CELLSIZE, this.x*App.CELLSIZE, tileSize, tileSize);
        }



    }
    
}
