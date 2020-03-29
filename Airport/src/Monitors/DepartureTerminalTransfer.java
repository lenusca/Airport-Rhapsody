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
    public Queue<Integer> passengersBus = new LinkedList<>();
    private final GeneralRepository gr;
    private boolean busArrived = false;
    private int count = 0;
    
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
        while(busArrived==false){ //enquanto o passageiro nao chega ao departure
            try{
                wait();    //passageiros esperam para chegar ao destino
            }catch(InterruptedException e){}
        }
        passengersBus.remove(threadID);
        gr.s[count] = "-";
        gr.setPassengerState("DTT", threadID);
        count += 1;
        
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
        busArrived = true;
       
        notifyAll();    //acordar os passageiros para sair  
        
        while(!passengersBus.isEmpty()){
            try{
                wait();      //espera que os passageiros saiam do bus
            }catch(InterruptedException e){}
        }
        busArrived = false;
    }
    
    /**
     * 
     * <p>O busDriver volta para o Arrival Terminal Transfer</p>
     */
    public synchronized void goToArrivalTerminal() {
        gr.setBusDriverState("DRBW");
        try{
            sleep(50);
        }catch(InterruptedException e){}
    }
    
    /****************************AUXILIAR METHODS********************************/
    /**
     * 
     * <p>Adiciona os passageiros que vão no autocarro</p>
     * @param threadID threadID do passageiro
     */
    public synchronized void addPassenger(int threadID){ 
       passengersBus.add(threadID); 
    }
}
