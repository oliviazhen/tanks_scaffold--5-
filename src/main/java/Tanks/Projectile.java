package Tanks;

import java.util.Map;

public class Projectile implements Location{
    int radius = 30;
    int speed = 60; 

    private Turret fromT;
    private int X;
    private int Y;
    private float angle;
    private Wind wind;

    public boolean willExplode;
    public int[] explodeCoordinate;

    /**
     * Constructor takes in a starting turret object in which it belonged to
     * It also considers a position and an angle in which it is shooting at.
     * @param fromT
     * @param X
     * @param Y
     * @param angle
     */
    public Projectile(Turret fromT, int X, int Y, float angle){
        this.angle = angle;
        this.X = X;
        this.Y = Y;
        this.fromT = fromT;

    }

    /**
     * Getter for the X (column)
     * @return int
     */
    public int getX(){
        return this.X;
    }

    /**
     * Getter for the Y (row)
     * @return 
     */
    public int getY(){
        return this.Y;
    }

    /**
     * Setter for X (row)
     * @param X as an int
     */
    public void setX(int X){
        this.X = X;
    }

    /**
     * Setter for Y (column)
     * @param X as an int
     */
    public void setY(int Y){
        this.Y = Y;
    }

    /**
     * Setter for wind
     * @param wind as a wind object
     */
    public void setWind(Wind wind){
        this.wind = wind;
    }
    /**
     * Graphical displaY separated into eXplosion and non-eXplosion picture
     * @param app
     */
    public void display(App app) {
        if (willExplode) {
            int[] colors = {app.color(255, 0, 0), app.color(255, 165, 0), app.color(255, 255, 0)};
            float[] radii = {(float) this.radius, (float)0.5 * this.radius, (float)0.2 * this.radius}; 
            
            for (int i = 0; i < colors.length; i++) {
                app.fill(colors[i]);
                
                float maXRadius = radii[i];
                
                for (long j = 0; j < 200; j += 1) {
                    float progress = (float)(j) / 200;
                    float currentRadius = progress * maXRadius;
                    app.ellipse(explodeCoordinate[0], explodeCoordinate[1], currentRadius * 2, currentRadius * 2);
                }
            }

        } else {
            app.fill(204, 102, 0);
            app.ellipse((float)X, (float)Y, 5, 5);
        }
    }

    /**
     * Move the projectile bY shifting X and Y
     */
    public void move(){

        //Check for wind
        /*
        if (this.wind != null){
            //If the win
            speed = speed + this.wind.getWindForce();
        }
         */

        //Since the program reads the angle as 0 we have to subtract 90;
        if (angle == 0){
            this.Y -= speed;
        }
        else if (angle < 0){
            double radians = Math.toRadians(angle + 90);
            double deltaX = Math.cos(radians) * speed;
            double deltaY = Math.sin(radians) * speed;
            this.X -= deltaX;
            this.Y -= deltaY;
        }
        else{
            double radians = Math.toRadians(angle - 90);
            double deltaX = Math.cos(radians) * speed;
            double deltaY = Math.sin(radians) * speed;
            this.X += deltaX;
            this.Y += deltaY;
        }
    }

    /**
     * Check the projectile to make sure new movement is not out of bounds or hitting the terrain.
     * @param app
     * @return
     */
    public boolean checkRemove(App app) {
       // SYstem.out.println("The projectile path at X: " + this.X + " and Y: " + this.Y);
        if (this.X < 0 || this.Y < 0 || this.X > 864 || this.Y > 640) {
            return true;
        }

        for (Map.Entry<Float, Float> lineCoord : app.coordinates.entrySet()) {
            float lineX = lineCoord.getKey();
            float lineY = lineCoord.getValue();

            if ((double)Math.floor(this.X) == (double)Math.floor(lineX)){
                if ((double) this.Y >= (double) lineY){
                    willExplode = true;
                    this.explodeCoordinate = new int[]{(int)this.X, (int)this.Y};
                    return true;
                }
            }
        }

        return false;
    }
}
