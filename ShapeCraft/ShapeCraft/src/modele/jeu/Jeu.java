package modele.jeu;

import modele.plateau.*;

public class Jeu extends Thread{
    private Plateau plateau;
    private Direction directionactuelle = Direction.North; // par défaut la direction est en haut




    public Jeu() {
        plateau = new Plateau();

        plateau.setMachine(5, 10, new Mine());
        plateau.setHub(6,5,new HubMain());

        //attention si on met '-' ou une autre couleur sa marche pas
        plateau.setGisement(5,11,"Cy--CbCr");
        plateau.setGisement(5,10,"CbCbCbCb");

        start();

    }

    public Plateau getPlateau() {
        return plateau;
    }


    public void press(int x, int y) {
        plateau.setMachine(x, y, new Tapis(),directionactuelle);
    }
    public void press_r(){ //si R est pressé alors on change la direction
        directionactuelle = directionactuelle.next();
        System.out.println(directionactuelle);
    }


    public void slide(int x, int y) {
        plateau.setMachine(x, y, new Tapis(),directionactuelle);
    }


    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {

        while(true) {
            try {
                plateau.run();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }


}
