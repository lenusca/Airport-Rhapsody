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
public class DepartureTerminalTransfer {
    
    /*o busDriver acorda os passageiros, chegaram ao destino do autocarro*/
    public synchronized void parkTheBusAndLetPassOff() {
        notifyAll();
    }


    public void goToDepartureTerminal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*os passageiros saem e o ultimo acorda o busDriver*/
    public synchronized void leaveTheBus() {
        notifyAll();
    }
    
}
