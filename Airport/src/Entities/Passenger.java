/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import AuxClasses.Bag;
import Monitors.ArrivalLounge;
import Monitors.ArrivalTerminalExit;
import Monitors.ArrivalTerminalTransfer;
import Monitors.BaggageCollection;
import Monitors.BaggageReclaimOffice;
import Monitors.DepartureTerminalEntrance;
import Monitors.DepartureTerminalTransfer;
import Monitors.GeneralRepository;
import Monitors.TemporaryStorageArea;
import java.util.Random;

/**
 * Quem faz a viagem neste aeroporto, podendo ser este o destino final ou ir para outra viagem
 *
 * @author lenin
 */
public class Passenger implements Runnable{
     //atributos
    private int id;
    private boolean isFinalDst = false;
    private boolean success = false;
    private boolean lostBag = false;
    private int numberOfBags = 0;
    private ArrivalLounge al;
    private BaggageCollection bc;
    private GeneralRepository gr;
    private TemporaryStorageArea tsa;
    private BaggageReclaimOffice bro;
    private ArrivalTerminalTransfer att;
    private DepartureTerminalTransfer dtt;
    private ArrivalTerminalExit ate;
    private DepartureTerminalEntrance dte;
    public static String status;
    
    //construtor
    public Passenger(int threadID, ArrivalLounge al, BaggageCollection bc, GeneralRepository gr, TemporaryStorageArea tsa, BaggageReclaimOffice bro, ArrivalTerminalTransfer att, DepartureTerminalTransfer dtt, ArrivalTerminalExit ate, DepartureTerminalEntrance dte  ){
        this.id = threadID;
        this.al = al;
        this.bc = bc;
        this.gr = gr;
        this.tsa = tsa;
        this.bro = bro;
        this.att = att;
        this.dtt = dtt;
        this.ate = ate;
        this.dte = dte;
        
    }
    
    /**
    *
    * <p> Thread id do passageiro </p>
    *    @return threadID - threadID do passageiro
    *   
    */
    public int getId() {
        return id;
    }
    
    /**
    *
    * <p> Se este aeroporto é o distino final ou não </p>
    *    @return isFinalDst - true(se for destino final) ou false(se não for destino final)
    *         
    */
    public boolean isIsFinalDst() {
        return isFinalDst;
    }
    
    /**
    *
    * <p> Se o passageiro conseguiu recolher a mala </p>
    *    @return success - true(se conseguiu recolher) ou false(se não conseguiu recolher)  
    *                      
    */
    public boolean isSuccess() {
        return success;
    }
    
    /**
    *
    * <p> Número total de malas que o passageiro levou para o aeroporto </p>
    *    @return numberOfBags - 0, 1 ou 2
    *                         
    */
    public int getNumberOfBags() {
        return numberOfBags;
    }
    
    /**
    *
    * <p> Retorna o estado do passageiro, se é um passageiro que chegou ao destino final ou se está em trânsito </p>
    *    @return status - TRF(passageiro em trânsito) ou FDT(passageiro cujo o destino final é este aeroporto) 
    *                   
    *                         
    */
    public String getStatus() {
        return status;
    }
    
    /**
    *
    * <p> Atribui um thread id ao passageiro </p>
    *    @param id threadID do passageiro
    *    
    */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
    *
    * <p> Atribui ao passageiro se é destino final ou não</p>
    *    @param isFinalDst  true(se for destino final) ou falso(se não for destino final) 
    *                      
    *    
    */
    public void setIsFinalDst(boolean isFinalDst) {
        this.isFinalDst = isFinalDst;
    }
    
    /**
    *
    * <p> Atribui ao passageiro se conseguiu ou não buscar a mala </p>
    *    @param success true(conseguiu) ou false(não conseguiu) 
    *                   
    *    
    */
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    /**
    *
    * <p> Atribui ao passageiro um número de malas </p>
    *    @param numberOfBags 0, 1 ou 2
    *    
    */
    public void setNumberOfBags(int numberOfBags) {
        this.numberOfBags = numberOfBags;
    }
    
