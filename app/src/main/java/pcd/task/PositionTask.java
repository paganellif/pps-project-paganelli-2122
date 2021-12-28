package task;

import common.Body;
import java.util.concurrent.Callable;

public class PositionTask implements Callable<Body> {
    private final Body body;
    private final double dt;

    public PositionTask(Body body, double dt) {
        this.body = body;
        this.dt = dt;
    }

    @Override
    public Body call() throws Exception {
        body.updatePosition(dt);
        return body;
    }
}
