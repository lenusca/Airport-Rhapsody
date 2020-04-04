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
    
    /**
    *
    * <p> É no Baggage Reclaim Office que os que os passageiros que perderam a mala, vão reclamar </p>
    *    @param threadID threadID do passageiro
    */
    public void reportMissingBags(int threadID) {
        gr.setPassengerState("BRO", threadID);
    }
    
}
