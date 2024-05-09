package Tanks;
import java.util.ArrayList;
import java.util.List;

public class Turret {

    final int TURRET_WIDTH = 8;
    final int TURRET_HEIGHT = 15;
    public int angle;
    private double x;
    private double y;

    public Turret(double defaultX, double defaultY){
        this.x = defaultX;
        this.y = defaultY;
        angle = 0;
    }

    public void setPosition(double newX, double newY){
        this.x = newX;
        this.y = newY;
    }

    public void setAngle(int newAngle){
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

    //Note that these are not accounting for CELLSIZE
    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public int getAngle(){
        return this.angle;
    }

}