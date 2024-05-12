package Tanks;

import java.util.Arrays;

public class Wind {
    private int windForce;
    // Constructor
    public Wind() {
        this.windForce = UsefulFunctions.random(-35, 35);
    }

    public void changeWindForce() {
        this.windForce += UsefulFunctions.random(-5, 5);
    }

    public int getWindForce() {
        return this.windForce;
    }

    public double accelerate() {
        return this.windForce * 0.03;
    }

    @Override
    public String toString() {
        return "Wind{" +
                " force=" + this.windForce +
                '}';
    }
}
