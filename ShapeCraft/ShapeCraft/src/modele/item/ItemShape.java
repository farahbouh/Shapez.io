package modele.item;

public class ItemShape extends Item {
    private SubShape[] tabSubShapes;
    private Color[] tabColors;
    private String code; //permet de collecter plus facilement les items

    public String getCode() {
        return code;
    }

    public enum Layer {one, two, three};

    public SubShape[] getSubShapes(Layer l) {
        switch(l) { // chaque case correspond à une couche
            case one : return new SubShape[] {tabSubShapes[0], tabSubShapes[1], tabSubShapes[2], tabSubShapes[3]}; // forme de base
            case two: return new SubShape[] {tabSubShapes[4], tabSubShapes[5], tabSubShapes[6], tabSubShapes[7]}; // decoration
            case three: return new SubShape[] {tabSubShapes[8], tabSubShapes[9], tabSubShapes[10], tabSubShapes[11]}; //decoration de decoration
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    public Color[] getColors(Layer l) {
        switch(l) { //meme systeme que subshapes
            case one : return new Color[] {tabColors[0], tabColors[1], tabColors[2], tabColors[3]};
            case two : return new Color[] {tabColors[4], tabColors[5], tabColors[6], tabColors[7]};
            case three: return new Color[] {tabColors[8], tabColors[9], tabColors[10], tabColors[11]};
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    /**;
     * Initialisation des formes par chaîne de caractères
     * @param str : codage : (sous forme + couleur ) * (haut-droit, bas-droit, bas-gauche, haut-gauche) * 3 Layers
     *            str.length multiple de 4
     */
    public ItemShape(String str) {
        this.code = str;
        tabSubShapes = new SubShape[str.length()/2 ];
        tabColors = new Color[str.length()/2];

        for (int i = 0; i < 4; i++) { // fait uniquement pour la première couche
            switch (str.charAt(i*2)) {
                case 'C' : tabSubShapes[i] = SubShape.Square; break;
                case 'S' : tabSubShapes[i] = SubShape.Quarter_circle; break;
                case 'F' : tabSubShapes[i] = SubShape.Fan; break;
                case 'T' : tabSubShapes[i] = SubShape.Star; break;
                case '-' : tabSubShapes[i] = SubShape.None; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt(i));
            }

            switch (str.charAt((i*2 + 1))) {
                case 'r' : tabColors[i] = Color.Red; break;
                case 'b' : tabColors[i] = Color.Blue; break;
                case 'w' : tabColors[i] = Color.White; break;
                case 'y' : tabColors[i] = Color.Yellow; break;
                case 'g' : tabColors[i] = Color.Green; break;
                case 'p' : tabColors[i] = Color.Purple; break;
                case 'c' : tabColors[i] = Color.Cyan; break;

                case '-' : tabColors[i] = Color.Grey; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt((i + 1)*2));
            }
        }
    }

    //on crée une copie
    public ItemShape(ItemShape item){
        this.tabColors = item.tabColors;
        this.tabSubShapes = item.tabSubShapes;
    }




    // TODO : écrire l'ensemble des fonctions de transformation souhaitées, définir les paramètres éventuels (sens, axe, etc.)
    public void rotate() {
    // l'item i prend place de l'item j permettant la rotation
        SubShape[] bufferSubShapes = new SubShape[4];
        bufferSubShapes[0] = tabSubShapes[3];
        bufferSubShapes [1] = tabSubShapes[0];
        bufferSubShapes [2] = tabSubShapes[1];
        bufferSubShapes [3] = tabSubShapes[2];

        Color[] bufferColors = new Color[4];
        bufferColors[0] = tabColors[3];
        bufferColors [1] = tabColors[0];
        bufferColors [2] = tabColors[1];
        bufferColors [3] = tabColors[2];

        tabSubShapes = bufferSubShapes;
        tabColors = bufferColors;


    }
    public void stack(ItemShape ShapeSup) { // ShapeSup est empilé sur this

    }

    public ItemShape[] Cut(int nb) {
        String code = this.getCode();
        int len = code.length();
        int taillepart = len / nb;   // taille d'une part (taille / nombre de part attendu)
        ItemShape[] resultats = new ItemShape[nb]; // creation d'un tab pour stocker les valeurs

        for (int i = 0; i < nb; i++) { //construit et ajoute le resultat de la boucle j dans resultats
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < nb; j++)  {//parcours la figure originale et verifie qu'on est bien au jieme morceau / nb attendu
                if (j == i) {

                    sb.append(code.substring(j * taillepart, (j + 1) * taillepart));
                } else {
                    sb.append("-".repeat(taillepart));
                }
            }
            resultats[i] = new ItemShape(sb.toString());
        }
        return resultats;
    }

    public void Color(Color c) {

    }

    public String mixShapeAndColor(ItemColor colorCode) {
        // Utilisation d'un StringBuilder pour construire la chaîne efficacement
        StringBuilder result = new StringBuilder();

        // Parcourt les 8 caractères, 2 par 2 (4 quadrants)
        for (int i = 0; i < 8; i += 2) {
            char shape = this.getCode().charAt(i);
            char color = colorCode.getCodecolor();
            // Si la forme est '-', alors il n'y a pas de couleur possible -> '-'
            // Sinon, on prend la couleur du deuxième string
            if (shape == '-') {
                result.append("-"); //vide
                result.append("-");
            } else {
                result.append(shape); // On garde la forme
                result.append(color); // On applique la couleur
            }
        }
        return result.toString();
    }

}
