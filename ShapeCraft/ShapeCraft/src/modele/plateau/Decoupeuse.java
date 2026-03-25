package modele.plateau;
import modele.item.ItemShape;
import modele.item.Item;


public class Decoupeuse extends Machine {

    public void ajouterItem(Item i)
    { //on crée cette fonction qui serra appellé par les classes esclaves
        this.current.add(i);
    }

    @Override // on ecrase le comportement de la machine
    public void work()
    {
        Item item = getCurrent();  // getCurrent() retourne null si vide
        if (item != null && item instanceof ItemShape) {
            ItemShape forme = (ItemShape) item;

            ItemShape reste = forme.Cut(); // recupere le reste de la forme
            current.removeFirst();
            current.add(forme);// on ajoute la forme
            current.add(reste);// ainsi que son reste
        }

    }
    @Override // on override car une seul sortie dans la class machine
    public void send()
    {
        Case sortie1 = c.plateau.getCase(c, Direction.North);
        Case sortie2 = c.plateau.getCase(c,Direction.Bizarre);
        if (sortie1 != null) {
            // item vers le nord
            Machine mNord = sortie1.getMachine();
            if (mNord != null && !current.isEmpty()) {
                mNord.current.add(current.removeFirst());
            }
        }

        if (sortie2 != null && !current.isEmpty()) {
            Machine mNE = sortie2.getMachine();
            if (mNE != null) {
                mNE.current.add(current.removeFirst());
            }
        }
    }
}
