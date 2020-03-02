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
    private int capacity = 3, schedule = 5;
    private boolean hasDaysWorkEnded = false;
    private ArrivalTerminalTransfer att;
    private DepartureTerminalTransfer dtt;
    private int threadID;
    //construtor
    public BusDriver(boolean endWork, int threadID, ArrivalTerminalTransfer att, DepartureTerminalTransfer dtt){
        this.hasDaysWorkEnded = endWork;
        this.threadID = threadID;
    }
    
    // vai acontecer no Terminal Transfer QUAY
      @Override
    public void run() {
        int passengers = 0;
        int schedule = 0;
        while(!hasDaysWorkEnded || !announcingBusBoarding){
            schedule++;
            /*Se o autocarro estiver cheio ou for a hora de ir embora*/
            /*Vai para Departure Terminal*/
            if(passengers == capacity || this.schedule == schedule ){
                dtt.goToDepartureTerminal();
                dtt.parkTheBusAndLetPassOff();
                att.goToArrivalTerminal();
                att.parkTheBus();
                
            }
        }
    }
}
