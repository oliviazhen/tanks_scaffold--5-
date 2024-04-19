package Tanks;

public class TankCharacter {

    private int[] colours = new int[3];
    private int row;
    private int column;

    public TankCharacter(int[] colours){
        this.colours = colours;
    
    }

    public String toString(){
        return "R: " + colours[0] + " B: " + colours[1] + " G: " + colours[2];
    }

    //TODO: create setters and getters for each attribute

    public void draw(App app){

        app.fill(colours[0], colours[1], colours[2]); // Blue color
        
        // Draw the bottom rectangle at (100, 100) with width 150 and height 100
        app.rect(100, 100, 150, 100);
        
        app.fill(colours[0], colours[1], colours[2]); // Blue color
        
        // Calculate the position of the top rectangle to be directly above the bottom one
        float topRectX = 100 + (150 - 100) / 2; // X-coordinate
        float topRectY = 100 - 50; // Y-coordinate, shifted up by the height of the top rectangle
        
        // Draw the top rectangle at the calculated position with width 100 and height 50
        app.rect(topRectX, topRectY, 100, 50);

    }
    
}
