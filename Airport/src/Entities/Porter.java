/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;
import java.util.*;
import AuxClasses.Bag;
import Monitors.ArrivalLounge;
import Monitors.BaggageCollection;
import Monitors.GeneralRepository;
import Monitors.TemporaryStorageArea;

/**
 * Quem descarrega as malas de um avião, quando pousa, e as leva para (um dos seguintes):
 * - Baggage Collection Point
 * - Temporary Storage Area
 * @author lenin
 */
public class Porter implements Runnable{
    //Atributos
    private TemporaryStorageArea tsa;
    private BaggageCollection bc;
    private ArrivalLounge al;
    private GeneralRepository gr;
    private int id;
    //construtor
    public Porter(int threadID, BaggageCollection bc, TemporaryStorageArea tsa, ArrivalLounge al, GeneralRepository gr){
        this.tsa = tsa;
        this.id = threadID;
        this.bc = bc;
        this.al = al;
        this.gr = gr;
    }
    /**
    *
    * <p> Ciclo de vida do Porter, enquanto não chegam os 6 passageiros este encontra-se adormecido. Depois quando é acordado começa na recolha das malas, e vai buscar uma a uma
    * no Arrival Lounge e leva-a ou para Temporary Storage Area se o dono da mala for TRF(passageiro em trânsito) ou para o Baggage Collection Point se o dono da malar for FDT(destino final este aerorporto).</p>
    * <p> Quando acabar a recolha das malas, este volta adormecer até o próximo vôo.</p>   
    */
    @Override
    public void run() {
        Bag bag;
        /* a espera que um avião chegue */
        /*true-descansa
        false-vai buscar as malas*/
        while(al.takeARest() != true){
            
            while( (bag = al.tryToCollectABag()) != null){
                /* Decidir em qual dos sitios se armazena a mala*/
                /* Se for um passageiro em transito(T)deixa TEMPORARY STORAGE AREA */
                /* Se for um passageiro já no destino deixa BAGGAGE COLLECTION POINT*/
                if(bag.getStatus() == "TRF"){
                    tsa.curryItToAppropriateStore(bag);
                }
                else{
                    if(al.BagsEmpty()){
                        bc.curryItToAppropriateStore(bag, true);
                    }
                    else{
                        bc.curryItToAppropriateStore(bag, false);
                    }
                }
         
            }

            /*Já não há mais malas*/
            al.noMoreBagstoCollect();
            gr.numOfBagsStoreroom = 0;
            gr.numOfBagsConveyor = 0;
            
        }
        System.out.println("O porter "+this.id+" terminou o serviço");
    }
}
