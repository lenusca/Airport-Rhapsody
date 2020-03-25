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
    private GeneralRepository gr;
    private int []idVoo = new int[5];
    private int []count = new int[5];
    private int sair = 0;
    
    public ArrivalTerminalExit(GeneralRepository gr){
        this.gr = gr;
    }
    
    /**
    *
    * <p> Passageiro que o destino é este aeroporto, têm como ponto saida o Arrival Terminal Exit. Quando um passageiro chega ao Arrival Terminal Exit fica a espera que cheguem todos os passageiros às saidas Arrival Terminal Exit e Departure Terminal Entrance. Quando chegam são todos acordados e vão para um novo voo ou terminam</p>
    *    @param threadID threadID do passageiro
    *    @param id id do voo
    *    @see DepartureTerminalEntrance
    */
    public synchronized void goHome(int threadID, int id) {
        gr.setPassengerState("EAT", threadID);
       
        this.count[id] += 1; 
  
        while(count[id] != this.idVoo[id]){
            System.out.println("thread: " + threadID + " count: " + count[id]);
            try{
               wait();             //Os passageiros ficam aguardar pelo sinal do ultimo passageiro
            }catch(InterruptedException e){}
        }
        System.out.println("thread: " + threadID + " count: " + count[id]);
        //acorda os outros passageiros
        if(count[id] == this.idVoo[id]){
             notifyAll();
        }     
    }
    
     /**
    *
    * <p> Conta o numero de passageiros que pretendem terminar o voo no Arrivel Terminal Exit </p>
    *    @param idVoo id do voo
    */
    public synchronized void nPassengers(int idVoo){
        this.idVoo[idVoo] += 1;
    }

}
