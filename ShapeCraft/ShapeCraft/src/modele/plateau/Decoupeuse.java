package modele.plateau;
import modele.item.ItemShape;
import modele.item.Item;

public class Decoupeuse extends Machine {

    public void ajouterItem(Item i) {
        this.current.add(i);
    }

    @Override
    public void work() {
        Item item = getCurrent();
        if (item != null && item instanceof ItemShape) {
            ItemShape i = (ItemShape) item;

            if (!i.getCode().contains("-")) // Ne découper que l'original (sans '-')
            {
                ItemShape[] results = i.Cut();
                current.removeFirst();
                current.add(results[0]); // première moitié
                current.add(results[1]); // seconde moitié
            }
        }
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;
        Item first = current.getFirst();
        if (!(first instanceof ItemShape)) return;

        ItemShape shape = (ItemShape) first;
        String code = shape.getCode();
        int mid = code.length() / 2;

        // Déterminer si c'est la première moitié (partie droite remplie de '-')
        boolean isFirstHalf = true;
        for (int i = mid; i < code.length(); i++) {
            if (code.charAt(i) != '-') { isFirstHalf = false; break; }
        }
        // Deuxième moitié (partie gauche remplie de '-')
        boolean isSecondHalf = true;
        for (int i = 0; i < mid; i++) {
            if (code.charAt(i) != '-') { isSecondHalf = false; break; }
        }

        Case sortie1 = c.plateau.getCase(c, Direction.North);
        if (sortie1 == null) return;
        // Modification ici : West -> East pour décaler vers la droite
        Case sortie2 = c.plateau.getCase(sortie1, Direction.East);

        // Première moitié → sortie nord-est (anciennement nord-ouest)
        if (isFirstHalf && sortie2 != null) {
            Machine m = sortie2.getMachine();
            if (m != null && m.current.isEmpty()) {
                m.current.add(current.removeFirst());
                return;
            }
        }
        // Deuxième moitié → sortie nord
        else if (isSecondHalf && sortie1 != null) {
            Machine m = sortie1.getMachine();
            if (m != null && m.current.isEmpty()) {
                m.current.add(current.removeFirst());
                return;
            }
        }
        // Si la sortie cible est bloquée, on attend (rien ne part)
    }
}