/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BuildingBlocks.Blocks;

import BuildingBlocks.Master.TimerBlock;
import java.util.Observable;
import java.util.TimerTask;

/**
 *
 * @author Ricardo
 */
public class OnDelay extends TimerBlock{
    
    public OnDelay(){
        super("OnDelay", "Output On Delay");
    }
    
    @Override
    protected void Logic () {
    }
    
    @Override
    protected void startTimer(){
        TimerTask timTask = new TimerTask() {
            @Override
            public void run () {
                setOutputAfterTimer(true);
            }
        };
        super.scheduleTimer(timTask);
    }
    
    
    @Override
    protected void setOutputAfterTimer(boolean bOutput){

    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
