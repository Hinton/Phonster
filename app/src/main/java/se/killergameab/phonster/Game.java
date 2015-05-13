package se.killergameab.phonster;

import se.killergameab.phonster.Battle.Battle;
import se.killergameab.phonster.Battle.Monster;
import se.killergameab.phonster.Battle.Player;

/**
 * This is probably not the best method, as singletons can be dangerous.
 * It would probably be better to inject the data somehow, but as this isn't a real project
 * lets just quickly create a static game.
 */
public class Game {

    private static Game instance;

    public static Game instance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private Player player;
    private Battle battle;

    public Game() {
        this.player = new Player();
    }

    public Player getPlayer() {
        return player;
    }

    public Battle getActiveBattle() {
        return battle;
    }

    public void newBattle(Monster monster) {
        battle = new Battle(player, monster);
    }
}
