public abstract class GameObject {
    protected int x, y;
    protected Collidable kolajder;

    protected GameObject(int x, int y, Collidable kolajder) {
        if (kolajder == null) throw new IllegalArgumentException("Kolajder ne smije biti null");
        this.x = x;
        this.y = y;
        this.kolajder = kolajder;
    }

    public abstract String getDisplayName();

    public int getX() { return x; }
    public int getY() { return y; }
    public Collidable getKolajder() { return kolajder; }

    public boolean intersects(GameObject o) {
        return x < o.x + o.kolajder.getWidth() && o.x < x + kolajder.getWidth()
                && y < o.y + o.kolajder.getHeight() && o.y < y + kolajder.getHeight();
    }
}
