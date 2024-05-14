package Tanks;

public class UsefulFunctions {

    /**
     * Parses in a string to convert it into an int[] of RBG values
     * @param RBG
     * @return
     */
    public static int[] RBGToArray(String RBG) {

        String[] ls = RBG.split(",");
        int r = Integer.parseInt(ls[0]);
        int g = Integer.parseInt(ls[1]);
        int b = Integer.parseInt(ls[2]);

        int[] int_colours = {r,g,b};
        return int_colours;
    }
    /**
     * More readable version of the random converter to allow eay input of bounds (inclusive)
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max){
        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return random_int;
    }
}
