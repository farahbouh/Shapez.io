package vuecontroleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;


import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
import modele.jeu.Jeu;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private Jeu jeu;
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    private static final int pxCase = 82; // nombre de pixel par case
    // icones affichées dans la grille
    private Image icoRouge;
    private Image icoTapisHaut;
    private Image icoTapisGauche;
    private Image icoPoubelle;
    private Image icoMine;
    private Image icoHub;
    private Image icoDecoupeuse;
    private Image icoDecoupeuse4;
    private JComponent grilleIP;
    private Image icoBalancer;
    private int lastX = -1;
    private int lastY = -1;
    private Image icoTapisDroite;
    private Image icoTapisGauche_Droite;
    private Image icoTapisDroite_Gauche;
    private Image icoTapisDroite2;
    private Image icoTapisGauche2;
    private Image icoTapisBas;

    private boolean mousePressed = false; // permet de mémoriser l'état de la souris

    private ImagePanel[][] tabIP; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône background et front, suivant ce qui est présent dans le modèle)


    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;

        chargerLesIcones();
        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();

    }


    private void chargerLesIcones() {
        icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_right.png").getImage();
        icoTapisGauche_Droite = new ImageIcon("./data/sprites/buildings/belt_gauche_droite.png").getImage();
        icoTapisDroite_Gauche = new ImageIcon("./data/sprites/buildings/belt_droite_gauche.png").getImage();
        icoTapisDroite2 = new ImageIcon("./data/sprites/buildings/belt_right_1.png").getImage();
        icoTapisGauche2 = new ImageIcon("./data/sprites/buildings/belt_left_1.png").getImage();

        icoTapisBas = new ImageIcon("./data/sprites/buildings/belt_bottom.png").getImage();
        icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoTapisHaut = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisGauche = new ImageIcon("./data/sprites/buildings/belt_left.png").getImage();
        icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoHub = new ImageIcon("./data/sprites/buildings/hub.png").getImage();
        icoDecoupeuse = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoDecoupeuse4 = new ImageIcon("./data/sprites/buildings/cutter-quad.png").getImage();
        icoBalancer = new ImageIcon("./data/sprites/buildings/balancer.png").getImage();
    }



    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft");
        setResizable(true);
        setSize(sizeX * pxCase, sizeX * pxCase);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        grilleIP = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille


        tabIP = new ImagePanel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ImagePanel iP = new ImagePanel();

                tabIP[x][y] = iP; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )

                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;
                // écouteur de clics
                iP.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mousePressed = false;
                        jeu.press(xx, yy);
                        System.out.println(xx + "-" + yy);
                    }
                    private Direction calculerDirection(int x1, int y1, int x2, int y2) {
                        if (x2 > x1) return Direction.East;
                        if (x2 < x1) return Direction.West;
                        if (y2 > y1) return Direction.South;
                        if (y2 < y1) return Direction.North;
                        return Direction.North; // Par défaut
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) { //pour être sur si le bouton est enffoncé
                            // Si on vient d'une case adjacente


                            if (lastX != -1 && (lastX != xx || lastY != yy)) {
                                Direction nouvelleDir = calculerDirection(lastX, lastY, xx, yy);
                                System.out.println(nouvelleDir);


                                // On pose le tapis avec la direction du mouvement
                                jeu.slideAvecDirection(xx, yy, nouvelleDir);
                            }

                            // On met à jour la position précédente
                            lastX = xx;
                            lastY = yy;
                        }

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousePressed = true;
                        lastX = xx;  // ← ajouter
                        lastY = yy;  // ← ajouter
                        jeu.press(xx, yy);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousePressed = false;
                        lastX = -1;
                        lastY = -1; //à chaque clique on réinitialise

                    }
                });
                iP.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_R: // Touche R pour Rotation
                                jeu.press_r();
                                System.out.println("change direction");
                                break;

                            case KeyEvent.VK_1: // Touche 1 pour Tapis
                                String typeSelectionne = "Tapis";
                                break;

                            case KeyEvent.VK_2: // Touche 2 pour Mine
                                String typeSelectionne2 = "Mine";
                                break;

                            case KeyEvent.VK_ESCAPE: // Annuler la sélection
                                String typeSelectionne3 = "Selection";
                                break;
                        }
                    }
                });

                iP.setFocusable(true); // Autorise le composant à recevoir le focus


                grilleIP.add(iP);
            }
        }

        add(grilleIP);
    }


    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabIP)
     */
    private void mettreAJourAffichage() {


        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                tabIP[x][y].setBackground((Image) null);

                tabIP[x][y].setFront(null);
                tabIP[x][y].setShape(null);

                Case c = plateau.getCases()[x][y];

                Machine m = c.getMachine();
                Item g = c.getGisement();

                if (m != null) {

                    if (m instanceof Tapis) {
                        Tapis tapis = (Tapis) m;
                        if(tapis.getD() == Direction.North && tapis.getDirection1() == Direction.South) {
                            tabIP[x][y].setBackground(icoTapisHaut);
                        }else if(tapis.getD() == Direction.South && tapis.getDirection1() == Direction.North) {
                            tabIP[x][y].setBackground(icoTapisBas);
                        }else if(tapis.getD() == Direction.East && tapis.getDirection1() == Direction.West){
                            tabIP[x][y].setBackground(icoTapisDroite_Gauche);
                        }else if(tapis.getD() == Direction.West && tapis.getDirection1() == Direction.East){
                            tabIP[x][y].setBackground(icoTapisGauche_Droite);
                        }else if(tapis.getD() == Direction.West && tapis.getDirection1() == Direction.North || tapis.getD() == Direction.North && tapis.getDirection1() == Direction.West){
                            tabIP[x][y].setBackground(icoTapisDroite2);
                        } else if(tapis.getD() == Direction.East && tapis.getDirection1() == Direction.North || tapis.getD() == Direction.North && tapis.getDirection1() == Direction.East){
                            tabIP[x][y].setBackground(icoTapisGauche2);
                        }else if(tapis.getD() == Direction.West && tapis.getDirection1() == Direction.South || tapis.getD() == Direction.South && tapis.getDirection1() == Direction.West){
                            tabIP[x][y].setBackground(icoTapisGauche);
                        }else if(tapis.getD() == Direction.East && tapis.getDirection1() == Direction.South || tapis.getD() == Direction.South && tapis.getDirection1() == Direction.East){
                            tabIP[x][y].setBackground(icoTapisDroite);
                        }

                    } else if (m instanceof Poubelle) {
                        tabIP[x][y].setBackground(icoPoubelle);
                    } else if (m instanceof Mine) {
                        tabIP[x][y].setBackground(icoMine);
                    } else if (m instanceof HubMain) {
                        tabIP[x][y].setBackground(icoHub);
                    } else if (m instanceof HubWorker) {
                        tabIP[x][y].setBackground(icoHub);

                    } else if (m instanceof Balancer) {
                        tabIP[x][y].setBackground(icoBalancer);
                    }
                    else if (m instanceof Decoupeuse || m instanceof  DecoupeuseWorker) {
                            // afficher la même icône que la Decoupeuse principale
                            tabIP[x][y].setBackground(icoDecoupeuse);
                        }

                    Item current = m.getCurrent();

                    if (current instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) current);
                    }
                    if (current instanceof ItemColor) {
                        // tabIP[x][y].setFront(); TODO : placer l'icone des couleurs approprié
                    }


                }
                // On n'affiche le gisement que s'il n'y a pas de machine par-dessus
                else if (g != null) {

                    // on prend le string du gisement pour le dessiner
                    if (g instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) g);
                    }
                }
            }
        }
        grilleIP.repaint();


    }

    @Override
    public void update(Observable o, Object arg) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mettreAJourAffichage();
            }
        });

    }
}
