package Tanks;

public interface Turret {
    final int TURRET_WIDTH = 8;
    final int TURRET_HEIGHT = 15;
    public int angle = 90;


    double getRow(); // Get tank's X position
    double getColumn(); // Get tank's Y position


    
    
}