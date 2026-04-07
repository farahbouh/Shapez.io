package modele.plateau;

import modele.item.Item;

public class Poubelle extends Machine {
    @Override
    public boolean ajouterItem(Item i) {
        // La poubelle détruit l'item immédiatement
        System.out.println("Poubelle a détruit " + i);
        return true;
    }
    @Override
    public void send() {
        // Une poubelle n'envoie rien
    }
}