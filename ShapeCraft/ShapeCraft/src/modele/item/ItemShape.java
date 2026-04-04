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
        switch(l) {
            case one : return new SubShape[] {tabSubShapes[0], tabSubShapes[1], tabSubShapes[2], tabSubShapes[3]}; // forme de base
            case two: return new SubShape[] {tabSubShapes[4], tabSubShapes[5], tabSubShapes[6], tabSubShapes[7]}; // decoration
            case three: return new SubShape[] {tabSubShapes[8], tabSubShapes[9], tabSubShapes[10], tabSubShapes[11]}; //decoration de decoration
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    public Color[] getColors(Layer l) {
        switch(l) { //meme sys que subshapes
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
                case '-' : tabSubShapes[i] = SubShape.None; break;
                case 'S' : tabSubShapes[i] = SubShape.Quarter_circle; break;
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

    public ItemShape[] Cut() {

        String codedebut = this.getCode();
        int mid = codedebut.length() / 2;

        String firstHalf = codedebut.substring(0, mid);
        String secondHalf = codedebut.substring(mid);

        String result1 = firstHalf + "-".repeat(mid);
        String result2 = "-".repeat(mid) + secondHalf;

        return new ItemShape[] {
                new ItemShape(result1),
                new ItemShape(result2)
        };
    }
    public ItemShape[] Cut4() {
        String code = this.getCode();
        int len = code.length();
        int quart = len / 4;

        String part1 = code.substring(0, quart);
        String part2 = code.substring(quart, 2 * quart);
        String part3 = code.substring(2 * quart, 3 * quart);
        String part4 = code.substring(3 * quart, len);

        String result1 = part1 + "-".repeat(len - quart);
        String result2 = "-".repeat(quart) + part2 + "-".repeat(len - 2 * quart);
        String result3 = "-".repeat(2 * quart) + part3 + "-".repeat(len - 3 * quart);
        String result4 = "-".repeat(3 * quart) + part4;

        return new ItemShape[] {
                new ItemShape(result1),
                new ItemShape(result2),
                new ItemShape(result3),
                new ItemShape(result4)
        };
    }

    public void Color(Color c) {

    }

    public String mixShapeAndColor(ItemShape colorCode) {
        // On utilise StringBuilder pour construire la chaîne efficacement
        StringBuilder result = new StringBuilder();

        // On parcourt les 8 caractères, 2 par 2 (4 quadrants)
        for (int i = 0; i < 8; i += 2) {
            char shape = this.getCode().charAt(i);
            char color = colorCode.getCode().charAt(i + 1);

            // LOGIQUE :
            // Si la forme est '-', alors il n'y a pas de couleur possible -> '-'
            // Sinon, on prend la couleur du deuxième string
            if (shape == '-') {
                result.append("-"); // Pas de forme
                result.append("-"); // Donc pas de couleur
            } else {
                result.append(shape); // On garde la forme
                result.append(color); // On applique la couleur
            }
        }

        return result.toString();
    }

}
