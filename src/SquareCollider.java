public class SquareCollider implements Collidable {
    private final int sirina;
    public SquareCollider(int sirina) {
        if (sirina <= 0) throw new IllegalArgumentException("Sirina mora biti > 0");
        this.sirina = sirina;
    }
    @Override public int getWidth() { return sirina; }
    @Override public int getHeight() { return sirina; }
    @Override public String toString() { return "Square " + sirina + "x" + sirina; }
}
