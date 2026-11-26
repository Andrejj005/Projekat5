public class BossEnemy extends Enemy {
    public BossEnemy(String type, int damage, int health, int x, int y, Collidable k) {
        super(type, damage, health, x, y, k);
    }
    @Override public int attack() { return damage * 2; }
    @Override public String toString() {
        return "BossEnemy[" + type + "] @ (" + x + "," + y + ") DMG=" + damage + "x2 HP=" + health;
    }
}
