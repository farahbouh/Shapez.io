/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
import java.util.HashMap;
import java.util.Observable;

public class Plateau extends Observable implements Runnable {

    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 16;

    private HashMap<Case, Point> map = new HashMap<Case, Point>();
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y];

    public Plateau() {
        initPlateauVide();
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    public Case getCase(Case source, Direction d) {
        Point p = map.get(source);
        return caseALaPosition(new Point(p.x + d.dx, p.y + d.dy));
    }

    private void initPlateauVide() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }
        }
    }

    public void setMachine(int x, int y, Machine m) {
        Point p = new Point(x, y);
        Case c = caseALaPosition(p);
        if (c != null && c.getMachine() == null) {  // ← plus de vérification sur gisement
            grilleCases[x][y].setMachine(m);
            setChanged();
            notifyObservers();
        }
    }
    public void setMachine(int x, int y, Machine m, Direction d) {
        Point p = new Point(x, y);
        Case c = caseALaPosition(p);
        if (c != null && c.getMachine() == null && c.getGisement() == null) {
            grilleCases[x][y].setMachine(m, d);
            setChanged();
            notifyObservers();
        }
    }

    public void setTapis(int x, int y, Direction d1, Direction d2) {
        Point p = new Point(x, y);
        Case c = caseALaPosition(p);
        if (c != null && c.getMachine() == null && c.getGisement() == null) {
            Tapis tapis = new Tapis(d1);
            grilleCases[x][y].setMachine(tapis, d2);
            setChanged();
            notifyObservers();
        }
    }

    public void setHub(int x, int y, HubMain hm) {
        boolean peut_etre_construit = true;
        int taille = 3;
        for (int i = 0; i < taille; i++) {
            for (int z = 0; z < taille; z++) {
                int posX = x + (i - 1);
                int posY = y + (z - 1);
                if (posX < 0 || posX >= SIZE_X || posY < 0 || posY >= SIZE_Y) {
                    peut_etre_construit = false;
                    break;
                }
                if (grilleCases[posX][posY].getMachine() != null || grilleCases[posX][posY].getGisement() != null) {
                    peut_etre_construit = false;
                }
            }
        }
        if (peut_etre_construit) {
            for (int i = 0; i < taille; i++) {
                for (int z = 0; z < taille; z++) {
                    int posX = x + (i - 1);
                    int posY = y + (z - 1);
                    if (i == 1 && z == 1) {
                        grilleCases[posX][posY].setMachine(hm);
                        setChanged();
                        notifyObservers();
                    } else {
                        HubWorker hw = new HubWorker(hm);
                        grilleCases[posX][posY].setMachine(hw);
                        setChanged();
                        notifyObservers();
                    }
                }
            }
        }
    }

    public void setGisement(int x, int y, String code) {
        Item item;
        if (code.length() <= 2) {
            char chode = code.charAt(0);
            item = new ItemColor(chode);
        } else {
            item = new ItemShape(code);
        }
        grilleCases[x][y].setGisement(item);
        setChanged();
        notifyObservers();
    }

    public void setBalancer(int x, int y, Direction orientation) {
        Point p0 = new Point(x, y);
        Point p1 = new Point(x, y, orientation.next());
        Case c1 = caseALaPosition(p0);
        Case c2 = caseALaPosition(p1);
        if (c1 != null && c2 != null &&
                c1.getMachine() == null &&
                c2.getMachine() == null) {
            Balancer balancer = new Balancer(orientation);
            BalancerWorker worker = new BalancerWorker(balancer, orientation);
            balancer.setWorker(worker);
            grilleCases[x][y].setMachine(balancer, orientation);
            grilleCases[p1.x][p1.y].setMachine(worker, orientation);
            setChanged();
            notifyObservers();
        }
    }

    public void setDecoupeuse(int x, int y, Direction orientation, int type) {
        boolean autoriser = true;
        Point p0[] = new Point[4];
        p0[0] = new Point(x, y);
        Case c0 = caseALaPosition(p0[0]);
        if (c0 == null || c0.getMachine() != null || c0.getGisement() != null) autoriser = false;
        for (int i = 1; i < type * 2; i++) {
            if (orientation == Direction.East) {
                p0[i] = new Point(x, y + i);
            }
            if (orientation == Direction.West) {
                p0[i] = new Point(x, y - i);
            }
            if (orientation == Direction.North) {
                p0[i] = new Point(x + i, y);
            }
            if (orientation == Direction.South) {
                p0[i] = new Point(x - i, y);
            }
            Case c = caseALaPosition(p0[i]);
            if (c == null || c.getMachine() != null || c.getGisement() != null) autoriser = false;
        }
        if (autoriser) {
            Decoupeuse d = new Decoupeuse(type);
            grilleCases[x][y].setMachine(d, orientation);
            setChanged();
            notifyObservers();
            for (int i = 1; i < type * 2; i++) {
                DecoupeuseWorker w = new DecoupeuseWorker(d, i);
                grilleCases[p0[i].x][p0[i].y].setMachine(w, orientation);
                setChanged();
                notifyObservers();
            }
        }
    }

    public void setMixer(int x, int y, Direction orientation) {
        Point p = new Point(x, y);
        Point p2 = new Point(x, y, orientation.next());
        Case c1 = caseALaPosition(p);
        Case c2 = caseALaPosition(p2);
        if (c1 != null && c2 != null &&
                c1.getMachine() == null &&
                c2.getMachine() == null) {
            Mixer m = new Mixer(orientation);
            MixerWorker worker = new MixerWorker(m, orientation);
            m.setWorker(worker);
            grilleCases[x][y].setMachine(m, orientation);
            grilleCases[p2.x][p2.y].setMachine(worker, orientation);
            setChanged();
            notifyObservers();
        }
    }

    public void setRotater(int x, int y, Direction orientation, int type) {
        Point p = new Point(x, y);
        Case c = caseALaPosition(p);
        if (c != null && c.getMachine() == null && c.getGisement() == null) {
            Rotater rotater = new Rotater(type);
            rotater.setD(orientation);
            grilleCases[x][y].setMachine(rotater, orientation);
            setChanged();
            notifyObservers();
        }
    }

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
        for (int x = 0; x < SIZE_X; x++)
            for (int y = 0; y < SIZE_Y; y++)
                if (grilleCases[x][y].getMachine() != null)
                    grilleCases[x][y].getMachine().work();
        for (int x = 0; x < SIZE_X; x++)
            for (int y = 0; y < SIZE_Y; y++)
                if (grilleCases[x][y].getMachine() != null)
                    grilleCases[x][y].getMachine().send();
        setChanged();
        notifyObservers();
    }
}