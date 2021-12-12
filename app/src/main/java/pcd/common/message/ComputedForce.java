package pcd.common.message;

import pcd.common.Body;

public class ComputedForce {
    private Body body;

    public ComputedForce(Body body){
        this.body = body;
    }

    public Body getBody(){
        return this.body;
    }
}
