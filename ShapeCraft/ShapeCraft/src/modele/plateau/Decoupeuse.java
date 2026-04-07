package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Decoupeuse extends Machine {
    private int type;
    private ItemShape[] reserve;


    public Decoupeuse(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    @Override
    public boolean estLibre() {
        return reserve == null && current.isEmpty();
    }

    @Override
    public boolean ajouterItem(Item i, Case source) {
        // Optionnel : vérifier la direction (mais ce n'est pas obligatoire)
        // Si tu veux laisser passer tous les items, supprime cette vérification
        Case entreeAttendue = c.getVoisine(this.d.inverse());
        if (source != entreeAttendue) return false;
        if (!estLibre()) return false;
        current.add(i);
        return true;
    }

    private void send_worker() {
        if (reserve == null) return;

        int nbWorkers = type * 2 - 1; // 2*2-1=3 donc 3 worker + machine 1*2-1 donc 1 worker + machine
        Case[] workerCases = new Case[nbWorkers];// on stocke les cases
        Machine[] workers = new Machine[nbWorkers];// on stocke les workers

        workerCases[0] = c.getVoisine(d.next());// on stocke le 1er worker
        if (workerCases[0] == null) return; // si vide alors arret
        workers[0] = workerCases[0].getMachine(); //stocke sa machine

        for (int i = 1; i < nbWorkers; i++) {
            workerCases[i] = workerCases[i-1].getVoisine(d.next());//on recupere le voisin de la i-1 donc de la case precedente
            if (workerCases[i] == null) return;// si vide on arret
            workers[i] = workerCases[i].getMachine(); //affectation de la machine worker
        }

        // Vérifie que tous les workers sont libres avant d'envoyer
        // on fait i+1 puique la 1er machine est un parent
        for (int i = 0; i < nbWorkers; i++) {
            if (reserve[i+1] != null && !reserve[i+1].getCode().equals("--------"))
            {//si reserve pleine et que son code contient des infos (! -) alors on verifie si les workers existent ou si ils sont libres
                if (workers[i] == null || !workers[i].estLibre()) return;
            }
        }

        // Envoi vers chaque worker
        Case sourceCase = this.c;
        for (int i = 0; i < nbWorkers; i++) {
            if (reserve[i+1] != null && !reserve[i+1].getCode().equals("--------")) {
                workers[i].ajouterItem(reserve[i+1], sourceCase);// on ajoute les item aux workers
            }
            sourceCase = workerCases[i]; // la source du suivant = la case du worker actuel
        }

        reserve = null;// on vide la reserve
    }


    @Override
    public void work() {
        if (reserve != null) return;
        Item item = getCurrent();
        if (item != null && item instanceof ItemShape) {
            ItemShape i = (ItemShape) item;
            if (type == 1)
            {
                reserve = i.Cut(2);
            }
            else
            {
                reserve = i.Cut(4);
            }
            current.removeFirst();
            if (!reserve[0].getCode().equals("--------"))
            {
                current.add(reserve[0]); // si reserve non vide on ajoute l'item
            }
            send_worker(); // on envoit aux workers
        }
    }

    @Override
    public void send() {
        send_worker();
        if (reserve != null)
            return; // si reserve existe on arrete d'envoyer des items
        super.send();// sinon on envoie
    }


}