    /**
    *
    * <p> Atribui ao passageiro se este aeroporto é o destino final ou não </p>
    *    @param status TRF(passageiro em trânsito) ou FDT(passageiro cujo o destino final é este aeroporto)
    *                 
    *    
    */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
    *
    * <p> Houve ou não probabilidade de perder a mala, em que a probabilidade de perder a mala é 20% </p>
    *    @return lostBag - true(perdeu a mala) ou false(não perdeu a mala)
    *                    
    *    
    */
    public boolean probLostBag(){
        Random rand = new Random();
        int prob = rand.nextInt(100);
        if(prob < 20){
            this.lostBag = true;
        }
        else{
            this.lostBag = false;
        }
        return this.lostBag;
    }
    
    /**
    *
    * <p> Dados todos de um passageiro </p>
    *    @return Passenger, retorna um passageiro
    *    
    */
    public Passenger getPassenger(){
       return new Passenger(this.id, this.al, this.bc, this.gr, this.tsa, this.bro, this.att, this.dtt, this.ate, this.dte);
    }
    
    /**
    *
    * <p> Setup dos dados iniciais do passageiro, número de malas que vai ter, o status do mesmo se é TRF(transito) ou FDT(final), se perdeu ou não malas </p>
    * <p> CONDIÇÕES : </p>
    * <p> numberOfBags : 0 a 2</p>
    * <p> status : TRF - passageiro em transito ou  FDT - passageiro chegou ao destino final</p>
    * <p> lostBag: true, perdeu a mala ou false, não perdeu a mala </p> 
    * @param idflight número de voo
    *
    *    
    */ 
    public void setupPassanger(int idflight){

        Random rand = new Random();
        String[] status = {"TRF", "FDT"};
        this.numberOfBags = rand.nextInt(3);
        
        this.status = status[1];
        
        if(this.status == "FDT"){
            ate.nPassengers(idflight);
            gr.numFDT();
        }
        else{
            dte.nPassengers(idflight);
            gr.numTRF();
        }
       
        
        Bag b = new Bag(this.getPassenger());
        if(this.numberOfBags == 1){
            if(!probLostBag()){
                al.addBag(b);
            }
            else{
                gr.lostBagTotal();
            }    
        }
        else if(this.numberOfBags == 2){
            for(int i=0; i<2; i++){
                if(!probLostBag()){
                    al.addBag(b);
                }
                else{
                    gr.lostBagTotal();
                }
                
            }
        }      
    }
    
    /**
    *
    * <p> Ciclo de vida do passageiro, se o destino final for este aeroporto este vai buscar as suas malas ao Baggage Collection Point, se não tiver malas vai logo para o Arrival Terminal Exit. 
    * Se o passageiro chegar a perder alguma das suas malas, vai ao Baggage Reclaim Office reclamar e depois segue para o Arrival Terminal Exit.</p>
    * <p> Caso o destino final não seja este aeroporto, o passageiro vai apanhar o autocarro no Arrival Terminal Transfer para ir para o Departure Terminal Transfer, 
    * onde seguidamente irá para o Departure Terminal Entrance esperar pelo o seu próximo voo. </p> 
    * 
    *
    *    
    */
    @Override
    public void run() {
        
        for(int flight = 0; flight<5; flight++){        
            bc.resetValues();
            setupPassanger(flight);
            isFinalDst = al.whatShouldIDo(this.status, this.id, this.numberOfBags, flight+1);
            /*DESTINO*/
            if(isFinalDst){

                if(this.numberOfBags == 0){
                    ate.goHome(id, flight);
                }
                else{
                    for(int j = 0; j < this.numberOfBags; j++ ){

                        success = bc.goCollectABag(id);

                        if(!success){
                            break;
                        }
                    }
                    if(!success){
                        System.out.println("P "+id);
                        bro.reportMissingBags(id);
                    }

                    ate.goHome(id, flight);
                }
            }
            /*TRANSITO*/
            else{
                att.takeABus(id);
                att.enterTheBus(id);
                dtt.leaveTheBus(id);
                dte.prepareNextLeg(id, flight);
            }
        }
    }

   

   
        
}
