/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitors;

/**
 *
 * @author lenin
 */
public class ArrivalTerminalExit {
    
    public synchronized void goHome(int threadID) {
        if(threadID == 5){
            notifyAll();
        }
       
    }
    
}
