package assignment4;

import java.util.List;
import java.util.ArrayList;

public class CritterWorld {
    protected static List<Critter> critterList = new ArrayList<>();


    public static boolean findCritter(int x, int y) {
        for (int i = 0; i < critterList.size(); i++) {
            if (critterList.get(i).getX() == x && critterList.get(i).getY() == y) {
                return true;
            }
        }
        return false;
    }

}
