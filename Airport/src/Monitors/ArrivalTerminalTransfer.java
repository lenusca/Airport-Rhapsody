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
    public GeneralRepository gr;
    public DepartureTerminalTransfer dtt = new DepartureTerminalTransfer(gr);
    
    
    public ArrivalTerminalTransfer(int busCapacity, GeneralRepository gr) {
        this.busCapacity=busCapacity;
        this.gr = gr;
    }

    public Queue<Integer> getPassengersBus() {
        return passengersBus;
    }
    
    /*O busdriver adormece*/
    public synchronized void parkTheBus() {
        gr.setBusDriverState("PKAT");
        try{
            wait();                     //fica a espera de novos passageiros
        }catch(InterruptedException e){
            //Thread.currentThread().interrupt();
        }
        
    }
    
    /*passageiros entram no autocarro*/
    public synchronized void enterTheBus(int threadID) {
        int passengerID = passengersBus.remove();
        System.out.println(count);
        gr.s[count] = String.valueOf(passengerID);
        count = count + 1;
        gr.setPassengerState("TRT", threadID);
        dtt.passengersBus.add(passengerID);       
   
        if(passengersBus.size() == 0){
            notifyAll();
        }
        else{
            try{
                wait();                     //fica a espera de novos passageiros
            }catch(InterruptedException e){
                //Thread.currentThread().interrupt();
            }
        }
        
    }
    /*No enunciado diz que o driver é acordado com o takeABus */
    /**/
    public synchronized void takeABus(int threadID) {
        gr.idPassengers[idPassenger] = String.valueOf(threadID);
        gr.setPassengerState("ATT", threadID);
        idPassenger=idPassenger+1;
        passengersBus.add(threadID);
        if(idPassenger == this.busCapacity){
           System.out.println("takeABus() --> passageiros suficientes para encher o bus");
           notifyAll(); 
        }
        else{
            try{
                wait();                     //fica a espera de novos passageiros
            }catch(InterruptedException e){
                //Thread.currentThread().interrupt();
            }
        }
        
            
    }
    
    /*anucia que os passageiros podem entrar no autocarro (invocada pelo busDriver)
        @return <li> true, chegou a hora de partir ou o numero de passageiros é suficiente para encher o autocarro
                <li> false, ainda nao chegou a hora ainda não há passageiros suficientes
    */
    //ACHO QUE ESTA ESTA MAL
    public synchronized boolean announcingBusBoarding() {
        for(int time=0; time<1; time++ ){
            System.out.println("ATT-AnnouncingBusBoarding()--->>>CountUp do time para o bus sair!! -> "+time);
            if(this.busCapacity==passengersBus.size()){
                break;
            }
            try{
                wait();             //O bus driver aguarda pela hora ou que chegue numero suficiente de
            }catch(InterruptedException e){}
            
        }
        notifyAll();                    //avisa os passageiros para entrar
        return true;
    }
    
    /*vai para o departure terminal transfer*/
    public synchronized void goToDepartureTerminal() {
        System.out.println("NUMERO DE PASSAGEIROS: " + passengersBus.size());
        gr.setBusDriverState("DRFW");
        while(passengersBus.size() != 0){
            try{
                wait();                     //aguarda que os passageitos entrem no autocarro antes de partir
            }catch(InterruptedException e){}
        }
        //System.out.println("Start the trip");
    }

}
