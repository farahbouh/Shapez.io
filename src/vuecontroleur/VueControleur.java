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
    private Image icoRotater2;
    private Image icoPainter;
    private JComponent grilleIP;
    private Image icoBalancer;
    private Image icoRotater;
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
    private Image icoMixer;
    private Image icoBleu;
    private Image icoCyan;
    private Image icoVert;
    private Image icoViolet;
    private Image icoGris;
    private Image icoBlanc;
    private Image icoJaune;


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
        icoTapisDroite = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/belt_right.png")).getImage();
        icoTapisGauche_Droite = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/belt_gauche_droite.png")).getImage();
        icoTapisDroite_Gauche = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/belt_droite_gauche.png")).getImage();
        icoTapisDroite2 = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/belt_right_1.png")).getImage();
        icoTapisGauche2 = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/belt_left_1.png")).getImage();

        icoTapisBas = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/belt_bottom.png")).getImage();

        icoTapisHaut = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/belt_top.png")).getImage();
        icoTapisGauche = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/belt_left.png")).getImage();
        icoPoubelle = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/trash.png")).getImage();
        icoMine = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/miner.png")).getImage();
        icoHub = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/hub.png")).getImage();
        icoDecoupeuse = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/cutter.png")).getImage();
        icoDecoupeuse4 = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/cutter-quad.png")).getImage();
        icoBalancer = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/balancer.png")).getImage();
        icoMixer = new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/mixer.png")).getImage();
        icoBleu = new ImageIcon(VueControleur.class.getResource("/data/sprites/colors/blue.png")).getImage();
        icoCyan = new ImageIcon(VueControleur.class.getResource("/data/sprites/colors/cyan.png")).getImage();
        icoVert = new ImageIcon(VueControleur.class.getResource("/data/sprites/colors/green.png")).getImage();
        icoViolet = new ImageIcon(VueControleur.class.getResource("/data/sprites/colors/purple.png")).getImage();
        icoRouge = new ImageIcon(VueControleur.class.getResource("/data/sprites/colors/red.png")).getImage();
        icoGris = new ImageIcon(VueControleur.class.getResource("/data/sprites/colors/uncolored.png")).getImage();
        icoBlanc = new ImageIcon(VueControleur.class.getResource("/data/sprites/colors/white.png")).getImage();
        icoJaune = new ImageIcon(VueControleur.class.getResource("/data/sprites/colors/yellow.png")).getImage();
        icoRotater=new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/rotater.png")).getImage();
        icoRotater2=new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/rotater-ccw.png")).getImage();
        icoPainter=new ImageIcon(VueControleur.class.getResource("/data/sprites/buildings/painter.png")).getImage();
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
                        lastX = xx;  //pour un potentielle glissement on sauvegarde les valeurs
                        lastY = yy;
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
                                break;
                        }
                    }
                });

                iP.setFocusable(true); // Autorise le composant à recevoir le focus


                grilleIP.add(iP);
            }
        }

        setLayout(new BorderLayout());
        add(grilleIP, BorderLayout.CENTER);
        setSize(sizeX * pxCase, sizeY * pxCase + 60);

