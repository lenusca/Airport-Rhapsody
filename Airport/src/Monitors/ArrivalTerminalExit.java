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
        if(count[id] == this.idVoo[id] && count[id] != 0){
             notifyAll();
            //gr.resetValues();
        }    
        
        
    }
    
    /*FUNCÃO AUXILIAR*/
    /*PARA SABERMOS QUANTOS PASSAGEIROS TÊM DE VIR PARA ESTA SAIDA*/
    public synchronized void nPassengers(int idVoo){
        this.idVoo[idVoo] += 1;
    }

}
