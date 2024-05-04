package Tanks;
import java.util.Arrays;

import processing.core.PApplet;

public class Tank implements Turret {

    private int[] colours = new int[3];

    private int TANK_WIDTH = 30;
    private int TANK_HEIGHT = 8;
    private int TOP_TANK_WIDTH = 20;
    private int TOP_TANK_HEIGHT = 8;

    private double X; // Y coordinate without accounting for cellsize
    private double Y; // X coordinate without accounting for cellsize
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

    public Tank(double X, double Y) {
        // The parameters already account for CELLSIZE
        this.X = X;
        this.Y = Y;

    }

    public double getX() {
        return X;
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

    public double getY() {
        return Y;
    }

    public int[] getColours(){
        return this.colours;
    }

    public int getFuelAmount(){
        //System.out.println("The new fuel amount is " + this.fuel);
        return this.fuel;
    }

    public void setFuelAmount(int amount){
        this.fuel -= fuel;
    }

    public void setPosition(double newX, double newCol){
        this.X = newX;
        this.Y = newCol;
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

    public void setSecondRectangle(double X, double col){
        double topRectX = col + (TANK_WIDTH - TOP_TANK_WIDTH) / 2; 
        double topRectY = X - TOP_TANK_HEIGHT;

        this.topRectX = topRectX;
        this.topRectY = topRectY;
    }  
    
    public void setTurret(double topRectX, double topRectY){
        topRectX = topRectX;
        topRectY = topRectY;
        double turretX = topRectX + (TOP_TANK_WIDTH - TURRET_WIDTH) / 2;
        double turretY = topRectY - TURRET_HEIGHT;

        this.turretX = turretX;
        this.turretY = turretY;
    }    

    public void setColour(int[] colours){
        this.colours = colours;
    }

    public void update(App app, float deltaTimeInSeconds){
        int pixelsToMove = (int)(speed * deltaTimeInSeconds); // Calculate pixels to move based on speed and elapsed time

        if (app.left){
            System.out.println("The tank processes a left turn");
            setPosition(X, Y - 1); // Move left by the calculated pixels
            setFuelAmount(pixelsToMove); // Decrease fuel based on the distance moved
        }
        if (app.right){
            System.out.println("The tank processes a right turn");
            setPosition(X, Y + 1); // Move right by the calculated pixels
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
        app.rect((float)Y, (float)X , TANK_WIDTH, TANK_HEIGHT);
        
        // Second rectangle has to be placed on top of the first rectangle.
        setSecondRectangle(X, Y);
        app.rect((float)topRectX, (float)topRectY, TOP_TANK_WIDTH, TOP_TANK_HEIGHT);
        //System.out.println("The second rectangle is placed at Y " + topRectX + " X " + topRectY);

        // Draw the turret
        setTurret(topRectX, topRectY);
        app.fill(0);
        app.rect((float)turretX * app.CELLSIZE, (float)turretY * app.CELLSIZE, TURRET_WIDTH, TURRET_HEIGHT);
        
    }

    @Override
    public String toString() {
        return "Tank{" +
                "X=" + X +
                ", Y=" + Y +
                ", fuel=" + fuel +
                ", health=" + health +
                ", power=" + power +
                ", score=" + score +
                ", speed=" + speed +
                ", colours=" + Arrays.toString(colours) +
                '}';
    }


}
