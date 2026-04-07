/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;

public class Case {


    protected Plateau plateau;
    protected Machine machine;
    protected Item gisement; // certaines cases sont des gisements, pour placer des mines


    public void setMachine(Machine m) {
        // set machine avec par defaut la direction a north

        machine = m;
        m.setCase(this);
    }
    public void setMachine(Machine m, Direction d) {
        //setmachine avec le choix de la direction
        m.setD(d);
        machine = m;
        m.setCase(this);
    }

    public Machine getMachine() {
        return machine;
    }

    public Case(Plateau _plateau) {
        machine = null;
        plateau = _plateau;
        gisement = null;
    }
    public Case getVoisine(Direction d) {
        if (plateau == null)
        {
            return null;
        }
        return plateau.getCase(this, d);
    }


    public Item getGisement() {
        return gisement;
    }
    public void setGisement(Item item) {
        this.gisement = item;
    }

}