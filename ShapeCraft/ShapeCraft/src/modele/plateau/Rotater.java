package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Rotater extends Machine {
    int type; //horaire=1 antihoraire=0
    public Rotater(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }
    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Vérifie que l'item vient de la case située à l'opposé de la direction de sortie
        Case entreeAttendue = c.plateau.getCase(this.c, this.d.inverse());
        if (source != entreeAttendue) {
            return false; // Refuse si ça n'entre pas par le bon côté
        }
        // Appelle la méthode parente pour ajouter l'item (ou gère toi-même)
        return super.ajouterItem(i);
    }

    @Override
    public void work() {
        Item item = getCurrent();
        if (item instanceof ItemShape) {
            if (type==1) {
                ((ItemShape) item).rotate();
            }
            else {
                ((ItemShape) item).rotategauche();
            }
            System.out.println("Rotater a pivoté une forme");
        }
    }
}