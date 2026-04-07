package modele.jeu;

import modele.plateau.*;

public class Jeu extends Thread {
    private Plateau plateau;
    private Direction directionactuelle = Direction.North;
    private String machineSelectionnee = "Tapis";

    public void setMachineSelectionnee(String nom) {
        this.machineSelectionnee = nom;
    }

    public Jeu() {
        plateau = new Plateau();
        int ligneBas = Plateau.SIZE_Y - 1; // dernière ligne (15 si SIZE_Y=16)

        // Mines et gisements sur toute la ligne du bas
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            //plateau.setMachine(x, ligneBas, new Mine());
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
        plateau.setGisement(7,8,"r");
        plateau.setHub(10,10,new HubMain());
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}