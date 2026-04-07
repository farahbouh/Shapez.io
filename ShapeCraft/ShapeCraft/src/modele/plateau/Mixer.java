package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;

public class Mixer extends Machine {
    private ItemColor couleur1 = null;
    private ItemColor couleur2 = null;
    private MixerWorker worker;

    public Mixer(Direction orientation) {
        this.d = orientation;
    }

    public void setWorker(MixerWorker w) {
        this.worker = w;
    }

    public boolean peutRecevoirWorker() {
        return couleur2 == null;
    }

    @Override
    public boolean estLibre() {
        return couleur1 == null;
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Vérifie que ça vient de l'entrée principale et qu'il s'agit d'un type couleur
        if (source != c.getVoisine(d.inverse())) return false;
        if (!(i instanceof ItemColor)) return false;
        if (couleur1 != null) return false;

        couleur1 = (ItemColor) i;
        return true;
    }

    public boolean recevoirDuWorker(Item i) {
        if (!(i instanceof ItemColor)) return false;
        if (couleur2 != null) return false;
        couleur2 = (ItemColor) i;
        return true;
    }

    private void trySend() {
        // Attend les 2 couleurs
        if (couleur1 == null || couleur2 == null) return;
        if (worker == null || worker.c == null) return;

        // Récupère les sorties
        Case sortie = c.getVoisine(d);
        if (sortie == null) return;

        Machine m = sortie.getMachine();
        if (m == null) return;
        if (!m.estLibre()) return;

        // Mélange les deux couleurs
        couleur1.transform(couleur2.getColor());

        // Envoie le résultat
        if (m.ajouterItem(couleur1, this.c)) {
            couleur1 = null;
            couleur2 = null;
        }
    }

    @Override
    public void work() {}

    @Override
    public void send() { trySend(); }
}