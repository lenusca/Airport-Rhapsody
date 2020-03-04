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
    private Passenger p;
    
    public Bag(Passenger p){
        this.p = p;
    }

    public Bag() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public char getStatus() {
        if(p.status == 'T'){
            return 'T';
        }
        else{
            return 'E';
        }
    }
    
}
