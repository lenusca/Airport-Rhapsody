/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

/**
 *
 * @author lenin
 */
public class BaggageReclaimOffice {
    public GeneralRepository gr;
    
    public BaggageReclaimOffice(GeneralRepository gr){
        this.gr = gr;
    }
    
    public void reportMissingBags(int threadID) {
        //System.out.println("Passenger "+id+" lost a bag");
        gr.setPassengerState("BRO", threadID);
    }
    
}
