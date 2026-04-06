package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Decoupeuse extends Machine {
    private int type;
    private ItemShape[] reserve;

    public Decoupeuse(int type) {
        this.type = type;
    }

    @Override
    public boolean isLibre() {
        return reserve == null && current.isEmpty();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Optionnel : vérifier la direction (mais ce n'est pas obligatoire)
        // Si tu veux laisser passer tous les items, supprime cette vérification
        Case entreeAttendue = c.getVoisine(this.d.inverse());
        if (source != entreeAttendue) return false;
        if (!isLibre()) return false;
        current.add(i);
        return true;
    }

    private void send_worker() {
        if (reserve == null) return;

        Case firstWorkerCase = c.getVoisine(d.next());
        if (firstWorkerCase == null) return;
        Machine firstWorker = firstWorkerCase.getMachine();

        // Vérifier que TOUT est libre avant d'envoyer quoi que ce soit
        if (reserve[1] != null && !reserve[1].getCode().equals("--------")) {
            if (firstWorker == null || !firstWorker.isLibre()) return;
        }

        if (type == 2) {
            Case secondWorkerCase = firstWorkerCase.getVoisine(d.next());
            if (secondWorkerCase == null) return;
            Machine secondWorker = secondWorkerCase.getMachine();

            if (reserve[2] != null && !reserve[2].getCode().equals("--------")) {
                if (secondWorker == null || !secondWorker.isLibre()) return;
            }

            Case thirdWorkerCase = secondWorkerCase.getVoisine(d.next());
            if (thirdWorkerCase == null) return;
            Machine thirdWorker = thirdWorkerCase.getMachine();

            if (reserve[3] != null && !reserve[3].getCode().equals("--------")) {
                if (thirdWorker == null || !thirdWorker.isLibre()) return;
            }

            // Tout est libre : on envoie à tous
            if (reserve[1] != null && !reserve[1].getCode().equals("--------"))
                firstWorker.ajouterItem(reserve[1], this.c);
            if (reserve[2] != null && !reserve[2].getCode().equals("--------"))
                secondWorker.ajouterItem(reserve[2], firstWorkerCase);
            if (reserve[3] != null && !reserve[3].getCode().equals("--------"))
                thirdWorker.ajouterItem(reserve[3], secondWorkerCase);
        } else {
            if (reserve[1] != null && !reserve[1].getCode().equals("--------"))
                firstWorker.ajouterItem(reserve[1], this.c);
        }

        reserve = null;
    }

    @Override
    public void work() {
        if (reserve != null) return;
        Item item = getCurrent();
        if (item != null && item instanceof ItemShape) {
            ItemShape i = (ItemShape) item;
            if (type == 1) reserve = i.Cut();
            else reserve = i.Cut4();
            current.removeFirst();
            if (!reserve[0].getCode().equals("--------")) current.add(reserve[0]);
            send_worker();
        }
    }

    @Override
    public void send() {
        send_worker();
        if (reserve != null) return;
        super.send();
    }

    public int getType() {
        return type;
    }
}
