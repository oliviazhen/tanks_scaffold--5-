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

    public static double multiplyPixelSize(double value){
        return value * App.CELLSIZE;
    }

    public static int random(int min, int max){
        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return random_int;
    }

    public static void main(String[] args){
        System.out.println(random(-35, 35));
    }
    
    
}
