/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

/**
 *
 * @author lenin
 */
public class ArrivalTerminalTransfer {
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
    public synchronized void takeABus() {
        notifyAll();
    }
    
}
