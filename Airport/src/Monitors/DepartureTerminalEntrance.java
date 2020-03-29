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
    private int []count2 = new int[5];
    public boolean sair = false;
    
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
    public void prepareNextLeg(int threadID, int idVoo) {
        gr.setPassengerState("EDT", threadID);
        this.count[idVoo] += 1; 
        

        //acorda os outros passageiros
        synchronized(this){
            while(!ate.allPassengers(idVoo) || !allPassengers(idVoo)){
                try{
                   wait();             //Os passageiros ficam aguardar pelo sinal do ultimo passageiro
                }catch(InterruptedException e){}
            }
            notifyAll();
        }
        
        
 
        count2[idVoo] += 1;
        if(count2[idVoo] == pTRF[idVoo]){
            ate.wakeUpAll();
        }
        
        
            //gr.resetValues();
        
        
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
            this.sair = true;
            return true;
        }
        else{
            this.sair = false;
            return false;
        }
    }
    
    public synchronized void wakeUpAll(){
        notifyAll();
    }
    
    public boolean getSair(){
        return sair;
    }
    
}
