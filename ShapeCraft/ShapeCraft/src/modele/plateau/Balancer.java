package modele.plateau;
import modele.item.Item;

public class Balancer extends Machine {

    private Item itemDirect = null;
    private Item itemWorker = null;
    private BalancerWorker worker;

    public Balancer(Direction orientation) { this.d = orientation; }
    public void setWorker(BalancerWorker w) { this.worker = w; }

    public boolean peutRecevoirWorker() {
        return itemWorker == null;
    }

    @Override
    public boolean estLibre() { return itemDirect == null; }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        //on verifie que les items arrives en bas de la machine
        if (itemDirect != null) {
            return false;
        }
        if (source != c.getVoisine(d.inverse())) {
            return false;
        }
        itemDirect = i;
        return true;
    }

    public boolean recevoirDuWorker(Item i) {
        // on verifie si on peut recuperer l'item du worker
        if (itemWorker != null) return false;
        itemWorker = i;
        return true;
    }

    private void trySend() {
        // verifie si on a bien 2 items avant de pouvoir envoyer
        if (itemDirect == null || itemWorker == null) return;

        //verifie que le worker existe
        if (worker == null || worker.c == null) return;

        // on recupere les sorties ainsi que les machines
        Case sortie1 = c.getVoisine(d);
        Case sortie2 = worker.c.getVoisine(d);

        //verifie que les sorties existent
        if (sortie1 == null || sortie2 == null) return;

        Machine m1 = sortie1.getMachine();
        Machine m2 = sortie2.getMachine();
        if (m1 == null || m2 == null) return;
        if (!m1.estLibre() || !m2.estLibre()) return; //si l'une des entrees est bouche on arrete


        // Si les deux entrees sont libres et qu'on a de la place alors on aju
        boolean ok1 = m2.ajouterItem(itemDirect, this.c);
        boolean ok2 = m1.ajouterItem(itemWorker, worker.c);
        if (ok1 && ok2) { // on vide les items du balancer si on apu les ajoute
            itemDirect = null;
            itemWorker = null;
        }
    }

    @Override
    public void work() {}

    @Override
    public void send() { trySend(); }
}