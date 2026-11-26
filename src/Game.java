import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final List<String> log = new ArrayList<>();

    public Game(Player player) {
        if (player == null) throw new IllegalArgumentException("Igrac ne smije biti null");
        this.player = player;
    }

    public Player getPlayer() { return player; }
    public ArrayList<Enemy> getEnemies() { return enemies; }
    public List<String> getLog() { return log; }

    public void addEnemy(Enemy e) {
        if (e == null) throw new IllegalArgumentException("Neprijatelj ne smije biti null");
        enemies.add(e);
    }

    public boolean checkCollision(Enemy e) { return player.intersects(e); }

    public void decreaseHealth(int amount) {
        player.setHealth(Math.max(0, player.getHealth() - amount));
    }

    public ArrayList<Enemy> findByType(String type) {
        ArrayList<Enemy> res = new ArrayList<>();
        for (Enemy e : enemies) if (e.getType().equalsIgnoreCase(type)) res.add(e);
        return res;
    }

    public ArrayList<Enemy> collidingWithPlayer() {
        ArrayList<Enemy> res = new ArrayList<>();
        for (Enemy e : enemies) if (checkCollision(e)) res.add(e);
        return res;
    }

    public boolean resolveCollisions() {
        for (Enemy e : collidingWithPlayer()) {
            int dmg = e.attack();
            decreaseHealth(dmg);
            log.add(e.getType() + " napada za " + dmg + " -> HP igraca: " + player.getHealth());
            if (player.getHealth() == 0) {
                log.add("Igrac " + player.getName() + " je poražen!");
                return false;
            }
        }
        log.add("Igrac " + player.getName() + " je preživio sa " + player.getHealth() + " HP!");
        return true;
    }

    public static ArrayList<Enemy> loadEnemiesFromFile(String path) throws IOException {
        ArrayList<Enemy> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            int n = 1;
            while ((line = br.readLine()) != null) {
                n++;
                if (line.trim().isEmpty()) continue;
                try {
                    list.add(parseEnemy(line));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    throw new IllegalArgumentException("Neispravna linija " + n + ": " + line, ex);
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Linija " + n + ": " + ex.getMessage(), ex);
                }
            }
        }
        return list;
    }

    private static Enemy parseEnemy(String line) {
        String[] p = line.split(",");
        for (int i = 0; i < p.length; i++) p[i] = p[i].trim();
        String type = p[0], klasa = p[1].toLowerCase();
        int damage = Integer.parseInt(p[2]), health = Integer.parseInt(p[3]);
        int x = Integer.parseInt(p[4]), y = Integer.parseInt(p[5]);
        String oblik = p[6].toLowerCase();
        Collidable c;
        if (oblik.equals("rectangle")) c = new RectangleCollider(Integer.parseInt(p[7]), Integer.parseInt(p[8]));
        else if (oblik.equals("square")) c = new SquareCollider(Integer.parseInt(p[7]));
        else throw new IllegalArgumentException("Nepoznat oblik: " + p[6]);
        switch (klasa) {
            case "melee": return new MeleeEnemy(type, damage, health, x, y, c);
            case "boss":  return new BossEnemy(type, damage, health, x, y, c);
            default: throw new IllegalArgumentException("Nepoznata klasa: " + p[1]);
        }
    }

    public void saveToFile(String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("STATUS: " + player);
            pw.println();
            pw.println("NEPRIJATELJI (" + enemies.size() + "):");
            for (Enemy e : enemies) pw.println("  " + e);
            ArrayList<Enemy> col = collidingWithPlayer();
            pw.println();
            pw.println("U KOLIZIJI SA IGRAČEM (" + col.size() + "):");
            for (Enemy e : col) pw.println("  " + e.getDisplayName());
            pw.println();
            pw.println("LOG:");
            for (String s : log) pw.println("  " + s);
        }
    }
}
