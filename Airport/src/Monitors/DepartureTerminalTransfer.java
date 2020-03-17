/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;


import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author lenin
 */
public class DepartureTerminalTransfer extends Thread{
    /*Fila de passageiros dentro do autocarro*/
    public static Queue<Integer> passengersBus = new LinkedList();
    
    public DepartureTerminalTransfer(){
        
    }
    
    /*o busDriver acorda os passageiros, chegaram ao destino do autocarro*/
    public synchronized void parkTheBusAndLetPassOff() {
        notifyAll();
    }


    /*os passageiros saem e o ultimo acorda o busDriver*/
    public synchronized void leaveTheBus() {
        System.out.println("DTT--leaveTheBus():"+passengersBus);
        passengersBus.remove();
        if(passengersBus.isEmpty()){
            notifyAll();
        }  
    }
    
    /*volta do Departure Terminal Transfer para Arrival Terminal Transfer*/
    public void goToArrivalTerminal() {
        System.out.println("END TRIP");
        try{
            sleep((long) (1+100*Math.random()));
        }catch(InterruptedException e){}
    }
    
}
