package Tanks;
import java.util.Arrays;

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
    public double turretAngle = 90;

    private int fuel = 250;
    private int health = 100;
    private int power = 50;
    private int score = 0;

    private int speed = 60;

    public Tank(double row, double column) {
        // The parameters already account for CELLSIZE
        this.row = row;
        this.column = column;

    }

    public double getRow() {
        return row;
    }

    public int getScore() {
        return score;
    }
    public int getPower(){
        return power;
    }
    public int getHealth(){
        return health;
    }

    public double getColumn() {
        return column;
    }

    public int[] getColours(){
        return this.colours;
    }

    public int getFuelAmount(){
        System.out.println("The new fuel amount is " + this.fuel);
        return this.fuel;
    }

    public void setFuelAmount(int amount){
        this.fuel -= fuel;
    }

    public void setPosition(double newRow, double newCol){
        this.row = newRow;
        this.column = newCol;
    }

    public void rotateTurret(double angle) {
        // Calculate the new position of the turret based on the angle
        double radians = Math.toRadians(angle);
        double xOffset = Math.cos(radians) * (TOP_TANK_WIDTH / 2);
        double yOffset = Math.sin(radians) * (TOP_TANK_WIDTH / 2);
        double newTurretX = topRectX + xOffset;
        double newTurretY = topRectY - yOffset;
    
        // Update the turret position
        turretX = newTurretX;
        turretY = newTurretY;
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

    public void update(App app, float deltaTimeInSeconds){
        int pixelsToMove = (int)(speed * deltaTimeInSeconds); // Calculate pixels to move based on speed and elapsed time

        if (app.left){
            System.out.println("The tank processes a left turn");
            setPosition(row, column - 1); // Move left by the calculated pixels
            setFuelAmount(pixelsToMove); // Decrease fuel based on the distance moved
        }
        if (app.right){
            System.out.println("The tank processes a right turn");
            setPosition(row, column + 1); // Move right by the calculated pixels
            setFuelAmount(pixelsToMove); // Decrease fuel based on the distance moved
        }

        if (app.up) {
            // Rotate turret left
            turretAngle -= 5;
            rotateTurret(turretAngle);
        }
        if (app.down) {
            // Rotate turret right
            turretAngle += 5;
            rotateTurret(turretAngle);
        }
        
    }

    
    public void display(App app) {

        // First rectangle
        app.fill(colours[0], colours[1], colours[2]);
        app.noStroke(); 
        app.rect((float)column * app.CELLSIZE, (float)row * app.CELLSIZE, TANK_WIDTH, TANK_HEIGHT);
        
        // Second rectangle has to be placed on top of the first rectangle.
        setSecondRectangle(row, column);
        app.rect((float)topRectX * app.CELLSIZE, (float)topRectY * app.CELLSIZE, TOP_TANK_WIDTH, TOP_TANK_HEIGHT);
        //System.out.println("The second rectangle is placed at column " + topRectX + " row " + topRectY);

        // Draw the turret
        setTurret(topRectX, topRectY);
        app.fill(0);
        app.rect((float)turretX * app.CELLSIZE, (float)turretY * app.CELLSIZE, TURRET_WIDTH, TURRET_HEIGHT);
        
    }

    @Override
    public String toString() {
        return "Tank{" +
                "row=" + row +
                ", column=" + column +
                ", fuel=" + fuel +
                ", health=" + health +
                ", power=" + power +
                ", score=" + score +
                ", speed=" + speed +
                ", colours=" + Arrays.toString(colours) +
                '}';
    }


}
