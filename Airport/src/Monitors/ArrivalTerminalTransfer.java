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
    private int id=0;
    private int n; //capaciadade do bus
    
    public ArrivalTerminalTransfer(int n) {
        this.n=n;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public synchronized void enterTheBus() {
        notifyAll();
    }
    /*No enunciado diz que o driver é acordado com o takeABus */
    /**/
    public synchronized void takeABus() {
        id=id+1;
        passengersBus.add(id);
        if(id==this.n){
           notifyAll(); 
        } 
        
    }
    
}
