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
    private int busCapacity; //capaciadade do bus
    public DepartureTerminalTransfer dtt = new DepartureTerminalTransfer();
    
    public ArrivalTerminalTransfer(int busCapacity) {
        this.busCapacity=busCapacity;
    }

    public Queue<Integer> getPassengersBus() {
        return passengersBus;
    }
    
    /*O busdriver adormece*/
    public synchronized void parkTheBus() {
        try{
            wait();                     //fica a espera de novos passageiros
        }catch(InterruptedException e){
            //Thread.currentThread().interrupt();
        }
        
    }
    
    /*passageiros entram no autocarro*/
    public synchronized void enterTheBus() {
        System.out.println("ATT-enterTheBus() --> :" + passengersBus);
        dtt.passengersBus.add(passengersBus.remove());       
        if(passengersBus.size() == 0){
            notifyAll();
        }
    }
    /*No enunciado diz que o driver é acordado com o takeABus */
    /**/
    public synchronized void takeABus() {
        idPassenger=idPassenger+1;
        passengersBus.add(idPassenger);
        System.out.println("ATT-takeABus() --> PASSAGEIROS:"+passengersBus);
        try{
            wait();                     //fica a espera de novos passageiros
        }catch(InterruptedException e){
            //Thread.currentThread().interrupt();
        }
        if(idPassenger == this.busCapacity){
           System.out.println("takeABus() --> passageiros suficientes para encher o bus");
           notifyAll(); 
        }    
    }
    
    /*anucia que os passageiros podem entrar no autocarro (invocada pelo busDriver)
        @return <li> true, chegou a hora de partir ou o numero de passageiros é suficiente para encher o autocarro
                <li> false, ainda nao chegou a hora ainda não há passageiros suficientes
    */
    public synchronized boolean announcingBusBoarding() {
        for(int time=0; time<10; time++ ){
            System.out.println("ATT-AnnouncingBusBoarding()--->>>CountUp do time para o bus sair!! -> "+time);
            if(this.busCapacity==passengersBus.size()){
                break;
            }
            try{
                wait();             //O bus driver aguarda pela hora ou que chegue numero suficiente de
            }catch(InterruptedException e){}
            return false;
        }
        notifyAll();                    //avisa os passageiros para entrar
        return true;
    }
    
    /*vai para o departure terminal transfer*/
    public synchronized void goToDepartureTerminal() {
        while(passengersBus.size() != 0){
            try{
                wait();                     //aguarda que os passageitos entrem no autocarro antes de partir
            }catch(InterruptedException e){}
        }
        System.out.println("Start the trip");
    }

}
