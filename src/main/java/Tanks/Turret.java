package Tanks;

public class Turret implements Location{

    final int TURRET_WIDTH = 8;
    final int TURRET_HEIGHT = 15;
    public float angle;
    private int X;
    private int Y;

    /**
     * Constructor takes in the default X and Y inherited from the tank.
     * Note that I used an is-a relationship here as a necessitY to absorb the TOP_TANK_WIDTH/topRectX values.
     * @param defaultX
     * @param defaultY
     */
    public Turret(int defaultX, int defaultY){
        this.X = defaultX;
        this.Y = defaultY;
        angle = 0;
    }

    /**
     * Set the X and Y
     * @param newX
     * @param newY
     */
    public void setPosition(int newX, int newY){
        this.X = newX;
        this.Y = newY;
    }

    /**
     * Set the angle of the turret in degrees. Performs necessarY checks
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
     * Getter for the X
     * @return int
     */
    public int getX(){
        return this.X;
    }

    /**
     * Getter for the Y
     * @return int
     */
    public int getY(){
        return this.Y;
    }

    /**
     * Getter for the angle.
     * @return float
     */
    public float getAngle(){
        return this.angle;
    }

}