/*
 * Log e as entidade não têm acesso a isto
 */
package Monitors;

import genclass.GenericIO;
import genclass.TextFile;
import genclass.FileOp;
import java.util.*;

/**
 * Local onde o estado interno vísivel do problema é armazenado
 * @author lenin
 */
public class GeneralRepository {
    
    /*
    *   <li> Estado de cada entidade:
    *
    *   <li> States Porter:
    *   <li> WPTL - waiting for a plane to land
    *   <li> APLH - at the plane's hold
    *   <li> ALCB - at the luggage conveyor belt
    *   <li> ASTR - at the storeroom
    *
    *   <li> States Passenger:
    *   <li> WSD - what shold I do
    *   <li> ATT - at the arrival transfer terminal
    *   <li> TRT - terminal transfer
    *   <li> DTT - at the departure transfer terminal
    *   <li> EDT - entering the departure terminal
    *   <li> LCP - at the luggage collection point
    *   <li> BRO - at the baggage reclaim office
    *   <li> EAT - exiting the arrival terminal
    *
    *   <li> States BusDriver:
    *   <li> PKAT - parking at the arrival terminal
    *   <li> DRFW - driving forward
    *   <li> PKDT - parking at the departure terminal
    *   <li> DRBW - driving backward
    */
    //States
    private String[] passenger_state = new String[6];
    private String busdriver_state = "PKAT";
    private String porter_state = "WPTL";
    
    // Passenger info
    private String[] st = new String[6]; // state of passenger # (# - 0 .. 5)
    public static String[] si = new String[6]; // situation of passenger # (# - 0 .. 5) – TRT (in transit) / FDT (has this airport as her final destination)
    public static String[] nr = new String[6]; // number of pieces of luggage the passenger # (# - 0 .. 5) carried at the start of her journey
    public static String[] na = new String[6]; // number of pieces of luggage the passenger # (# - 0 .. 5) she has presently collected
    private int cntFDT = 0;
    private int cntTRF = 0;
    // BusDriver info
    public static LinkedList<String> idPassengers = new LinkedList<String>();
    public static String[] s = new String[3]; 
    
    private int numFlight = 1;
    private int numOfBags = 0;
    private int totalBags = 0;
    private int totalLostBags = 0;
    public static int numOfBagsConveyor = 0;
    public static int numOfBagsStoreroom = 0;
    
    //File
    String filename = "logFile.txt";
    TextFile log = new TextFile();
    private String msg, msgFinal;
    
    /**
    *
    * <p> Gerado o ficheiro onde vai ser guardado os dados e parte do cabeçalho do logger file </p>
    */
   public GeneralRepository(){
      if(FileOp.exists(".", filename)){
          FileOp.deleteFile(".", filename);
      }
      if(!log.openForAppending(".", filename)){
          GenericIO.writelnString("Failed to create the file!");
          System.exit(1);
       }
       msg = "" + "               AIRPORT RHAPSODY - Description of the internal state of the problem\n\n"
              + "PLANE    PORTER                  DRIVER\n" + "FN BN  Stat CB SR   Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3\n"
              + "                                                        PASSENGERS\n" + "St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6";  
       log.writelnString(msg);
       if(!log.close()){
         GenericIO.writelnString("I can't close the file");
         System.exit(1);
        }  
        // Initialization
        Arrays.fill(passenger_state, "---");
        Arrays.fill(na, "-");
        Arrays.fill(si, "---");
        Arrays.fill(nr, "-");
        for(int i=0; i<6; i++){idPassengers.add("-");}
        Arrays.fill(s, "-");
    }
   
   /**
    * <p> Guardar os dados todos num ficheiro durante a ocorrência da simulação </p>
    */
   public void generateLog(){
        if(!log.openForAppending(".", filename)){
             GenericIO.writelnString("Failed to create the file!");
             System.exit(1);
        } 
        msg = String.format("%2d  %2d  "+porter_state+ "%2d  %2d  "+busdriver_state+"   "+idPassengers.get(0)+"  "+idPassengers.get(1)+"  "+idPassengers.get(2)+"  "+idPassengers.get(3)+"  "+idPassengers.get(4)+"  "+idPassengers.get(5)+"   "+ s[0] 
                +"  "+ s[1] +"  "+ s[2] +"\n"+passenger_state[0]+ " "+si[0]+"  "+nr[0]+"   "+na[0]+"  "+passenger_state[1]+ " "+si[1]+"  "+nr[1]+"   "+na[1]+"  "+passenger_state[2]+ " "+si[2]+"  "+nr[2]+"   "+na[2]+"  " 
                +passenger_state[3]+ " "+si[3]+"  "+nr[3]+"   "+na[3]+"  "+passenger_state[4]+ " "+si[4]+"  "+nr[4]+"   "+na[4]+"  "+passenger_state[5]+ " "+si[5]+"  "+nr[5]+"   "+na[5]+" ", numFlight, numOfBags, numOfBagsConveyor, numOfBagsStoreroom);
        log.writelnString(msg);
        
        if(!log.close()){
            GenericIO.writelnString("I can't close the file");
            System.exit(1);
        }
        
        if(this.numFlight == 5 && (passenger_state[0] == "EAT" || passenger_state[0] == "EDT") && (passenger_state[1] == "EAT" || passenger_state[1] == "EDT") && (passenger_state[2] == "EAT" || passenger_state[2] == "EDT") && (passenger_state[3] == "EAT" || passenger_state[3] == "EDT") && (passenger_state[4] == "EAT" || passenger_state[4] == "EDT") && (passenger_state[5] =="EAT"|| passenger_state[5] == "EDT") && porter_state == "WPTL" && busdriver_state == "PKAT" ){
            finalReport();
        }
        
        else if((passenger_state[0] == "EAT" || passenger_state[0] == "EDT") && (passenger_state[1] == "EAT" || passenger_state[1] == "EDT") && (passenger_state[2] == "EAT" || passenger_state[2] == "EDT") && (passenger_state[3] == "EAT" || passenger_state[3] == "EDT") && (passenger_state[4] == "EAT" || passenger_state[4] == "EDT") && (passenger_state[5] =="EAT"|| passenger_state[5] == "EDT") && porter_state == "WPTL" && busdriver_state == "PKAT" ){
           resetValues(); 
        } 
   }
   
