/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import AuxClasses.Bag;
import java.util.*;

/**
 *
 * @author lenin
 */
public class TemporaryStorageArea {
    private LinkedList<Bag> bags = new LinkedList();
    private GeneralRepository gr;
    
    public TemporaryStorageArea(GeneralRepository gr){
        this.gr = gr;
    }
    
    /**
    *
    * <p> Porter traz para Temporary Storage Area as malas dos passageiros que não têm como destino este aeroporto </p>
    *    @param bag mala que vem do Arrival Lounge
    *  
    */
    public synchronized void curryItToAppropriateStore(Bag bag) {
        bags.add(bag);
        gr.addBagsStoreroom();
        gr.setPorterState("ASTR");   
    }
    
}
