package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Tapis extends Machine {
    private Direction direction1; //direction d'entrer

    public Tapis(Direction direction1) {
        this.direction1 = direction1;

    }
    public Tapis() {

    }

    @Override
    public void setD(Direction d) {
        this.d = d;
        if (this.direction1 == null){
            this.direction1 = d.inverse();
        }

    }

    @Override
    public void run() {
        send();

    }

    public Direction getDirection1() {
        return direction1;
    }

    public void setDirection1(Direction direction1) {
        this.direction1 = direction1;
    }






}
