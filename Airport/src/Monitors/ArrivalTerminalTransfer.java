/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

import java.util.*;
/**
 *
 * @author lenin
 */
public class ArrivalTerminalTransfer extends Thread{

    private Queue<Integer> passengersBus = new LinkedList<>();
    private Queue<Integer> passengersBus2 = new LinkedList<>();
    private int[] nPassengersFlight = new int[5]; 
    private boolean[] enteringPassengers = new boolean[6]; //passageiros que vão entrar
    
    private int idPassenger=0;
    private int index=0;
    
    private int numPassengersBus = 0;
    private int count = 0;
    private int nFlight;
    private int busCapacity; //capaciadade do bus
    private int numFlight; //numero de voos para terminar o dia
    private GeneralRepository gr;
    private DepartureTerminalTransfer dtt;
    
    
    public ArrivalTerminalTransfer(int busCapacity, int numFlight, GeneralRepository gr, DepartureTerminalTransfer dtt) {
        this.busCapacity=busCapacity;
        this.gr = gr;
        this.numFlight = numFlight;
        this.dtt = dtt;
        Arrays.fill(enteringPassengers, false);
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
        passengersBus.add(threadID);
  //adicionado o passageiro à fila de espera
        System.out.println(passengersBus);
        if(passengersBus.size() == busCapacity){
            notifyAll();
        } //notifica o bus que a fila atingiu a capacidade do bus

        while(enteringPassengers[threadID]==false){
            
            try{
                wait();    //passageiros aguardam o anuncio para entrar no bus
            }catch(InterruptedException e){ }
           
            if (numPassengersBus++< busCapacity){
                idPassenger+=1;
                System.out.println(passengersBus.size());
                enteringPassengers[passengersBus.remove()] = true;
            }
            System.out.println("AQYUUUUU"+enteringPassengers[threadID]);

        } 
        System.out.println("numPassengersBus"+numPassengersBus);
        enteringPassengers[threadID] = false;
        index -= 1; //para o proximo passageiro que entrar aparecer no log atras do ultimo da fila
        
    }
    
    /**
     * 
     * <p>Os passageiros entram para o autocarro, o ultimo acorda o busDriver, e dormem enquanto não chegam ao destino</p>
     * @param threadID threadID do passageiro
     */
    public synchronized void enterTheBus(int threadID) {
        gr.s[count] = String.valueOf(gr.idPassengers.pollFirst());
        System.out.println(enteringPassengers[threadID]);
        gr.s[count] = String.valueOf(threadID);
        gr.setPassengerState("TRT", threadID);
        count += 1;
        
        dtt.addPassenger(threadID);
        System.out.println("Count Passengers: "+idPassenger);    
        if(count == idPassenger){
            System.out.println("ACORDEII");
            notifyAll();   //Acorda o busDriver, já entraram todos no bus
            
        }

        while(!dtt.getBusArrived()){ //enquanto o passageiro nao chega ao departure
            try{
                wait();    //passageiros esperam para chegar ao destino
            }catch(InterruptedException e){}
        }
        nPassengersFlight[nFlight] -= 1; //decrementa o numero de passageiros no Arrival Terminal, necessário para matar a thread do busDriver
        count = 0;
        idPassenger = 0;
        index = 0;
   
    }
    
    /****************************BUSDRIVER METHODS*******************************/
    
    /**
     * 
     * <p>Verifica se já chegaram todos os voos, para terminar o dia de trabalho do busDriver.
     * Se não tiver passageiros dorme, se tiver passageiros na fila dá ordem para começar a viagem, isto é, se houver passageiros para a capacidade do autocarro na fila de espera, os passageiros acordam-no
     * e procede ao embarque, caso contrário aguarda pela hora de partida</p>
     * @return <p>true, terminou o dia de trabalho, terminaram os voos, o seu ciclo de vida chegou ao fim</p>
     *         <p>false, ainda não terminou o seu dia de trabalho, ainda tem passageiros para serem reencaminhados</p>
     */
    public synchronized boolean hasDaysWorkEnded() {

        
        if((nPassengersFlight[nFlight] == 0) && (nFlight==numFlight-1)){ //nflight begin:0 numFLight begin: 1
            return true; //é o ultimo voo e não há mais passageiros, bus work ended your day
        }
        
        if(nPassengersFlight[nFlight] >= busCapacity){
          return false; 
        }
        
        while(nPassengersFlight[nFlight] < busCapacity){
            try{
                wait(10);    //passageiros esperam para chegar ao destino
            }catch(InterruptedException e){}
        }
        
        return false;
             
    }
    
    /**
     * 
     * <p>Notifica os passageiros que podem entrar no autocarro, ou seja, acorda-os e espera que eles entrem</p>
     */
    public synchronized void announcingBusBoarding() {
        notifyAll(); //notifica os passageiros para entrar
        System.out.println("PASSENGERS BUS: "+ passengersBus);
        while(!((idPassenger == nPassengersFlight.length) || (3 < nPassengersFlight.length && idPassenger == 3))){
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
        //try{
        //   sleep(300);             //simulação de viagem para o o dtt
        //}catch(InterruptedException e){}
    }
    
    /**
     * 
     * <p>O busDriver estaciona o autocarro</p>
     */
    public synchronized void parkTheBus() {
        gr.setBusDriverState("PKAT");
        dtt.setBusArrived(false);
        numPassengersBus = 0;
        //try{
        //   sleep(50);             //simulação de park the bus
        //}catch(InterruptedException e){}     
    } 
    
    
    /******************************AUXILIAR METHODS******************************/
    /**
     * 
     * <p>Adiciona os passageiros à fila de espera</p>
     * @param threadID threadID do passageiro
     */
    public synchronized void addPassenger(int threadID){ 
        synchronized (passengersBus) {
            // add an element and notify all that an element exists 
           passengersBus.add(threadID);
           passengersBus.notifyAll();
        }
    }
    
    /**
     * 
     * <p>Remove os passageiros da fila de espera</p>
     */
    public int removePassenger() {
        synchronized (passengersBus) {
            
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
