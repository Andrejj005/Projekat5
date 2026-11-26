public class MeleeEnemy extends Enemy {
    public MeleeEnemy(String type, int damage, int health, int x, int y, Collidable k) {
        super(type, damage, health, x, y, k);
    }
    @Override public int attack() { return damage; }
    @Override public String toString() {
        return "MeleeEnemy[" + type + "] @ (" + x + "," + y + ") DMG=" + damage + " HP=" + health;
    }
}
