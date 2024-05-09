package Tanks;

public class Projectile {
    int d = 5;
    int speed = 10; 
    private double x;
    private double y;
    private double angle; // Add angle as an instance variable

    public Projectile(double projectileX, double projectileY, int angle){
        this.x = projectileX;
        this.y = projectileY;
        this.angle = angle;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public void display(App app){
        app.fill(204, 102, 0);
        app.ellipse((float)this.x, (float)this.y , d,d);
    }

    public void move(){
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

    public boolean checkRemove(){
        if (this.x < 0 || this.y < 0){
            return true;
        }
        if ((this.x > 864) || (this.y > 640)){
            return true;
        }
        return false;
    }
 
}
