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
    private int []idVoo = new int[5];
    private int []count = new int[5];
    
    public DepartureTerminalEntrance(GeneralRepository gr){
        this.gr = gr;
    }
    
    /**
    *
    * <p> Passageiro que o destino não é este aeroporto, têm como ponto saida o Departure Terminal Entrance. Quando um passageiro chega ao Departure Terminal Entrance fica a espera que cheguem todos os passageiros às saidas Arrival Terminal Exit e Departure Terminal Entrance. Quando chegam são todos acordados e vão para um novo voo ou terminam </p>
    *    @param threadID threadID do passageiro
    *    @param id id do voo
    *    @see ArrivalTerminalExit
    */
    public synchronized void prepareNextLeg(int threadID, int id) {
        gr.setPassengerState("EDT", threadID);
        this.count[id] += 1; 
  
        while(count[id] != this.idVoo[id]){
            System.out.println("thread: " + threadID + " count: " + count[id]);
            try{
               wait();             //Os passageiros ficam aguardar pelo sinal do ultimo passageiro
            }catch(InterruptedException e){}
        }
        System.out.println("thread: " + threadID + " count: " + count[id]);
        //acorda os outros passageiros
        if(count[id] == this.idVoo[id] && count[id] != 0){
             notifyAll();
            //gr.resetValues();
        }  
    }
    
    /**
    *
    * <p> Conta o numero de passageiros que pretendem terminar o voo no Departure Terminal Entrance </p>
    *    @param idVoo id do voo
    */
    public synchronized void nPassengers(int idVoo){
        this.idVoo[idVoo] += 1;
    }
    
}
