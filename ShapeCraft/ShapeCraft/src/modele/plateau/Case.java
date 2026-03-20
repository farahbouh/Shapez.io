/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Case {


    protected Plateau plateau;
    protected Machine machine;
    protected Item gisement; // certaines cases sont des gisements, pour placer des mines


    public void setMachine(Machine m) {
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

    public void setGisement(String code) {

        if (code.length() <= 2) {
            // Implémentation simplifiée pour ItemColor si nécessaire
            // this.gisement = new ItemColor(code);
        } else {
            this.gisement = new ItemShape(code);
        }
    }

    public Item getGisement() {
        return gisement;
    }

}