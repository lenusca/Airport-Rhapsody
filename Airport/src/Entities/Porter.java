/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Monitors.BaggageCollection;
import Monitors.TemporaryStorageArea;

/**
 *
 * @author lenin
 */
public class Porter {
    //Atributos
    private TemporaryStorageArea tsa;
    private int threadID;
    //construtor
    public Porter(int threadID, BaggageCollection bc, TemporaryStorageArea tsa){
        this.tsa = tsa;
        this.threadID = threadID;

    }

    public void lifeCycle(){
        Bag bag;
        boolean planeOldEmpty;
        //espera por um aviao
        while(arrivalLounge.takeARest != 'E'){
            boolean planeOldEmpty = false;
            
            while(!arrivalLounge.planeOldEmpty){
                bag = tryToCollectABag();
                if(bag == null){
                    planeOldEmpty = true;
                }else if(bag.getStatus() == 'T'){
                    TMPS.curryItToAppropriateStore(bag);
                }else{
                    BCP.curryItToAppropriateStore(bag);
                }
            }
            arrivalLounge.noMoreBagstoCollect();
        }
    }
}
