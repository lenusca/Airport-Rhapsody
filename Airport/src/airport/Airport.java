/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airport;

import Entities.BusDriver;
import Entities.Passenger;
import Entities.Porter;
import Monitors.ArrivalLounge;
import Monitors.ArrivalTerminalExit;
import Monitors.ArrivalTerminalTransfer;
import Monitors.BaggageCollection;
import Monitors.BaggageReclaimOffice;
import Monitors.DepartureTerminalEntrance;
import Monitors.DepartureTerminalTransfer;
import Monitors.GeneralRepository;
import Monitors.TemporaryStorageArea;

/**
 * K aviões e N passageiros, em que cada passageiro tem 0 a M malas de bagagem
 * no porão do avião. O autocarro, que leva os passageiros entre os terminais,
 * tem capacidade T lugares
 * K = 5 aviões
 * N = 6 passageiros
 * M = 2 malas
 * T = 3 lugares no autocarro
 * @author lenin
 */
public class Airport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("START");
        /*variables*/
        int nFlight = 5;
        int nPassengers = 6;
        int nBags = 2;
        int nSeatingPlaces = 3;
        
        /*Monitors/Locals*/
        GeneralRepository gr = new GeneralRepository();
        ArrivalLounge al = new ArrivalLounge();
        BaggageCollection bc = new BaggageCollection();
        TemporaryStorageArea tsa = new TemporaryStorageArea();
        BaggageReclaimOffice bro = new BaggageReclaimOffice();
        ArrivalTerminalTransfer att = new ArrivalTerminalTransfer(nSeatingPlaces);
        DepartureTerminalTransfer dtt = new DepartureTerminalTransfer();
        ArrivalTerminalExit ate = new ArrivalTerminalExit();
        DepartureTerminalEntrance dte = new DepartureTerminalEntrance();
        
        /*tamanho do array = numero de passageiros + numero de porters + numero de bus driver*/
        /*Arranque da simulação*/
        Thread []threads = new Thread[nPassengers + 1 + 1];
        for(int threadcount = 0; threadcount < nPassengers + 1 + 1; threadcount++){
            
            if(threadcount < nPassengers){
                Runnable passenger_runnable = new Passenger(threadcount, al, bc, gr, tsa, bro, att, dtt, ate, dte);
                threads[threadcount] = new Thread(passenger_runnable);
                threads[threadcount].start();
            }
            /*Porter threadID = 6 */
            else if (threadcount < nPassengers + 1){
                Runnable porter_runnable = new Porter(threadcount, bc, tsa, al);
                threads[threadcount] = new Thread(porter_runnable);
                threads[threadcount].start();
            }
            /*BusDriver threadID = 7*/
            else{
                Runnable busdriver_runnable = new BusDriver(threadcount, att, dtt);
                threads[threadcount] = new Thread(busdriver_runnable);
                threads[threadcount].start();
            }
        }
        System.out.println("AQUIIIIIII"+threads.length);
        /*Aguardar o fim da simulação*/
        for(int i = 0; i < threads.length; i++){
            try{
                threads[i].join();
            }catch(InterruptedException e ){
                System.out.println("DEU RUIMMMM");
                System.exit(1);
            }
        }
        
        
    }
    
}
