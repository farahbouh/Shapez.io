package modele.plateau;

import modele.item.Item;

public class BalancerWorker extends Machine {
    private Balancer master;

    public BalancerWorker(Balancer master) {
        this.master = master;
    }

    @Override
    public boolean isLibre() {
        return current.isEmpty() && master.peutRecevoirWorker();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (!isLibre()) return false;

        // La source doit être la case juste derrière (entrée = direction inverse)
        Case entreeAttendue = c.getVoisine(d.inverse());
        if (source != entreeAttendue) return false;

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
    public void send() {
        // Le worker n'envoie rien directement, c'est le master qui le fait
    }
}
