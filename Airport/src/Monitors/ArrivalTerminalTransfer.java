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
    private int[] nPassengersFlight = new int[5]; 
    private boolean[] enteringPassengers = new boolean[6]; //passageiros que vão entrar
    
    private int index=0;
    private boolean allIn = false, allPassengers=false;
    private boolean doneWork = false, haveMorePassengers=false;
    private int count = 0;
    private int nFlight;
    private int busCapacity; //capaciadade do bus
    private int numFlight; //numero de voos para terminar o dia
    private GeneralRepository gr;
    private DepartureTerminalTransfer dtt;
    private ArrivalTerminalExit ate;
    private DepartureTerminalEntrance dte;
    
    
    public ArrivalTerminalTransfer(int busCapacity, int numFlight, GeneralRepository gr, DepartureTerminalTransfer dtt) {
        this.busCapacity=busCapacity;
        this.gr = gr;
        this.numFlight = numFlight;
        this.dtt = dtt;
        //this.ate = ate;
        //this.dte = dte;
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
        
        this.passengersBus.add(threadID); //adicionado o passageiro à fila de espera
        
        if((nPassengersFlight[nFlight]<busCapacity) && (nPassengersFlight[nFlight]==passengersBus.size())){
            allPassengers = true;
        }
        if(passengersBus.size() == busCapacity){
            //flag para verificar se tem todos
            allPassengers = true;
            notifyAll();
        } //notifica o bus que a fila atingiu a capacidade do bus

        while(this.enteringPassengers[threadID]==false){
            try{
                wait();    //passageiros aguardam o anuncio para entrar no bus
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        } 
          
        enteringPassengers[threadID] = false;
    }
    
    /**
     * 
     * <p>Os passageiros entram para o autocarro, o ultimo acorda o busDriver</p>
     * @param threadID threadID do passageiro
     */
    public synchronized void enterTheBus(int threadID) {
        gr.idPassengers.remove();
        gr.s[count] = String.valueOf(threadID); 
        gr.setPassengerState("TRT", threadID);
        count += 1;
        
        dtt.addPassenger(threadID);
        System.out.println("enterTheBUs busCapacity "+busCapacity+" count "+count);
        if(count == busCapacity){
            allIn = true;
            notifyAll();   //Acorda o busDriver, já entraram todos no bus
        }
        nPassengersFlight[nFlight] -= 1; //decrementa o numero de passageiros no Arrival Terminal, necessário para matar a thread do busDriver  
        if(passengersBus.size() <= busCapacity) { 
            haveMorePassengers = true;} //há mais 3 passageiros 
        if(passengersBus.size() == 0) { //reset flags
            haveMorePassengers = false;
            allPassengers = false;}
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
        busCapacity = 3;
        System.out.println("allPassengers "+allPassengers+" pSize() "+passengersBus.size()+" busCapacity "+busCapacity+" npflight: "+nPassengersFlight[nFlight]+" flight: "+nFlight+" haveMorePassengers "+haveMorePassengers);
        
        while((((!allPassengers) && (passengersBus.size()<busCapacity) && !haveMorePassengers) || (nPassengersFlight[nFlight] == 0)) && !(doneWork)){
            
            try{
                wait(10);    //passageiros esperam para chegar ao destino
            }catch(InterruptedException e){}
        }
        doneWork = false;
        if((nPassengersFlight[nFlight] == 0) && (nFlight==numFlight-1)){
            return true; //é o ultimo voo e não há mais passageiros, bus work ended your day
        }
        
        // colocar aqui o valor que deve entrar numero de passageiros
        if(passengersBus.size() >= busCapacity){
            this.busCapacity = 3;
        }
        else if(passengersBus.size() < busCapacity){  
            this.busCapacity = passengersBus.size(); 
        }
        
        return false;         
    }
    
    /**
     * 
     * <p>Notifica os passageiros que podem entrar no autocarro, ou seja, acorda-os e espera que eles entrem</p>
     */
    public synchronized void announcingBusBoarding() {
        // Colocar os passageiros a true
        System.out.println(" busCapacity "+busCapacity);
        for(int i = 0; i < busCapacity; i++){
            enteringPassengers[passengersBus.remove()] = true;
        }
        
        notifyAll(); //notifica os passageiros para entrar
        
        while(!allIn){
            try{
                wait();      //espera que os passageiros entrem no bus
            }catch(InterruptedException e){}
        }
        allIn = false;
        
    }
    
    /**
     * 
     * <p>O busDriver parte para o Departure Terminal Transfer</p>
     */
    public synchronized void goToDepartureTerminal() {
        gr.setBusDriverState("DRFW");
        try{
           sleep(10);             //simulação de viagem para o o dtt
        }catch(InterruptedException e){}
        count = 0;
        index = 0;
    }
    
    /**
     * 
     * <p>O busDriver estaciona o autocarro</p>
     */
    public synchronized void parkTheBus() {
        gr.setBusDriverState("PKAT");
    } 
    
    
    /******************************AUXILIAR METHODS******************************/
    /**
     * 
     * <p>Conta o número de passageiros em trânsito, que vão apanhar o autocarro</p>
     * @param idVoo id do Voo
     */
    public synchronized void nPassengers(int idVoo){
        this.nPassengersFlight[idVoo] +=1;    
    }
    
    /**
     * 
     * <p>Atualiza o numero do voo no Arrival Terminar Transfer<p>
     * @param idVoo id do voo
     */
    public synchronized void setIdVoo(int idVoo){
        nFlight = idVoo;
    }
    
    /**
    *
    * <p> Acorda o busDriver no final do dia de trabalho </p>
    * 
    */
    public synchronized void wakeUpAll(){
        doneWork = true;
        notifyAll();
        //System.out.println("Acorda o bus no fim!!!! "+doneWork);
    }

}
