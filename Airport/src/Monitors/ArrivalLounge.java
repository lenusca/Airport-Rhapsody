/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import AuxClasses.Bag;

/**
 *
 * @author lenin
 */
public class ArrivalLounge {

    public char takeARest;

    public void noMoreBagstoCollect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /*Carrega 1 mala de cada vez */
    /*Adormecer os passageiros*/
    public synchronized Bag tryToCollectABag() {
        Bag x = new Bag();
        notifyAll();
        return x; 
    }
    
    /* false-passageiros não terminam a viagem neste aeroporto, seguem para o cais de transferencias */
    /* true-passageiros terminam a viagem neste aeroporto, vao buscar a bagagem se tiverem*/
    public synchronized boolean whatShouldIDo() {
        notifyAll();
        return false;
    }
    
}
