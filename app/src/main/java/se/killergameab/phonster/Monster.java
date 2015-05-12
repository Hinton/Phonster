package se.killergameab.phonster;

public class Monster {
    private int life;

    public Monster() {
        life = 100;
    }

    public int lifeLeft(int accuracy) {
        life = life - accuracy;

        return life;
    }
}
