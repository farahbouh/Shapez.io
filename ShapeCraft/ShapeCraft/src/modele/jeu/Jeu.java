package modele.jeu;

import modele.plateau.*;

public class Jeu extends Thread{
    private Plateau plateau;
    private Direction directionactuelle = Direction.North; // par défaut la direction est en haut




    public Jeu() {
        plateau = new Plateau();


        plateau.setMachine(5, 10, new Mine());
        plateau.setMachine(5, 8, new Decoupeuse(),Direction.North);
        plateau.setHub(6,5,new HubMain());

        // Tapis entrée (pour amener à la découpeuse)
        plateau.setMachine(5, 9, new Tapis(), Direction.North);

        // Decoupeuse (garde celle-ci)
        plateau.setMachine(5, 8, new Decoupeuse(), Direction.North);

        // Tapis sortie 1 (juste au nord)
        plateau.setMachine(5, 7, new Tapis(), Direction.North);

        // Tapis sortie 2 (à côté, vers l'est)
        plateau.setMachine(6, 7, new Tapis(), Direction.North);

        plateau.setGisement(5,11,"Cy--CbCr");
        plateau.setGisement(5,10,"CbCbCbCb");
        plateau.setGisement(5,10,"SySySySy");

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
