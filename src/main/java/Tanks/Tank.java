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
    private int power = 50;
    private int turretPower = 100;
    private int score = 0;

    private int speed = 60;

    private Turret turret = new Turret(0,0);
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

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
        //System.out.println("The new fuel amount is " + this.fuel);
        return this.fuel;
    }

    public void setFuelAmount(int amount){
        this.fuel -= fuel;
    }

    public void changeTurretPower(int addOrSubtract){
        int AMOUNT = 36;
        if (addOrSubtract > 0){
            if (this.turretPower + AMOUNT > 100){
                this.turretPower = 100;
            }
            else this.turretPower += AMOUNT;
        }
        else{
            if (this.turretPower - AMOUNT < 0){
                this.turretPower = 0;
            }
            else this.turretPower -= AMOUNT;
        }
    }
    
    public void setPosition(double newRow, double newCol){
        this.row = newRow;
        this.column = newCol;
    }

    public void setColour(int[] colours){
        this.colours = colours;
    }

    public void setSecondRectangle(double row, double col){
        double topRectX = col * App.CELLSIZE + (TANK_WIDTH - TOP_TANK_WIDTH) / 2; 
        double topRectY = row * App.CELLSIZE - TOP_TANK_HEIGHT;

        this.topRectX = topRectX / App.CELLSIZE;
        this.topRectY = topRectY / App.CELLSIZE;
    }  

    public void rotateTurret(double angle) {
        topRectX = topRectX * App.CELLSIZE;
        topRectY = topRectY * App.CELLSIZE;

        if (angle < -90){
            angle = -90;
        }
        if (angle > 90){
            angle = 90;
        }

        // Calculate the new position of the turret based on the angle
        double radians = Math.toRadians(angle);

        double xOffset = Math.cos(radians) * (TOP_TANK_WIDTH / 2);
        double yOffset = Math.sin(radians) * (TOP_TANK_WIDTH / 2);
        double newTurretX = topRectX + xOffset;
        double newTurretY = topRectY - yOffset;
    
        // Update the turret position
        this.turret.setPosition(newTurretX / App.CELLSIZE, newTurretY / App.CELLSIZE);
    }    
    
    public void setTurret(double topRectX, double topRectY){

        topRectX = topRectX * App.CELLSIZE;
        topRectY = topRectY * App.CELLSIZE;
        double turretX = topRectX + (TOP_TANK_WIDTH) / 2;
        double turretY = topRectY;

        //Create a new turret
        this.turret.setPosition(turretX / App.CELLSIZE, turretY / App.CELLSIZE);
    }

    public void addProjectile(Turret t) {
        // Calculate the position of the projectile based on turret position and angle
        double projectileX = t.getX() * App.CELLSIZE;
        double projectileY = t.getY() * App.CELLSIZE;

        // Create a new projectile and add it to the list
        Projectile projectile = new Projectile((float) projectileX, (float) projectileY, t.getAngle());

        // Add an extra layer of control so that we only have one projectile in the array
        //this.projectiles.add(projectile);
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

    public void update(App app) {

        if (app.left) {
            setPosition(row, ((column * App.CELLSIZE) + 60) / App.CELLSIZE);
        }
    
        if (app.right) {
            setPosition(row, ((column * App.CELLSIZE) + 60) / App.CELLSIZE);
        }
    
        if (app.up) {
            // Rotate turret left
            int newAngle = this.turret.getAngle() - 5;
            this.turret.setAngle(newAngle);
            rotateTurret(newAngle);
        }
        if (app.down) {
            // Rotate turret right
            int newAngle = this.turret.getAngle() + 5;
            this.turret.setAngle(newAngle);
            rotateTurret(newAngle);
        }
        if (app.w) {
            changeTurretPower(2);
        }
        if (app.s) {
            changeTurretPower(-1);
        }
        if (app.space) {
            if ((this.turret.getX() == 0) && (this.turret.getY() == 0)) {
                System.out.println("The turret is not in the correct position.");
            } else {
                this.addProjectile(this.turret);
            }
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
    
        // Draw the turret
        setTurret(topRectX, topRectY);
        Turret t = this.getTurret();
        
        app.pushMatrix(); 
        app.translate((float)t.getX() * app.CELLSIZE, (float) t.getY() * app.CELLSIZE); 
    
        app.rotate((float) Math.toRadians(t.getAngle())); 
        
        //Draw the new turret
        app.fill(0);
        app.rect(-t.TURRET_WIDTH / 2, -t.TURRET_HEIGHT, t.TURRET_WIDTH, t.TURRET_HEIGHT); 

        float x = app.screenX(-t.TURRET_WIDTH / 2, -t.TURRET_HEIGHT);
        float y = app.screenY(-t.TURRET_WIDTH / 2, -t.TURRET_HEIGHT);

        t.setPosition((x+(t.TURRET_WIDTH/2))/App.CELLSIZE , y/App.CELLSIZE);
        //System.out.println("Turret's new X is " + x + " and the new Y is " + y);
        
        app.popMatrix();

        /* 
        app.fill(255,0,255);
        app.ellipse((float)t.getX() * App.CELLSIZE, (float)t.getY() * App.CELLSIZE, 5, 5);
        */
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