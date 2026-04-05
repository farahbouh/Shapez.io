package modele.plateau;

import modele.item.Item;

public class Balancer extends Machine {
    private Item itemDirect = null;
    private Item itemWorker = null;
    private BalancerWorker worker;

    public Balancer() { this.d = Direction.North; }
    public Balancer(Direction orientation) { this.d = orientation; }

    public void setWorker(BalancerWorker w) { this.worker = w; }

    public boolean peutRecevoirWorker() {
        return itemWorker == null;
    }

    @Override
    public boolean isLibre() {
        return itemDirect == null;
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Pas de vérification de source pour ne pas casser
        if (itemDirect != null) return false;
        itemDirect = i;
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

        if (!m1.isLibre() || !m2.isLibre()) return;

        // CORRECTION : on passe la case source réelle (celle qui envoie)
        boolean ok1 = m2.ajouterItem(itemDirect, this.c);   // source = master
        boolean ok2 = m1.ajouterItem(itemWorker, worker.c); // source = worker
        if (ok1 && ok2) {
            itemDirect = null;
            itemWorker = null;
            System.out.println("Balancer: croisement réussi");
        }
    }

    @Override public void work() {}
    @Override public void send() { trySend(); }
}