package se.killergameab.phonster.Battle;

public class Player {

    private int life;
    private int experience = 1;

    public Player() {
        life = 100;
    }

    public void takeDamage(int damage) {
        life -= damage;
    }

    public int getLife() {
        return life;
    }

    public int getExperience() {
        return experience;
    }

    public void setLife(int nbrLives) { life = nbrLives; }
}
