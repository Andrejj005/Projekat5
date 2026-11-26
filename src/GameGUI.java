import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/** GUI ne zna logiku igre - samo prikuplja unos i poziva model / Game. */
public class GameGUI extends JFrame {
    private final JTextField imeF = new JTextField("bobo smrad");
    private final JTextField healthF = new JTextField("90");
    private final JTextField xF = new JTextField("12");
    private final JTextField yF = new JTextField("9");
    private final JComboBox<String> kolajderC =
            new JComboBox<>(new String[]{"Pravougaonik 40x24", "Pravougaonik 32x32", "Kvadrat 32x32"});
    private final JTextArea rezultat = new JTextArea();
    private Game game;

    public GameGUI() {
        super("Game Setup");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        kolajderC.setSelectedIndex(2);
        rezultat.setEditable(false);
        rezultat.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JPanel forma = new JPanel(new GridLayout(0, 2, 5, 8));
        forma.setBorder(BorderFactory.createTitledBorder("Igrač"));
        forma.add(new JLabel("Ime:"));               forma.add(imeF);
        forma.add(new JLabel("Health (0-100):"));    forma.add(healthF);
        forma.add(new JLabel("X pozicija:"));        forma.add(xF);
        forma.add(new JLabel("Y pozicija:"));        forma.add(yF);
        forma.add(new JLabel("Kolajder:"));          forma.add(kolajderC);

        JButton pokreni = new JButton("Pokreni igru");
        JButton sacuvaj = new JButton("Sačuvaj");
        pokreni.addActionListener(e -> pokreni());
        sacuvaj.addActionListener(e -> sacuvaj());

        JPanel dugmad = new JPanel(new GridLayout(2, 1, 5, 5));
        dugmad.add(pokreni);
        dugmad.add(sacuvaj);

        JPanel lijevo = new JPanel(new BorderLayout(5, 10));
        lijevo.add(forma, BorderLayout.NORTH);
        lijevo.add(dugmad, BorderLayout.SOUTH);

        JScrollPane sp = new JScrollPane(rezultat);
        sp.setBorder(BorderFactory.createTitledBorder("Rezultat"));

        JPanel root = new JPanel(new GridLayout(1, 2, 10, 0));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(lijevo);
        root.add(sp);
        setContentPane(root);
        setSize(850, 420);
        setLocationRelativeTo(null);
    }

    private Collidable napraviKolajder() {
        switch (kolajderC.getSelectedIndex()) {
            case 0:  return new RectangleCollider(40, 24);
            case 1:  return new RectangleCollider(32, 32);
            default: return new SquareCollider(32);
        }
    }

    private void pokreni() {
        try {
            Player p = new Player(imeF.getText(), Integer.parseInt(healthF.getText().trim()),
                    Integer.parseInt(xF.getText().trim()), Integer.parseInt(yF.getText().trim()),
                    napraviKolajder());
            game = new Game(p);
            for (Enemy e : Game.loadEnemiesFromFile("src/enemies")) game.addEnemy(e);
            boolean prezivio = game.resolveCollisions();
            prikazi();
            JOptionPane.showMessageDialog(this,
                    prezivio ? "Igrač " + p.getName() + " je preživio sa " + p.getHealth() + " HP!"
                            : "Igrač " + p.getName() + " je poražen!",
                    "Kraj igre", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            greska("X, Y i Health moraju biti cijeli brojevi.");
        } catch (IllegalArgumentException ex) {
            greska(ex.getMessage());
        } catch (java.io.IOException ex) {
            greska("Greška pri čitanju enemies.txt: " + ex.getMessage());
        }
    }

    private void prikazi() {
        StringBuilder sb = new StringBuilder("STATUS: " + game.getPlayer() + "\n\n");
        sb.append("NEPRIJATELJI (").append(game.getEnemies().size()).append("):\n");
        for (Enemy e : game.getEnemies()) sb.append("  ").append(e).append('\n');
        ArrayList<Enemy> col = game.collidingWithPlayer();
        sb.append("\nU KOLIZIJI SA IGRAČEM (").append(col.size()).append("):\n");
        for (Enemy e : col) sb.append("  ").append(e.getDisplayName()).append('\n');
        sb.append("\nLOG:\n");
        for (String s : game.getLog()) sb.append("  ").append(s).append('\n');
        rezultat.setText(sb.toString());
        rezultat.setCaretPosition(0);
    }

    private void sacuvaj() {
        if (game == null) { greska("Prvo pokreni igru."); return; }
        try {
            game.saveToFile("rezultat.txt");
            JOptionPane.showMessageDialog(this, "Sačuvano u " + new File("rezultat.txt").getAbsolutePath());
        } catch (java.io.IOException ex) {
            greska("Greška pri upisu: " + ex.getMessage());
        }
    }

    private void greska(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Greška", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI().setVisible(true));
    }
}
