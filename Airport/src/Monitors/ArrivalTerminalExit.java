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
    private int []count2 = new int[5];
    
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
        this.count[idVoo] += 1;
    

        synchronized(this){
           
            while(cond(idVoo)){
                try{
                   wait();             //Os passageiros ficam aguardar pelo sinal do ultimo passageiro
                }catch(InterruptedException e){}
            }
            notifyAll();
        }
      
        //o último deste lado acorda os outros passageiros
        count2[idVoo] += 1;
        if(count2[idVoo] == pFDT[idVoo]){
            dte.wakeUpAll();
            if(idVoo == 5-1) {att.wakeUpAll();} //ultimo acorda o bus o dia terminou
        } 
        //System.out.println("saiu goHome "+threadID+" voo "+idVoo+" pFDT "+pFDT[idVoo]);
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
    
    public synchronized int pFDT(int idVoo){
        return pFDT[idVoo];
    }

    public synchronized boolean cond(int idVoo){
        return !dte.allPassengers(idVoo) || !this.allPassengers(idVoo) || this.dte.pTRF(idVoo)+this.pFDT(idVoo) < 6; 
    }
   
}
