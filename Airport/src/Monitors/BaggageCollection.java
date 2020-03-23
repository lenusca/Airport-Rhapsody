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
    // FLAGS
    //flag para avisar quando já não ha mais malas para recolher
    private boolean allBagsAtColletionPoint = false;

     /*coloca a bagagem no tapete
       adiciona na linkedlist a bag*/
    public BaggageCollection(GeneralRepository gr){
        this.gr = gr;
    }
    
    public synchronized void curryItToAppropriateStore(Bag bag, boolean finish) {
        this.allBagsAtColletionPoint = finish;
        bags.add(bag);
        gr.numOfBagsConveyor = gr.numOfBagsConveyor + 1;
        gr.setPorterState("ALCB");
        notifyAll();            //chegou malas ao tapete de recolha
    }
   
    /*true- tem a mala*/
    /*false- nao tem mala, seguem para o gabinete de reclamaçao*/
    public synchronized boolean goCollectABag(int threadID) {
        gr.setPassengerState("LCP", threadID);
        boolean notfindBag = true;
        while((bags.isEmpty() || (notfindBag = doesNotContainBag(threadID))) && !allBagsAtColletionPoint ){      //passageiro espera enquanto não há malas
            try{ 
                wait();
            }
            catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
        }
        
        if(!notfindBag){
            return true;
        }
        return false;
        
          
    }
    
    /* Função auxiliar
    *    @return <li> true, se houver uma mala do passageiro
    *            <li> false, se não houver nenhuma mala do passageiro
    */
    public synchronized boolean doesNotContainBag(int passengerID){
        for(int i=0; i < bags.size(); i++){
            if(bags.get(i).passenger.getId() == passengerID){
                bags.remove(i);
                gr.numOfBagsConveyor = gr.numOfBagsConveyor - 1;
                gr.na[passengerID] = String.valueOf(Integer.parseInt(gr.na[passengerID]) + 1);
               
                return false;
            }
        }
        return true;
    }
    
    /*Função auxiliar*/
    public synchronized void resetValues(){
        this.allBagsAtColletionPoint = false;
    }
    
    
}
