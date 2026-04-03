package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import java.util.ArrayList;
import java.util.List;

public class Decoupeuse extends Machine {
    public int type; // 1 = coupe en 2, 2 = coupe en 4

    public Decoupeuse(Direction orientation, int type) {
        this.d = orientation;
        this.type = type;
    }

    // Méthode sans source : on la bloque pour forcer l'usage de la version avec source
    @Override
    public boolean ajouterItem(Item i) {
        System.out.println("Decoupeuse: appel sans source, refusé");
        return false;
    }

    // Validation : l'item n'est accepté que s'il arrive sur la première case (celle d'entrée)
    @Override
    public boolean ajouterItem(Item i, Case source) {
        List<Case> mesCases = getMesCases();
        if (mesCases.isEmpty()) return false;

        // La première case de la liste est celle qui a été posée en premier (entrée)
        Case caseEntree = mesCases.get(0);
        if (!source.equals(caseEntree)) {
            System.out.println("Decoupeuse: entrée refusée – item envoyé sur une case de droite");
            return false;
        }

        int capacite = (type == 1) ? 2 : 4;
        if (current.size() < capacite) {
            current.add(i);
            System.out.println("Decoupeuse: item accepté sur case entrée, file=" + current.size());
            return true;
        } else {
            System.out.println("Decoupeuse: file pleine!");
            return false;
        }
    }

    @Override
    public void work() {
        if (current.isEmpty()) return;
        Item item = current.getFirst();
        if (!(item instanceof ItemShape)) return;
        ItemShape itemShape = (ItemShape) item;
        if (itemShape.getCode().contains("-")) return; // déjà découpé

        ItemShape[] res = (type == 1) ? itemShape.Cut() : itemShape.Cut4();
        int capacite = (type == 1) ? 2 : 4;
        int currentSize = current.size();
        int needed = res.length;
        int freeAfterRemoval = capacite - (currentSize - 1);
        if (freeAfterRemoval >= needed) {
            current.removeFirst();
            for (ItemShape r : res) current.add(r);
            System.out.println("Decoupeuse: découpage effectué, file=" + current.size());
        } else {
            System.out.println("Decoupeuse: pas assez de place pour les morceaux");
        }
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;
        List<Case> mesCases = getMesCases();
        if (mesCases.isEmpty()) return;

        if (type == 1) {
            if (current.size() < 2 || mesCases.size() != 2) return;
            ItemShape piece1 = (ItemShape) current.get(0);
            ItemShape piece2 = (ItemShape) current.get(1);
            Case sortie1 = c.plateau.getCase(mesCases.get(0), this.d);
            Case sortie2 = c.plateau.getCase(mesCases.get(1), this.d);
            if (sortie1 == null || sortie2 == null) return;
            Machine m1 = sortie1.getMachine();
            Machine m2 = sortie2.getMachine();
            if (m1 == null || m2 == null) return;

            if (peutAccepter(m1) && peutAccepter(m2)) {
                // Envoi avec la case destination (la machine suivante recevra la source)
                if (m2.ajouterItem(piece1, sortie2) && m1.ajouterItem(piece2, sortie1)) {
                    current.remove(0);
                    current.remove(0);
                    System.out.println("Decoupeuse2: envoi réussi");
                }
            }
        } else if (type == 2) {
            if (current.size() < 4 || mesCases.size() != 4) return;
            ItemShape[] pieces = new ItemShape[4];
            for (int k = 0; k < 4; k++) pieces[k] = (ItemShape) current.get(k);
            Case[] destinations = new Case[4];
            for (int k = 0; k < 4; k++) {
                destinations[k] = c.plateau.getCase(mesCases.get(k), this.d);
                if (destinations[k] == null) return;
            }
            Machine[] machinesDest = new Machine[4];
            boolean toutesLibres = true;
            for (int k = 0; k < 4; k++) {
                if (destinations[k] != null) {
                    machinesDest[k] = destinations[k].getMachine();
                    if (machinesDest[k] == null || !peutAccepter(machinesDest[k])) {
                        toutesLibres = false;
                    }
                } else {
                    toutesLibres = false;
                }
            }
            if (toutesLibres) {
                boolean success = true;
                for (int k = 0; k < 4; k++) {
                    if (!machinesDest[k].ajouterItem(pieces[k], destinations[k])) {
                        success = false;
                        break;
                    }
                }
                if (success) {
                    for (int k = 0; k < 4; k++) current.remove(0);
                    System.out.println("Decoupeuse4: envoi réussi");
                }
            }
        }
    }

    private boolean peutAccepter(Machine m) {
        if (m == null) return false;
        if (m instanceof Decoupeuse) {
            int capa = ((Decoupeuse) m).type == 1 ? 2 : 4;
            return m.current.size() < capa;
        }
        if (m instanceof Balancer) return ((Balancer) m).peutRecevoir(null);
        if (m instanceof Tapis) return m.current.isEmpty();
        return m.current.isEmpty();
    }

    private List<Case> getMesCases() {
        List<Case> list = new ArrayList<>();
        for (int x = 0; x < c.plateau.SIZE_X; x++)
            for (int y = 0; y < c.plateau.SIZE_Y; y++) {
                Case ca = c.plateau.getCases()[x][y];
                if (ca.getMachine() == this) list.add(ca);
            }
        return list;
    }
}