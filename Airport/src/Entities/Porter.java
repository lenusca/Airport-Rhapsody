/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AuxClasses.Bag;
import Monitors.ArrivalLounge;
import Monitors.BaggageCollection;
import Monitors.TemporaryStorageArea;

/**
 *
 * @author lenin
 */
public class Porter {
    //Atributos
    private TemporaryStorageArea tsa;
    private BaggageCollection bc;
    private ArrivalLounge al;
    private int threadID;
    //construtor
    public Porter(int threadID, BaggageCollection bc, TemporaryStorageArea tsa, ArrivalLounge al){
        this.tsa = tsa;
        this.threadID = threadID;
        this.bc = bc;
        this.al = al;
    }
    // ArrivalLounge e TemporaryStorageArea são regiões onde são efetuadas as operações
    public void lifeCycle(){
        Bag bag;
        boolean planeOldEmpty;
        //espera por um aviao, enquanto não é para sair
        while(al.takeARest != 'E'){
            planeOldEmpty = false;
            
            while(!planeOldEmpty){
                bag = al.tryToCollectABag();
                if(bag == null){
                    planeOldEmpty = true;
                // Decidir em qual dos sitios se armazena a mala, T = transito    
                }else if(bag.getStatus() == 'T'){
                    tsa.curryItToAppropriateStore(bag);
                }else{
                    bc.curryItToAppropriateStore(bag);
                }
            }
            al.noMoreBagstoCollect();
        }
    }
}