   /**
    * <p> Imprimir a parte final do logger, depois de ocorrer os 5 vôos </p>
    */
   public void finalReport(){
      
       if(!log.openForAppending(".", filename)){
             GenericIO.writelnString("Failed to create the file!");
             System.exit(1);
        }
        msgFinal = String.format("\nFinal report\nN. of passengers which have this airport as their final destination = %2d \nN. of passengers which are in transit = %2d \nN. of bags that should have been transported in the the planes hold = %2d \nN. of bags that were lost = %2d", cntFDT, cntTRF, totalBags, totalLostBags);
        log.writelnString(msgFinal);
        if(!log.close()){
            GenericIO.writelnString("I can't close the file");
            System.exit(1);
        }
   }
   
    /***********************************************************Setters for states*******************************************************************************/
   /**
    * <p> Atualizar o passageiro  </p>
    * @param state WSD(what shoul i do), ATT(arrival transfer terminal), TRT(terminal transfer), DTT(departure transger terminal), EDT (entering the departure terminal),
    * LCP(luggage collection point), BRO(baggagge reclaim office) e EAT(exiting the arrival terminal)
    * @param threadID threadID do passageiro
    * @param s_si FDT(destino final este aeroporto) ou TRF(este aeroporto não é o destino final)
    * @param s_nr lugar no autocarro
    * 
    */
    public synchronized void setPassengerSetup(String state, int threadID, String s_si, int s_nr){
       si[threadID] = s_si;
       nr[threadID] = String.valueOf(s_nr);
       na[threadID] = "0";
       passenger_state[threadID] = state;
       generateLog();
    }

   /**
    * <p> Atualiza o estado do porter </p>
    * @param state WPTL(waiting for a plane to land), APLH(at the plane's hold), ALCB(at the luggage conveyor belt), ASTR(at the storeroom)
    */
    public synchronized void setPorterState(String state){
        porter_state = state;
        generateLog();
    }
   
    /**
    * <p> Atualiza o estado do passageiro </p>
    * @param state WSD(what shoul i do), ATT(arrival transfer terminal), TRT(terminal transfer), DTT(departure transger terminal), EDT (entering the departure terminal),
    * LCP(luggage collection point), BRO(baggagge reclaim office) e EAT(exiting the arrival terminal)
    * @param threadID threadID do passageiro
    */
    public synchronized void setPassengerState(String state, int threadID){
        passenger_state[threadID] = state;
        generateLog();
    }
    
    /**
    * <p> Atualiza o estado do bus driver </p>
    * @param state PKAT(parking at the arrival terminal), DRFW(driving forward), PKDT(parking at the departure terminal), DRBW(driving backward)
    */
    public synchronized void setBusDriverState(String state){
        busdriver_state = state;
        generateLog();
    }
    
    /**
    * <p> Colocar todos os dados com os valores default </p>
    * 
    */
    public synchronized void resetValues(){
        // Initialization
        Arrays.fill(passenger_state, "---");
        Arrays.fill(na, "-");
        Arrays.fill(si, "---");
        Arrays.fill(nr, "-");
        for(int i=0; i<6; i++){idPassengers.add("-");}
        Arrays.fill(s, "-");
        numOfBagsConveyor = 0;
        numOfBags = 0;
        numOfBagsStoreroom = 0;   
    }
    
    /**
    * <p> Calcular o número de malas em cada vôo e o número total de malas nos 5 vôos </p>
    * 
    */
    public synchronized void bagTotal(){
        this.numOfBags += 1;
        this.totalBags +=1;
    }
    
    /**
    * <p> Calcular número total de malas perdidas nos 5 vôos </p>
    * 
    */
    public synchronized void lostBagTotal(){
        this.totalLostBags +=1;
    }
    
    /**
    * <p> Calcular o número de malas em cada vôo </p>
    * @param bagsSize número total de malas no Arrival Lounge
    * 
    */
    public synchronized void numBags(int bagsSize){
        this.numOfBags = bagsSize;
    }
    
    /**
    * <p> Calcular número total de passageiros que tenham destino este aeroporto nos 5 vôos </p>
    * 
    */
    public synchronized void numFDT(){
       this.cntFDT += 1;
    }
    
    /**
    * <p> Calcular número total de passageiros que não tenham destino este aeroporto nos 5 vôos </p>
    * 
    */
    public synchronized void numTRF(){
       this.cntTRF += 1;
    }
    
    public synchronized void numFlight(int numFlight){
       this.numFlight = numFlight;
    }
    
    
}
