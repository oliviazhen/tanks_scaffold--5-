package Tanks;

public class Tank {

    private int[] colours = new int[3];


    private int TANK_WIDTH = 30;
    private int TANK_HEIGHT = 8;
    private int TOP_TANK_WIDTH = 20;
    private int TOP_TANK_HEIGHT = 8;
    private int TURRET_WIDTH = 8;
    private int TURRET_HEIGHT = 20;

    private double row;
    private double column;

    public Tank(double row, double column) {
        this.row = row;
        this.column = column;
    }

    public double getRow () {
        return row;
    }

    public double getColumn () {
        return column;
    }

    public void setRow(double newRow) {
        this.row = newRow;
    }

    public void setCol(double newCol) {
        this.column = newCol;
    }

    public void setPosition(double newRow, double newCol){
        this.row = newRow;
        this.column = newCol;
    }

    public void setColour(int[] colours){
        this.colours = colours;
    }

    public void draw(App app) {
    
        // First rectangle
        app.fill(colours[0], colours[1], colours[2]);
        app.noStroke(); 
        app.rect((float) column * App.CELLSIZE, (float) row * App.CELLSIZE, TANK_WIDTH, TANK_HEIGHT);
    
        // Second rectangle has to be placed on top of the first rectangle.
        float topRectX = (float)column * App.CELLSIZE + (TANK_WIDTH - TOP_TANK_WIDTH) / 2; 
        float topRectY = (float)row * App.CELLSIZE - TOP_TANK_HEIGHT;
        app.rect(topRectX, topRectY, TOP_TANK_WIDTH, TOP_TANK_HEIGHT);

        //Draw the turret
        app.fill(0);
        float turretX = topRectX + (TOP_TANK_WIDTH - TURRET_WIDTH) / 2;
        float turretY = topRectY - TURRET_HEIGHT;
        app.rect(turretX, turretY, TURRET_WIDTH, TURRET_HEIGHT);

        //Draw testing with a simple rectangle
        /*
        app.fill(255,0, 123);
        app.rect((float) column * App.CELLSIZE, (float) row * App.CELLSIZE, 10, 10);
        */

    }
    
}
