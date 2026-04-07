package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;

import java.util.Random;

public class Mine extends Machine {


    @Override
    public void work() {
        Item gisement = c.getGisement();
        if(gisement != null && current.isEmpty())
        { //si pas de gisement alors la mine ne produit pas
            if (new Random().nextInt(4) == 0)
            {
                if (gisement instanceof ItemShape) //gisement objet
                {
                    ItemShape copie = new ItemShape(((ItemShape) gisement).getCode());
                    current.add(copie); // on ajoute l'item du meme type
                }
                if (gisement instanceof ItemColor) //gisement couleur
                {
                    ItemColor copie = new ItemColor(((ItemColor) gisement).getCodecolor());
                    current.add(copie); // on ajoute la couleur
                }
            }
        }
    }

    @Override
    public void send() {
        super.send();
    }
}
