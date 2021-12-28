package pcd.task;

import pcd.common.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class SimulatorService extends Thread{
    private final int poolSize, nBodies, nIter;
    private final SimulationView gui;
    private final ExecutorService executor;
    private final CompletionService<Body> forcesService, positionsService;
    private final CompletionService<Void> progressService, guiService;
    private final Boundary boundary= new Boundary(-1, -1, 1, 1);
    private List<Body> bodies = new LinkedList<>();
    private final double dt = 0.01;
    private final SimulationFrames sharedResult = new SimulationFrames();
    private final Chrono chrono = new Chrono(false, 0);
    private boolean stopped = false, suspended = false;
    private final Random rand = new Random(System.currentTimeMillis());

    public SimulatorService(int poolSize, int nBodies, int nIter, SimulationView gui){
        this.poolSize = poolSize;
        this.nBodies = nBodies;
        this.nIter = nIter;
        this.gui = gui;

        executor = (poolSize > 0) ? Executors.newFixedThreadPool(poolSize) : Executors.newCachedThreadPool();
        forcesService = new ExecutorCompletionService<>(executor);
        positionsService = new ExecutorCompletionService<>(executor);
        progressService = new ExecutorCompletionService<>(executor);
        guiService = new ExecutorCompletionService<>(executor);
    }

    @Override
    public void run() {
        System.out.println("---------SIMULATION STARTED---------");
        System.out.println("Bodies: "+nBodies);
        System.out.println("Iter: "+nIter);
        if(poolSize > 0)
            System.out.println("Pool Size:"+poolSize+" [FIXED]");
        else
            System.out.println("Pool size unlimited [CACHED]");
        initBodies();
        chrono.start();

        while((sharedResult.getCurrentIter() < nIter) && (!stopped)) {
            synchronized(this) {
                if(suspended)
                    while(suspended) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }

            // compute forces applied on each body
            if(!this.executor.isShutdown())
                this.bodies.forEach(body -> {
                    try {
                        forcesService.submit(new ForceTask(body, bodies));
                    } catch (RejectedExecutionException e){
                        System.out.println("[FORCE-TASK] Rejected execution!");
                    }
                });

            // update positions
            if(!this.executor.isShutdown())
                this.bodies.forEach(body -> {
                    Body b = null;
                    try {
                        b = forcesService.take().get();
                        positionsService.submit(new PositionTask(b, dt));
                    } catch (InterruptedException | ExecutionException | RejectedExecutionException e) {
                        System.out.println("[POSITION-TASK] Rejected execution!");
                    }
                });

            // update shared result
            if(!this.executor.isShutdown())
                this.bodies.forEach(body -> {
                    Body b = null;
                    try {
                        b = positionsService.take().get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    sharedResult.addUpdatedBody(b);
                });

            // increase iter and time
            if(!this.executor.isShutdown())
                try {
                    progressService.submit(new ProgressTask(sharedResult, dt));
                } catch (RejectedExecutionException e){
                    System.out.println("[PROGRESS-TASK] Rejected execution!");
                }

            // updateGUI
            if(!this.executor.isShutdown())
                try {
                    guiService.submit(new GUITask(sharedResult, gui));
                } catch (RejectedExecutionException e){
                    System.out.println("[GUI-TASK] Rejected execution!");
                }
        }

        chrono.stop();
        System.out.println("Time: "+chrono.getTime());
        System.out.println("---------SIMULATION ENDED---------");
    }

    public void startSimulation() { start(); }

    synchronized void stopSimulation() throws InterruptedException {
        stopped = true;
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    synchronized void suspendSimulation(){ suspended = true; }

    synchronized void resumeSimulation(){
        suspended = false;
        notify();
    }

    private void initBodies(){
        for(int i = 0; i < nBodies; i++){
            if(i == (nBodies-1))
                bodies.add(new Body(new Position(0,0), new Velocity(0,0), 0.07, 100_000_000));
            else {
                double x = boundary.getX0() + rand.nextDouble() * (boundary.getX1() - boundary.getX0());
                double y = boundary.getX0() + rand.nextDouble() * (boundary.getX1() - boundary.getX0());
                double dx = -1 + rand.nextDouble() * 2;
                double speed = rand.nextDouble() * 0.000002;
                Body body = new Body(new Position(x,y),
                        new Velocity(dx * speed, Math.sqrt(1 - dx * dx) * speed),
                        0.01 + ((0.04 - 0.01) * rand.nextDouble()),
                        1_000 + ((100_000 - 1_000) * rand.nextDouble()));
                bodies.add(body);
            }
        }
    }
}
