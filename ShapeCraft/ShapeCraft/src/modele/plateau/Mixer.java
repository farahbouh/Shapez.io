package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;

public class Mixer extends Machine{
    private ItemColor Couleur;
    private Item resultat;

    public Mixer(){
        Couleur = null;
        resultat = null;
    }

    public Item getCouleur() {
        return Couleur;
    }

    public void setCouleur(ItemColor couleur) {
        Couleur = couleur;
    }



    @Override
    public void work() {
        if(Couleur != null && !current.isEmpty()){
            ItemShape is = (ItemShape) current.removeFirst();
            String s = is.mixShapeAndColor((ItemColor) Couleur);
            current.add(new ItemShape(s));
        }
    }
    @Override
    public void send(){
        super.send();
    }

}
