package pcd.task;

import java.util.concurrent.Callable;

public class GUITask implements Callable<Void> {
    private final SimulationFrames sharedResult;
    private final SimulationView gui;

    public GUITask(SimulationFrames sharedResult, SimulationView gui) {
        this.sharedResult = sharedResult;
        this.gui = gui;
    }

    @Override
    public Void call() throws Exception {
        gui.display(sharedResult.getUpdatedBodies(), sharedResult.getCurrentTime(), sharedResult.getCurrentIter());
        return null;
    }
}
