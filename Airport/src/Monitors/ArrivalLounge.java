/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import AuxClasses.Bag;
import Entities.Passenger;
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
    public static Queue<Integer> passengersHome = new LinkedList<>();
    public static Queue<Integer> passengersLeg = new LinkedList<>();
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
        gr.setPassengerSetup("WSD", threadID, status, nr);
        countPassenger++;
        if(countPassenger == this.numPassenger){
            System.out.println("WhatShouldIDo() --> WAKEN UP THE PORTER");
            notifyAll();      //Acorda o Porter
        }
        
        if(status.equals("FDT")){
            passengersHome.add(threadID);
            return true;            
        }
        else{
            passengersLeg.add(threadID);
            return false;
        }
       
        
    }
    
    /*Carrega 1 mala de cada vez */
    /*Adormecer os passageiros*/
    /*  @return a mala        */
    public synchronized Bag tryToCollectABag() {
        gr.setPorterState("APLH");
        gr.numOfBags = bags.size();
        //System.out.println("SIZE: " + bags.size());
        Bag b = bags.pollFirst();
        //System.out.println("SIZE: " + bags.size());
        if(bags.isEmpty()){
           notifyAll();            //acorda os passageiros que não têm mala
        }
        else{
            gr.numOfBags = gr.numOfBags - 1;
        }
        return b;
    }
    
    /* Operação não há mais malas para retirar do avião (originada pelo PORTER )*/
    public synchronized void noMoreBagstoCollect() {
        System.out.println("AL -> noMoreBagsToCollect");
        
        if(bags.isEmpty()){
            notifyAll();            //acorda os passageiros que não têm mala
        }
        
        while(bags.isEmpty()){
            try{
                wait();             //O porter fica aguardar pelo proximo avião
            }catch(InterruptedException e){}
        }
    }
    
    
    /*espera por um aviao*/
    /* dencansar - originada pelo porter
    *    @return <li> true, descansa, o seu ciclo de vida chegou ao fim
    *            <li> false, o seu ciclo ainda não chegou ao fim
    */
    public synchronized boolean takeARest() {
        gr.numFlight = flight;
        gr.setPorterState("WPTL");
        //System.out.println("FLIGHT " + flight);
        if(this.flight != this.numFlight){ //aguarda enquanto nao terminaram os voos
            try{
                wait();
            }catch(InterruptedException e){
                return true;
                //Thread.currentThread().interrupt();
            }
        }
        return false;   
        
        
        
    }
    
    
    
}
