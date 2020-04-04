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
    
    /**
    *
    * <p> Retorna o status do dono desta mala, passageiro </p>
    *    @return <p> TRF, se o destino do passageiro não for este aeroporto </p>
    *            <p> FDT, se o destino do passageiro for este aeroporto </p>
    *    
    */
    public String getStatus() {
        if(passenger.getStatus() == "TRF"){
            return "TRF";
        }
        else{
            return "FDT";
        }
    }
    
    
    
}
