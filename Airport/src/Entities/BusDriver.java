/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Monitors.ArrivalTerminalTransfer;
import Monitors.DepartureTerminalTransfer;

/**
 * Quem leva os passageiros em trânsito entre a chegada e
 * os terminais de partida
 * @author lenin
 */
public class BusDriver implements Runnable{
    //atributos
    private ArrivalTerminalTransfer att;
    private DepartureTerminalTransfer dtt;
    private int threadID;
    //construtor
    public BusDriver(int threadID, ArrivalTerminalTransfer att, DepartureTerminalTransfer dtt){
        this.threadID = threadID;
    }
    
    private boolean hasDaysWorkEnded() {
        return true; //To change body of generated methods, choose Tools | Templates.
    }

    private boolean announcingBusBoarding() {
        for(int time=0; time<10; time++ ){
            return false; 
        }
        return true;
    }
    
    // vai acontecer no Terminal Transfer QUAY
      @Override
    public void run() {
        while(!hasDaysWorkEnded() || !announcingBusBoarding()){
            /*Se o autocarro estiver cheio ou for a hora de ir embora*/
            /*Vai para Departure Terminal*/
           
            dtt.goToDepartureTerminal();
            dtt.parkTheBusAndLetPassOff();
            att.goToArrivalTerminal();
            att.parkTheBus();

            
        }
    }

    
}
