package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image imgBackground;
    private Image imgFront;
    private ItemShape shape;
    // Ajouter ces 4 champs après les existants
    private int tileCol = 0;
    private int tileRow = 0;
    private int tileCols = 1;
    private int tileRows = 1;

    // Ajouter ces deux méthodes
    public void setTile(int col, int row, int totalCols, int totalRows) {
        tileCol = col; tileRow = row; tileCols = totalCols; tileRows = totalRows;
    }
    public void resetTile() {
        tileCol = 0; tileRow = 0; tileCols = 1; tileRows = 1;
    }

    public void setShape(ItemShape _shape) {
        shape = _shape;
    }

    public void setBackground(Image _imgBackground) {
        imgBackground = _imgBackground;
    }

    public void setFront(Image _imgFront) {
        imgFront = _imgFront;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int bordure= 1;
        final int xBack = bordure;
        final int yBack = bordure;
        final int widthBack = getWidth() - bordure*2;
        final int heigthBack = getHeight() - bordure*2;

        final int subPartWidth = widthBack / 4;
        final int subPartHeigth = heigthBack / 4;

        final int xFront = bordure + subPartWidth;
        final int yFront = bordure + subPartHeigth;
        final int widthFront = subPartWidth*2;
        final int heigthFront = subPartHeigth*2;



        // cadre
        g.drawRoundRect(bordure, bordure, widthBack, heigthBack, bordure, bordure);


        if (imgBackground != null) {
            if (tileCols == 1 && tileRows == 1) {
                // cas normal : afficher l'image entière
                g.drawImage(imgBackground, xBack, yBack, widthBack, heigthBack, this);
            } else {
                // cas multi-case : créer une image à la taille totale, puis extraire la portion
                int fullW = widthBack * tileCols;
                int fullH = heigthBack * tileRows;

                java.awt.image.BufferedImage full = new java.awt.image.BufferedImage(fullW, fullH, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                Graphics gFull = full.createGraphics();
                gFull.drawImage(imgBackground, 0, 0, fullW, fullH, null);
                gFull.dispose();

                int srcX = tileCol * widthBack;
                int srcY = tileRow * heigthBack;
                g.drawImage(full, xBack, yBack, xBack + widthBack, yBack + heigthBack,
                        srcX, srcY, srcX + widthBack, srcY + heigthBack, this);
            }
        }

        if (imgFront != null) {
            g.drawImage(imgFront, xFront, yFront, widthFront, heigthFront, this);
        }


        if (shape != null) {

            // TODO autres layers
            SubShape[] tabS = shape.getSubShapes(ItemShape.Layer.one);
            modele.item.Color[] tabC = shape.getColors(ItemShape.Layer.one);

            for (int i = 0; i < 4; i++) {

                SubShape ss = tabS[i];
                modele.item.Color cc = tabC[i];


                if (ss != SubShape.None) {

                    switch (tabC[i]) {
                        case modele.item.Color.Red:
                            g.setColor(Color.RED);
                            break;
                        case modele.item.Color.White:
                            g.setColor(Color.WHITE);
                            break;
                        case modele.item.Color.Blue:
                            g.setColor(Color.BLUE);
                            break;
                        case modele.item.Color.Cyan:
                            g.setColor(Color.CYAN);
                            break;
                        case modele.item.Color.Green:
                            g.setColor(Color.GREEN);
                            break;
                        case modele.item.Color.Purple:
                            g.setColor(new Color(128, 0, 128));
                            break;
                        case modele.item.Color.Yellow:
                            g.setColor(Color.YELLOW);
                            break;
                        case modele.item.Color.Grey:
                            g.setColor(new Color(128,128,128));
                            break;
                        default:
                            //si on donne aucune couleur alors c'est noir
                            g.setColor(Color.BLACK);
                            break;

                    }

                    switch (ss) {
                        case SubShape.Square:
                            g.fillRect(xFront + (widthFront / 2) * ((i >> 1) ^ 1), yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1)), widthFront / 2, heigthFront / 2);
                            break;

                        case SubShape.Quarter_circle:
                            g.fillArc(xFront, yFront, widthFront, heigthFront, (4-i) * 90, 90);
                            break;
                        // TODO autres formes
                    }
                }
            }
        }
    }


}





