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
    
    public void curryItToAppropriateStore(Bag bag) {
        gr.numOfBagsStoreroom = gr.numOfBagsStoreroom + 1;
        gr.setPorterState("ASTR");
        bags.add(bag);
    }
    
}
