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
public class ArrivalTerminalTransfer extends Thread{

    private Queue<Integer> passengersBus = new LinkedList<>();
    private int idPassenger=0;
    private static int count = 0;
    private int busCapacity; //capaciadade do bus
    private int numFlight; //numero de voos para terminar o dia
    public GeneralRepository gr;
    public DepartureTerminalTransfer dtt = new DepartureTerminalTransfer(gr);
    
    
    public ArrivalTerminalTransfer(int busCapacity, int numFlight, GeneralRepository gr) {
        this.busCapacity=busCapacity;
        this.gr = gr;
    }

    public Queue<Integer> getPassengersBus() {
        return passengersBus;
    }
    
    /*---------------------PASSENGER METHODS-------------------------*/
    
    /*No enunciado diz que o driver é acordado com o takeABus */
    /**/
    public synchronized void takeABus(int threadID) {
        gr.idPassengers[idPassenger] = String.valueOf(threadID);
        gr.setPassengerState("ATT", threadID);
        idPassenger=idPassenger+1;
        addPassenger(threadID);
        
        if(idPassenger == this.busCapacity){
           //System.out.println("takeABus() --> passageiros suficientes para encher o bus");
           notifyAll(); 
        }
        else{
            try{
                wait();                     //fica a espera de novos passageiros
            }catch(InterruptedException e){ }
        }
            
    }
    
    /*passageiros entram no autocarro*/
    public synchronized void enterTheBus(int threadID) {
        System.out.println("enterTheBus() "+threadID);
        removePassenger(threadID);
        
        dtt.addPassenger(threadID);
        
        gr.s[count] = String.valueOf(threadID);
        count = count + 1;
        gr.setPassengerState("TRT", threadID);
            
        if(passengersBus.size() == 0){
            notifyAll();
        }
        else{
            try{
                wait();                     //fica a espera de novos passageiros
            }catch(InterruptedException e){}
        }
       
    }
    
    /*---------------------BUSDRIVER METHODS-------------------------*/
    
    /*o dia de trabalho do motorista terminou
        @return <li> true, o seu dia terminou
                <li> false, o seu dia ainda não terminou
    */
    public boolean hasDaysWorkEnded(int flight) {
        if(flight != this.numFlight){;
            return false;
        }
        return true;
    }
    
    /*anucia que os passageiros podem entrar no autocarro (invocada pelo busDriver)
        @return <li> true, chegou a hora de partir ou o numero de passageiros é suficiente para encher o autocarro
                <li> false, ainda nao chegou a hora ainda não há passageiros suficientes
    */
    public synchronized boolean announcingBusBoarding() {
        while(this.busCapacity != passengersBus.size()){
            try{
                wait(500);             //O bus driver aguarda pela hora ou que chegue numero suficiente de
            }catch(InterruptedException e){}
        }
        notifyAll();                    //avisa os passageiros para entrar
        return true;
    }
    
    /*vai para o departure terminal transfer*/
    public synchronized void goToDepartureTerminal() {
        //System.out.println("NUMERO DE PASSAGEIROS: " + passengersBus.size());
        gr.setBusDriverState("DRFW");
        while(passengersBus.size() != 0){
            try{
                wait();                     //aguarda que os passageitos entrem no autocarro antes de partir
            }catch(InterruptedException e){}
        }
        //System.out.println("Start the trip");
        try{
           sleep(500);             //simulação de viagem para o o dtt
        }catch(InterruptedException e){}
    }
    
    /*O busdriver adormece*/
    public synchronized void parkTheBus() {
        gr.setBusDriverState("PKAT");
        try{
            wait(100);                     //fica a espera de novos passageiros
        }catch(InterruptedException e){
            //Thread.currentThread().interrupt();
        }
        
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
    public void removePassenger(int threadID) {
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

}
