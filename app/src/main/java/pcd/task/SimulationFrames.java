package pcd.task;

import pcd.common.Body;

import java.util.LinkedList;
import java.util.List;

public class SimulationFrames {
    private long iter= 0;
    private double vt = 0.0;
    private final List<Body> bodies = new LinkedList<>();

    synchronized long getCurrentIter(){
        return iter;
    }

    synchronized double getCurrentTime(){
        return vt;
    }

    synchronized boolean addUpdatedBody(Body body){
        return bodies.add(body);
    }

    synchronized List<Body> getUpdatedBodies(){
        List<Body> result = new LinkedList<>(bodies);
        bodies.clear();
        return bodies;
    }

    synchronized void increaseIterAndTime(double dt){
        iter++;
        vt += dt;
    }
}
