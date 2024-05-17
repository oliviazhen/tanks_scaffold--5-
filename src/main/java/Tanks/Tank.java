package Tanks;
import java.util.ArrayList;
import java.util.Arrays;

import jogamp.opengl.x11.glx.X11GLXContext;
import jogamp.opengl.x11.glx.X11GLXDynamicLibraryBundleInfo;
import processing.core.PApplet;

public class Tank implements Location{

    private int[] colours = new int[3];

    protected int TANK_WIDTH = 30;
    protected int TANK_HEIGHT = 8;
    protected int TOP_TANK_WIDTH = 20;
    protected int TOP_TANK_HEIGHT = 8;

    protected int Y; // Row coordinate 
    protected int X; // Column coordinate 
    protected int topRectX;
    protected int topRectY;

    private int fuel = 250;
    private int health = 100;
    protected float power = 50;
    private int score = 0;
    private int parachute = App.INITIAL_PARACHUTES;

    private int speed = 60;

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    public Turret turret = new Turret(0, 0);
    /**
     * Constructor accepting a row and column relating to Y and X respectively.
     * @param row
     * @param column
     */
    public Tank(int X, int Y) {
        this.Y = Y; //Note that the X and Y are switched around since my logic follows the Row, Column logic.
        this.X = X;
    }

    /**
     * Getter for int[] of colours as the RGB value
     * @return int[] of colours
     */
    public int[] getColours(){
        return this.colours;
    }

    /**
     * Getter for Row
     * @return int 
     */
    public int getY() {
        return Y;
    }

    /**
     * Getter for Column
     * @return int 
     */
    public int getX() {
        return X;
    }

    /**
     * Getter for Score
     * @return int 
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for Power. Design choice to display power to the decimal for easy rounding.
     * @return float 
     */
    public float getPower(){
        return power;
    }

    /**
     * Getter for Speed
     * @return int 
     */
    public int getSpeed(){
        return speed;
    }

    /**
     * Getter for Health
     * @return int 
     */
    public int getHealth(){
        return health;
    }

    /**
     * Getter for Parachute
     * @return int 
     */
    public int getParachutes(){
        return parachute;
    }

     /**
     * Getter for Fuel
     * @return int 
     */
    public int getFuelAmount(){
        //System.out.println("The new fuel amount is " + this.fuel);
        return this.fuel;
    }

    /**
     * Setter for both row and column
     * @param newRow
     * @param newCol
     */
    public void setPosition(int newRow, int newCol){
        this.Y = newRow;
        this.X = newCol;   
    }

    /**
     * Individual setter for X
     */
    public void setX(int X){
        this.X = X;
    }
    /**
     * Individual setter for Y
     */
    public void setY(int Y){
        this.Y = Y;
    }

     /**
     * Setter for Fuel.
     * @return int 
     */
    public void setFuelAmount(int amount){
        this.fuel = amount;
    }

     /**
     * Setter for parachutes
     * @return int 
     */
    public void setParachutes(int amount){
        if (!(amount > 3 || amount < 0)) this.parachute = amount;
    }

    /**
     * Setter for health
     * @param amount
     */
    public void setHealth(int amount){
        if (!(amount < 0 || amount > 60)) this.health = amount;
    }
    /**
     * Setter for the colours
     * @param colours
     */
    public void setColour(int[] colours){
        this.colours = colours;
    }

    /**
     * Setter for the score
     * @param score
     */
    public void setScore(int score){
        if (!(score < 0)){
            this.score += score;
        }
    }

    /**
     * Set the second rectangle on top's coordinates
     * @param row
     * @param col
     */
    public void setSecondRectangle(int row, int col){
        int topRectX = col  + (TANK_WIDTH - TOP_TANK_WIDTH) / 2; 
        int topRectY = row - TOP_TANK_HEIGHT;

        this.topRectX = topRectX ;
        this.topRectY = topRectY;
    }  
    
    /**
     * Set the turret's measurements by accessing the second rectangle.
     * @param topRectX
     * @param topRectY
     */
    public void setTurret(int topRectX, int topRectY){
        int turretX = topRectX + (TOP_TANK_WIDTH) / 2;
        int turretY = topRectY;

        this.turret.setPosition(turretX, turretY);
    }

     /**
     * Get the turret. Note that we must set the turret before we get it.
     * @return Turret object corresponding to the tank
     */
    public Turret getTurret(){
        if (this.turret != null){
            return this.turret;
        }
        return null;
    }

