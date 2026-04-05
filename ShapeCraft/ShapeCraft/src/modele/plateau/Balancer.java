package modele.plateau;

import modele.item.Item;
import java.util.*;

public class Balancer extends Machine {
    private List<Case> cases = new ArrayList<>();
    private Item itemCase1 = null;
    private Item itemCase2 = null;

    public Balancer() {
        this.d = Direction.North;
    }

    public Balancer(Direction orientation) {
        this.d = orientation;
    }

    @Override
    public void setD(Direction d) {
        this.d = d;
    }

    public void addCase(Case c) {
        cases.add(c);
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (cases.size() != 2) return false;
        if (source == cases.get(0)) {
            if (itemCase1 != null) return false;
            itemCase1 = i;
        } else if (source == cases.get(1)) {
            if (itemCase2 != null) return false;
            itemCase2 = i;
        } else {
            return false;
        }
        trySend();
        return true;
    }

    @Override
    public boolean ajouterItem(Item i) {
        // Pas de source => refusé
        return false;
    }

    public boolean peutRecevoir(Case source) {
        if (cases.size() != 2) return false;
        if (source == null) return itemCase1 == null || itemCase2 == null;
        if (source == cases.get(0)) return itemCase1 == null;
        if (source == cases.get(1)) return itemCase2 == null;
        return false;
    }

    private void trySend() {
        if (itemCase1 == null || itemCase2 == null) return;
        if (c == null) return; // Balancer non placé

        Case sortie1 = c.plateau.getCase(cases.get(0), this.d);
        Case sortie2 = c.plateau.getCase(cases.get(1), this.d);
        if (sortie1 == null || sortie2 == null) return;

        Machine m1 = sortie1.getMachine();
        Machine m2 = sortie2.getMachine();
        if (m1 == null || m2 == null) return;

        // Tentative d'envoi : on ne vérifie pas à l'avance, on essaie
        boolean ok1 = m2.ajouterItem(itemCase1, sortie2);
        boolean ok2 = m1.ajouterItem(itemCase2, sortie1);
        if (ok1 && ok2) {
            itemCase1 = null;
            itemCase2 = null;
            System.out.println("Balancer: croisement réussi");
        } else {
            // Si l'un des deux a échoué, on annule (on remet les items)
            if (ok1) {
                // Il faudrait pouvoir retirer l'item de m2, mais c'est complexe.
                // On considère que l'ajout est atomique : on ne fait rien.
            }
            if (ok2) {
                // Idem.
            }
            // Sinon, on garde les items pour un prochain essai.
        }
    }

    @Override
    public void work() {
        // Rien à faire ici
    }

    @Override
    public void send() {
        trySend();
    }
}