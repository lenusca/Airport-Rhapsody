package app;

public class Passenger{
    //atributos
    boolean isFinalDst = false;
    boolean success = false;
    int numberOfBags = 0;

    //construtor
    public Passenger(int numberOfBags){
        this.numberOfBags = numberOfBags;
    }

    private void goHome() {
    }

    /* false-passageiros não terminam a viagem neste aeroporto, seguem para o cais de transferencias */
    /* true-passageiros terminam a viagem neste aeroporto, vao buscar a bagagem se tiverem*/
    private boolean whatShouldIDo() {
        return false;
    }
    /*true-tem a mala*/
    /*false-nao tem mala, seguem para o gabinete de reclamaçao*/
    //vao pegar na mala
    //baggage collection point
    private boolean goCollectABag() {
        return false;
    }
    
    //ir para o gabinete de reclamaçao de bagagens
    //baggage reclaim office
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

    public void lifeCycle(){
        
        isFinalDst = whatShouldIDo();
        //destino
        if(isFinalDst){
            if(numberOfBags == 0){
                goHome();
            }
            else{
                for(int i = 0; i < numberOfBags; i++ ){
                    success = goCollectABag();
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