package modele.plateau;

import modele.item.Item;
import java.util.*;

public class Balancer extends Machine {
    private List<Case> cases = new ArrayList<>();
    private Item itemCase1 = null;
    private Item itemCase2 = null;

    public Balancer() { this.d = Direction.North;
    }
    public Balancer(Direction orientation) { this.d = orientation; }

    @Override public void setD(Direction d) { this.d = d; }
    public void addCase(Case c) { cases.add(c); }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (cases.size() != 2) return false;
        if (source.equals(cases.get(0))) {
            if (itemCase1 != null) return false;
            itemCase1 = i;
        } else if (source.equals(cases.get(1))) {
            if (itemCase2 != null) return false;
            itemCase2 = i;
        } else return false;
        System.out.println("Balancer: reçu sur case " + (source.equals(cases.get(0)) ? "1" : "2"));
        trySend();
        return true;
    }

    @Override
    public boolean ajouterItem(Item i) {
        System.out.println("Balancer: appel sans source, refusé");
        return false;
    }

    public boolean peutRecevoir(Case source) {
        if (cases.size() != 2) return false;
        if (source == null) return itemCase1 == null || itemCase2 == null;
        if (source.equals(cases.get(0))) return itemCase1 == null;
        if (source.equals(cases.get(1))) return itemCase2 == null;
        return false;
    }

    private void trySend() {
        if (itemCase1 == null || itemCase2 == null) return;
        Case sortie1 = c.plateau.getCase(cases.get(0), this.d);
        Case sortie2 = c.plateau.getCase(cases.get(1), this.d);
        if (sortie1 == null || sortie2 == null) return;
        Machine m1 = sortie1.getMachine();
        Machine m2 = sortie2.getMachine();
        if (m1 == null || m2 == null) return;
        if (peutAccepter(m1) && peutAccepter(m2)) {
            m2.ajouterItem(itemCase1, sortie2);
            m1.ajouterItem(itemCase2, sortie1);
            itemCase1 = null;
            itemCase2 = null;
            System.out.println("Balancer: croisement réussi");
        }
    }

    private boolean peutAccepter(Machine m) {
        if (m == null) return false;
        if (m instanceof Balancer) {
            Balancer b = (Balancer) m;
            return b.itemCase1 == null || b.itemCase2 == null;
        }
        if (m instanceof Decoupeuse) {
            int capa = ((Decoupeuse) m).getType() == 1 ? 2 : 4;
            return m.current.size() < capa;
        }
        if (m instanceof Tapis) return m.current.isEmpty();
        return m.current.isEmpty();
    }

    @Override public void work() {}
    @Override public void send() { trySend(); }
}