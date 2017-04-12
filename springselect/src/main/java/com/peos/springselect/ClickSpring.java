package com.peos.springselect;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/31.
 */

public class ClickSpring {

    private int timeMS;
    private boolean isStart;
    private Timer timerClick;

    private boolean oldState;
    private boolean newState;

    public ClickSpring(){
        init(1000);
    }

    public ClickSpring(int timeMS){
        init(timeMS);
    }

    private void init(int timeMS){
        this.timeMS = timeMS;
        this.isStart = false;
    }

    public boolean isStart(){
        return isStart;
    }

    public void start(boolean state){

        if(isStart) {
            newState = state;
            return;
        }

        isStart = true;
        oldState = state;

        if(clickSpringListener != null)
            clickSpringListener.onClickSpring();

        timerClick = new Timer();
        timerClick.schedule(
                new TimerTask(){
                    @Override
                    public void run() {
                        isStart = false;
                        if(newState != oldState) {
                            if (clickSpringListener != null)
                                clickSpringListener.onClickSpring();
                        }
                    }
                }, timeMS);
    }

    public void cancel(){

        if(timerClick != null) {
            isStart = false;

            timerClick.cancel();
            timerClick.purge();

            timerClick = null;
        }
    }

    private ClickSpringListener clickSpringListener;
    public void setClickSpringListener(ClickSpringListener clickSpringListener) {
        this.clickSpringListener = clickSpringListener;
    }

}
