package concurrent;

import common.Body;
import common.BodyCouple;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread{
    private List<BodyCouple> bodiesCouple;
    private List<Body> bodies;
    private double dt;
    private CyclicBarrier forcesCalculated, positionsUpdated;
    private boolean stopped = false, suspended = false;

    public Worker(List<BodyCouple> bodiesCouple, List<Body> bodies, double dt, CyclicBarrier forcesCalculated, CyclicBarrier positionsUpdated){
        this.bodiesCouple = bodiesCouple;
        this.bodies = bodies;
        this.dt = dt;
        this.forcesCalculated = forcesCalculated;
        this.positionsUpdated = positionsUpdated;
    }

    @Override
    public void run() {

    }

    synchronized void stopThread(){
        stopped = true;
    }

    synchronized void suspendThread(){
        suspended = true;
    }

    synchronized void resumeThread(){
        suspended = false;
        notify();
    }

    private synchronized void log(String msg){
        System.out.println("["+getName()+"] "+msg);
    }
}
