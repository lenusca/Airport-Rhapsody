/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Monitors.ArrivalTerminalTransfer;
import Monitors.DepartureTerminalTransfer;

/**
 * Quem leva os passageiros em trânsito, ou seja, não terminaram a viagem neste aeroporto, entre a chegada e
 * os terminais de partida
 * @author lenin
 */
public class BusDriver implements Runnable{
    //atributos
    private ArrivalTerminalTransfer att;
    private DepartureTerminalTransfer dtt;
    private int id;
    private int numFlight=5;
    private int flight=0;
    //construtor
    public BusDriver(int threadID, ArrivalTerminalTransfer att, DepartureTerminalTransfer dtt){
        this.id = threadID;
        this.att = att;
        this.dtt = dtt;
    }
    
    /**
     * <p> Ciclo de vida do BusDriver, enquanto não chegam passageiros ele encontrasse adormecido. Quando a fila de espera para entrar no autocarro for igual ao número de lugares do autocarro ou
     * chegou a hora de partida, o busDriver dá ordem para os passageiros entrarem, partindo para o Departure Terminal Transfer, quando chega ao local deixa os passageiros e retorna ao Arrival Terminal Transfer.
     * Se ainda houver passageiros na fila de espera volta a fazer a viagem, senão adormece até chegar passageiros de outros voos.</p>  
     * <p> O busDriver termina o seu dia de trabalho quando já não houver mais voos ou passageiros</p>
     */
    @Override
    public void run() {
        while(!att.hasDaysWorkEnded()){ //espera que a viagem começa 
            att.announcingBusBoarding(); /*Se o autocarro estiver cheio ou for a hora de ir embora*/
            att.goToDepartureTerminal(); /*Vai para Departure Terminal*/
            dtt.parkTheBusAndLetPassOff();
            dtt.goToArrivalTerminal();
            att.parkTheBus();
        }
        System.out.println("O busDrive "+this.id+" terminou o serviço");
        
    }

    
}
