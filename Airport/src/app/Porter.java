package app;

public class Porter{
    
    //construtor
    public Porter(){

    }

    public void lifeCycle(){
        Bag bag;
        boolean planeOldEmpty;
        //espera por um aviao
        while(arrivalLounge.takeARest != 'E'){
            boolean planeOldEmpty = false;
            
            while(!arrivalLounge.planeOldEmpty){
                bag = tryToCollectABag();
                if(bag == null){
                    planeOldEmpty = true;
                }else if(bag.getStatus() == 'T'){
                    TMPS.curryItToAppropriateStore(bag);
                }else{
                    BCP.curryItToAppropriateStore(bag);
                }
            }
            arrivalLounge.noMoreBagstoCollect();
        }
    }
   
}