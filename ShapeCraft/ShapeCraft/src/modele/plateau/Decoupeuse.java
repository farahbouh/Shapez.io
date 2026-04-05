package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Decoupeuse extends Machine {
    private int type;
    private ItemShape[] reserve;

    public Decoupeuse(int type) {
        this.type = type;
    }

    @Override
    public boolean isLibre() {
        return reserve == null && current.isEmpty();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (!isLibre()) return false;
        current.add(i);
        return true;
    }

    private void send_worker() {
        if (reserve == null) return;
        Case case1 = c.plateau.getCase(c, d.next());
        if (case1 == null) return;
        Machine machine1 = case1.getMachine();
        if (machine1 != null && reserve[1] != null && !reserve[1].getCode().equals("--------")) {
            if (!machine1.ajouterItem(reserve[1], case1)) return;
        }
        if (type == 2) {
            Case case2 = c.plateau.getCase(case1, d.next());
            if (case2 == null) return;
            Case case3 = c.plateau.getCase(case2, d.next());
            if (case3 == null) return;
            Machine machine2 = case2.getMachine();
            Machine machine3 = case3.getMachine();
            if (machine2 != null && reserve[2] != null && !reserve[2].getCode().equals("--------")) {
                if (!machine2.ajouterItem(reserve[2], case2)) return;
            }
            if (machine3 != null && reserve[3] != null && !reserve[3].getCode().equals("--------")) {
                if (!machine3.ajouterItem(reserve[3], case3)) return;
            }
        }
        reserve = null;
    }

    @Override
    public void work() {
        if (reserve != null) return;
        Item item = getCurrent();
        if (item != null && item instanceof ItemShape) {
            ItemShape i = (ItemShape) item;
            if (type == 1) reserve = i.Cut();
            else reserve = i.Cut4();
            current.removeFirst();
            if (!reserve[0].getCode().equals("--------")) current.add(reserve[0]);
            send_worker();
        }
    }

    @Override
    public void send() {
        send_worker();
        if (reserve != null) return;
        super.send();
    }

    public int getType() {
        return type;
    }
}