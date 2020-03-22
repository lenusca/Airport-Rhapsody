/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuxClasses;

import Entities.Passenger;

/**
 *
 * @author lenin
 */
public class Bag {
    public Passenger passenger;
    
    public Bag(Passenger passenger){
        this.passenger = passenger;
    }
    
    public String getStatus() {
        if(passenger.status == "TRF"){
            return "TRF";
        }
        else{
            return "FDT";
        }
    }
    
    
    
}
