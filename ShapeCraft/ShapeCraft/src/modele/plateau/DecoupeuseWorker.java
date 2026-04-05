package modele.plateau;

import modele.item.Item;

public class DecoupeuseWorker extends Machine {
    public DecoupeuseWorker() {}

    @Override
    public boolean isLibre() {
        return current.isEmpty();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (!isLibre()) return false;
        current.add(i);
        return true;
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;
        if (c == null) return;
        Case destination = c.plateau.getCase(c, this.d);
        if (destination == null) return;
        Machine m = destination.getMachine();
        if (m == null) return;
        Item item = current.getFirst();
        // Tente d'ajouter à la machine suivante
        if (m.ajouterItem(item, destination)) {
            current.removeFirst();
            System.out.println(getClass().getSimpleName() + " a envoyé " + item);
        }
    }
}