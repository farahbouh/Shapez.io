package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import java.util.LinkedList;

public class Decoupeuse extends Machine {

    private static final int CAPACITE_SORTIE = 1;   // une seule place par sortie

    private LinkedList<Item> sortieNord = new LinkedList<>();
    private LinkedList<Item> sortieOuest = new LinkedList<>();

    // Méthode pour savoir si on peut accepter un nouvel item en entrée
    private boolean peutAccepterEntree() {
        return sortieNord.size() < CAPACITE_SORTIE && sortieOuest.size() < CAPACITE_SORTIE;
    }
    public Item getItemNord() {
        if (sortieNord.isEmpty()) return null;
        return sortieNord.getFirst();
    }

    public Item getItemOuest() {
        if (sortieOuest.isEmpty()) return null;
        return sortieOuest.getFirst();
    }
    @Override
    public void work() {
        // On ne prend un item que s'il y a de la place dans les deux sorties
        if (!current.isEmpty() && peutAccepterEntree()) {
            Item item = current.removeFirst();
            if (item instanceof ItemShape) {
                ItemShape[] results = ((ItemShape) item).Cut();
                sortieNord.add(results[0]);
                sortieOuest.add(results[1]);
            }
        }
    }

    @Override
    public void send() {
        // Envoi vers le Nord
        Case caseNord = c.plateau.getCase(c, Direction.North);
        if (caseNord != null && !sortieNord.isEmpty()) {
            Machine machineNord = caseNord.getMachine();
            // On vérifie que la machine cible existe et qu'elle est libre (current vide)
            if (machineNord != null && machineNord.current.isEmpty()) {
                Item item = sortieNord.removeFirst();
                machineNord.current.add(item);
            }
        }

        // Envoi vers l'Ouest
        Case caseOuest = c.plateau.getCase(c, Direction.West);
        if (caseOuest != null && !sortieOuest.isEmpty()) {
            Machine machineOuest = caseOuest.getMachine();
            if (machineOuest != null && machineOuest.current.isEmpty()) {
                Item item = sortieOuest.removeFirst();
                machineOuest.current.add(item);
            }
        }
    }
}