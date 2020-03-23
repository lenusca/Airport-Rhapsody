/*
 * Log e as entidade não têm acesso a isto
 */
package Monitors;

import genclass.GenericIO;
import genclass.TextFile;
import genclass.FileOp;
import java.util.*;

/**
 *
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
    // BusDriver info
    public static String[] idPassengers = new String[6];
    public static String[] s = new String[3]; 
    // Monitors Tates
    // ArrivalLounge
    public static int numFlight = 1;
    public static int numOfBags = 0;
    // ArrivalTerminalExit
    // BaggageColletion
    public static int numOfBagsConveyor = 0;
    // BaggageReclaimOffice
    // DepartureTerminalTransfer
    // DepartureTerminalEntrance
    // TemporaryStorageArea
    public static int numOfBagsStoreroom = 0;
    
    //File
    String filename = "logFile.txt";
    TextFile log = new TextFile();
    private String msg;
    
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
        Arrays.fill(idPassengers, "-");
        Arrays.fill(s, "-");
   }
   
   public void generateLog(){
    if(!log.openForAppending(".", filename)){
         GenericIO.writelnString("Failed to create the file!");
         System.exit(1);
      } 
     msg = String.format("%2d  %2d  "+porter_state+ "%2d  %2d  "+busdriver_state+"   "+idPassengers[0]+"  "+idPassengers[1]+"  "+idPassengers[2]+"  "+idPassengers[3]+"  "+idPassengers[4]+"  "+idPassengers[5]+"   "+ s[0] 
             +"  "+ s[1] +"  "+ s[2] +"\n"+passenger_state[0]+ " "+si[0]+"  "+nr[0]+"   "+na[0]+"  "+passenger_state[1]+ " "+si[1]+"  "+nr[1]+"   "+na[1]+"  "+passenger_state[2]+ " "+si[2]+"  "+nr[2]+"   "+na[2]+"  " 
             +passenger_state[3]+ " "+si[3]+"  "+nr[3]+"   "+na[3]+"  "+passenger_state[4]+ " "+si[4]+"  "+nr[4]+"   "+na[4]+"  "+passenger_state[5]+ " "+si[5]+"  "+nr[5]+"   "+na[5]+" ", numFlight, numOfBags, numOfBagsConveyor, numOfBagsStoreroom);
     log.writelnString(msg);
     if(!log.close()){
         GenericIO.writelnString("I can't close the file");
         System.exit(1);
     } 
   }
   
    // Setters for states
   public synchronized void setPassengerSetup(String state, int threadID, String s_si, int s_nr){
       si[threadID] = s_si;
       nr[threadID] = String.valueOf(s_nr);
       na[threadID] = "0";
       passenger_state[threadID] = state;
       generateLog();
   }
   public synchronized void setPorterState(String state){
        porter_state = state;
        generateLog();
    }
    
    public synchronized void setPassengerState(String state, int threadID){
        passenger_state[threadID] = state;
        generateLog();
    }
    
    public synchronized void setBusDriverState(String state){
        busdriver_state = state;
        generateLog();
    }
    
    public synchronized void resetValues(){
        // Initialization
        Arrays.fill(passenger_state, "---");
        Arrays.fill(na, "-");
        Arrays.fill(si, "---");
        Arrays.fill(nr, "-");
        Arrays.fill(idPassengers, "-");
        Arrays.fill(s, "-");
        numOfBagsConveyor = 0;
        numOfBags = 0;
        numOfBagsStoreroom = 0;   
    }
    
    public synchronized void numberBags(int numOfBags){
        this.numOfBags = numOfBags;
    }
    
    
}
