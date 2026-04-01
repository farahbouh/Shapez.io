package modele.plateau;
import modele.item.ItemShape;
import modele.item.Item;
import java.util.ArrayList;
import java.util.List;

public class Decoupeuse extends Machine {
    public int type; // type 1 = 2 sorties, type 2 = 4 sorties

    public Decoupeuse(Direction orientation, int type)
    {
        this.d = orientation;// recuperation du d dans le cons de machine
        this.type = type;
    }

    public void ajouterItem(Item i) {
        this.current.add(i);
    }

    @Override
    public void work() {
        if (current.isEmpty()) return;

        Item item = current.getFirst(); //recupere le 1er item
        if (item != null && item instanceof ItemShape) {
            ItemShape itemShape = (ItemShape) item;  // l'item a decouper

            if (!itemShape.getCode().contains("-")) // si l'item non decoupe alors on coupe
            {
                ItemShape[] res;
                if (type == 1) {
                    res = itemShape.Cut();      // cut
                } else {
                    res = itemShape.Cut4();     // quad
                }
                current.removeFirst();

                // Ajout des morceaux coupés dans current
                for (int j = 0; j < res.length; j++)
                {
                    ItemShape r = res[j];// on recuper chaque item
                    current.add(r);
                }
            }
        }
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;

        if (type == 1)
        {
            // Découpeuse 2 sorties : il faut 2 morceaux à envoyer
            if (current.size() >= 2) {
                // Récupérer les 2 morceaux
                ItemShape piece1 = (ItemShape) current.get(0);
                ItemShape piece2 = (ItemShape) current.get(1);

                // On a 2 sorties
                Case sortie1 = c.plateau.getCase(c, d); // Devant
                Case sortie2 = null;
                if (sortie1 != null) {
                    sortie2 = c.plateau.getCase(sortie1, Direction.West); // Gauche de devant
                }

                // Récupérer les machines des sorties
                Machine m1;
                if (sortie1 != null) {
                    m1 = sortie1.getMachine();
                } else {
                    m1 = null;
                }

                Machine m2;
                if (sortie2 != null) {
                    m2 = sortie2.getMachine();
                } else {
                    m2 = null;
                }
                // Vérifier si la machine est libre et existante mais aussi si elle accepte les element
                if (m1 != null && m1.current.isEmpty() && m2 != null && m2.current.isEmpty()) {

                    m1.ajouterItem(piece1);
                    m2.ajouterItem(piece2);

                    current.remove(0);
                    current.remove(0);
                }
            }
        }
        else if (type == 2)  // Découpeuse 4 sorties
        {

            if (current.size() >= 4)
            {
                // Récupérer les 4 morceaux
                ItemShape[] pieces = new ItemShape[4];
                for (int k = 0; k < 4; k++)
                {
                    pieces[k] = (ItemShape) current.get(k);
                }

                List<Case> mesCases = new ArrayList<>();
                for (int x = 0; x < c.plateau.SIZE_X; x++)
                {
                    for (int y = 0; y < c.plateau.SIZE_Y; y++)
                    {
                        Case caseCourante = c.plateau.getCases()[x][y]; //initialisation des 4 case en partant vers la droite
                        if (caseCourante.getMachine() == this)
                        {
                            mesCases.add(caseCourante);
                        }
                    }
                }

                Case[] destinations = new Case[4];
                for (int k = 0; k < 4 && k < mesCases.size(); k++)
                {
                    Case maCase = mesCases.get(k);
                    Case uneCaseNord = c.plateau.getCase(maCase, Direction.North);
                    if (uneCaseNord != null) {
                        destinations[k] = c.plateau.getCase(uneCaseNord, Direction.North);
                    }
                }

                // Récupérer les machines des destinations
                Machine[] machinesDest = new Machine[4];
                boolean toutesLibres = true;

                for (int k = 0; k < 4; k++) {
                    if (destinations[k] != null) {
                        machinesDest[k] = destinations[k].getMachine();
                        if (machinesDest[k] == null || !machinesDest[k].current.isEmpty()) {
                            toutesLibres = false;
                        }
                    } else {
                        toutesLibres = false;
                    }
                }

                // Si les 4 sorties sont libres, on envoie
                if (toutesLibres) {
                    for (int k = 0; k < 4; k++) {
                        machinesDest[k].ajouterItem(pieces[k]);
                    }

                    // Retirer les 4 morceaux
                    for (int k = 0; k < 4; k++) {
                        current.remove(0);
                    }
                }
                // Sinon, on attend (on ne fait rien)
            }
        }
    }
}