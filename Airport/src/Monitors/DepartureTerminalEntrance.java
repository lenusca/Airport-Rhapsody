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
public class DepartureTerminalEntrance {
    public GeneralRepository gr;
    public ArrivalLounge al = new ArrivalLounge(gr, 5, 6);
    
    public DepartureTerminalEntrance(GeneralRepository gr){
        this.gr = gr;
    }
    
    public synchronized void prepareNextLeg(int threadID) {
        gr.setPassengerState("EDT", threadID);
        System.out.println("LEG "+al.passengersHome +  al.passengersLeg);
        al.passengersLeg.poll();
        System.out.println("LEG "+al.passengersHome +  al.passengersLeg);
        if(al.passengersHome.isEmpty() && al.passengersLeg.isEmpty()){          //ultimo passageiro
            notifyAll();            //acorda os outros passageiros
        }
        
        else{         //ultimo passageiro
             try{
                wait();             //Os passageiros ficam aguardar pelo sinal do ultimo passageiro
            }catch(InterruptedException e){}
        }
    }
    
}
