package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import java.util.ArrayList;
import java.util.List;

public class Balancer extends Machine {
    private List<Case> cases;  // Les 2 cases du Balancer

    public Balancer() {
        this.d = Direction.North;
        this.cases = new ArrayList<>();
    }

    public Balancer(Direction orientation) {
        this.d = orientation;
        this.cases = new ArrayList<>();
    }

    @Override
    public void setD(Direction d) {
        this.d = d;
    }

    @Override
    public boolean ajouterItem(Item i) {
        // Le Balancer peut accumuler jusqu'à 2 items (un par entrée possible)
        if (current.size() >= 2) {
            System.out.println("Balancer: FILE PLEINE! impossible d'ajouter");
            return false;
        }

        this.current.add(i);
        System.out.println("Balancer: item reçu, taille file = " + current.size());

        // Essayer d'envoyer si on a 2 items
        if (current.size() == 2) {
            send();
        }

        return true;
    }

    // Ajouter une case au Balancer (appelé par Plateau.setBalancer)
    public void addCase(Case caseBalancer) {
        this.cases.add(caseBalancer);
    }

    @Override
    public void work() {
        if (current.isEmpty()) return;
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;

        System.out.println("Balancer: send() appelé, taille file = " + current.size());

        if (current.size() == 2 && cases.size() == 2) {
            ItemShape piece1 = (ItemShape) current.get(0);
            ItemShape piece2 = (ItemShape) current.get(1);

            System.out.println("Balancer: item1 = " + piece1.getCode() + ", item2 = " + piece2.getCode());

            // Pour un Balancer orienté vers le Nord, les sorties sont au Nord des deux cases
            // Pour un Balancer orienté vers l'Est, les sorties sont à l'Est des deux cases
            Case sortie1 = c.plateau.getCase(cases.get(0), this.d);
            Case sortie2 = c.plateau.getCase(cases.get(1), this.d);

            Machine m1 = (sortie1 != null) ? sortie1.getMachine() : null;
            Machine m2 = (sortie2 != null) ? sortie2.getMachine() : null;

            System.out.println("Balancer: machine1 = " + m1 + ", machine2 = " + m2);

            // Vérifier si les machines de destination peuvent accepter
            boolean m1Accepte = false;
            boolean m2Accepte = false;

            if (m1 != null) {
                if (m1 instanceof Balancer) {
                    m1Accepte = m1.current.size() < 2;
                } else if (m1 instanceof Decoupeuse) {
                    m1Accepte = true;
                } else {
                    m1Accepte = m1.current.isEmpty();
                }
            } else {
                // Si pas de machine, c'est un trou - on ne peut pas envoyer
                m1Accepte = false;
            }

            if (m2 != null) {
                if (m2 instanceof Balancer) {
                    m2Accepte = m2.current.size() < 2;
                } else if (m2 instanceof Decoupeuse) {
                    m2Accepte = true;
                } else {
                    m2Accepte = m2.current.isEmpty();
                }
            } else {
                m2Accepte = false;
            }

            if (m1Accepte && m2Accepte) {
                // INVERSION : le premier item va vers la deuxième sortie, le deuxième vers la première
                System.out.println("Balancer: envoi avec inversion - piece1 vers sortie2, piece2 vers sortie1");
                m2.ajouterItem(piece1);
                m1.ajouterItem(piece2);

                current.remove(0);
                current.remove(0);
                System.out.println("Balancer: envoi réussi, reste " + current.size() + " items");
            } else {
                System.out.println("Balancer: destinations non libres, envoi impossible");
            }
        }
    }
}