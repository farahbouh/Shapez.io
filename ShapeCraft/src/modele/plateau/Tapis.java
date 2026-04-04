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
        super.send();
    }

    @Override
    public void run() { send(); }
}