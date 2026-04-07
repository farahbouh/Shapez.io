package modele.plateau;
import modele.item.Item;

public class RotaterWorker extends Machine {

    Rotater parent;

    public Rotater getParent() {
        return parent;
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Même vérification : le worker a aussi une direction de sortie (this.d)
        Case entreeAttendue = c.getVoisine(this.d.inverse());
        if (source != entreeAttendue) {
            return false;
        }
        return super.ajouterItem(i);
    }

    @Override
    public void send() {
        super.send();
    } // envoi classique (1 entree une sortie)

    @Override
    public void work() {
        super.work();
    }
}