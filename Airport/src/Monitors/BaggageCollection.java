/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;
import java.util.*;
import AuxClasses.Bag;

/**
 *
 * @author lenin
 */
public class BaggageCollection {
    private LinkedList<Bag> bags = new LinkedList();
    
    public void curryItToAppropriateStore(Bag bag) {
        bags.add(bag);
        notifyAll();
    }
    
    /*true- tem a mala*/
    /*false- nao tem mala, seguem para o gabinete de reclamaçao*/
    public boolean goCollectABag(int threadID) {
        for(int i=0; i<bags.size(); i++){
            if(bags.get(i).passenger.getId() == threadID){
                bags.remove(i);
                return true;
            }
        }
        return false;
    }
    
}
