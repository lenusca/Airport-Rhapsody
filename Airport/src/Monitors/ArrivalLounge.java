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
    private boolean allDone = false;
    private LinkedList<Bag> bags = new LinkedList<>();
    // Repository
    GeneralRepository gr;
    

    public ArrivalLounge(GeneralRepository gr, int numFlight, int numPassenger){
        this.gr = gr;
        this.numFlight = numFlight;
        this.numPassenger = numPassenger;
    }
    
    /**
    *
    * <p> Ultimo passageiro a chegar ao Arrival Lounge acorda o porter, para este começar na sua recolha de malas. Existem dois tipos de passageiros: o que termina a viagem neste aeroporto e o que segue para o cais de transferência</p>
    * @param status  FDT(passageiro no destino final) ou TRF(passageiro em trânsito)
    * @param threadID  threadID do passageiro
    * @param nr número de malas do passageiro
    * @param nflight  número do voo
    * @return <p> true, passageiros terminam a viagem neste aeroporto</p>
    *         <p> false, se passageiros não terminam a viagem neste aeroporto </p>
    */
    public synchronized boolean whatShouldIDo(String status, int threadID, int nr, int nflight) {
        this.flight = nflight;
        gr.numFlight = flight;
        gr.setPassengerSetup("WSD", threadID, status, nr);
        countPassenger++;
        
        if(countPassenger == numPassenger){
            System.out.println("WhatShouldIDo() --> WAKEN UP THE PORTER");
            notifyAll();      //Acorda o Porter
            if(this.flight ==this.numFlight ){
                this.allDone = true;
            }
        }
        
        if(status.equals("FDT")){
            return true;            
        }
        else{
            return false;
        }
        
    }
    
    /**
    *
    * <p> O porter vai ao conjunto de malas que existem no Arrival Lounge e vai buscar uma e somente uma mala para levar para o destino do dono da mala (Passageiro): </p>
    * <p>1. Leva para o Temporary Storage Area, se o dono da mala tratar-se de um passageiro em trânsito </p>
    * <p>2. Leva para o Baggage Collection Point, se o dono da mala tratar-se de um passageiro que se encontra no destino final </p>
    * @return mala
    */
    public synchronized Bag tryToCollectABag() {
        gr.setPorterState("APLH");         
        Bag b = bags.pollFirst();
        gr.numberBags(bags.size());
        return b;
    }
    
    /**
    *
    * <p> Não existem mais malas no Arrival Lounge, os passageiros que estavam a espera da sua mala e não a encontraram são avisados que já não existem mais malas para recolher </p>
    *    
    */
    public synchronized void noMoreBagstoCollect() {
        if(bags.isEmpty()){
            countPassenger = 0;
            notifyAll();            
        }
    }
    
    
    /**
    *
    * <p> Determina se já terminou ou não o ciclo de vida do porter, isto é, se terminaram o número de voos deste aeroporto </p>
    *    @return <p> true, acabou o número de voos, o seu ciclo de vida chegou ao fim </p>
    *            <p> false, não terminou o número de voos e ainda não chegaram todos ao aeroporto </p>
    */
    public synchronized boolean takeARest() {
        gr.setPorterState("WPTL");
        /*
            Aguardar para ser acordado pelo 6º passageiros, 
            e se os passageiros não tiverem mala vai a mesma trabalhar
        */
        if((this.flight <= this.numFlight) && (this.countPassenger < this.numPassenger && !allDone)){ 
            try{
                wait();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            return false;  
        }
        
        return true;
    }
    
    /***************************************** FUNÇÕES AUXILIARES**********************************************/
    
    /** 
    * 
    *<p>Adiciona uma mala ao conjunto de malas do Arrival Lounge</p>
    *@param bag malas que chegam do voo
    * 
    */
    public synchronized void addBag(Bag bag){
        this.bags.add(bag);
        gr.numOfBags = gr.numOfBags + 1;
    }
    
    /** 
    * 
    * <p>Verifica se existem malas no Arrival Lounge </p>
    * 
    *    @return <p> true, se não há malas </p>
    *            <p> false, se houver malas </p>
    *
    */
    public synchronized boolean BagsEmpty(){
        if(bags.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
    
    /** 
    * 
    * <p>Calcula o número de malas existentes no Arrival Lounge</p>
    * 
    *    @return total de malas no Arrival Lounge
    *
    */
    public synchronized int BagsSize(){
        return bags.size();
    }
   
     
 
    
}
