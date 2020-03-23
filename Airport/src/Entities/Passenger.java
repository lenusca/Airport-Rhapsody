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
    public static String status;
    
    //construtor
    public Passenger(int threadID, ArrivalLounge al, BaggageCollection bc, GeneralRepository gr, TemporaryStorageArea tsa, BaggageReclaimOffice bro, ArrivalTerminalTransfer att, DepartureTerminalTransfer dtt, ArrivalTerminalExit ate, DepartureTerminalEntrance dte  ){
        this.id = threadID;
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

    public String getStatus() {
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

    public void setStatus(String status) {
        this.status = status;
    }
    /*  numberOfBags : 0 a 2
        status : T - passageiro em transito
                 E - passageiro chegou ao destino final
        lostBag : true - perdeu a mala
                  false - não perdeu
        
    */
    public boolean probLostBag(){
        Random rand = new Random();
        int prob = rand.nextInt(100);
        if(prob < 20){
            this.lostBag = true;
        }
        else{
            this.lostBag = false;
        }
        return this.lostBag;
    }
    
    public Passenger getPassenger(){
       return new Passenger(this.id, this.al, this.bc, this.gr, this.tsa, this.bro, this.att, this.dtt, this.ate, this.dte);
    }
    
    public void setupPassanger(int idflight){

        Random rand = new Random();
        String[] status = {"TRF", "FDT"};
        this.numberOfBags = rand.nextInt(3);
        
        this.status = status[1];
        if(this.status == "FDT"){
            ate.nPassengers(idflight);
        }
        else{
            dte.nPassengers(idflight);
        }
       
        
        Bag b = new Bag(this.getPassenger());
        if(this.numberOfBags == 1){
            if(!probLostBag()){
                al.addBag(b);
            }
        
        }
        else if(this.numberOfBags == 2){
            for(int i=0; i<2; i++){
                if(!probLostBag()){

                    al.addBag(b);
                }
                
            }
        }
        
  
        
    }
    
    /*LifeCycle*/
    @Override
    public void run() {
        
        for(int flight = 0; flight<5; flight++){        
            bc.resetValues();
            setupPassanger(flight);
            isFinalDst = al.whatShouldIDo(this.status, this.id, this.numberOfBags, flight+1);
            /*DESTINO*/
            if(isFinalDst){

                if(this.numberOfBags == 0){
                    ate.goHome(id, flight);
                }
                else{
                    for(int j = 0; j < this.numberOfBags; j++ ){

                        success = bc.goCollectABag(id);

                        if(!success){
                            break;
                        }
                    }
                    /* Se perderem alguma mala, vão fazer queixa da mesma RECLAIM OFFICE
                    e só depois é que vão para ARRIVAL TERMINA EXIT*/
                    if(!success){
                        System.out.println("P "+id);
                        bro.reportMissingBags(id);
                    }
                    /* Se tiverem as malas todas é que vão para ARRIVAL TERMINA EXIT*/
                    ate.goHome(id, flight);
                }
            }
            /*TRANSITO*/
            else{
                att.takeABus(id);
                att.enterTheBus(id);
                dtt.leaveTheBus(id);
                dte.prepareNextLeg(id, flight);
            }
        }
    }

   

   
        
}
