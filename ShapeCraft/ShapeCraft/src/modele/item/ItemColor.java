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

    public void transform(Color add) {
            // faire varier la couleur suivant la couleur ajoutée
            // on regarde effectue les melanges connues
            if (this.color == Color.Red && add == Color.Blue) {
                this.color = Color.Purple;      // Rouge + Bleu = Violet
            } else if (this.color == Color.Blue && add == Color.Red) {
                this.color = Color.Purple;      // Bleu + Rouge = Violet
            } else if (this.color == Color.Red && add == Color.Green) {
                this.color = Color.Yellow;      // Rouge + Vert = Jaune
            } else if (this.color == Color.Green && add == Color.Red) {
                this.color = Color.Yellow;      // Vert + Rouge = Jaune
            } else if (this.color == Color.Blue && add == Color.Green) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Green && add == Color.Blue) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Red && add == Color.Yellow) {
                this.color = Color.Yellow;
            } else if (this.color == Color.Yellow && add == Color.Red) {
                this.color = Color.Yellow;
            } else if (this.color == Color.Blue && add == Color.Purple) {
                this.color = Color.Purple;
            } else if (this.color == Color.Purple && add == Color.Blue) {
                this.color = Color.Purple;
            } else if (this.color == Color.Green && add == Color.Yellow) {
                this.color = Color.Yellow;
            } else if (this.color == Color.Yellow && add == Color.Green) {
                this.color = Color.Yellow;
            } else if (this.color == Color.Cyan && add == Color.Blue) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Blue && add == Color.Cyan) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Cyan && add == Color.Green) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Green && add == Color.Cyan) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Purple && add == Color.Red) {
                this.color = Color.Purple;
            } else if (this.color == Color.Red && add == Color.Purple) {
                this.color = Color.Purple;
            } else if (this.color == Color.Yellow && add == Color.Cyan) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Cyan && add == Color.Yellow) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Purple && add == Color.Yellow) {
                this.color = Color.Yellow;
            } else if (this.color == Color.Yellow && add == Color.Purple) {
                this.color = Color.Yellow;
            } else if (this.color == Color.Purple && add == Color.Cyan) {
                this.color = Color.Cyan;
            } else if (this.color == Color.Cyan && add == Color.Purple) {
                this.color = Color.Cyan;
            } else {
                this.color = add;               // Par défaut, prend la nouvelle couleur
            }// Par défaut, prend la nouvelle couleur

            // on assigne le nouveau code à la couleur creer
            switch (this.color) {
                case Red:    Codecolor = 'r'; break;
                case Blue:   Codecolor = 'b'; break;
                case Purple: Codecolor = 'p'; break;
                case Yellow: Codecolor = 'y'; break;
                case Green:  Codecolor = 'g'; break;
                case Cyan:   Codecolor = 'c'; break;
                case White:  Codecolor = 'w'; break;
                case Grey:   Codecolor = '-'; break;
            }
    }

    public Color getColor() {
        return color;
    }

    public char getCodecolor() {
        return Codecolor;
    }
}
