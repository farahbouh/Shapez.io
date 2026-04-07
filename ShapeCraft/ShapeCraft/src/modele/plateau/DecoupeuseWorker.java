package modele.plateau;

import modele.item.Item;

public class DecoupeuseWorker extends Machine {

    private Decoupeuse parent;
    private int index; // 1, 2 ou 3

    public DecoupeuseWorker(Decoupeuse parent, int index) {
        this.parent = parent;
        this.index = index; // connaitre le num du worker
    }
    public int getIndex() { return index; }

    public void setParent(Decoupeuse parent) {
        this.parent = parent;
    }

    public Decoupeuse getParent() {
        return parent;
    }

    @Override
    public boolean estLibre() {
        return current.isEmpty();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Vérifie que la machine source est une Decoupeuse ou un DecoupeuseWorker
        Machine sourceMachine = source.getMachine();
        if (!(sourceMachine instanceof Decoupeuse) && !(sourceMachine instanceof DecoupeuseWorker)) {
            return false; // Refuse si la source n'est pas une découpeuse ou un worker
        }
        if (!estLibre()) return false; //si n'est pas libre on peut pas ajouter d'item (return false)
        current.add(i); // sinon on ajoute l'item + return false
        return true;
    }

    @Override
    public void send() {
        if (current.isEmpty()) return; // recup la liste sauf si vide donc return
        if (c == null) return;

        Case destination = c.getVoisine(this.d); // recup destination... sinon arret
        if (destination == null) return;

        Machine m = destination.getMachine(); // recup machine.. sinon arret
        if (m == null) return;

        Item item = current.getFirst(); // on prends le 1er item
        // Passe la case de ce worker comme source
        if (m.ajouterItem(item, this.c)) {
            current.removeFirst(); // supp de la liste si l'objet a bien ete ajouter
            System.out.println(getClass().getSimpleName() + " a envoyé " + item);

        }
    }
}
