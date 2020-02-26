/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

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
 *
 * @author lenin
 */
public class Passenger {
     //atributos
    private int id;
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

    private void goHome() {
    }

    /* false-passageiros não terminam a viagem neste aeroporto, seguem para o cais de transferencias */
    /* true-passageiros terminam a viagem neste aeroporto, vao buscar a bagagem se tiverem*/
    private boolean whatShouldIDo() {
        return false;
    }
    /*true-tem a mala*/
    /*false-nao tem mala, seguem para o gabinete de reclamaçao*/
    //vao pegar na mala
    //baggage collection point
    private boolean goCollectABag() {
        return false;
    }
    
    //ir para o gabinete de reclamaçao de bagagens
    //baggage reclaim office
    private void reportMissingBags() {
    }

    private void prepareNextLeg() {
    }

    private void leaveTheBus() {
    }

    private void enterTheBus() {
    }

    private void takeABus() {
    }

    public void lifeCycle(){
        
        isFinalDst = whatShouldIDo();
        //destino
        if(isFinalDst){
            if(numberOfBags == 0){
                goHome();
            }
            else{
                for(int i = 0; i < numberOfBags; i++ ){
                    success = goCollectABag();
                    if(!success){
                        break;
                    }
                }
                if(!success){
                    reportMissingBags();
                }
                goHome();
               
            }
        }
        //em transito
        else{
            takeABus();
            enterTheBus();
            leaveTheBus();
            prepareNextLeg();
        }

        
    }
}
