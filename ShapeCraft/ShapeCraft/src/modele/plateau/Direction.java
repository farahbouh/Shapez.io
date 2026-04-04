package modele.plateau;

public enum Direction {
    North(0, -1), South(0, 1), East(1, 0), West(-1, 0), Bizarre(1,-1);//test pour Decoupeuse
    int dx;
    int dy;
    private Direction(int _dx, int _dy) {
        dx = _dx;
        dy = _dy;
    }


    public Direction next() { //permet de faire tourner la direction de 90degrée quand on l'appel
        switch (this) {
            case North: return East;
            case East:  return South;
            case South: return West;
            case West:  return North;
            default:    return North; // de base au nord
        }
    }
    public Direction inverse(){
        switch (this) {
            case North:
                return South;
            case East:
                return West;
            case South:
                return North;
            case West:
                return East;
            default:
                return North;
        }
    }

    public int[] addition(int x, int y) {
        return new int[] { x + this.dx, y + this.dy };
    }

}
