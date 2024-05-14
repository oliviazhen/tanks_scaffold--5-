package Tanks;

public class Wind {
    private int windForce;
    /**
     * Constructor. Initialises the wind object to a random value within the bounds of 5.
     */
    public Wind() {
        this.windForce = UsefulFunctions.random(-35, 35);
    }

    /**
     * Changes the wind force to a random value within the bounds of 5.
     */
    public void changeWindForce() {
        this.windForce += UsefulFunctions.random(-5, 5);
    }

    /**
     * Getter for the wind force
     * @return
     */
    public int getWindForce() {
        return this.windForce;
    }

    /**
     * Accelerate for the windforce
     * @return
     */
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
