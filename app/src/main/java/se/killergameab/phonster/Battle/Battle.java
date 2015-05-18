package se.killergameab.phonster.Battle;

/**
 * A battle between a player and monster, keeps track of the turn, and other information.
 */
public class Battle {

    private Player player;
    private Monster monster;

    public Battle(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
    }

    public Player getPlayer() {
        return player;
    }

    public Monster getMonster() {
        return monster;
    }

    public void attack(int accuracy, int progress) {
        int playerAttack = accuracy * progress;
        int monsterAttack = monster.getExperience() / player.getExperience();

        monster.takeDamage(playerAttack);

        if (monster.getLife() > 0) {
            player.takeDamage(monsterAttack);
        }
    }
}
