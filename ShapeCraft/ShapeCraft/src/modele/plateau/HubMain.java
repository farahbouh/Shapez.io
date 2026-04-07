package modele.plateau;

import java.util.HashMap;
import modele.item.Item;
import modele.item.ItemShape;

/// zone de livraison principal (au centre, cette relation maitre/esclave permet de créer une structure 3*3)
public class HubMain extends Machine {
    private static HashMap<String, Integer> ressourceStocke = new HashMap<>();
    private static int CAPACITE_RESTANTE[];

    public HubMain() {

    }

    public boolean gagner() {
        return ressourceStocke.values().stream().allMatch(v -> v == 0);
    }

    public void ajouterdefis(String s[], int I[]) {
        for (int i = 0; i < s.length; i++) {
            ressourceStocke.put(s[i], I[i]);
        }
    }


    public boolean ajouterItem(Item i) {
        ItemShape is = (ItemShape) i;
        if (ressourceStocke.containsKey(is.getCode())) {
            if (ressourceStocke.get(is.getCode()) != 0) {
                ressourceStocke.merge(is.getCode(), -1, Integer::sum);
                return true;
            }
        }
        return false;

    }

    @Override
    public void work() {
        while (!current.isEmpty()) {
            Item arrivee = current.removeFirst(); // des que item recu on le supprime
            ajouterItem(arrivee);
            if (this.gagner()) {
                ressourceStocke.clear();
                System.out.println("gagner");
            }
        }
    }


    @Override
    public void send() {
        // Le Hub ne renvoie pas d'items
    }
}