package pcd.common.message;

import pcd.common.Body;

import java.util.List;

public class ComputeForce implements Message {
    private Body body;
    private List<Body> bodies;

    public ComputeForce(Body body, List<Body> bodies){
        this.body = body;
        this.bodies = bodies;
    }

    public Body getBody() {
        return body;
    }

    public List<Body> getBodies() {
        return bodies;
    }
}
