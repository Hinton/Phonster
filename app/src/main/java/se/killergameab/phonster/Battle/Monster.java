package se.killergameab.phonster.Battle;


public class Monster {

    private int zone;
    private int experience = 1;
    private int life = 100;

    // Create a new monster for a specific zone
    public Monster(int zone) {

        // Ensure the zone is more than 1, to prevent negative experience.
        zone = Math.max(zone, 1);

        this.zone = zone;

        experience = zone * 10;
    }

    public int getZone() {
        return zone;
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
