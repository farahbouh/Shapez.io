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
        if (itemDirect != null) return false;

        // La source doit être la case juste derrière (entrée = direction inverse)
        Case entreeAttendue = c.getVoisine(d.inverse());
        if (source != entreeAttendue) return false;

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
        if (worker == null || worker.c == null) return;

        Case sortie1 = c.getVoisine(d);
        Case sortie2 = worker.c.getVoisine(d);
        if (sortie1 == null || sortie2 == null) return;

        Machine m1 = sortie1.getMachine();
        Machine m2 = sortie2.getMachine();
        if (m1 == null || m2 == null) return;

        if (!m1.isLibre() || !m2.isLibre()) return;

        boolean ok1 = m2.ajouterItem(itemDirect, this.c);
        boolean ok2 = m1.ajouterItem(itemWorker, worker.c);
        if (ok1 && ok2) {
            itemDirect = null;
            itemWorker = null;
            System.out.println("Balancer: croisement réussi");
        }
    }

    @Override public void work() {}
    @Override public void send() { trySend(); }
}
