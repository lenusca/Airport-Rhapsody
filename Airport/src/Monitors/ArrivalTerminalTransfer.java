/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;
import Entities.Passenger;
import java.util.*;
/**
 *
 * @author lenin
 */
public class ArrivalTerminalTransfer extends Thread{

    private Queue<Integer> passengersBus = new LinkedList<>();
    private int[] nPassengersFlight = new int[5];
    private Queue<Integer> enteringPassengers = new LinkedList<>(); //passageiros que vão entrar
    private int idPassenger=0;
    private int index=0;
    private static int count = 0;
    private static int nFlight;
    private int busCapacity; //capaciadade do bus
    private int numFlight; //numero de voos para terminar o dia
    public GeneralRepository gr;
    public DepartureTerminalTransfer dtt = new DepartureTerminalTransfer(gr);
    
    
    public ArrivalTerminalTransfer(int busCapacity, int numFlight, GeneralRepository gr) {
        this.busCapacity=busCapacity;
        this.gr = gr;
    }
    
    /******************************PASSENGER METHODS****************************/
    
    /**
     * 
     * <p>Os passageiros aguardam pela hora para entrar no autocarro, se a fila atingir a capacidade do autocarro acorda o busDriver.</p>
     * @param threadID threadID do passageiro 
     */
    public synchronized void takeABus(int threadID) {
        gr.idPassengers.add(index, String.valueOf(threadID));
        gr.setPassengerState("ATT", threadID);
        index=index+1;
      
        addPassenger(threadID);
        idPassenger=idPassenger+1;
        
        while(passengersBus.size() > busCapacity){ //se o id for maior que 3, de certeza que o bus já se encontra cheio
            try{
                wait();          //passageiros aguardam o anuncio para entrar no bus, encontrando-se em andamento
            }catch(InterruptedException e){ }
        }
        
        if(idPassenger == busCapacity){ //chegou o 3º ou 6º passageiro à fila de espera
            System.out.println("notificou o bus");
            notifyAll();    //notifica o bus que a fila atingiu a capacidade do bus
            idPassenger = 0;
        }
 
        if(idPassenger<=busCapacity){ //numero passageiros inferior à capacidade do bus, aguardam para entrar
            System.out.println("Aguarda: "+threadID);
            try{
                wait();          //passageiros aguardam o anuncio para entrar no bus
            }catch(InterruptedException e){ }
        }
        
        enteringPassengers.add(removePassenger());
            
    }
    
    /**
     * 
     * <p>Os passageiros entram para o autocarro, o ultimo acorda o busDriver, e dormem enquanto não chegam ao destino</p>
     * @param threadID threadID do passageiro
     */
    public synchronized void enterTheBus(int threadID) {
        gr.idPassengers.remove(threadID);
        gr.s[count] = String.valueOf(threadID);
        gr.setPassengerState("TRT", threadID);
        count += 1;
       
        dtt.addPassenger(enteringPassengers.remove());
            
        if(passengersBus.size() == 0){
            notifyAll();        //Acorda o busDriver
        }        
        while(!dtt.getBusArrived()){ //enquanto o passageiro nao chega ao departure
            try{
                wait();    //passageiros esperam para chegar ao destino
            }catch(InterruptedException e){}
        }
        nPassengersFlight[nFlight] -= 1; //decrementa o numero de passageiros no Arrival Terminal, necessário para matar a thread do busDriver
        count = 0;
    }
    
    /****************************BUSDRIVER METHODS*******************************/
    
    /**
     * 
     * <p>Verifica se o DusDriver já terminou o seu trabalho ou ainda tem passageiros para efetuar a viagem</p>
     * @return <p>true, terminou o dia de trabalho, terminaram os voos, o seu ciclo de vida chegou ao fim</p>
     *         <p>false, ainda não terminou o seu dia de trabalho, ainda tem passageiros para serem reencaminhados</p>
     */
    public boolean hasDaysWorkEnded() {
        System.out.println("FLIGHT: "+nFlight);
       
        if((nPassengersFlight[nFlight] == 0) && (nFlight==numFlight-1)){ //nflight begin:0 numFLight begin: 1
            return true; //é o ultimo voo e não há mais passageiros, bus work ended your day
        }
        
        while(nPassengersFlight[nFlight] == 0){
            try{
                wait();        //não há passageiros para serem servidos
            }catch(InterruptedException e){}
        }
        
        if(nPassengersFlight[nFlight] != 0){ //há passageiros, verifica para a segunda ronda segunda ronda para o mesmo voo
            if(nPassengersFlight[nFlight] >= busCapacity){
                try{
                    wait();      //espera pelo anuncio do passageiro
                }catch(InterruptedException e){}
                return false;    //fila para o bus atingiu a capacidade do bus, retorna false para começar a viagem
            }
            if((nPassengersFlight[nFlight] < busCapacity) && (nPassengersFlight[nFlight] > 0)){ //numero passageiros inferior ao bus capacity mas há passageiros
                try{
                   wait(1000);      //dá um compasso de espera para chegar a hora do bus
                }catch(InterruptedException e){}
                return false;
            }    
        }
                
        return false;       //retorna false para começar a viagem
    }
    
    /**
     * 
     * <p>Notifica os passageiros que podem entrar no autocarro, ou seja, acorda-os e espera que eles entrem</p>
     */
    public synchronized void announcingBusBoarding() {
        notifyAll(); //notifica os passageiros para entrar
        while(!enteringPassengers.isEmpty()){
            try{
                wait();      //espera que os passageiros entrem no bus
            }catch(InterruptedException e){}
        }
    }
    
    /**
     * 
     * <p>O busDriver parte para o Departure Terminal Transfer</p>
     */
    public synchronized void goToDepartureTerminal() {
        gr.setBusDriverState("DRFW");
        try{
           sleep(300);             //simulação de viagem para o o dtt
        }catch(InterruptedException e){}
    }
    
    /**
     * 
     * <p>O busDriver estaciona o autocarro</p>
     */
    public synchronized void parkTheBus() {
        gr.setBusDriverState("PKAT");
        dtt.setBusArrived(false);
        try{
           sleep(50);             //simulação de park the bus
        }catch(InterruptedException e){}     
    } 
    
    
    /******************************AUXILIAR METHODS******************************/
    /**
     * 
     * <p>Adiciona os passageiros à fila de espera</p>
     * @param threadID threadID do passageiro
     */
    public void addPassenger(int threadID){ 
        synchronized (passengersBus) {
        // add an element and notify all that an element exists 
       passengersBus.add(threadID);
       System.out.println("Passageiro adicionado: "+threadID);
       passengersBus.notifyAll();
        
      }
    }
    
    /**
     * 
     * <p>Remove os passageiros da fila de espera</p>
     */
    public int removePassenger() {
        synchronized (passengersBus) {
            while (passengersBus.isEmpty()) {
                try{
                    passengersBus.wait();
                }
                catch(InterruptedException e){}
            }
            int id = passengersBus.remove();
            return id;
        }
    }
    
    /**
     * 
     * <p>Conta o número de passageiros em trânsito, que vão apanhar o autocarro</p>
     * @param idVoo id do Voo
     */
    public synchronized void nPassengers(int idVoo){
        this.nPassengersFlight[idVoo] +=1;
        nFlight = idVoo;
    } 

}
