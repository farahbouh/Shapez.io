package modele.plateau;

import modele.item.ItemShape;
import modele.item.Item;
import java.util.ArrayList;
import java.util.List;

public class Decoupeuse extends Machine {
    public int type;

    public Decoupeuse(Direction orientation, int type) {
        this.d = orientation;
        this.type = type;
    }

    @Override
    public boolean ajouterItem(Item i) {
        if (type == 1) {
            if (current.size() < 2) {
                this.current.add(i);
                System.out.println("Decoupeuse: item reçu, file=" + current.size());
                return true;
            } else {
                System.out.println("Decoupeuse: file pleine!");
                return false;
            }
        } else {
            if (current.size() < 4) {
                this.current.add(i);
                System.out.println("Decoupeuse: item reçu, file=" + current.size());
                return true;
            } else {
                System.out.println("Decoupeuse: file pleine!");
                return false;
            }
        }
    }

    @Override
    public void work() {
        if (current.isEmpty()) return;

        Item item = current.getFirst();
        if (item != null && item instanceof ItemShape) {
            ItemShape itemShape = (ItemShape) item;

            // Ne découper que si l'item n'est pas déjà découpé
            if (!itemShape.getCode().contains("-")) {
                ItemShape[] res;
                if (type == 1) {
                    res = itemShape.Cut();
                } else {
                    res = itemShape.Cut4();
                }
                current.removeFirst();

                for (int j = 0; j < res.length; j++) {
                    ItemShape r = res[j];
                    current.add(r);
                }
                System.out.println("Decoupeuse: découpage effectué");
            }
        }
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;

        if (type == 1) {
            if (current.size() >= 2) {
                ItemShape piece1 = (ItemShape) current.get(0);
                ItemShape piece2 = (ItemShape) current.get(1);

                // Récupérer les deux cases de la découpeuse
                List<Case> mesCases = new ArrayList<>();
                for (int x = 0; x < c.plateau.SIZE_X; x++) {
                    for (int y = 0; y < c.plateau.SIZE_Y; y++) {
                        Case caseCourante = c.plateau.getCases()[x][y];
                        if (caseCourante.getMachine() == this) {
                            mesCases.add(caseCourante);
                        }
                    }
                }

                if (mesCases.size() == 2) {
                    // Pour une découpeuse, les sorties sont devant chaque case dans la direction
                    Case sortie1 = c.plateau.getCase(mesCases.get(0), this.d);
                    Case sortie2 = c.plateau.getCase(mesCases.get(1), this.d);

                    Machine m1 = (sortie1 != null) ? sortie1.getMachine() : null;
                    Machine m2 = (sortie2 != null) ? sortie2.getMachine() : null;

                    if (m1 != null && m1.current.isEmpty() && m2 != null && m2.current.isEmpty()) {
                        m1.ajouterItem(piece1);
                        m2.ajouterItem(piece2);

                        current.remove(0);
                        current.remove(0);
                        System.out.println("Decoupeuse2: envoi réussi");
                    }
                }
            }
        } else if (type == 2) {
            if (current.size() >= 4) {
                ItemShape[] pieces = new ItemShape[4];
                for (int k = 0; k < 4; k++) {
                    pieces[k] = (ItemShape) current.get(k);
                }

                // Récupérer les 4 cases de la découpeuse
                List<Case> mesCases = new ArrayList<>();
                for (int x = 0; x < c.plateau.SIZE_X; x++) {
                    for (int y = 0; y < c.plateau.SIZE_Y; y++) {
                        Case caseCourante = c.plateau.getCases()[x][y];
                        if (caseCourante.getMachine() == this) {
                            mesCases.add(caseCourante);
                        }
                    }
                }

                if (mesCases.size() == 4) {
                    // Pour chaque case, la sortie est dans la direction de la découpeuse
                    Case[] destinations = new Case[4];
                    for (int k = 0; k < 4; k++) {
                        destinations[k] = c.plateau.getCase(mesCases.get(k), this.d);
                    }

                    Machine[] machinesDest = new Machine[4];
                    boolean toutesLibres = true;

                    for (int k = 0; k < 4; k++) {
                        if (destinations[k] != null) {
                            machinesDest[k] = destinations[k].getMachine();
                            if (machinesDest[k] == null || !machinesDest[k].current.isEmpty()) {
                                toutesLibres = false;
                            }
                        } else {
                            toutesLibres = false;
                        }
                    }

                    if (toutesLibres) {
                        for (int k = 0; k < 4; k++) {
                            machinesDest[k].ajouterItem(pieces[k]);
                        }

                        for (int k = 0; k < 4; k++) {
                            current.remove(0);
                        }
                        System.out.println("Decoupeuse4: envoi réussi");
                    }
                }
            }
        }
    }
}