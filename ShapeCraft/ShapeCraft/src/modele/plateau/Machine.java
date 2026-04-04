package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.LinkedList;

public abstract class Machine implements Runnable {
    LinkedList<Item> current;
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
            return current.get(0);
        } else {
            return null;
        }
    }

    public boolean ajouterItem(Item i) {
        current.add(i);
        return true;
    }
    public boolean ajouterItem(Item i, Case source) {
        return ajouterItem(i);
    }
    // Dans la classe Machine - modifiez la méthode send()
    public void send() {
        if (current.isEmpty()) return;

        Case destination = c.plateau.getCase(c, this.d);
        if (destination == null) return;

        Machine m = destination.getMachine();
        if (m == null) return;

        Item item = current.getFirst();
        boolean peutAccepter = false;

        // Logique de capacité (version 2)
        if (m instanceof Balancer) {
            peutAccepter = m.current.size() < 2;
        } else if (m instanceof Decoupeuse) {
            peutAccepter = true;
        } else if (m instanceof MixerWorker) {
            peutAccepter = m.current.size() < 1;
    }else if (m instanceof Mixer) {
            peutAccepter = m.current.size() < 1;
        } else if (m instanceof HubWorker) {
            peutAccepter = m.current.size() < 5;
        } else if (m instanceof Tapis) {   // ← AJOUT depuis version 1
            Tapis tapis = (Tapis) m;
            // Vérifie l'orientation (logique version 1)
            peutAccepter = tapis.getDirection1() == this.d.inverse();
        }

        if (peutAccepter) {
            if (m.ajouterItem(item, destination)) {
                current.remove(item);
                System.out.println(getClass().getSimpleName() + " a envoyé " + item + " à " + m.getClass().getSimpleName());
            }
        } else {
            //System.out.println(getClass().getSimpleName() + " ne peut pas envoyer - machine suivante pleine ou non connectée");
        }
    }

    public void work() {
        // action de la machine, aucune par défaut
    }

    @Override
    public void run() {
        work();
        send();
    }

    public void run2() {
        work();
    }
}