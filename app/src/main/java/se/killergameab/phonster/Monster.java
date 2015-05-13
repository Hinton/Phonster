package se.killergameab.phonster;

import android.widget.ImageView;

public class Monster {
    private int XP;
    private int life = 100;

    public Monster(int zone) {

        if (zone == -1) {
            zone = 0;
        }

        XP = zone * 10;
    }

    public int lifeLeft(int accuracy) {
        life = life - accuracy;

        return life;
    }

}
