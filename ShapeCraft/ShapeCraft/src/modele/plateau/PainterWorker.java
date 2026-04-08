package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;

public class PainterWorker extends Machine {
    private Painter1 parent;
    private ItemShape itemPeint = null;

    public PainterWorker(Painter1 parent, Direction orientation) {
        this.parent = parent;
        this.d = orientation;
    }

    @Override
    public boolean estLibre() {
        return itemPeint == null;
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (itemPeint != null) return false;
        if (source != c.getVoisine(Direction.North)) return false;
        if (!(i instanceof ItemColor)) return false;

        System.out.println("PainterWorker a reçu une couleur du Nord: " + ((ItemColor)i).getColor());

        // Transmet la couleur au Painter
        boolean result = parent.recevoirDuWorker(i);
        if (result) {
            System.out.println("PainterWorker a transmis la couleur au Painter");
        }
        return result;
    }

    public boolean recevoirDuPainter(ItemShape item) {
        if (itemPeint != null) return false;
        if (item == null) return false;

        itemPeint = item;
        System.out.println("PainterWorker a reçu l'item peint du Painter: " + itemPeint.getCode());
        return true;
    }

    @Override
    public void work() {}

    @Override
    public void send() {
        if (itemPeint == null) return;

        Case sortie = c.getVoisine(Direction.East);
        if (sortie == null) return;

        Machine machineSortie = sortie.getMachine();
        if (machineSortie == null) return;
        if (!machineSortie.estLibre()) return;

        System.out.println("PainterWorker envoie l'item peint vers l'Est: " + itemPeint.getCode());

        if (machineSortie.ajouterItem(itemPeint, this.c)) {
            itemPeint = null;
            System.out.println("PainterWorker a envoyé l'item avec succès");
        }
    }
}