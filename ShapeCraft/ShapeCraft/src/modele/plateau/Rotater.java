package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Rotater extends Machine {
    @Override
    public void work() {
        Item item = getCurrent();
        if (item instanceof ItemShape) {
            ((ItemShape) item).rotate();
            System.out.println("Rotater a pivoté une forme");
        }
        send();
    }
}