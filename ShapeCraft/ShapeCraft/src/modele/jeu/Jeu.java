package modele.jeu;

import modele.plateau.*;

public class Jeu extends Thread {
    private Plateau plateau;
    private Niveau niveau;
    private Direction directionactuelle = Direction.North;
    private String machineSelectionnee = "Tapis";

    public void setMachineSelectionnee(String nom) {
        this.machineSelectionnee = nom;
    }

    public Jeu() {
        plateau = new Plateau();
        niveau = new Niveau(plateau);
        niveau.niveaux(1); // premier niveau
        start();
    }

    public Plateau getPlateau() { return plateau; }

    public void press(int x, int y) {
        if (plateau.getCases()[x][y].getMachine() == null) {
            switch (machineSelectionnee) {
                case "Mine":       plateau.setMachine(x, y, new Mine()); break;
                case "Poubelle":   plateau.setMachine(x, y, new Poubelle()); break;
                case "Decoupeuse": plateau.setDecoupeuse(x, y, directionactuelle, 1); break;
                case "Decoupeuse2":  plateau.setDecoupeuse(x, y, directionactuelle, 2); break;
                case "Rotater":      plateau.setRotater(x, y, directionactuelle,1); break;
                case "Rotater2":plateau.setRotater(x, y, directionactuelle,2); break;
                case "Balancer":     plateau.setBalancer(x, y, directionactuelle); break;
                case "Mixer":        plateau.setMixer(x, y, directionactuelle); break;
                case "Painter1" :plateau.setPainter(x,y,directionactuelle); break;
                default:           plateau.setMachine(x, y, new Tapis(), directionactuelle); break;
            }
        }
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

                if (niveau.gagner()) {
                    System.out.println("Niveau terminé !");
                    niveau.niveaux(niveau.getNiveauActuel() + 1);
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}