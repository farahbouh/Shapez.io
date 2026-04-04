/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;



import modele.item.ItemColor;
import modele.item.ItemShape;

import java.util.HashMap;
import java.util.Observable;


public class Plateau extends Observable implements Runnable {

    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 16;


    private HashMap<Case, Point> map = new HashMap<Case, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées

    public Plateau() {
        initPlateauVide();
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    //return case d'a côté
    public Case getCase(Case source, Direction d) {

        Point p = map.get(source);
        return caseALaPosition(new Point(p.x+d.dx, p.y+d.dy));


    }

    private void initPlateauVide() {

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }

        }

    }
    //pour poser des machines sur le plateau
    public void setMachine(int x, int y, Machine m) {
        Point p = new Point(x, y);
        if(caseALaPosition(p).getMachine() == null) {
            grilleCases[x][y].setMachine(m);
            setChanged();
            notifyObservers();
        }
    }
    public void setMachine(int x, int y, Machine m, Direction d) {
        Point p = new Point(x, y);
        if(caseALaPosition(p).getMachine() == null) {
            grilleCases[x][y].setMachine(m,d);
            setChanged();
            notifyObservers();
        }
    }
    public void setTapis(int x, int y,Direction d1,Direction d2) {
        Point p = new Point(x, y);

        if(caseALaPosition(p).getMachine() == null) {
            Tapis tapis = new Tapis(d1);
            grilleCases[x][y].setMachine(tapis,d2);
            setChanged();
            notifyObservers();
        }
    }

    public void setHub(int x, int y, HubMain hm) {
        boolean peut_etre_construit = true;
        int taille =  3;
        // on verifie qu'il y a rien autour avant de poser
        for (int i = 0; i < taille; i++) {
            for (int z = 0; z < taille; z++) {
                int posX = x + (i - 1);
                int posY = y + (z - 1);

                // On vérifie qu'on ne dépasse pas la taille de la grille
                if (posX < 0 || posX >= SIZE_X || posY < 0 || posY >= SIZE_Y) {
                    peut_etre_construit = false;
                    break;
                }

                // On vérifie si une machine existe déjà sur chaque case
                if (grilleCases[posX][posY].getMachine() != null) {
                    peut_etre_construit = false;
                }
            }
        }

        // on construit le hub
        if (peut_etre_construit) {
            for (int i = 0; i < taille; i++) {
                for (int z = 0; z < taille; z++) {
                    int posX = x + (i - 1);
                    int posY = y + (z - 1);

                    if (i == 1 && z == 1) {
                        // position central ou on place le hubmain
                        grilleCases[posX][posY].setMachine(hm);
                        setChanged();
                        notifyObservers();
                    } else {
                        // On place tout autour des hubworker
                        HubWorker hw = new HubWorker(hm);
                        grilleCases[posX][posY].setMachine(hw);
                        setChanged();
                        notifyObservers();
                    }
                }
            }

        }
    }
    //pour poser des gisement sur le plateau
// le string est de 8lettre pour les 4petites formes de chaque côté + leurs couleurs
    public void setGisement(int x, int y, String code) {

        grilleCases[x][y].setGisement(code);
        setChanged();
        notifyObservers();

    }

    public void setBalancer(int x, int y, Direction orientation) {
        int x2 = x, y2 = y;

        // Déterminer la deuxième case (le Balancer occupe 2 cases)
        if (orientation == Direction.North || orientation == Direction.South) {
            // Orientation Nord ou Sud : les cases sont alignées horizontalement
            x2 = x + 1;
            y2 = y;
        } else if (orientation == Direction.East || orientation == Direction.West) {
            // Orientation Est ou Ouest : les cases sont alignées verticalement
            x2 = x;
            y2 = y + 1;
        }

        if (x2 >= 0 && x2 < SIZE_X && y2 >= 0 && y2 < SIZE_Y &&
                grilleCases[x][y].getMachine() == null && grilleCases[x2][y2].getMachine() == null) {

            Balancer balancer = new Balancer(orientation);
            balancer.setD(orientation);

            // Ajouter les deux cases au Balancer
            balancer.addCase(grilleCases[x][y]);
            balancer.addCase(grilleCases[x2][y2]);

            grilleCases[x][y].setMachine(balancer);
            grilleCases[x2][y2].setMachine(balancer);
        }

        setChanged();
        notifyObservers();
    }
    public void setDecoupeuse(int x, int y, Direction orientation, int type) {
        boolean autoriser = true;
        Point p0[] = new Point[4];
        p0[0] = new Point(x, y);
        if(caseALaPosition(p0[0]).getMachine() != null) autoriser = false;
        for(int i = 1;i<type*2;i++){
            if (orientation == Direction.East){
                p0[i] = new Point(x,y-i); //on met à la vertical
                if(caseALaPosition(p0[i]).getMachine() != null) autoriser = false;
            }
            if (orientation == Direction.West){
                p0[i] = new Point(x,y+i); //on met à la vertical
                if(caseALaPosition(p0[i]).getMachine() != null) autoriser = false;
            }
            if (orientation == Direction.North){
                p0[i] = new Point(x+i,y); //on met à l'horizontal
                if(caseALaPosition(p0[i]).getMachine() != null) autoriser = false;

            }
            if (orientation == Direction.South){
                p0[i] = new Point(x-i,y); //on met à l'horizontal
                if(caseALaPosition(p0[i]).getMachine() != null) autoriser = false;
            }
        }


for(int i = 0;i<type*2;i++){
        if(autoriser) {
                if(i==0){
                    Decoupeuse d = new Decoupeuse(type);
                    grilleCases[x][y].setMachine(d,orientation);
                    setChanged();
                    notifyObservers();
                } else{
                    DecoupeuseWorker w = new DecoupeuseWorker();
                    grilleCases[p0[i].x][p0[i].y].setMachine(w,orientation);
                    setChanged();
                    notifyObservers();
                }




        }}

    }
    public void setMixer(int x, int y, Direction orientation){
        Point p = new Point(x, y);
        Point p2 = new Point(x,y,orientation.next());


        if(caseALaPosition(p).getMachine() == null && caseALaPosition(p2).getMachine() == null) {
            Mixer m = new Mixer();
            MixerWorker mw = new MixerWorker(m);
            mw.setD(orientation);
            grilleCases[x][y].setMachine(m,orientation);
            grilleCases[p2.x][p2.y].setMachine(mw,orientation);
            setChanged();
            notifyObservers();
        }

    }
    /**
     * Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Case caseALaPosition(Point p) {
        Case retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }


    @Override
    public void run() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Case c = grilleCases[x][y];
                if (c.getMachine() != null) {
                    c.getMachine().run2();
                }
            }
        }
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Case c = grilleCases[x][y];
                if (c.getMachine() != null) {
                    c.getMachine().run();
                }
            }
        }
        setChanged();
        notifyObservers();
    }

}