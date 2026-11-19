package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private Image imgBackground;
    private Image imgFront;
    private ItemShape shape;

    private int tileCol  = 0;
    private int tileRow  = 0;
    private int tileCols = 1;
    private int tileRows = 1;

    // Angle de rotation en degrés (0, 90, 180, 270), sens horaire
    private int rotationAngle = 0;

    public void setTile(int col, int row, int totalCols, int totalRows) {
        tileCol = col; tileRow = row; tileCols = totalCols; tileRows = totalRows;
    }

    public void resetTile() {
        tileCol = 0; tileRow = 0; tileCols = 1; tileRows = 1;
    }

    public void setRotationAngle(int angle) {
        this.rotationAngle = angle;
    }

    public void setShape(ItemShape _shape)       { shape = _shape; }
    public void setBackground(Image _img)        { imgBackground = _img; }
    public void setFront(Image _img)             { imgFront = _img; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int bordure      = 1;
        final int xBack        = bordure;
        final int yBack        = bordure;
        final int widthBack    = getWidth()  - bordure * 2;
        final int heightBack   = getHeight() - bordure * 2;

        final int subPartW     = widthBack  / 4;
        final int subPartH     = heightBack / 4;
        final int xFront       = bordure + subPartW;
        final int yFront       = bordure + subPartH;
        final int widthFront   = subPartW * 2;
        final int heightFront  = subPartH * 2;

        g.drawRoundRect(bordure, bordure, widthBack, heightBack, bordure, bordure);

        if (imgBackground != null) {

            // ── 1. Construire l'image source à la taille totale du sprite ──────────
            //    Pour un sprite 2×1 cases, fullW = 2*widthBack, fullH = heightBack
            int fullW = widthBack  * tileCols;
            int fullH = heightBack * tileRows;

            BufferedImage full = new BufferedImage(fullW, fullH, BufferedImage.TYPE_INT_ARGB);
            {
                Graphics gf = full.createGraphics();
                gf.drawImage(imgBackground, 0, 0, fullW, fullH, null);
                gf.dispose();
            }

            // ── 2. Extraire la portion correspondant à cette case ─────────────────
            int srcX = Math.min(tileCol * widthBack,  fullW - widthBack);
            int srcY = Math.min(tileRow * heightBack,  fullH - heightBack);
            BufferedImage portion = full.getSubimage(srcX, srcY, widthBack, heightBack);

            // ── 3. Appliquer la rotation si nécessaire ────────────────────────────
            if (rotationAngle != 0) {
                portion = rotateImage(portion, rotationAngle);
            }

            g.drawImage(portion, xBack, yBack, widthBack, heightBack, this);
        }

        if (imgFront != null) {
            g.drawImage(imgFront, xFront, yFront, widthFront, heightFront, this);
        }

        if (shape != null) {
            SubShape[]          tabS = shape.getSubShapes(ItemShape.Layer.one);
            modele.item.Color[] tabC = shape.getColors(ItemShape.Layer.one);

            for (int i = 0; i < 4; i++) {
                if (tabS[i] == SubShape.None) continue;

                switch (tabC[i]) {
                    case Red:    g.setColor(Color.RED);                  break;
                    case White:  g.setColor(Color.WHITE);                break;
                    case Blue:   g.setColor(Color.BLUE);                 break;
                    case Cyan:   g.setColor(Color.CYAN);                 break;
                    case Green:  g.setColor(Color.GREEN);                break;
                    case Purple: g.setColor(new Color(128, 0, 128));     break;
                    case Yellow: g.setColor(Color.YELLOW);               break;
                    case Grey:   g.setColor(new Color(128, 128, 128));   break;
                    default:     g.setColor(Color.BLACK);                break;
                }

                switch (tabS[i]) {
                    case Square:
                        g.fillRect(
                            xFront + (widthFront  / 2) * ((i >> 1) ^ 1),
                            yFront + (heightFront / 2) * ((i & 1) ^ ((i >> 1) & 1)),
                            widthFront / 2, heightFront / 2);
                        break;
                    case Quarter_circle:
                        g.fillArc(xFront, yFront, widthFront, heightFront, (4 - i) * 90, 90);
                        break;
                }
            }
        }
    }

    /** Fait pivoter un BufferedImage de `angle` degrés dans le sens horaire. */
    private BufferedImage rotateImage(BufferedImage src, int angle) {
        int w = src.getWidth();
        int h = src.getHeight();
        // Pour 90° et 270°, largeur et hauteur s'échangent
        int nw = (angle % 180 != 0) ? h : w;
        int nh = (angle % 180 != 0) ? w : h;
        BufferedImage result = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();
        g2.translate(nw / 2.0, nh / 2.0);
        g2.rotate(Math.toRadians(angle));
        g2.translate(-w / 2.0, -h / 2.0);
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return result;
    }
}
