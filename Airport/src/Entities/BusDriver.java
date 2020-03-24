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
    private int id;
    private int numFlight=5;
    private int flight=0;
    //construtor
    public BusDriver(int threadID, ArrivalTerminalTransfer att, DepartureTerminalTransfer dtt){
        this.id = threadID;
        this.att = att;
        this.dtt = dtt;
    }
    
    // vai acontecer no Terminal Transfer QUAY
      @Override
    public void run() {
      
        while(!att.hasDaysWorkEnded(++flight)){
           while(att.announcingBusBoarding()){
            /*Se o autocarro estiver cheio ou for a hora de ir embora*/
            /*Vai para Departure Terminal*/
            att.goToDepartureTerminal();
            dtt.parkTheBusAndLetPassOff();
            dtt.goToArrivalTerminal();
            att.parkTheBus();
        
           }
        }
        System.out.println("O busDrive "+this.id+" terminou o serviço");
        
    }

    
}
