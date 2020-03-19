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
    /*Lifecycle*/
    @Override
    public void run() {
        Bag bag;
        /* a espera que um avião chegue */
        /*true-descansa
        false-vai buscar as malas*/
        while(al.takeARest() != true){
            System.out.println("PORTER COMEÇOU ATIVIDADE");
            
            while( (bag = al.tryToCollectABag()) != null){
                /* Decidir em qual dos sitios se armazena a mala*/
                /* Se for um passageiro em transito(T)deixa TEMPORARY STORAGE AREA */
                /* Se for um passageiro já no destino deixa BAGGAGE COLLECTION POINT*/
                if(bag.getStatus() == "TRF"){
                    tsa.curryItToAppropriateStore(bag);
                }
                else{
                    bc.curryItToAppropriateStore(bag);
                }
         
            }
            System.out.println("PASSEI");
            /*Já não há mais malas*/
            al.noMoreBagstoCollect();
            gr.numOfBagsStoreroom = 0;
            gr.numOfBagsConveyor = 0;
            
        }
        System.out.println("O porter "+this.id+" terminou o serviço");
    }
}
