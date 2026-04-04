package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;

public class MixerWorker extends Machine{
    private Mixer master;
    public MixerWorker(Mixer master){
        this.master = master;
    }

    @Override
    public void work() {
        if(!current.isEmpty() && master.getCouleur() == null){
            ItemColor i = (ItemColor) current.removeFirst();

            master.setCouleur(i);
        }

    }
    @Override
    public void send(){
        //ne doit rien envoyer
    }
}
