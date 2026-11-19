package modele.plateau;

import modele.item.Item;

import java.util.LinkedList;

public abstract class Machine implements Runnable {
    LinkedList<Item> current; //queue stockant nos elements
    Case c;
    Direction d = Direction.North;

    public Direction getD() {
        return d;
    }

    public void setD(Direction d) {
        this.d = d;
    }

    public Machine() {
        current = new LinkedList<Item>();
    }

    public Machine(Item _item) {
        this();
        current.add(_item);
    }

    public void setCase(Case _c) {
        c = _c;
    }

    public Item getCurrent() {
        if (current.size() > 0) {
            return current.get(0); // on recup le 1er
        } else {
            return null; // pas d'item
        }
    }
    public boolean estLibre() {
        return current.isEmpty();
    } // si la liste est vide alors on peut work

    public boolean ajouterItem(Item i) {
        current.add(i);
        return true;
    }
    public boolean ajouterItem(Item i, Case source) {
        return ajouterItem(i);
    }

    public void send() {
        if (current.isEmpty()) return;
        Case destination = c.plateau.getCase(c, this.d);
        if (destination == null) return;
        Machine m = destination.getMachine();
        if (m == null) return;

        Item item = current.getFirst();
        boolean peutAccepter = false; //par defaut on accepte pas l'item

        if (m instanceof Tapis) {
            Tapis tapis = (Tapis) m;
            // verifie si la machine peut acceper l'element en fonction de dir
            peutAccepter = tapis.getDirection1() == this.d.inverse() && m.current.isEmpty();

        } else if (m instanceof Poubelle) {
            peutAccepter = true;
        } else if (m instanceof HubWorker) {
            peutAccepter = m.current.size() < 5;
        } else {
            peutAccepter = m.estLibre();
        }

        if (peutAccepter) {
            // on passe la case de la machine qui envoie au lieu de destination
            if (m.ajouterItem(item, this.c)) {
                current.remove(item);
                System.out.println(getClass().getSimpleName() + " a envoyé " + item + " à " + m.getClass().getSimpleName());
            }
        }
    }

    public void work() {
        // action de la machine, aucune par défaut
    }

    @Override
    public void run() {
        work(); // work puis envoie
        send();
    }
}