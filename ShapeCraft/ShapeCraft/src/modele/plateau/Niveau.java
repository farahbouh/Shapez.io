package modele.plateau;
import modele.item.Item;
import modele.plateau.*;

public class Niveau {
    private int niveauactu = 0;
    private Plateau plateau;
    private HubMain hub;

    public Niveau(Plateau plateau) {
        this.plateau = plateau;
    }
    public int getNiveauActuel() {
        return niveauactu;
    }
    public boolean gagner() {
        return hub.gagner();
    }

    public void niveau1() {

        for (int x = 5; x <= 7; x++) {
            for (int y = 5; y <= 7; y++) {
                plateau.setGisement(x, y, "S-S-S-S-");
            }
        }


        hub = new HubMain();
        plateau.setHub(5, 1, hub);
        hub.ajouterdefis(new String[]{"S-S-S-S-"}, new int[]{3}); //cercle gris x3
    }

    public void niveau2() {
        System.out.print("niveau2");
        hub.ajouterdefis(new String[]{"S-S-----"}, new int[]{3}); //3 moitie droite

    }
    public void niveau3() {
        System.out.print("niveau3");
        plateau.setGisement(2, 2, "r");   // Rouge
        plateau.setGisement(2, 4, "b");   // Bleu

        hub.ajouterdefis(new String[]{"Sp------"}, new int[]{6}); // collecter 6 moitie de cercle

    }
    public void niveau4() {
        hub.ajouterdefis(new String[]{"Sb"}, new int[]{6});

    }

    public void niveaux(int num) {
        switch (num) {
            case 1:
                niveau1();
                break;
            case 2:
                niveau2();
                break;
            case 3:
                niveau3();
                break;
            case 4:
                niveau4();
        }
        niveauactu = num;
    }
}