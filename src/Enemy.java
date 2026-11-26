public abstract class Enemy extends GameObject implements Attacker {
    protected final String type;
    protected final int damage;
    protected int health;

    protected Enemy(String type, int damage, int health, int x, int y, Collidable kolajder) {
        super(x, y, kolajder);
        if (type == null || type.trim().isEmpty())
            throw new IllegalArgumentException("Tip ne smije biti prazan");
        if (damage < 0) throw new IllegalArgumentException("Damage ne smije biti negativan");
        if (health < 0 || health > 100)
            throw new IllegalArgumentException("Health mora biti u opsegu [0, 100]");
        this.type = type.trim();
        this.damage = damage;
        this.health = health;
    }

    public String getType() { return type; }
    public int getDamage() { return damage; }
    public int getHealth() { return health; }
    @Override public String getDisplayName() { return type; }
}
