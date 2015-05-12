package se.killergameab.phonster;

public class Player {
    private int life;

    public Player() {
        life = 100;
    }

    public int lifeLeft(int accuracy) {
        life = life - accuracy;

        return life;
    }
}
