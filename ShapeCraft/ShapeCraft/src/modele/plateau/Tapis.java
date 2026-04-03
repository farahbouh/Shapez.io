package modele.plateau;

import modele.item.Item;

public class Tapis extends Machine {
    private Direction directionEntree;

    public Tapis(Direction directionEntree) {
        this.directionEntree = directionEntree;
        this.d = directionEntree.inverse();
    }
    public Tapis() {}

    @Override
    public void setD(Direction d) {
        this.d = d;
        if (directionEntree == null) directionEntree = d.inverse();
    }
    public Direction getDirection1() { return directionEntree; }
    public void setDirectionEntree(Direction directionEntree) {
        this.directionEntree = directionEntree;
        this.d = directionEntree.inverse();
    }

    @Override
    public boolean ajouterItem(Item i) {
        if (current.isEmpty()) {
            current.add(i);
            System.out.println("Tapis a reçu un item");
            return true;
        }
        System.out.println("Tapis déjà occupé, item refusé");
        return false;
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;
        Case destination = c.plateau.getCase(c, this.d);
        if (destination == null) return;
        Machine machineDest = destination.getMachine();
        if (machineDest == null) return;

        boolean peutAccepter = false;
        if (machineDest instanceof Tapis) {
            Tapis t = (Tapis) machineDest;
            peutAccepter = t.getDirection1() == this.d.inverse() && t.current.isEmpty();
        } else if (machineDest instanceof Decoupeuse) {
            Decoupeuse dec = (Decoupeuse) machineDest;
            int capa = (dec.type == 1) ? 2 : 4;
            peutAccepter = dec.current.size() < capa;
        } else if (machineDest instanceof Balancer) {
            peutAccepter = ((Balancer) machineDest).peutRecevoir(destination);
        } else {
            peutAccepter = machineDest.current.isEmpty();
        }

        if (peutAccepter) {
            Item item = current.getFirst();
            if (machineDest.ajouterItem(item, destination)) {
                current.removeFirst();
                System.out.println("Tapis a envoyé un item");
            }
        }
    }

    @Override
    public void run() { send(); }
}