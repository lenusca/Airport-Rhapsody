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
    
    public ArrivalTerminalTransfer(int n) {
        this.busCapacity=n;
    }

    public ArrivalTerminalTransfer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
        dtt.passengersBus.add(passengersBus.poll());
        
        if(passengersBus.size() == 0){
            notifyAll();
        }
    }
    /*No enunciado diz que o driver é acordado com o takeABus */
    /**/
    public synchronized void takeABus() {
        idPassenger=idPassenger+1;
        passengersBus.add(idPassenger);
        if(idPassenger == busCapacity){
           notifyAll(); 
        } 
        
    }
    
}
