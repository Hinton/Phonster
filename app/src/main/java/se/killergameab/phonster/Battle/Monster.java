package se.killergameab.phonster.Battle;


public class Monster {
    private int experience = 1;
    private int life = 100;

    // Create a new monster for a specific zone
    public Monster(int zone) {

        if (zone == -1) {
            zone = 2;
        }

        experience = zone * 10;
    }

    // Get the remaining life of the monster
    public int getLife() {
        return life;
    }

    public int getExperience() {
        return experience;
    }

    public void takeDamage(int damage) {
        life -= damage;
    }
}