    /**
     * Add a new projectile to the turret
     * @param t
     */
    public void addProjectile(Turret t) {
        Projectile projectile = new Projectile(t, (int) t.getX(), (int) t.getY(), (float) t.getAngle());

        if (projectiles.isEmpty()){
            this.projectiles.add(projectile);
        }
        else{
            projectiles = new ArrayList<Projectile>();
            projectiles.add(projectile);
        }
    }

    /**
     * Get the projectile
     * @return ArrayList<Projectile>
     */
    public ArrayList<Projectile> getProjectiles(){
        return this.projectiles;
    }

    /**
     * Set power of the tank. This will affect projectiles
     * @param power
     */
    public void setPower(float power){
        int maxPower = this.getHealth();
        
        if (power <= 0) {
            this.power = 0;
        } else if (power > maxPower) {
            this.power = maxPower;
        } else {
            this.power = power;
        }
        //System.out.println("The tank now has " + this.getPower() + " power");
    }

    /**
     * Move the tank's position
     * @param app
     * @param newTankPosition
     */
    public void move(App app, int newTankPosition) {

        if (app.currentPlayer.getFuelAmount() > 0){
            if (newTankPosition > 864) newTankPosition = 864;
            if (newTankPosition < 0) newTankPosition = 0;
            int newRow = (int) (app.HEIGHT - app.movingAvgWithCELLSIZE[newTankPosition]);
            setPosition(newRow, newTankPosition);
            app.currentPlayer.setFuelAmount(app.currentPlayer.getFuelAmount() - 1);
         }


    }

    /**
     * Move the tank's turret's posiion
     * @param newTurretPosition
     */
    public void moveTurret(float newTurretPosition){
        this.turret.setAngle(newTurretPosition);
        this.rotateTurret(newTurretPosition);
    }

    /**
     * Rotates the turret by calculating the new position accounting for the angle and the setting the position
     * @param angle
     */
    public void rotateTurret(float angle) {
        if (angle < -90){
            angle = -90;
        }
        if (angle > 90){
            angle = 90;
        }
        double radians = Math.toRadians(angle);

        double xOffset = Math.cos(radians) * (TOP_TANK_WIDTH / 2);
        double yOffset = Math.sin(radians) * (TOP_TANK_WIDTH / 2);
        int newTurretX = (int) (topRectX + xOffset);
        int newTurretY = (int) (topRectY - yOffset);
    
        // Update the turret position
        this.turret.setPosition(newTurretX, newTurretY);
    }    


    /**
     * Tank explosion graphic
     * @param app
     */
    public void explode(App app){
        int[] colors = {app.color(this.getColours()[0],this.getColours()[1], this.getColours()[2]) , app.color(255, 165, 0), app.color(255, 255, 0)};
        float[] radii = {5, 5,5 }; 
        
        for (int i = 0; i < colors.length; i++) {
            app.fill(colors[i]);
            
            float maxRadius = radii[i];
            
            for (long j = 0; j < 200; j += 1) {
                float progress = (float)(j) / 200;
                float currentRadius = progress * maxRadius;
                app.ellipse((float)this.getX(), (float)this.getY(), currentRadius * 2, currentRadius * 2);
            }
        }
    }

    /**
     * Tank move graphic
     * @param app
     */
    public void display(App app) {

        app.fill(colours[0], colours[1], colours[2]);
        app.noStroke(); 
        app.rect((float)X, (float)Y, TANK_WIDTH, TANK_HEIGHT);
        
        // Second rectangle has to be placed on top of the first rectangle.
        setSecondRectangle(Y, X);
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

        int x = Math.round(app.screenX(-t.TURRET_WIDTH / 2, -t.TURRET_HEIGHT));
        int y = Math.round(app.screenY(-t.TURRET_WIDTH / 2, -t.TURRET_HEIGHT));

        t.setPosition((x+(t.TURRET_WIDTH/2)), y);
        
        app.popMatrix();
    }

    /**
     * Generate a string of Tank for debugging purposes
     */
    @Override
    public String toString() {
        return "Tank{" +
                "row=" + Y +
                ", column=" + X +
                ", fuel=" + fuel +
                ", health=" + health +
                ", power=" + power +
                ", score=" + score +
                ", speed=" + speed +
                ", colours=" + Arrays.toString(colours) +
                '}';
    }

} 