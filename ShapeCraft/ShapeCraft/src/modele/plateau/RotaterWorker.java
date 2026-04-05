package modele.plateau;
import modele.item.Item;

public class RotaterWorker extends Machine {

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Même vérification : le worker a aussi une direction de sortie (this.d)
        Case entreeAttendue = c.plateau.getCase(this.c, this.d.inverse());
        if (source != entreeAttendue) {
            return false;
        }
        return super.ajouterItem(i);
    }

    @Override
    public void send() {
        super.send();
    }

    @Override
    public void work() {
        super.work();
    }
}