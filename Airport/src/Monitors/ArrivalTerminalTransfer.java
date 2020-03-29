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
    private Queue<Integer> passengersInsideTheBus = new LinkedList<>();
    private int[] nPassengersFlight = new int[5]; 
    private boolean[] enteringPassengers = new boolean[6]; //passageiros que vão entrar
    
    private int idPassenger=0;
    private int index=0;
    private boolean allIn = false, allPassengers=false;
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
        this.passengersBus.add(threadID); //adicionado o passageiro à fila de espera
        System.out.println("takeABusInicio: "+passengersBus.size()+" "+nPassengersFlight[nFlight]+" "+allPassengers);
        if((nPassengersFlight[nFlight]<busCapacity) && (nPassengersFlight[nFlight]==passengersBus.size())){allPassengers = true;}
        System.out.println("busCapacity: "+busCapacity);
        if(passengersBus.size() == nPassengersFlight[nFlight]){
            System.out.println("NOTIFICAAAAAA");
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
        //System.out.println("takeABusFim: "+passengersBus.size()+" "+nPassengersFlight[nFlight]+" "+allPassengers);
        
        
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
        
        dtt.addPassenger(threadID);
          
        if(count == busCapacity){
            allIn = true;
            notifyAll();   //Acorda o busDriver, já entraram todos no bus
        }
        nPassengersFlight[nFlight] -= 1; //decrementa o numero de passageiros no Arrival Terminal, necessário para matar a thread do busDriver  
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
        System.out.println("Flight: "+nFlight);
        System.out.println("VERIFICAR:"+nPassengersFlight[nFlight]+"  "+passengersBus.size()+"   "+busCapacity+"    "+allPassengers);  
        while((((!allPassengers) && passengersBus.size()<busCapacity) || (nPassengersFlight[nFlight] == 0)) && !((nPassengersFlight[nFlight] == 0) && (nFlight==numFlight-1))){
            //System.out.println("Entrouuuuu");
            try{
                wait(10);    //passageiros esperam para chegar ao destino
            }catch(InterruptedException e){}
        }
        if((nPassengersFlight[nFlight] == 0) && (nFlight==numFlight-1)){ //nflight begin:0 numFLight begin: 1
            return true; //é o ultimo voo e não há mais passageiros, bus work ended your day
        }
        // colocar aqui o valor que deve entrar numero de passageiros
        //System.out.println("todos "+allPassengers);
        //System.out.println("busCapacity: "+busCapacity);
        if(passengersBus.size() >= busCapacity){
            this.busCapacity = 3;
        }
        else if(passengersBus.size() < busCapacity){  
            this.busCapacity = passengersBus.size(); 
        }
        System.out.println("VERIFICAR:"+nPassengersFlight[nFlight]+"  "+passengersBus.size()+"   "+busCapacity+"    "+allPassengers);
        return false;         
    }
    
    /**
     * 
     * <p>Notifica os passageiros que podem entrar no autocarro, ou seja, acorda-os e espera que eles entrem</p>
     */
    public synchronized void announcingBusBoarding() {
        // Colocar os passageiros a true aqui, os que devem entrar
        //System.out.println("ANNOUNCING "+passengersBus.size());
        for(int i = 0; i < busCapacity; i++){
            enteringPassengers[passengersBus.remove()] = true;
        }
        System.out.println("passengersBus.size() "+passengersBus.size());
        if(passengersBus.size()==0){allPassengers = false;}
        notifyAll(); //notifica os passageiros para entrar
        //System.out.println("announcingBusBoarding: "+allIn);
        while(!allIn){
            //System.out.println("ACorda");
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
        System.out.println("PArtiu");
        gr.setBusDriverState("DRFW");
        //try{
        //   sleep(300);             //simulação de viagem para o o dtt
        //}catch(InterruptedException e){}
        count = 0;
        idPassenger = 0;
        index = 0;
    }
    
    /**
     * 
     * <p>O busDriver estaciona o autocarro</p>
     */
    public synchronized void parkTheBus() {
        //notifyAll();
        gr.setBusDriverState("PKAT");
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
     * <p>Conta o número de passageiros em trânsito, que vão apanhar o autocarro</p>
     * @param idVoo id do Voo
     */
    public synchronized void nPassengers(int idVoo){
        this.nPassengersFlight[idVoo] +=1;    
    }
    
    public synchronized void setIdVoo(int idVoo){
        nFlight = idVoo;
    }

}
