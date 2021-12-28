package task;

import common.Body;

import java.util.List;
import java.util.concurrent.Callable;

public class ForceTask implements Callable<Body> {
    private final Body body;
    private final List<Body> bodies;

    public ForceTask(Body body, List<Body> bodies) {
        this.body = body;
        this.bodies = bodies;
    }

    @Override
    public Body call() {
        bodies.stream()
                .filter(body1 -> body1 != body)
                .forEach(body::addForce);
        return body;
    }
}
