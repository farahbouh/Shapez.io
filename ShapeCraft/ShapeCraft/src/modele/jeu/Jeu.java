package modele.jeu;

import modele.plateau.Mine;
import modele.plateau.Plateau;
import modele.plateau.Poubelle;
import modele.plateau.Tapis;
import modele.item.ItemShape;

public class Jeu extends Thread{
    private Plateau plateau;




    public Jeu() {
        plateau = new Plateau();

        plateau.setMachine(5, 10, new Mine());
        plateau.setMachine(5, 5, new Poubelle());

        //attention si on met '-' ou une autre couleur sa marche pas
        plateau.setGisement(5,11,"CyCrCbCr");
        plateau.setGisement(5,10,"CbCbCbCb");

        start();

    }

    public Plateau getPlateau() {
        return plateau;
    }


    public void press(int x, int y) {

        plateau.setMachine(x, y, new Tapis());
    }

    public void slide(int x, int y) {
        plateau.setMachine(x, y, new Tapis());
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
