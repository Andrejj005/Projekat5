    public class Player extends GameObject {
        private String name;
        private int health;

        public Player(String name, int health, int x, int y, Collidable kolajder) {
            super(x, y, kolajder);
            setName(name);
            setHealth(health);
        }

        public void setName(String name) {
            if (name == null || name.trim().isEmpty())
                throw new IllegalArgumentException("Ime ne smije biti prazno");
            StringBuilder sb = new StringBuilder();
            for (String w : name.trim().split("\\s+")) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
            }
            this.name = sb.toString();
        }

        public void setHealth(int health) {
            if (health < 0 || health > 100)
                throw new IllegalArgumentException("Health mora biti u opsegu [0, 100]");
            this.health = health;
        }

        public String getName() { return name; }
        public int getHealth() { return health; }
        @Override public String getDisplayName() { return name; }
        @Override public String toString() {
            return "Player[" + name + "] @ (" + x + "," + y + ") HP=" + health;
        }
}

