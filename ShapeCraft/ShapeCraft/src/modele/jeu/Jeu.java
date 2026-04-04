package modele.jeu;

import modele.plateau.*;

public class Jeu extends Thread {
    private Plateau plateau;
    private Direction directionactuelle = Direction.North;

    public Jeu() {
        plateau = new Plateau();
        int ligneBas = Plateau.SIZE_Y - 1; // dernière ligne (15 si SIZE_Y=16)

        // === 1. Mines et gisements sur toute la ligne du bas ===
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            plateau.setMachine(x, ligneBas, new Mine());
            String code;
            int choix = x % 6;
            switch (choix) {
                case 0: code = "C-C-CbCy"; break;
                case 1: code = "SpSpSpSp"; break;
                case 2: code = "SwScSwSc"; break;
                case 3: code = "CbCrCgCy"; break;
                case 4: code = "SySgSbSr"; break;
                default: code = "-r-g-b-c"; break;
            }
            plateau.setGisement(x, ligneBas, code);
        }

        // === 2. Machines pour tester ===
        plateau.setDecoupeuse(5, 10, Direction.North, 2);
        plateau.setDecoupeuse(10, 10, Direction.North, 1);
        plateau.setBalancer(10, 5, Direction.North);
        // Ajout d'une découpeuse type 1 (2 sorties) juste après le balancer (au-dessus)
        plateau.setGisement(7,8,"r");
        plateau.setMachine(7,8,new Mine());
        plateau.setMixer(8,8,Direction.North);
        plateau.setDecoupeuse(10, 3, Direction.North, 1);
        plateau.setDecoupeuse(3, 8, Direction.North, 1);

        // Poubelle en (0,0)
        plateau.setMachine(0, 0, new Poubelle());
        for (int y = ligneBas-1; y >= 1; y--) {
            if (plateau.getCases()[0][y].getMachine() == null)
                plateau.setTapis(0, y, Direction.South, Direction.North);
        }

        start();
    }

    public Plateau getPlateau() { return plateau; }

    public void press(int x, int y) {
        if (plateau.getCases()[x][y].getMachine() == null)
            plateau.setMachine(x, y, new Tapis(), directionactuelle);
        else
            System.out.println("Case occupée");
    }

    public void press_r() {
        directionactuelle = directionactuelle.next();
        System.out.println(directionactuelle);
    }

    public void slide(int x, int y) { press(x, y); }

    public void slideAvecDirection(int x, int y, Direction d) {
        Case caseCourante = plateau.getCases()[x][y];
        Case c = plateau.getCase(caseCourante, d.inverse());
        if (c != null && c.getMachine() instanceof Tapis) {
            c.getMachine().setD(d);
            Tapis t = (Tapis) c.getMachine();
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