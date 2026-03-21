package modele.plateau;


import java.util.HashMap;
import modele.item.Item;
import modele.item.ItemShape;

/// zone de livraison principal (au centre, cette relation maitre/esclave permet de créer une structure 3*3)
public class HubMain extends Machine {
    private static HashMap<String, Integer> ressourceStocke = new HashMap<>(); //List pour stocker nos item
    //on choisit un hashmap (comme un dictionnaire en python) de string et int au lieux d'une liste d'item pour une meilleur optimisation du systeme

    public void ajouterItem(Item i) { //on crée cette fonction qui serra appellé par les classes esclaves
        this.current.add(i);
    }

    @Override
    public void work() {
        for(int i=0;i<12;i++){ // comme il y a 12 entrée possibles, dans la pire hypotese où on resoit tout sur un round, on fait 12test pour savoir si un a des items recus
        if (!current.isEmpty()) { //on reprend current de machine et on appel que si un item vient dans le hub
            Item arrivee = current.removeFirst(); //removeFirst permet d'envoyé et de supprimer comme pop()
            if (arrivee instanceof ItemShape) {
                String code = ((ItemShape) arrivee).getCode();

                // On incrémente le compteur pour ce code spécifique
                ressourceStocke.put(code, ressourceStocke.getOrDefault(code, 0) + 1);

                System.out.println("Hub : Reçu " + code + " | Total : " + ressourceStocke.get(code));
            }
        }}
    }
    @Override
    public void send() {
        // On vide send car le Hub ne renvoie pas d'items aux voisins
    }
}



