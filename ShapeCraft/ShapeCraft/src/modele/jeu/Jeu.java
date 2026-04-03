package modele.jeu;

import modele.plateau.*;

public class Jeu extends Thread {
    private Plateau plateau;
    private Direction directionactuelle = Direction.North;

    public Jeu() {
        plateau = new Plateau();

        int ligneBas = Plateau.SIZE_Y - 1; // dernière ligne (y=15)

        // === 1. Mines sur toute la ligne du bas ===
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            plateau.setMachine(x, ligneBas, new Mine());
            String code;
            if (x % 3 == 0) code = "SrSgSbSr";
            else if (x % 3 == 1) code = "SyScSwSy";
            else code = "SpCpSpCp";
            plateau.setGisement(x, ligneBas, code);
        }

        // === 2. Machines ===
        plateau.setDecoupeuse(5, 8, Direction.East, 2);   // type2 (4 sorties) orientée Est
        plateau.setDecoupeuse(8, 5, Direction.South, 1);  // type1 (2 sorties) orientée Sud
        plateau.setDecoupeuse(2, 6, Direction.West, 2);   // type2 orientée Ouest
        plateau.setBalancer(10, 4, Direction.North);      // balancer 2 cases horizontal
        plateau.setBalancer(3, 9, Direction.East);        // balancer 2 cases vertical

        // === 3. Ajout d'une poubelle en (15,0) ===
        // On place la poubelle
        plateau.setMachine(15, 0, new Poubelle());
        // On crée un tapis vertical sur la colonne 15 depuis la mine jusqu'à la case juste en dessous de la poubelle
        // La mine est en (15,15). On met des tapis montants de y=14 à y=1 (entrée Sud, sortie Nord)
        for (int y = ligneBas-1; y >= 1; y--) {
            if (plateau.getCases()[15][y].getMachine() == null)
                plateau.setTapis(15, y, Direction.South, Direction.North);
        }
        // Le tapis en (15,1) envoie vers le Nord, donc vers (15,0) qui est la poubelle.
        // Ainsi, tous les items produits par la mine de la colonne 15 iront directement à la poubelle.

        // === 4. Chemins de tapis pour les autres machines (inchangé) ===
        // Pour la découpeuse (5,8) orientée Est : elle reçoit par l'Ouest.
        for (int y = ligneBas-1; y >= 8; y--) {
            if (plateau.getCases()[5][y].getMachine() == null)
                plateau.setTapis(5, y, Direction.South, Direction.North);
        }

        // Pour la découpeuse (8,5) orientée Sud : elle reçoit par le Nord.
        for (int y = ligneBas-1; y >= 5; y--) {
            if (plateau.getCases()[8][y].getMachine() == null)
                plateau.setTapis(8, y, Direction.South, Direction.North);
        }
        if (plateau.getCases()[8][4].getMachine() == null)
            plateau.setTapis(8, 4, Direction.North, Direction.South);

        // Pour la découpeuse (2,6) orientée Ouest : elle reçoit par l'Est.
        for (int y = ligneBas-1; y >= 6; y--) {
            if (plateau.getCases()[2][y].getMachine() == null)
                plateau.setTapis(2, y, Direction.South, Direction.North);
        }
        if (plateau.getCases()[3][6].getMachine() == null)
            plateau.setTapis(3, 6, Direction.East, Direction.West);
        for (int y = ligneBas-1; y >= 6; y--) {
            if (plateau.getCases()[3][y].getMachine() == null)
                plateau.setTapis(3, y, Direction.South, Direction.North);
        }

        // Pour le balancer (10,4) orienté Nord : il reçoit par le Sud.
        for (int y = ligneBas-1; y >= 4; y--) {
            if (plateau.getCases()[10][y].getMachine() == null)
                plateau.setTapis(10, y, Direction.South, Direction.North);
        }

        // Pour le balancer (3,9) orienté Est : il reçoit par l'Ouest.
        if (plateau.getCases()[2][9].getMachine() == null)
            plateau.setTapis(2, 9, Direction.West, Direction.East);
        for (int y = ligneBas-1; y >= 9; y--) {
            if (plateau.getCases()[2][y].getMachine() == null)
                plateau.setTapis(2, y, Direction.South, Direction.North);
        }

        start();
    }

    public Plateau getPlateau() { return plateau; }

    public void press(int x, int y) {
        if (plateau.getCases()[x][y].getMachine() != null) {
            System.out.println("Case occupée");
            return;
        }
        plateau.setMachine(x, y, new Tapis(), directionactuelle);
    }

    public void press_r() {
        directionactuelle = directionactuelle.next();
        System.out.println(directionactuelle);
    }

    public void slide(int x, int y) {
        plateau.setMachine(x, y, new Tapis(), directionactuelle);
    }

    public void slideAvecDirection(int x, int y, Direction d) {
        Case caseCourante = plateau.getCases()[x][y];
        Case c = plateau.getCase(caseCourante, d.inverse());
        if (c == null) return;
        Machine m = c.getMachine();
        if (m instanceof Tapis) {
            m.setD(d);
            Tapis t = (Tapis) m;
            System.out.println(t.getDirection1() + "" + t.getD());
        }
        plateau.setTapis(x, y, d.inverse(), d);
    }

    public void run() { jouerPartie(); }

    public void jouerPartie() {
        while (true) {
            try {
                plateau.run();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}