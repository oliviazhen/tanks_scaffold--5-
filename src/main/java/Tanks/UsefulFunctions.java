package Tanks;

public class UsefulFunctions {

    public static int[] RBGToArray(String RBG) {

        String[] ls = RBG.split(",");
        int r = Integer.parseInt(ls[0]);
        int g = Integer.parseInt(ls[1]);
        int b = Integer.parseInt(ls[2]);

        int[] int_colours = {r,g,b};

        return int_colours;
    }

    
    
}
