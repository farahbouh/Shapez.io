package modele.plateau;

import modele.item.Item;

public class BalancerWorker extends Machine {
    private Balancer parent;

    public BalancerWorker(Balancer parent, Direction orientation) {
        this.parent = parent;
        this.d = orientation;
    }

    @Override
    public boolean estLibre() {
        return current.isEmpty();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // On vérifie que le parent peut recevoir et que le worker est vide
        if (!current.isEmpty() || !parent.peutRecevoirWorker()) return false;

        //verifier si l'item vient de la bonne entree
        // si il vient de la bonne entree alors on l'ajoute dans la file d'attente
        if (source != c.getVoisine(d.inverse())) return false;
        current.add(i);
        return true;
    }

    @Override
    public void work() {
        if (current.isEmpty()) return; // si file vide on fait rien
        Item i = current.removeFirst(); //suppresion de la fil et traitement de l'element
        if (!parent.recevoirDuWorker(i)) { // si Balancerplein on l'ajoute a la lsite a nouveau
            current.addFirst(i);
        }
    }

    @Override
    public void send() {} // le maitre gère l'envoi
}