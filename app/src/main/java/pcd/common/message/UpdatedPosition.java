package pcd.common.message;

import pcd.common.Body;

public class UpdatedPosition implements Message {
    private Body body;

    public UpdatedPosition(Body body){
        this.body = body;
    }

    public Body getBody() {
        return body;
    }
}
