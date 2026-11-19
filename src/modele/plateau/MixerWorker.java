package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;

public class MixerWorker extends Machine {
    private Mixer parent;

    public MixerWorker(Mixer parent, Direction orientation) {
        this.parent = parent;
        this.d = orientation;
    }

    @Override
    public boolean estLibre() {
        return current.isEmpty();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (!(i instanceof ItemColor)) return false;
        if (!current.isEmpty()) return false;
        if (source != c.getVoisine(d.inverse())) return false;

        current.add(i);
        return true;
    }

    @Override
    public void work() {
        if (current.isEmpty()) return;
        if (!parent.peutRecevoirWorker()) return;

        Item i = current.removeFirst();
        parent.recevoirDuWorker(i);
    }

    @Override
    public void send() {}
}