/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import AuxClasses.Bag;

import java.util.LinkedList;
import java.util.Queue;


/**
 *
 * @author lenin
 */
public class ArrivalLounge {
    private int countPassenger = 0;
    private int flight = 1;
    private int numPassenger;
    private int numFlight;
    public static LinkedList<Bag> bags = new LinkedList<>();
    // Repository
    GeneralRepository gr;
    
    
    public ArrivalLounge(GeneralRepository gr, int numFlight, int numPassenger){
        this.gr = gr;
        this.numFlight = numFlight;
        this.numPassenger = numPassenger;
    }
    
    /* false-passageiros não terminam a viagem neste aeroporto, seguem para o cais de transferencias */
    /* true-passageiros terminam a viagem neste aeroporto, vao buscar a bagagem se tiverem*/
    /* Ultimo passageiro acorda o porter */
    public synchronized boolean whatShouldIDo(String status, int threadID, int nr, int nflight) {
        //System.out.println(countPassenger);
        this.flight = nflight;
        gr.numFlight = flight;
        gr.setPassengerSetup("WSD", threadID, status, nr);
        countPassenger++;
        
        if(countPassenger == this.numPassenger){
            System.out.println("WhatShouldIDo() --> WAKEN UP THE PORTER");
            notifyAll();      //Acorda o Porter
        }
        
        if(status.equals("FDT")){
            return true;            
        }
        else{
            return false;
        }
        
    }
    
    /*Carrega 1 mala de cada vez */
    /*Adormecer os passageiros*/
    /*  @return a mala        */
    public synchronized Bag tryToCollectABag() {
        gr.setPorterState("APLH");         
        Bag b = bags.pollFirst();
        gr.numOfBags = bags.size();
        System.out.println("MALA "+b);
        return b;
    }
    
    /* Operação não há mais malas para retirar do avião (originada pelo PORTER )*/
    public synchronized void noMoreBagstoCollect() {
        System.out.println("NO MORE BAGS TO COLLECT");
        if(bags.isEmpty()){
            System.out.println("AL -> noMoreBagsToCollect");
            notifyAll();            //acorda os passageiros que não têm mala
        }
    }
    
    
    /*espera por um aviao*/
    /* dencansar - originada pelo porter
    *    @return <li> true, descansa, o seu ciclo de vida chegou ao fim
    *            <li> false, o seu ciclo ainda não chegou ao fim
    */
    public synchronized boolean takeARest() {
        
        gr.setPorterState("WPTL");
        //System.out.println("FLIGHT " + flight);
        System.out.println("MALAS: "+ bags.size());
        
        /*
            Aguardar para ser acordado pelo 6º passageiros, e se os passageiros não tiverem mala vai
        */
        if(this.flight <= this.numFlight){ 
            try{
                wait();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            return false;  
        }
        
        else{
            return true;
        }
        
         
        
        
        
    }
    
    
    
}
