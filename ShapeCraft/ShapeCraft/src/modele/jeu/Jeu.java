package modele.jeu;

import modele.plateau.*;

public class Jeu extends Thread{
    private Plateau plateau;
    private Direction directionactuelle = Direction.North; // par défaut la direction est en haut




    public Jeu() {
        plateau = new Plateau();

        //Pour tester Decoupeuse
        plateau.setDecoupeuse(5, 8, Direction.North, 2);
        plateau.setDecoupeuse(10,4,Direction.North,1);
        plateau.setDecoupeuse(10, 8, Direction.North, 1);
        //plateau.setMachine(5, 8, new Decoupeuse(),Direction.North);
        //plateau.setHub(6,4,new HubMain());


        plateau.setMachine(5, 10, new Mine());
        plateau.setMachine(10, 10, new Mine());
        plateau.setGisement(5,10,"SySgSbSr");
        plateau.setGisement(10,10,"SySgSbSr");

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
