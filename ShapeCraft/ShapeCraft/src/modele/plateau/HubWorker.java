package modele.plateau;

import modele.item.Item;

public class HubWorker extends Machine {
    private HubMain master;

    public HubWorker(HubMain master) {
        this.master = master;
    }

    @Override
    public void work() {
        // Si un item arrive sur un bord du Hub, on le donne immédiatement au centre
        if (!current.isEmpty()) {
            Item i = current.removeFirst();
            master.ajouterItem(i); // On l'envoie au  Hub main
        }
    }

    @Override
    public void send() { /* Ne fait rien */ }
}
