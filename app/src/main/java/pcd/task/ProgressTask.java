package task;

import java.util.concurrent.Callable;

public class ProgressTask implements Callable<Void> {
    private final SimulationFrames sharedResult;
    private final double dt;

    public ProgressTask(SimulationFrames sharedResult, double dt) {
        this.sharedResult = sharedResult;
        this.dt = dt;
    }

    @Override
    public Void call() throws Exception {
        sharedResult.increaseIterAndTime(dt);
        return null;
    }
}
