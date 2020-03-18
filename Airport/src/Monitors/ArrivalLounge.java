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
    /* Operação não há mais malas para retirar do avião (originada pelo PORTER )*/
    public synchronized void noMoreBagstoCollect() {
        System.out.println("AL -> noMoreBagsToCollect");
        while(bags.isEmpty()){
            try{
                wait();             //O porter fica aguardar pelo proximo avião
            }catch(InterruptedException e){}
        }
        
        if(bags.isEmpty()){
            notifyAll();            //acorda os passageiros que não têm mala
        }
    }
    /*Carrega 1 mala de cada vez */
    /*Adormecer os passageiros*/
    /*  @return a mala        */
    public synchronized Bag tryToCollectABag() {
        gr.setPorterState("APLH");
        gr.generateLog();
        gr.numOfBags = gr.numOfBags - 1; 
        Bag b = bags.pollFirst();
        if(bags.isEmpty()){
           notifyAll();            //acorda os passageiros que não têm mala
        }
        System.out.print("AL-tryToCollectABAG:"+b);
        return b;
    }
    
    /* false-passageiros não terminam a viagem neste aeroporto, seguem para o cais de transferencias */
    /* true-passageiros terminam a viagem neste aeroporto, vao buscar a bagagem se tiverem*/
    /* Ultimo passageiro acorda o porter */
    public synchronized boolean whatShouldIDo(char status, int threadID) {
        //System.out.println(countPassenger);
        gr.setPassengerState("WSD", threadID);
        countPassenger++;
        if(countPassenger == this.numPassenger){
            System.out.println("WhatShouldIDo() --> WAKEN UP THE PORTER");
            notifyAll();      //Acorda o Porter
        }
        if(status == 'E' ){
            return true;            
        }
        else{
            return false;
        }
       
        
    }
    /*espera por um aviao*/
    /* dencansar - originada pelo porter
    *    @return <li> true, descansa, o seu ciclo de vida chegou ao fim
    *            <li> false, o seu ciclo ainda não chegou ao fim
    */
    public synchronized boolean takeARest() {
        System.out.println(flight);
        gr.numFlight = flight;
        
        while(flight==this.numFlight){ //aguarda enquanto nao terminaram os voos
            try{
                wait();
            }catch(InterruptedException e){
                return true;
                //Thread.currentThread().interrupt();
            }
        }
        flight += 1;
        return false;   
        
        
        
    }
    
    
    
}
