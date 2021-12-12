package pcd.common.message;

import pcd.common.Body;

public class UpdatePosition implements Message {
    private Body body;
    private double dt;

    public UpdatePosition(Body body, double dt){
        this.body = body;
        this.dt = dt;
    }

    public Body getBody() {
        return body;
    }

    public double getDt() {
        return dt;
    }
}
