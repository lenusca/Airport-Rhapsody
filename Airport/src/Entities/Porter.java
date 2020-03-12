/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;
import java.util.*;
import AuxClasses.Bag;
import Monitors.ArrivalLounge;
import Monitors.BaggageCollection;
import Monitors.TemporaryStorageArea;

/**
 * Quem descarrega as malas de um avião, quando pousa, e as leva para (um dos seguintes):
 * - Baggage Collection Point
 * - Temporary Storage Area
 * @author lenin
 */
public class Porter implements Runnable{
    //Atributos
    private TemporaryStorageArea tsa;
    private BaggageCollection bc;
    private ArrivalLounge al;
    private int id;
    //construtor
    public Porter(int threadID, BaggageCollection bc, TemporaryStorageArea tsa, ArrivalLounge al){
        this.tsa = tsa;
        this.id = threadID;
        this.bc = bc;
        this.al = al;
    }
    /*Lifecycle*/
    @Override
    public void run() {
        Bag bag;
        boolean planeOldEmpty;
        /* a espera que um avião chegue */
        while(al.takeARest()){
            planeOldEmpty = false;
            bag = al.tryToCollectABag();
            while( bag != null){

                /* Decidir em qual dos sitios se armazena a mala*/
                /* Se for um passageiro em transito(T)deixa TEMPORARY STORAGE AREA */
                /* Se for um passageiro já no destino deixa BAGGAGE COLLECTION POINT*/
                if(bag.getStatus() == 'T'){
                    tsa.curryItToAppropriateStore(bag);
                }
                else{
                    bc.curryItToAppropriateStore(bag);
                }
            }
            /*Já não há mais malas*/
            al.noMoreBagstoCollect();
        }
    }
}
