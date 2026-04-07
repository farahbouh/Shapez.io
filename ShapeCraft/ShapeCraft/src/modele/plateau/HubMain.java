package modele.plateau;

import java.util.HashMap;
import modele.item.Item;
import modele.item.ItemShape;

/// zone de livraison principal (au centre, cette relation maitre/esclave permet de créer une structure 3*3)
public class HubMain extends Machine {
    private static HashMap<String, Integer> ressourceStocke = new HashMap<>();
    private static final int CAPACITE_MAX = 5;

    @Override
    public boolean ajouterItem(Item i) {
        if (current.size() >= CAPACITE_MAX) {
            System.out.println("HubMain: FILE PLEINE! impossible d'ajouter " + ((ItemShape)i).getCode());
            return false;
        }
        this.current.add(i);
        return true;
    }

    @Override
    public void work() {
        while (!current.isEmpty()) {
            Item arrivee = current.removeFirst(); // des que item recu on le supprime
            if (arrivee != null && arrivee instanceof ItemShape) {
                String code = ((ItemShape) arrivee).getCode();// on recupere le code
                ressourceStocke.put(code, ressourceStocke.getOrDefault(code, 0) + 1);
                System.out.println("Hub : Reçu " + code + " | Total : " + ressourceStocke.get(code));
            }
        }
    }

    @Override
    public void send() {
        // Le Hub ne renvoie pas d'items
    }
}