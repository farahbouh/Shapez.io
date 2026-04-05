package modele.plateau;

import modele.item.Item;

public class BalancerWorker extends Machine {
    private Balancer master;

    public BalancerWorker(Balancer master) {
        this.master = master;
    }
    @Override
    public boolean isLibre() {
        // Le worker n'accepte que si son master n'a pas déjà un itemWorker
        return current.isEmpty() && master.peutRecevoirWorker();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (!isLibre()) return false;
        current.add(i);
        return true;
    }

    @Override
    public void work() {
        if (!current.isEmpty()) {
            Item i = current.removeFirst();
            master.recevoirDuWorker(i);
        }
    }

    @Override
    public void send() {}
}