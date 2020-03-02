/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AuxClasses.Bag;
import Monitors.ArrivalLounge;
import Monitors.ArrivalTerminalExit;
import Monitors.ArrivalTerminalTransfer;
import Monitors.BaggageCollection;
import Monitors.BaggageReclaimOffice;
import Monitors.DepartureTerminalEntrance;
import Monitors.DepartureTerminalTransfer;
import Monitors.GeneralRepository;
import Monitors.TemporaryStorageArea;

/**
 * Que terminam a sua viagem no aeroporto ou ainda continuam 
 * em transito (seguir para outra viagemm)
 * @author lenin
 */
public class Passenger implements Runnable{
     //atributos
    private int id;
    private Bag[] bag; //CONJUNTO DE MALAS?
    private boolean isFinalDst = false;
    private boolean success = false;
    private int numberOfBags = 0;
    private ArrivalLounge al;
    private BaggageCollection bc;
    private GeneralRepository gr;
    private TemporaryStorageArea tsa;
    private BaggageReclaimOffice bro;
    private ArrivalTerminalTransfer att;
    private DepartureTerminalTransfer dtt;
    private ArrivalTerminalExit ate;
    private DepartureTerminalEntrance dte;
    
    //construtor
    public Passenger(int threadID, int numberOfBags, ArrivalLounge al, BaggageCollection bc, GeneralRepository gr, TemporaryStorageArea tsa, BaggageReclaimOffice bro, ArrivalTerminalTransfer att, DepartureTerminalTransfer dtt, ArrivalTerminalExit ate, DepartureTerminalEntrance dte  ){
        this.id = threadID;
        this.numberOfBags = numberOfBags;
        this.al = al;
        this.bc = bc;
        this.gr = gr;
        this.tsa = tsa;
        this.bro = bro;
        this.att = att;
        this.dtt = dtt;
        this.ate = ate;
        this.dte = dte;
        
    }

    /*LifeCycle*/
    @Override
    public void run() {
        isFinalDst = al.whatShouldIDo();
        /*DESTINO*/
        if(isFinalDst){
            if(numberOfBags == 0){
                ate.goHome();
            }
            else{
                for(int i = 0; i < numberOfBags; i++ ){
                   
                    success = bc.goCollectABag();
                    if(!success){
                        break;
                    }
                }
                /* Se perderem alguma mala, vão fazer queixa da mesma RECLAIM OFFICE
                e só depois é que vão para ARRIVAL TERMINA EXIT*/
                if(!success){
                    bro.reportMissingBags();
                }
                /* Se tiverem as malas todas é que vão para ARRIVAL TERMINA EXIT*/
                ate.goHome();
            }
        }
        /*TRANSITO*/
        else{
            att.takeABus();
            att.enterTheBus();
            dtt.leaveTheBus();
            dte.prepareNextLeg();
        }
    }
}
