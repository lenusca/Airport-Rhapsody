/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Monitors.ArrivalTerminalTransfer;
import Monitors.DepartureTerminalTransfer;

/**
 *
 * @author lenin
 */
public class BusDriver {
    //atributos
    private int capacity = 3;
    private boolean hasDaysWorkEnded = false;
    private ArrivalTerminalTransfer att;
    private DepartureTerminalTransfer dtt;
    private int threadID;
    //construtor
    public BusDriver(boolean endWork, int threadID, ArrivalTerminalTransfer att, DepartureTerminalTransfer dtt){
        this.hasDaysWorkEnded = endWork;
        this.threadID = threadID;
    }
    
    
    public void lifeCycle(){
        while(!hasDaysWorkEnded){
            
        }
    }
}
