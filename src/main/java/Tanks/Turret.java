package Tanks;

public class Turret{

    final int TURRET_WIDTH = 8;
    final int TURRET_HEIGHT = 15;
    public float angle;
    private int x;
    private int y;

    /**
     * Constructor takes in the default X and Y inherited from the tank.
     * Note that I used an is-a relationship here as a necessity to absorb the TOP_TANK_WIDTH/topRectX values.
     * @param defaultX
     * @param defaultY
     */
    public Turret(int defaultX, int defaultY){
        this.x = defaultX;
        this.y = defaultY;
        angle = 0;
    }

    /**
     * Set the X and Y
     * @param newX
     * @param newY
     */
    public void setPosition(int newX, int newY){
        this.x = newX;
        this.y = newY;
    }

    /**
     * Set the angle of the turret in degrees. Performs necessary checks
     * @param newAngle as a float
     */
    public void setAngle(float newAngle){
        if (newAngle < -90){
            this.angle = -90;
        }
        else if (newAngle > 90){
            this.angle = 90;
        }
        else{
            this.angle = newAngle;
        }
    }

    /**
     * Getter for the X
     * @return int
     */
    public int getX(){
        return this.x;
    }

    /**
     * Getter for the Y
     * @return int
     */
    public int getY(){
        return this.y;
    }

    /**
     * Getter for the angle.
     * @return float
     */
    public float getAngle(){
        return this.angle;
    }

}