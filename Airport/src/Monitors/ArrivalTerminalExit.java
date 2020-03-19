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
public class ArrivalTerminalExit {
    public GeneralRepository gr;
    public ArrivalLounge al = new ArrivalLounge(gr, 5, 6);
    
    public ArrivalTerminalExit(GeneralRepository gr){
        this.gr = gr;
    }
    
    public synchronized void goHome(int threadID) {
        gr.setPassengerState("EAT", threadID);
        System.out.println("HOME "+al.passengersHome +  al.passengersLeg);
        al.passengersHome.poll();
        System.out.println("HOME "+al.passengersHome +  al.passengersLeg);
        if(al.passengersHome.isEmpty() && al.passengersLeg.isEmpty()){          //ultimo passageiro
            notifyAll();            //acorda os outros passageiros
        }
        
        else{
            try{
                wait();             //Os passageiros ficam aguardar pelo sinal do ultimo passageiro
            }catch(InterruptedException e){}
        }
       
    }
    
}
