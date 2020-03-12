/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import AuxClasses.Bag;
import java.util.LinkedList;


/**
 *
 * @author lenin
 */
public class ArrivalLounge {
    private int countPassenger = 0;
    private int flight = 0;
    public LinkedList<Bag> bags = new LinkedList();

    public synchronized void noMoreBagstoCollect() {
        if(bags.isEmpty()){
            notifyAll();
        }
    }
    /*Carrega 1 mala de cada vez */
    /*Adormecer os passageiros*/
    public synchronized Bag tryToCollectABag() { 
        Bag b = bags.pollFirst();
        if(bags.isEmpty()){
           notifyAll();
        }
        return b;
        
    }
    
    /* false-passageiros não terminam a viagem neste aeroporto, seguem para o cais de transferencias */
    /* true-passageiros terminam a viagem neste aeroporto, vao buscar a bagagem se tiverem*/
    /* Ultimo passageiro acorda o porter */
    public synchronized boolean whatShouldIDo(char status) {
        countPassenger++;
        
        if(countPassenger == 6){
            notifyAll();
        }
        
        if(status == 'E' ){
            return true;
        }
        else{
            return false;
        }
       
        
    }

    public synchronized boolean takeARest() {
        flight++;
        if(flight == 5){
            try{
                wait();
            }catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
            return true;
        }
        else{
            return true;
        }
        
        
    }
    
}
