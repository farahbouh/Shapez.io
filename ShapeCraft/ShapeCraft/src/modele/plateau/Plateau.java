/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;



import modele.item.Item;

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
        Point p = new Point(x, y);

            grilleCases[x][y].setGisement(code);
            setChanged();
            notifyObservers();

    }
    public void setDecoupeuse(int x, int y, Direction orientation, int type) {
        if (type == 1) {
            // Type 1 : 2 sorties, occupe 2 cases en ligne HORIZONTALE
            int x2 = x, y2 = y;

            if (orientation == Direction.North) {
                // Ligne horizontale : case à gauche et case à droite
                x2 = x + 1;
            } else if (orientation == Direction.South) {
                x2 = x + 1;
            } else if (orientation == Direction.East) {
                // Ligne verticale pour orientation Est
                y2 = y + 1;
            } else if (orientation == Direction.West) {
                y2 = y + 1;
            }

            if (x2 >= 0 && x2 < SIZE_X && y2 >= 0 && y2 < SIZE_Y &&
                    grilleCases[x][y].getMachine() == null && grilleCases[x2][y2].getMachine() == null) {

                Decoupeuse decoupeuse = new Decoupeuse(orientation, type);
                decoupeuse.setD(orientation);
                grilleCases[x][y].setMachine(decoupeuse);
                grilleCases[x2][y2].setMachine(decoupeuse);
            }
        }
        else {
            // Type 2 : 4 sorties, occupe 4 cases en ligne selon l'orientation
            // (garde le code existant pour type 2)
            int[][] cases = new int[4][2];
            cases[0][0] = x;
            cases[0][1] = y;

            if (orientation == Direction.North) {
                cases[1][0] = x + 1; cases[1][1] = y;
                cases[2][0] = x + 2; cases[2][1] = y;
                cases[3][0] = x + 3; cases[3][1] = y;
            } else if (orientation == Direction.South) {
                cases[1][0] = x + 1; cases[1][1] = y;
                cases[2][0] = x + 2; cases[2][1] = y;
                cases[3][0] = x + 3; cases[3][1] = y;
            } else if (orientation == Direction.East) {
                cases[1][0] = x; cases[1][1] = y + 1;
                cases[2][0] = x; cases[2][1] = y + 2;
                cases[3][0] = x; cases[3][1] = y + 3;
            } else if (orientation == Direction.West) {
                cases[1][0] = x; cases[1][1] = y + 1;
                cases[2][0] = x; cases[2][1] = y + 2;
                cases[3][0] = x; cases[3][1] = y + 3;
            }

            boolean allFree = true;
            for (int i = 0; i < 4; i++) {
                int cx = cases[i][0];
                int cy = cases[i][1];
                if (cx < 0 || cx >= SIZE_X || cy < 0 || cy >= SIZE_Y ||
                        grilleCases[cx][cy].getMachine() != null) {
                    allFree = false;
                    break;
                }
            }

            if (allFree) {
                Decoupeuse decoupeuse = new Decoupeuse(orientation, type);
                decoupeuse.setD(orientation);
                for (int i = 0; i < 4; i++) {
                    int cx = cases[i][0];
                    int cy = cases[i][1];
                    grilleCases[cx][cy].setMachine(decoupeuse);
                }
            }
        }
        setChanged();
        notifyObservers();
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