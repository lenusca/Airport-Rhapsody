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
    private int totalPassengers = 6;
    private LinkedList<Bag> bags = new LinkedList<>();
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
        this.flight = nflight;
        gr.numFlight = flight;
        gr.setPassengerSetup("WSD", threadID, status, nr);
        countPassenger++;
        
        if(countPassenger == numPassenger){
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
        gr.numberBags(bags.size());
        return b;
    }
    
    /* Operação não há mais malas para retirar do avião (originada pelo PORTER )*/
    public synchronized void noMoreBagstoCollect() {
        if(bags.isEmpty()){
            countPassenger = 0;
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
        /*
            Aguardar para ser acordado pelo 6º passageiros, 
            e se os passageiros não tiverem mala vai a mesma trabalhar
        */
        if((this.flight <= this.numFlight) && (this.countPassenger < this.numPassenger)){ 
            try{
                wait();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            return false;  
        }
        
        return true;
    }
    
    /* FUNÇÕES AUXILIARES */
    
    /*Adicionar a linkedlist a mala*/
    public synchronized void addBag(Bag bag){
        this.bags.add(bag);
        gr.numOfBags = gr.numOfBags + 1;
    }
    
    /* Verifica se a linkedlist de malas no arrival lounge está vazia
    *    @return <li> true, se não houver malas na LinkedList
    *            <li> false, se houver malas
    */
    public synchronized boolean BagsEmpty(){
        if(bags.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
    
    /*BAGS SIZE*/
    public synchronized int BagsSize(){
        return bags.size();
    }
    
    /*LEAVE THE AIROPORT*/
    public synchronized int decrementPassengerSize(){
        this.totalPassengers = totalPassengers - 1;
        System.out.println(totalPassengers);
        return this.totalPassengers;
    }
     
 
    
}
