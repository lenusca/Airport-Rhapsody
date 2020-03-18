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
    private GeneralRepository gr;
    private LinkedList<Bag> bags = new LinkedList();
     /*coloca a bagagem no tapete
       adiciona na linkedlist a bag*/
    public BaggageCollection(GeneralRepository gr){
        this.gr = gr;
    }
    public synchronized void curryItToAppropriateStore(Bag bag) {
        
        bags.add(bag);
        gr.numOfBagsConveyor = gr.numOfBagsConveyor + 1;
        gr.setPorterState("ALCB");
        notifyAll();            //chegou malas ao tapete de recolha
    }
    
    /*true- tem a mala*/
    /*false- nao tem mala, seguem para o gabinete de reclamaçao*/
    public synchronized boolean goCollectABag(int threadID) {
        gr.setPassengerState("LCP", threadID);
        while(bags.isEmpty()){      //passageiro espera enquanto não há malas
            try{ 
                wait();
            }
            catch (InterruptedException e) {}
        }
        for(int i=0; i<bags.size(); i++){
            if(bags.get(i).passenger.getId() == threadID){
                gr.na[threadID] = gr.na[threadID] + 1;
                bags.remove(i);
                gr.numOfBagsConveyor = gr.numOfBagsConveyor - 1;
                return true;
            }
        }
        return false;
    }
    
}
