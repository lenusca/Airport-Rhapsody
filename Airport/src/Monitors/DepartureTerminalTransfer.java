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
    
    /*************************PASSENGER METHODS*********************************/

    /**
     * 
     * <p>Passageiros saem do autocarro, o ultimo acorda o busDriver</p>
     * @param threadID threadID do passageiro
     */
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
    
    /**
     * 
     * <p>O autocarro chegou ao destino, o busDriver acorda os passageiros</p>
     */
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
    
    /**
     * 
     * <p>O busDriver volta para o Arrival Terminal Transfer</p>
     */
    public void goToArrivalTerminal() {
        gr.setBusDriverState("DRBW");
        try{
            sleep(300);
        }catch(InterruptedException e){}
    }
    
    /****************************AUXILIAR METHODS********************************/
    /**
     * 
     * <p>Adiciona os passageiros que vão no autocarro</p>
     * @param threadID threadID do passageiro
     */
    public void addPassenger(int threadID){ 
        synchronized (passengersBus) {
        // add an element and notify all that an element exists 
       passengersBus.add(threadID);
       System.out.println("Passageiro adicionado: "+threadID);
       passengersBus.notifyAll();
        
      }
    }
    
    /**
     * 
     * <p>Remove os passageiros do autocarro</p>
     * @return <p>id, retorna o thearID do passageiro que foi removido</p>
     */
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
    
    /**
     * 
     * <p>Atualiza se o busDriver chegou ao destino</p>
     * @param busArrived busDriver chegou ao destino
     */
    public synchronized void setBusArrived(boolean busArrived){
        this.busArrived = busArrived;
    }
    
    /**
     * 
     * <p>Retorna se o busDriver está no destino</p>
     * @return <p> true, o busDriver chegou ao Departure Terminal Transfer</p>
     *         <p> false, o busDriver ainda não se encontra no Departure Terminal Transfer</p>
     */
    public synchronized boolean getBusArrived(){
        return busArrived;
    }
}
