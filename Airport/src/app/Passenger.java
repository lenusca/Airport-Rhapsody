package app;

public class Passenger{
    //atributos
    boolean isFinalDst = false;
    boolean success = false;
    int numberofBags = 0;

    //construtor
    public Passenger(int numberofBags){
        this.numberofBags = numberofBags;
    }

    private void goHome() {
    }

    private boolean whatShouldIDO() {
        return false;
    }

    private boolean goCollectionBag() {
        return false;
    }

    private void reportMissingBags() {
    }

    private void prepareNextLeg() {
    }

    private void leaveTheBus() {
    }

    private void enterTheBus() {
    }

    private void takeABus() {
    }

    public void LifeCycle(){
        isFinalDst = whatShouldIDO();
        //destino
        if(isFinalDst){
            if(numberofBags == 0){
                goHome();
            }
            else{
                for(int i = 0; i < numberofBags; i++ ){
                    success = goCollectionBag();
                    
                    if(!success){
                        break;
                    }
                }

                if(!success){
                    reportMissingBags();
                }

                goHome();

               
            }
        }
        //em transito
        else{
            takeABus();
            enterTheBus();
            leaveTheBus();
            prepareNextLeg();
        }

        
    }
    
}