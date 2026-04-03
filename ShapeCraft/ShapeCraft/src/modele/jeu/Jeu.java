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

        // === 3. Chemins de tapis ===

        // Pour la découpeuse (5,8) orientée Est : elle reçoit par l'Ouest.
        // On place un tapis vertical sur la colonne 5 de y=14 à y=8 (entrée Sud, sortie Nord)
        for (int y = ligneBas-1; y >= 8; y--) {
            if (plateau.getCases()[5][y].getMachine() == null)
                plateau.setTapis(5, y, Direction.South, Direction.North);
        }
        // La découpeuse occupe (5,8) et (6,8) ; le tapis en (5,8) alimente directement la machine.

        // Pour la découpeuse (8,5) orientée Sud : elle reçoit par le Nord.
        // Tapis vertical colonne 8 de y=14 à y=5 (montant)
        for (int y = ligneBas-1; y >= 5; y--) {
            if (plateau.getCases()[8][y].getMachine() == null)
                plateau.setTapis(8, y, Direction.South, Direction.North);
        }
        // Il faut que l'item arrive par le Nord de (8,5) : donc un tapis en (8,4) pointant vers le Sud.
        if (plateau.getCases()[8][4].getMachine() == null)
            plateau.setTapis(8, 4, Direction.North, Direction.South);

        // Pour la découpeuse (2,6) orientée Ouest : elle reçoit par l'Est.
        // Tapis vertical colonne 2 de y=14 à y=6
        for (int y = ligneBas-1; y >= 6; y--) {
            if (plateau.getCases()[2][y].getMachine() == null)
                plateau.setTapis(2, y, Direction.South, Direction.North);
        }
        // Pour venir de l'Est, on place un tapis horizontal en (3,6) pointant vers l'Ouest.
        if (plateau.getCases()[3][6].getMachine() == null)
            plateau.setTapis(3, 6, Direction.East, Direction.West);
        // Il faut aussi que la colonne 3 ait des tapis montants jusqu'à y=6 (pour amener l'item depuis sa mine)
        for (int y = ligneBas-1; y >= 6; y--) {
            if (plateau.getCases()[3][y].getMachine() == null)
                plateau.setTapis(3, y, Direction.South, Direction.North);
        }

        // Pour le balancer (10,4) orienté Nord : il reçoit par le Sud.
        // Tapis vertical colonne 10 de y=14 à y=4
        for (int y = ligneBas-1; y >= 4; y--) {
            if (plateau.getCases()[10][y].getMachine() == null)
                plateau.setTapis(10, y, Direction.South, Direction.North);
        }

        // Pour le balancer (3,9) orienté Est : il reçoit par l'Ouest.
        // Tapis vertical colonne 3 jusqu'à y=9 (déjà fait dans la boucle précédente pour colonne 3)
        // Il manque un tapis horizontal en (2,9) pointant vers l'Est.
        if (plateau.getCases()[2][9].getMachine() == null)
            plateau.setTapis(2, 9, Direction.West, Direction.East);
        // Il faut aussi que la colonne 2 ait des tapis jusqu'à y=9 (déjà fait pour colonne 2 jusqu'à y=6, on étend)
        for (int y = 8; y >= 9; y--) { // de 8 à 9 (mais y décroissant, mieux : faire une boucle de 14 à 9)
            // On va simplement étendre la boucle précédente pour colonne 2 jusqu'à 9
        }
        // Plus simple : on refait une boucle pour colonne 2 de 14 à 9 (sans écraser)
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