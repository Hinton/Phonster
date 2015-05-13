package se.killergameab.phonster.Battle;

/**
 * A battle between a player and monster, keeps track of the turn, and other information.
 */
public class Battle {

    private Player player;
    private Monster monster;
    private int turn;


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
        int playerAttack = accuracy + progress;
        int monsterAttack = monster.getExperience() / player.getExperience();

        player.takeDamage(monsterAttack);
        monster.takeDamage(playerAttack);
    }
}
