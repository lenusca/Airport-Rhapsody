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

    public BaggageCollection(GeneralRepository gr){
        this.gr = gr;
    }
    
    /**
    *
    * <p> Porter traz para Baggage Collection as malas dos passageiros que têm como destino este aeroporto </p>
    *    @param bag mala que vem do Arrival Lounge
    *   
    */
    public synchronized void curryItToAppropriateStore(Bag bag) {
        bags.add(bag);
        gr.addBagsCoveyor();
        gr.setPorterState("ALCB");
        notifyAll();            //chegou malas ao tapete de recolha
    }
    
   /**
    *
    * <p> Passageiro vai buscar a sua mala </p>
    *    @param threadID threadID do passageiro
    *    @return <p> true, se encontrou a sua mala </p>
    *            <p> false, se não encontrou a sua mala e segue para o gabinete de reclamação </p>
    */
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
    
    /**
    *
    * <p> Verifica se a mala do passageiro encontra-se nas malas que chegaram ao Baggage Collection Point </p>
    *    @param passengerID threadID do passageiro
    *    @return <p> true, se houver uma mala do passageiro </p>
    *            <p> false, se não houver a mala do passageiro </p>
    */
    public synchronized boolean doesNotContainBag(int passengerID){
        for(int i=0; i < bags.size(); i++){
            if(bags.get(i).passenger.getId() == passengerID){
                bags.remove(i);
                gr.removeBagsCoveyor();
                gr.na[passengerID] = String.valueOf(Integer.parseInt(gr.na[passengerID]) + 1);
                return false;
            }
        }
        return true;
    }
    
    /**
    *
    * <p> Coloca a flag de verificar se todas as malas já foram recolhidas a falso </p>
    *    
    */
    public synchronized void resetValues(){
        this.allBagsAtColletionPoint = false;
    }
    
    /**
    *
    * <p> Se já foram recolhidas todas as malas </p>
    * @param finish <p> true, todas as malas recolhidas </p>
    *               <p> false, nem todas as malas foram recolhidas </p>
    *    
    */
    public synchronized void allBags(boolean finish){
        this.allBagsAtColletionPoint = finish;
        notifyAll();
    }
}
