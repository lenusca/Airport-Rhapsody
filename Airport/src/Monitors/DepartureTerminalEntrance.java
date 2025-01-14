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
    private ArrivalTerminalTransfer att;
    private int []pTRF = {0, 0, 0, 0, 0};
    private int []count = new int[5];
    
    public DepartureTerminalEntrance(GeneralRepository gr, ArrivalTerminalTransfer att){
        this.gr = gr;
        this.att = att;
    }
    
    /**
    *
    * <p> Instanciar a zona Departure Terminal, pois ambas vão ter os passageiros a saídas, têm de estar sincronizados. </p>
    *    @param ate outra saída
    *    @see ArrivalTerminalExit
    */
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
      
        count(idVoo);

        while(this.ate.pFDT(idVoo)+this.pTRF(idVoo) < 6 || !this.ate.allPassengers(idVoo) || !this.allPassengers(idVoo)){
                //acorda os outros passageiros
            synchronized(this){
                try{
                    wait();             //Os passageiros ficam aguardar pelo sinal do ultimo passageiro
                }catch(InterruptedException e){}  
            } 
        }

        synchronized(this){
            notifyAll();
        }  
        
        ate.wakeUpAll();
        if(idVoo == 5-1) {att.wakeUpAll();} //ultimo acorda o bus o dia terminou
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
        return count[idVoo] == pTRF[idVoo];
    }
    
    /**
    *
    * <p> Acorda os outros passageiros que se encontram a espera na outra saída </p>
    * 
    */
    public synchronized void wakeUpAll(){
        notifyAll();
    }
    
    /**
    *
    * <p> Retorna o número total de passageiros de cada vôo que vêem para esta saida </p>
    *    @param idVoo id do voo
    *    @return número de passageiros
    */
    public synchronized int pTRF(int idVoo){
        return pTRF[idVoo];
    }
    
    public synchronized void count(int idVoo){
        this.count[idVoo] += 1; 
    }
    
}
