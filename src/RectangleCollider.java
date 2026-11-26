public class RectangleCollider implements Collidable {
    private final int sirina, visina;
    public RectangleCollider(int sirina, int visina) {
        if (sirina <= 0 || visina <= 0)
            throw new IllegalArgumentException("Sirina i visina moraju biti > 0");
        this.sirina = sirina;
        this.visina = visina;
    }
    @Override public int getWidth() { return sirina; }
    @Override public int getHeight() { return visina; }
    @Override public String toString() { return "Rectangle " + sirina + "x" + visina; }
}
