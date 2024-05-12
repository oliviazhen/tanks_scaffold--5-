package Tanks;

import java.util.Map;

public class Projectile{
    int radius = 30;
    int speed = 60; 

    private Turret fromT;
    private float x;
    private float y;
    private double angle;
    private Wind wind;

    public boolean willExplode;
    public int[] explodeCoordinate;


    public Projectile(Turret fromT, float x, float y, int angle){
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.fromT = fromT;

    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void setWind(Wind wind){
        this.wind = wind;
    }

    public void hitTarget(){

    }

    public void display(App app) {
        if (willExplode) {
            int[] colors = {app.color(255, 0, 0), app.color(255, 165, 0), app.color(255, 255, 0)};
            float[] radii = {(float) this.radius, (float)0.5 * this.radius, (float)0.2 * this.radius}; 
            
            for (int i = 0; i < colors.length; i++) {
                app.fill(colors[i]);
                
                float maxRadius = radii[i];
                
                for (long j = 0; j < 200; j += 1) {
                    float progress = (float)(j) / 200;
                    float currentRadius = progress * maxRadius;
                    app.ellipse(explodeCoordinate[0], explodeCoordinate[1], currentRadius * 2, currentRadius * 2);
                }
            }

        } else {
            app.fill(204, 102, 0);
            app.ellipse((float)x, (float)y, 5, 5);
        }
    }

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
            this.y -= speed;
        }
        else if (angle < 0){
            double radians = Math.toRadians(angle + 90);
            double deltaX = Math.cos(radians) * speed;
            double deltaY = Math.sin(radians) * speed;
            this.x -= deltaX;
            this.y -= deltaY;
        }
        else{
            double radians = Math.toRadians(angle - 90);
            double deltaX = Math.cos(radians) * speed;
            double deltaY = Math.sin(radians) * speed;
            this.x += deltaX;
            this.y += deltaY;
        }
    }

    public boolean checkRemove(App app) {
       // System.out.println("The projectile path at X: " + this.x + " and Y: " + this.y);
        if (this.x < 0 || this.y < 0 || this.x > 864 || this.y > 640) {
            return true;
        }

        for (Map.Entry<Float, Float> lineCoord : app.coordinates.entrySet()) {
            float lineX = lineCoord.getKey();
            float lineY = lineCoord.getValue();

            if ((double)Math.floor(this.x) == (double)Math.floor(lineX)){
                if ((double) this.y >= (double) lineY){
                    willExplode = true;
                    this.explodeCoordinate = new int[]{(int)this.x, (int)this.y};
                    return true;
                }
            }
        }

        return false;
    }
}
