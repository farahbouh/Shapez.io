package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;

public class Painter1 extends Machine {

    private ItemShape itemShape = null;
    private ItemColor itemColor = null;
    private PainterWorker worker;

    public Painter1(Direction orientation) {
        this.d = orientation;
    }

    public void setWorker(PainterWorker w) {
        this.worker = w;
    }

    public boolean peutRecevoirWorker() {
        return itemColor == null;
    }

    @Override
    public boolean estLibre() {
        return itemShape == null;
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        if (itemShape != null) return false;
        if (source != c.getVoisine(Direction.West)) return false;
        if (!(i instanceof ItemShape)) return false;

        itemShape = (ItemShape) i;
        return true;
    }

    public boolean recevoirDuWorker(Item i) {
        if (itemColor != null) return false;
        if (!(i instanceof ItemColor)) return false;

        itemColor = (ItemColor) i;
        return true;
    }

    private void trySend() {
        if (itemShape == null || itemColor == null) {
            return;
        }

        if (worker == null || worker.c == null) return;

        ItemShape itemPeint = peindreItem(itemShape, itemColor);
        if (itemPeint == null) {
            return;
        }


        if (worker.recevoirDuPainter(itemPeint)) {
            itemShape = null;
            itemColor = null;
        }
    }

    private ItemShape peindreItem(ItemShape item, ItemColor peinture) {
        if (item == null || item.getCode() == null || peinture == null) {
            return null;
        }

        // Appliquer la couleur directement sur l'item original
        String nouveauCode = item.mixShapeAndColor(peinture);

        if (nouveauCode == null) {
            return null;
        }

        return new ItemShape(nouveauCode);
    }

    @Override
    public void work() {}

    @Override
    public void send() {
        trySend();
    }
}