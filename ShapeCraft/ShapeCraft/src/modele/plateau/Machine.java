package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Machine implements Runnable {
    LinkedList<Item> current; // liste des items dans la machine


    Case c;
    Direction d = Direction.North; // par défaut, pour commencer, tout est orienté au north

    public Direction getD() {
        return d;
    }

    public void setD(Direction d) {
        this.d = d;
    }

    public Machine()
    {
        current = new LinkedList<Item>();
    }

    public Machine(Item _item) {
        this();
        current.add(_item);
    }

    public void setCase(Case _c) {
        c= _c;
    }

    public Item getCurrent() {
        if (current.size() > 0) {
            return current.get(0);
        } else {
            return null;
        }
    }
    public void ajouterItem(Item i) {
        current.add(i);
    }
    public void send() {
        if (current.isEmpty()) return; // Rien à envoyer

        // On récupère la case devant nous selon l'orientation de la machine
        Case destination = c.plateau.getCase(c, this.d);

        if (destination != null) {
            Machine m = destination.getMachine();

            if (m != null && m.current.isEmpty()) { // La machine cible a de la place
                boolean canSend = false;

                if (m instanceof Tapis) {
                    Tapis prochainTapis = (Tapis) m;
                    // Si ma sortie (this.d) est l'opposé de son entrée, ça connecte
                    if (prochainTapis.getDirection1() == this.d.inverse()) {
                        canSend = true;
                    }
                } else {
                    // Si c'est une autre machine (Hub, etc.), on accepte par défaut
                    canSend = true;
                }

                if (canSend) {
                    Item item = current.removeFirst(); // Utilise removeFirst() pour LinkedList
                    m.current.add(item);
                }
            }
        }
    }

    public void work() {

            /*if (current.size() > 0) {
                ((ItemShape) current.get(0)).rotate();

            }*/

    }; // action de la machine, aucune par défaut

    @Override
    public void run() {
        work(); //delete ??
        send();

    }
    public void run2(){
        work();
    }

}
