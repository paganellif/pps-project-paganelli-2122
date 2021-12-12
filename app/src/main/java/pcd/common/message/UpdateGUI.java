package pcd.common.message;

import pcd.common.Body;

import java.util.List;

public class UpdateGUI implements Message {
    private List<Body> bodies;
    private double vt;
    private long iter;

    public UpdateGUI(List<Body> bodies, double vt, long iter){
        this.bodies = bodies;
        this.vt = vt;
        this.iter = iter;
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public double getVt() {
        return vt;
    }

    public long getIter() {
        return iter;
    }
}
