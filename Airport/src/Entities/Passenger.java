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
import java.util.Random;


/**
 * Que terminam a sua viagem no aeroporto ou ainda continuam 
 * em transito (seguir para outra viagemm)
 * @author lenin
 */
public class Passenger implements Runnable{
     //atributos
    private int id;
    private boolean isFinalDst = false;
    private boolean success = false;
    private boolean lostBag = false;
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
    public char status;
    
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

    public int getId() {
        return id;
    }

    public boolean isIsFinalDst() {
        return isFinalDst;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getNumberOfBags() {
        return numberOfBags;
    }

    public char getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsFinalDst(boolean isFinalDst) {
        this.isFinalDst = isFinalDst;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setNumberOfBags(int numberOfBags) {
        this.numberOfBags = numberOfBags;
    }

    public void setStatus(char status) {
        this.status = status;
    }
    /*  numberOfBags : 0 a 2
        status : T - passageiro em transito
                 E - passageiro chegou ao destino final
        lostBag : true - perdeu a mala
                  false - não perdeu
        
    */
    public void setupPassanger(){
        Random rand = new Random();
        String status = "TE";
        this.numberOfBags = rand.nextInt(3);
        this.status = status.charAt(rand.nextInt(2));
        int prob = rand.nextInt(100);
        if(prob > 10){
            this.lostBag = true;
        }
        else{
            this.lostBag = false;
        }
    }
    /*LifeCycle*/
    @Override
    public void run() {
        for(int i=0; i<5; i++){
            setupPassanger();
            isFinalDst = al.whatShouldIDo();
            /*DESTINO*/
            if(isFinalDst){
                if(this.numberOfBags == 0){
                    ate.goHome();
                }
                else{
                    for(int j = 0; j < this.numberOfBags; j++ ){

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

    private int Random(int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
}
