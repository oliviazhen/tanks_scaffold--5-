package Tanks;
import processing.core.PApplet;

public class Tank implements Turret {

    private int[] colours = new int[3];

    private int TANK_WIDTH = 30;
    private int TANK_HEIGHT = 8;
    private int TOP_TANK_WIDTH = 20;
    private int TOP_TANK_HEIGHT = 8;

    private double row; // Y coordinate without accounting for cellsize
    private double column; // X coordinate without accounting for cellsize
    private double topRectX;
    private double topRectY;
    private double turretX;
    private double turretY;

    private int fuel = 250;
    private int health = 100;
    private int power = 50;

    public Tank(double row, double column) {
        this.row = row;
        this.column = column;
    }

    public double getRow() {
        return row;
    }

    public double getColumn() {
        return column;
    }

    public void setPosition(double newRow, double newCol){
        this.row = newRow;
        this.column = newCol;
    }

    public void setSecondRectangle(double row, double col){
        double topRectX = col * App.CELLSIZE + (TANK_WIDTH - TOP_TANK_WIDTH) / 2; 
        double topRectY = row * App.CELLSIZE - TOP_TANK_HEIGHT;

        this.topRectX = topRectX / App.CELLSIZE;
        this.topRectY = topRectY / App.CELLSIZE;
    }  
    
    public void setTurret(double topRectX, double topRectY){
        topRectX = topRectX * App.CELLSIZE;
        topRectY = topRectY * App.CELLSIZE;
        double turretX = topRectX + (TOP_TANK_WIDTH - TURRET_WIDTH) / 2;
        double turretY = topRectY - TURRET_HEIGHT;

        this.turretX = turretX / App.CELLSIZE;
        this.turretY = turretY / App.CELLSIZE;
    }    

    public void setColour(int[] colours){
        this.colours = colours;
    }

    public void draw(App app) {
    
        // First rectangle
        app.fill(colours[0], colours[1], colours[2]);
        app.noStroke(); 
        app.rect((float)column * App.CELLSIZE, (float)row * App.CELLSIZE, TANK_WIDTH, TANK_HEIGHT);
        
        // Second rectangle has to be placed on top of the first rectangle.
        setSecondRectangle(row, column);
        app.rect((float)topRectX * App.CELLSIZE, (float)topRectY * App.CELLSIZE, TOP_TANK_WIDTH, TOP_TANK_HEIGHT);
        
        // Second rectangle has to be placed on top of the first rectangle.
        setSecondRectangle(row, column);
        app.rect((float)topRectX * App.CELLSIZE, (float)topRectY * App.CELLSIZE, TOP_TANK_WIDTH, TOP_TANK_HEIGHT);
        //System.out.println("The second rectangle is placed at column " + topRectX + " row " + topRectY);

        // Draw the turret
        app.fill(0);
        setTurret(topRectX, topRectY);
        System.out.println("Set turret at column " + turretX + " row " + turretY);
        app.rect((float) turretX * App.CELLSIZE, (float) turretY * App.CELLSIZE, TURRET_WIDTH, TURRET_HEIGHT);


        

        
    }
}
