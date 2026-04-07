package modele.plateau;

import modele.item.Item;

public class HubWorker extends Machine {
    private HubMain parent;

    public HubWorker(HubMain parent) {
        this.parent = parent;
    }

    @Override
    public void work() {
        // Si un item arrive sur un bord du Hub, on le donne immédiatement au centre
        if (!current.isEmpty()) {
            Item i = current.removeFirst();
            parent.ajouterItem(i); // On l'envoie au Hub main
        }
    }

    @Override
    public void send() { /* Ne fait rien */ }
}
