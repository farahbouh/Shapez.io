package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import java.util.ArrayList;
import java.util.List;

public class Decoupeuse extends Machine {
    private int type; // 1 = coupe en 2, 2 = coupe en 4
    private ItemShape[] reserve;

    public Decoupeuse(int type) {

        this.type = type;
    }

    public void send_worker() { // envoie au autre découpeur en fonction du type
         if(reserve != null){
             Case case1 = c.plateau.getCase(c,d.next()); //next car si la direction est nord alors c'est à l'horizonatl

             if(case1 != null){
                 Machine machine1 = case1.getMachine();
                 if(machine1 != null && machine1.current.isEmpty()){
                     if(!reserve[1].getCode().equals("--------")) machine1.current.add(reserve[1]);
                 }
             }// on a fait pour la premiere case qui va forcement être envoyé qu'importe le type maintenant on fait pour le type 2
             if (type == 2){
             Case case2 = case1.plateau.getCase(case1,d.next());
             Case case3 = case2.plateau.getCase(case2,d.next());
             if(case2 != null && case3 != null){
                 Machine machine2 = case2.getMachine();
                 Machine machine3 = case3.getMachine();
                 if(machine2 != null && machine3 != null && machine2.current.isEmpty() && machine3.current.isEmpty()){
                     if(!reserve[2].getCode().equals("--------")) machine2.current.add(reserve[2]);
                     if(!reserve[3].getCode().equals("--------")) machine3.current.add(reserve[3]);

                 }
             }

         }}
         reserve= null;
    }

    @Override
    public void work() {
            Item item = getCurrent();  // getCurrent() retourne null si vide
            if (item != null && item instanceof ItemShape) {
                ItemShape i = (ItemShape) item;
                if(type == 1) {
                    reserve = i.Cut(); // recupere le reste de la forme
                } else if(type == 2) {
                    reserve = i.Cut4();
                }

                current.removeFirst();
                if(!reserve[0].getCode().equals("--------")) current.add(reserve[0]);
                this.send_worker();

            }

        }

    @Override
    public void send() {
        super.send();
    }




    public int getType() {
        return type;
    }
}