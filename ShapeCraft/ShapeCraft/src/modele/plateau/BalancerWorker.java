package modele.plateau;

import modele.item.Item;

public class BalancerWorker extends Machine {
    private Balancer master;

    public BalancerWorker(Balancer master) {
        this.master = master;
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
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