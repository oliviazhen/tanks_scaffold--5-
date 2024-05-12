package Tanks;
import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;

public class Tank{

    private int[] colours = new int[3];

    private int TANK_WIDTH = 30;
    private int TANK_HEIGHT = 8;
    private int TOP_TANK_WIDTH = 20;
    private int TOP_TANK_HEIGHT = 8;

    private double row; // Y coordinate without accounting for cellsize
    private double column; // X coordinate without accounting for cellsize
    private double topRectX;
    private double topRectY;

    private int fuel = 250;
    private int health = 100;
    private float power = 50;
    private int turretPower = 100;
    private int score = 0;
    private int parachute = App.INITIAL_PARACHUTES;

    private int speed = 30;

    private Turret turret = new Turret(0,0);
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();


    public Tank(double row, double column) {
        this.row = row;
        this.column = column;
    }

    public double getRow() {
        return row;
    }
    public int getScore() {
        return score;
    }
    public float getPower(){
        return power;
    }
    public int getHealth(){
        return health;
    }

    public int getParachutes(){
        return parachute;
    }

    public double getColumn() {
        return column;
    }

    public int[] getColours(){
        return this.colours;
    }

    public int getFuelAmount(){
        //System.out.println("The new fuel amount is " + this.fuel);
        return this.fuel;
    }

    public void setFuelAmount(int amount){
        this.fuel -= amount;
    }

    public void setParachutes(int amount){
        this.parachute += amount;
    }

    public void setHealth(int amount){

        if (amount > 60){
            amount = 60;
        }

        this.health -= amount;
    }
    
    public void setPosition(double newRow, double newCol){
        this.row = newRow;
        this.column = newCol;   
    }

    public void setColour(int[] colours){
        this.colours = colours;
    }

    public void setSecondRectangle(double row, double col){
        double topRectX = col  + (TANK_WIDTH - TOP_TANK_WIDTH) / 2; 
        double topRectY = row - TOP_TANK_HEIGHT;

        this.topRectX = topRectX ;
        this.topRectY = topRectY;
    }  
    
    public void setTurret(double topRectX, double topRectY){

        double turretX = topRectX + (TOP_TANK_WIDTH) / 2;
        double turretY = topRectY;

        this.turret.setPosition(turretX, turretY);
    }

    public void addProjectile(Turret t) {
        Projectile projectile = new Projectile(t, (float) t.getX(), (float) t.getY(), t.getAngle());

        if (projectiles.isEmpty()){
            this.projectiles.add(projectile);
        }
        else{
            projectiles = new ArrayList<Projectile>();
            projectiles.add(projectile);
        }
    }

    public ArrayList<Projectile> getProjectiles(){
        return this.projectiles;
    }
    
    public Turret getTurret(){
        if (this.turret != null){
            return this.turret;
        }
        return null;
    }

    public void rotateTurret(double angle) {
        if (angle < -90){
            angle = -90;
        }
        if (angle > 90){
            angle = 90;
        }
        double radians = Math.toRadians(angle);

        double xOffset = Math.cos(radians) * (TOP_TANK_WIDTH / 2);
        double yOffset = Math.sin(radians) * (TOP_TANK_WIDTH / 2);
        double newTurretX = topRectX + xOffset;
        double newTurretY = topRectY - yOffset;
    
        // Update the turret position
        this.turret.setPosition(newTurretX, newTurretY);
    }    


    public void setPower(float power){
        int maxPower = this.getHealth();
        float newPower = this.getPower() + power;
        
        // Bound Checking
        if (newPower <= 0) {
            this.power = 0;
        } else if (newPower > maxPower) {
            this.power = maxPower;
        } else {
            this.power = newPower;
        }
    
        //System.out.println("The tank now has " + this.getPower() + " power");
    }

    public void move(App app, int dx) {

        if (app.left) { 
            double newCol = column - dx; // Move left

            if ((newCol > 864)||(newCol < 0)) newCol = App.WIDTH;
            double newRow = app.HEIGHT - (double) app.movingAvgWithCELLSIZE[(int) newCol];
            setPosition(newRow, newCol);
        }
        if (app.right) {
            double newCol = column + dx;
            if ((newCol > 864)||(newCol < 0)) newCol = 0;
            double newRow = app.HEIGHT - (double) app.movingAvgWithCELLSIZE[(int) newCol];
            setPosition(newRow, newCol);
        }
    }

    public void moveTurret(App app, int dx){
        if (app.up) {
            // Rotate turret left
            int newAngle = this.turret.getAngle() - dx;
            this.turret.setAngle(newAngle);
            rotateTurret(newAngle);
        }
        if (app.down) {
            // Rotate turret right
            int newAngle = this.turret.getAngle() + dx;
            this.turret.setAngle(newAngle);
            rotateTurret(newAngle);
        }
    }

    public void changePower(App app, int dx){
        if (app.w) {
            this.setPower(((float) 36/60) * dx); 
        }
        if (app.s) {
            this.setPower(((float) -36/60) * dx); 
        }
    }
    
    public void display(App app) {

        app.fill(colours[0], colours[1], colours[2]);
        app.noStroke(); 
        app.rect((float)column, (float)row, TANK_WIDTH, TANK_HEIGHT);
        
        // Second rectangle has to be placed on top of the first rectangle.
        setSecondRectangle(row, column);
        app.rect((float)topRectX , (float)topRectY , TOP_TANK_WIDTH, TOP_TANK_HEIGHT);
    
        // Draw the turret
        setTurret(topRectX, topRectY);
        Turret t = this.getTurret();
        
        app.pushMatrix(); 
        app.translate((float)t.getX(), (float) t.getY()); 
    
        app.rotate((float) Math.toRadians(t.getAngle())); 
        
        //Draw the new turret
        app.fill(0);
        app.rect(-t.TURRET_WIDTH / 2, -t.TURRET_HEIGHT, t.TURRET_WIDTH, t.TURRET_HEIGHT); 

        float x = app.screenX(-t.TURRET_WIDTH / 2, -t.TURRET_HEIGHT);
        float y = app.screenY(-t.TURRET_WIDTH / 2, -t.TURRET_HEIGHT);

        t.setPosition((x+(t.TURRET_WIDTH/2)), y);
        //System.out.println("Turret's new X is " + x + " and the new Y is " + y);
        
        app.popMatrix();
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