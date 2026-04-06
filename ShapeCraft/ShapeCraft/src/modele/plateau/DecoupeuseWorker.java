package modele.plateau;

import modele.item.Item;

public class DecoupeuseWorker extends Machine {

    public DecoupeuseWorker(Decoupeuse master) {
        this.master = master;
    }
    private Decoupeuse master;

    public void setMaster(Decoupeuse master) {
        this.master = master;
    }

    public Decoupeuse getMaster() {
        return master;
    }
    @Override
    public boolean isLibre() {
        return current.isEmpty();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Vérifie que la machine source est une Decoupeuse ou un DecoupeuseWorker
        Machine sourceMachine = source.getMachine();
        if (!(sourceMachine instanceof Decoupeuse) && !(sourceMachine instanceof DecoupeuseWorker)) {
            return false; // Refuse si la source n'est pas une découpeuse ou un worker
        }
        if (!isLibre()) return false;
        current.add(i);
        return true;
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;
        if (c == null) return;
        Case destination = c.getVoisine(this.d);
        if (destination == null) return;
        Machine m = destination.getMachine();
        if (m == null) return;
        Item item = current.getFirst();
        // Passe la case de ce worker comme source
        if (m.ajouterItem(item, this.c)) {
            current.removeFirst();
            System.out.println(getClass().getSimpleName() + " a envoyé " + item);
        }
    }
}
