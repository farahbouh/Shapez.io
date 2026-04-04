package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.Random;

public class Mine extends Machine {


    @Override
    public void work() { // TODO : modifier, suivant le gisement
        Item gisement = c.getGisement();
        if(gisement != null && current.isEmpty())
        { //si pas de gisement alors la mine ne produit rien, pareil si la mine possede déjà un mineral sur lui
            if (new Random().nextInt(4) == 0)
            {
                if (gisement instanceof ItemShape)
                {
                    ItemShape copie = new ItemShape(((ItemShape) gisement).getCode());
                    current.add(copie);
                }
            }
        }
    }

    @Override
    public void send() {
        super.send();
    }
}
