package modele.plateau;

import modele.item.Item;

public class Balancer extends Machine {
    private Item itemDirect = null;
    private Item itemWorker = null;
    private BalancerWorker worker;

    public Balancer() { this.d = Direction.North; }
    public Balancer(Direction orientation) { this.d = orientation; }

    public void setWorker(BalancerWorker w) { this.worker = w; }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (source == this.c) {
            if (itemDirect != null) return false;
            itemDirect = i;
        } else return false;
        trySend();
        return true;
    }

    public boolean recevoirDuWorker(Item i) {
        if (itemWorker != null) return false;
        itemWorker = i;
        trySend();
        return true;
    }

    private void trySend() {
        if (itemDirect == null || itemWorker == null) return;
        if (c == null || worker == null || worker.c == null) return;

        Case sortie1 = c.plateau.getCase(this.c, this.d);
        Case sortie2 = c.plateau.getCase(worker.c, this.d);
        if (sortie1 == null || sortie2 == null) return;

        Machine m1 = sortie1.getMachine();
        Machine m2 = sortie2.getMachine();
        if (m1 == null || m2 == null) return;

        boolean ok1 = m2.ajouterItem(itemDirect, sortie2);
        boolean ok2 = m1.ajouterItem(itemWorker, sortie1);
        if (ok1 && ok2) {
            itemDirect = null;
            itemWorker = null;
            System.out.println("Balancer: croisement réussi");
        }
    }

    @Override public void work() {}
    @Override public void send() { trySend(); }
}