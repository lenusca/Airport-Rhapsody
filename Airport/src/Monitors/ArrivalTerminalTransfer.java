/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;
import Entities.Passenger;
import java.util.*;
/**
 *
 * @author lenin
 */
public class ArrivalTerminalTransfer {

    private Queue<Integer> passengersBus = new LinkedList<>();
    private int idPassenger=0;
    private int busCapacity; //capaciadade do bus
    public DepartureTerminalTransfer dtt = new DepartureTerminalTransfer();
    
    public ArrivalTerminalTransfer(int busCapacity) {
        this.busCapacity=busCapacity;
    }

    public Queue<Integer> getPassengersBus() {
        return passengersBus;
    }
    
    /*O busdriver adormece*/
    public synchronized void parkTheBus() {
        try{
            wait();
        }catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        
    }

    public void goToArrivalTerminal() {
        System.out.println("END TRIP");
    }
    /**/
    public synchronized void enterTheBus() {
        System.out.println("ATT-enterTheBus() --> :" + passengersBus);
        dtt.passengersBus.add(passengersBus.remove());
                
        if(passengersBus.size() == 0){
            notifyAll();
        }
    }
    /*No enunciado diz que o driver é acordado com o takeABus */
    /**/
    public synchronized void takeABus() {
        idPassenger=idPassenger+1;
        passengersBus.add(idPassenger);
        System.out.println("ATT-takeABus() --> PASSAGEIROS:"+passengersBus);
        if(idPassenger == this.busCapacity){
           System.out.println("takeABus() --> passageiros suficientes para encher o bus");
           notifyAll(); 
        } 
        
    }
    
}
