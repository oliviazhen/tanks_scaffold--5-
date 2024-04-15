package Tanks;

import java.util.List;
import java.util.Map;

public class GameData {
    private List<Level> levels;
    private Map<String, String> player_colours;

    // getters and setters
    public List<Level> getLevels(){
        return levels;
    }

    public Map<String, String> getPlayerColours(){
        return player_colours;
    }
}

class Level {
    private String layout;
    private String background;
    private String foregroundColour;
    private String trees; // this field is optional

    // getters and setters
    public String getLayout(){
        return layout;
    }

    public String getBackground(){
        return background;
    }

    public String getForegroundColour(){
        return foregroundColour;
    }

    public String getTrees(){
        return trees;
    }

}