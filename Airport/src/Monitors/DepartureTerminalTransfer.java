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
    public static Queue<Integer> passengersBus = new LinkedList<>();
    public GeneralRepository gr;
    private static boolean busArrived = false;
    private static int count = 0;
    
    public DepartureTerminalTransfer(GeneralRepository gr){
        this.gr = gr;
    }
    
    /*---------------------PASSENGER METHODS-------------------------*/

    /*os passageiros saem e o ultimo acorda o busDriver*/
    public synchronized void leaveTheBus(int threadID) {
        gr.s[count] = "-";
        gr.setPassengerState("DTT", threadID);
        count += 1;
        removePassenger();
        if(passengersBus.isEmpty()){
            count = 0;
            notifyAll();
        }  
    }
    
    /*o busDriver acorda os passageiros, chegaram ao destino do autocarro*/
    public synchronized void parkTheBusAndLetPassOff() {
        gr.setBusDriverState("PKDT");
        setBusArrived(true);
       
        notifyAll();    //acordar os passageiros para sair  
        
        while(!passengersBus.isEmpty()){
            try{
                wait();      //espera que os passageiros saiam do bus
            }catch(InterruptedException e){}
        }
    }
    
    /*volta do Departure Terminal Transfer para Arrival Terminal Transfer*/
    public void goToArrivalTerminal() {
        gr.setBusDriverState("DRBW");
        try{
            sleep(300);
        }catch(InterruptedException e){}
    }
    
    /*---------------------AUXILIAR METHODS-------------------------*/
    // method to add a passenger in the linkedList
    public void addPassenger(int threadID){ 
        synchronized (passengersBus) {
        // add an element and notify all that an element exists 
       passengersBus.add(threadID);
       System.out.println("Passageiro adicionado: "+threadID);
       passengersBus.notifyAll();
        
      }
    }
    
    // method used to remove a passenger from the queue
    public void removePassenger() {
        synchronized (passengersBus) {
            while (passengersBus.isEmpty()) {
                try{
                    passengersBus.wait();
                }
                catch(InterruptedException e){}
            }
            passengersBus.remove();
        }
    }
    //verefica se o bus ja chegou
    public synchronized void setBusArrived(boolean busArrived){
        this.busArrived = busArrived;
    }
    
    public synchronized boolean getBusArrived(){
        return busArrived;
    }
}
