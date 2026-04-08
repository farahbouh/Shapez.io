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
        plateau.setGisement(1, 1, "S-S-S-S-");
        plateau.setGisement(2, 2, "r");   // Rouge
        plateau.setGisement(2, 10, "c");  // Cyan
        // tapis shape vers painter

        plateau.setPainter(4, 1, Direction.North);

        hub = new HubMain();
        plateau.setHub(5, 1, hub);
        hub.ajouterdefis(new String[]{"S-S-S-S-"}, new int[]{3});
    }

    public void niveau2() {
        System.out.print("niveau2");
        plateau.setGisement(1, 10, "SbSbSbSb"); // 10 moitiés de cercle

        // collecter 10 moitiés de cercle
        hub.ajouterdefis(new String[]{"S-S-----"}, new int[]{10});

    }
    public void niveau3() {
        System.out.print("niveau3");
        plateau.setGisement(10, 10, "SbSbSbSb"); // 10 moitiés de cercle
        plateau.setGisement(12, 10, "SbSbSbSb"); // 10 moitiés de cercle
        plateau.setGisement(12, 11, "SbSbSbSb"); // 10 moitiés de cercle
        plateau.setGisement(2, 2, "r");   // Rouge
        plateau.setGisement(2, 4, "b");   // Bleu

        // Défi : collecter 6 moitiés de cercle correctement découpées et orientées
        hub.ajouterdefis(new String[]{"S-"}, new int[]{6});

    }
    public void niveau4() {
        System.out.print("niveau4");
        plateau.setGisement(1, 10, "SbSbSbSb"); // 10 moitiés de cercle

        // Défi : collecter 6 moitiés de cercle correctement découpées et orientées
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