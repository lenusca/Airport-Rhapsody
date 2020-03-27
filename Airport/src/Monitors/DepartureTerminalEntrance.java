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
    private GeneralRepository gr;
    private ArrivalTerminalExit ate;
    private int []pTRF = {0, 0, 0, 0, 0};
    private int []count = new int[5];
    
    public DepartureTerminalEntrance(GeneralRepository gr){
        this.gr = gr;
    }
    
    public synchronized void setArrivalTerminalExit (ArrivalTerminalExit ate){
        this.ate = ate;
    }
    
    /**
    *
    * <p> Passageiro que o destino não é este aeroporto, têm como ponto saida o Departure Terminal Entrance. Quando um passageiro chega ao Departure Terminal Entrance fica a espera que cheguem todos os passageiros às saidas Arrival Terminal Exit e Departure Terminal Entrance. Quando chegam são todos acordados e vão para um novo voo ou terminam </p>
    *    @param threadID threadID do passageiro
    *    @param idVoo id do voo
    *    @see ArrivalTerminalExit
    */
    public synchronized void prepareNextLeg(int threadID, int idVoo) {
        gr.setPassengerState("EDT", threadID);
        this.count[idVoo] += 1; 
        System.out.println(ate.allPassengers(idVoo));
        while(!allPassengers(idVoo) || !ate.allPassengers(idVoo)){
            try{
               wait();             //Os passageiros ficam aguardar pelo sinal do ultimo passageiro
            }catch(InterruptedException e){}
        }

        //acorda os outros passageiros
        if(allPassengers(idVoo) && ate.allPassengers(idVoo)){
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
        this.pTRF[idVoo] += 1;
    }
    
    /**
    *
    * <p> Verifica se todos os passageiros que não têm como destino este aeroporto, se chegaram ao Departure Terminal Entrance </p>
    *    @param idVoo id do voo
    *    @return <p> true, se chegaram todos os passageiros </p>
    *            <p> false, se não chegaram todos os passageiros </p>
    */
    public synchronized boolean allPassengers(int idVoo){
        if(count[idVoo] == pTRF[idVoo]){
            return true;
        }
        else{
            return false;
        }
    }
    
}
