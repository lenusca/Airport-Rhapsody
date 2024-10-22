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
    private DepartureTerminalEntrance dte;
    private ArrivalTerminalTransfer att;
    private int []pFDT = {0, 0, 0, 0, 0};
    private int []count = new int[5];
    
    public ArrivalTerminalExit(GeneralRepository gr, DepartureTerminalEntrance dte, ArrivalTerminalTransfer att){
        this.gr = gr;
        this.dte = dte;
        this.att = att;
    }
    
    /**
    *
    * <p> Instanciar a zona Departure Terminal, pois ambas vão ter os passageiros a saídas, têm de estar sincronizados. </p>
    *    @param dte outra saída
    *    @see DepartureTerminalEntrance
    */
    public synchronized void setDepartureTerminalEntrance (DepartureTerminalEntrance dte){
        this.dte = dte;
    }
    
    /**
    *
    * <p> Passageiro que o destino é este aeroporto, têm como ponto saida o Arrival Terminal Exit. Quando um passageiro chega ao Arrival Terminal Exit fica a espera que cheguem todos os passageiros às saidas Arrival Terminal Exit e Departure Terminal Entrance. Quando chegam são todos acordados e vão para um novo voo ou terminam</p>
    *    @param threadID threadID do passageiro
    *    @param idVoo id do voo
    *    @see DepartureTerminalEntrance
    */
    public void goHome(int threadID, int idVoo) {
        gr.setPassengerState("EAT", threadID);

        count(idVoo);
        
        while(this.pFDT(idVoo)+this.dte.pTRF(idVoo) < 6 || !this.dte.allPassengers(idVoo) || !this.allPassengers(idVoo)){
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
        
        dte.wakeUpAll();
        if(idVoo == 5-1) {att.wakeUpAll();} //ultimo acorda o bus o dia terminou
    }
    
    /**
    *
    * <p> Conta o numero de passageiros que pretendem terminar o voo no Arrivel Terminal Exit </p>
    *    @param idVoo id do voo
    */
    public synchronized void nPassengers(int idVoo){
        this.pFDT[idVoo] += 1;
    }
    
    /**
    *
    * <p> Verifica se todos os passageiros que tinham como destino este aeroporto, se chegaram ao Arrival Terminal Exit </p>
    *    @param idVoo id do voo
    *    @return <p> true, se chegaram todos os passageiros </p>
    *            <p> false, se não chegaram todos os passageiros </p>
    */
    public synchronized boolean allPassengers(int idVoo){
        return count[idVoo] == pFDT[idVoo];
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
    public synchronized int pFDT(int idVoo){
        return pFDT[idVoo];
    }

    public synchronized void count(int idVoo){
        this.count[idVoo] += 1; 
    }
   
}
