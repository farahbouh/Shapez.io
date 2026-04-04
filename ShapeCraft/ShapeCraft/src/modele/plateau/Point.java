package modele.plateau;

public class Point {
    public  int x;
    public  int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x,int y, Direction D){
        if(D == Direction.North){
            this.x = x;
            this.y = y+1;

        } else if(D == Direction.South){
            this.x = x;
            this.y = y-1;

        }
        else if(D == Direction.East){
            this.x = x+1;
            this.y = y;

        }
        else if(D == Direction.West){
            this.x = x-1;
            this.y = y;

        }
    }

}
