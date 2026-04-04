package modele.item;

public class ItemColor extends Item {
    private Color color;
    private char Codecolor;

    public ItemColor(char Codecolor) {
        this.Codecolor = Codecolor;
        switch (Codecolor) {
            case 'r' : color = Color.Red; break;
            case 'b' : color = Color.Blue; break;
            case 'w' : color = Color.White; break;
            case 'y' : color = Color.Yellow; break;
            case 'g' : color = Color.Green; break;
            case 'p' : color = Color.Purple; break;
            case 'c' : color = Color.Cyan; break;

            case '-' : color = Color.Grey; break;

        }
    }


    public void transform(Color add) { // faire varier la couleur suivant la couleur ajoutée

    }

    public Color getColor() {
        return color;
    }

    public char getCodecolor() {
        return Codecolor;
    }


}