// Barre du bas
        JPanel barre = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8)); //Flowlayout car elle permet de mettre des boutons de gauche a droite
        barre.setBackground(new Color(40, 40, 40));
        barre.setPreferredSize(new Dimension(0, 60)); //fixe hauteur a 60

        String[] noms = {"Tapis", "Mine", "Decoupeuse", "Decoupeuse2", "Poubelle", "Mixer", "Rotater", "Rotater2", "Balancer", "Painter1"};
        Image[] icones = {icoTapisHaut, icoMine, icoDecoupeuse, icoDecoupeuse4, icoPoubelle, icoMixer, icoRotater,icoRotater2, icoBalancer,icoPainter};
        for (int i = 0; i < noms.length; i++) {
            final String nom = noms[i];
            JButton btn = new JButton(nom, new ImageIcon(icones[i].getScaledInstance(32, 32, Image.SCALE_SMOOTH))); //image en plus petit 32*32
            btn.setFocusable(false); //prend pas evenement clavier
            btn.addActionListener(e -> jeu.setMachineSelectionnee(nom));
            barre.add(btn);
        }

        add(barre, BorderLayout.SOUTH);
    }


    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabIP)
     */
    private void mettreAJourAffichage() {


        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                tabIP[x][y].resetTile();
                tabIP[x][y].setRotationAngle(0);
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
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));
                    } else if (m instanceof Mixer) {
                        tabIP[x][y].setBackground(icoMixer);
                        tabIP[x][y].setTile(0, 0, 2, 1);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));
                    } else if (m instanceof MixerWorker) {
                        tabIP[x][y].setBackground(icoMixer);
                        tabIP[x][y].setTile(1, 0, 2, 1);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));
                    } else if (m instanceof Mine) {
                        tabIP[x][y].setBackground(icoMine);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));
                    } else if (m instanceof HubMain) {
                        tabIP[x][y].setBackground(icoHub);
                    } else if (m instanceof HubWorker) {
                        tabIP[x][y].setBackground(icoHub);
                    } else if (m instanceof Painter1) {
                        tabIP[x][y].setBackground(icoPainter);
                        tabIP[x][y].setTile(0, 0, 2, 1);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));
                    } else if (m instanceof PainterWorker) {
                        tabIP[x][y].setBackground(icoPainter);
                        tabIP[x][y].setTile(1, 0, 2, 1);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));
                    } else if (m instanceof Balancer) {
                        tabIP[x][y].setBackground(icoBalancer);
                        tabIP[x][y].setTile(0, 0, 2, 1);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));
                    } else if (m instanceof BalancerWorker) {
                        tabIP[x][y].setBackground(icoBalancer);
                        tabIP[x][y].setTile(1, 0, 2, 1);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));
                    } else if (m instanceof Rotater) {
                        Rotater rot = (Rotater) m;
                        int sens = rot.getType();
                        if (sens == 0) {
                            tabIP[x][y].setBackground(icoRotater);
                            tabIP[x][y].setTile(0, 0, 1, 1);
                            tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));

                        } else {
                            tabIP[x][y].setBackground(icoRotater2);
                            tabIP[x][y].setTile(0, 0, 1, 1);
                            tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));

                        }
                    } else if (m instanceof RotaterWorker) {
                        RotaterWorker rw = (RotaterWorker) m;
                        Rotater parent = rw.getParent();
                        if (parent != null && parent.getType() == 1) {
                            tabIP[x][y].setBackground(icoRotater);
                            tabIP[x][y].setTile(0, 0, 1, 1);
                            tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));

                        } else {
                            tabIP[x][y].setBackground(icoRotater2);
                            tabIP[x][y].setTile(0, 0, 1, 1);
                            tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));

                        }
                    } else if (m instanceof Decoupeuse) {
                        int type = ((Decoupeuse) m).getType();
                        Image ico;
                        if (type == 2) {
                            ico = icoDecoupeuse4;
                        } else {
                            ico = icoDecoupeuse;
                        }
                        tabIP[x][y].setBackground(ico);
                        tabIP[x][y].setTile(0, 0, type * 2, 1);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));

                    } else if (m instanceof DecoupeuseWorker) {
                        DecoupeuseWorker worker = (DecoupeuseWorker) m;
                        Decoupeuse master = worker.getParent();
                        int type;
                        if (master != null) {
                            type = master.getType();
                        } else {
                            type = 1;
                        }
                        int index = worker.getIndex();
                        Image ico;
                        if (type == 2) {
                            ico = icoDecoupeuse4;
                        } else {
                            ico = icoDecoupeuse;
                        }
                        tabIP[x][y].setBackground(ico);
                        tabIP[x][y].setTile(index, 0, type * 2, 1);
                        tabIP[x][y].setRotationAngle(directionToAngle(m.getD()));

                    }

                    // Affichage de l'item dans la machine
                    Item current = m.getCurrent();
                    if (current instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) current);
                    } else if (current instanceof ItemColor) {
                        ItemColor ic = (ItemColor) current;
                        modele.item.Color couleur = ic.getColor();
                        if (couleur == modele.item.Color.Red)         tabIP[x][y].setFront(icoRouge);
                        else if (couleur == modele.item.Color.Blue)   tabIP[x][y].setFront(icoBleu);
                        else if (couleur == modele.item.Color.Cyan)   tabIP[x][y].setFront(icoCyan);
                        else if (couleur == modele.item.Color.Green)  tabIP[x][y].setFront(icoVert);
                        else if (couleur == modele.item.Color.Purple) tabIP[x][y].setFront(icoViolet);
                        else if (couleur == modele.item.Color.White)  tabIP[x][y].setFront(icoBlanc);
                        else if (couleur == modele.item.Color.Yellow) tabIP[x][y].setFront(icoJaune);
                        else if (couleur == modele.item.Color.Grey)   tabIP[x][y].setFront(icoGris);
                    }
                }
// On n'affiche le gisement que s'il n'y a pas de machine par-dessus
                else if (g != null) {

                    // on prend le string du gisement pour le dessiner
                    if (g instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) g);
                    } else if (g instanceof ItemColor) {
                        ItemColor ic = (ItemColor) g;
                        modele.item.Color couleur = ic.getColor();
                        if (couleur == modele.item.Color.Red)         tabIP[x][y].setFront(icoRouge);
                        else if (couleur == modele.item.Color.Blue)   tabIP[x][y].setFront(icoBleu);
                        else if (couleur == modele.item.Color.Cyan)   tabIP[x][y].setFront(icoCyan);
                        else if (couleur == modele.item.Color.Green)  tabIP[x][y].setFront(icoVert);
                        else if (couleur == modele.item.Color.Purple) tabIP[x][y].setFront(icoViolet);
                        else if (couleur == modele.item.Color.White)  tabIP[x][y].setFront(icoBlanc);
                        else if (couleur == modele.item.Color.Yellow) tabIP[x][y].setFront(icoJaune);
                        else if (couleur == modele.item.Color.Grey)   tabIP[x][y].setFront(icoGris);
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
    /** Convertit une Direction en angle de rotation en degrés (sens horaire).
     *  North = 0°, East = 90°, South = 180°, West = 270° */
    private int directionToAngle(Direction d) {
        switch (d) {
            case East:  return 90;
            case South: return 180;
            case West:  return 270;
            default:    return 0;
        }
    }

}
