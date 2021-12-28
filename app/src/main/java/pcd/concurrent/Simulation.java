package concurrent;

import common.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Simulation {
    private int nIter = 0, iter = 0, nThreads = 0, nBodies = 0;
    private double vt = 0.0;
    private double dt = 0.0;
    private boolean isStarted = true;
    private List<Body> bodies = new LinkedList<>();
    private List<Worker> threadPool = new LinkedList<>();
    private Boundary boundary = new Boundary(-1,-1,1,1);
    private Chrono chrono = new Chrono(false, 0);
    private CyclicBarrier forcesCalculated, positionsUpdated;
    private SimulationView view;
    private Random rand = new Random(System.currentTimeMillis());

    public Simulation() {
        int WIDTH = 700;
        int HEIGHT = WIDTH;
        this.view = new SimulationView(WIDTH, HEIGHT, this);
    }

    synchronized void start(int nBodies, int nIter, int nThreads){
        this.nBodies = nBodies;
        this.nIter = nIter;
        this.nThreads = nThreads;
        initBodies();
        initBarriers();
        initWorkerThreads();
        System.out.println("---------SIMULATION STARTED---------");
        System.out.println("Bodies: "+nBodies);
        System.out.println("Iter: "+nIter);
        System.out.println("Threads: "+nThreads);
        isStarted = true;
        threadPool.forEach(Thread::start);
        chrono.start();
    }

    synchronized void stop(){
        chrono.stop();
        threadPool.forEach(Worker::stopThread);
        view.display(new LinkedList<Body>(), dt, iter);
        System.out.println("Time: "+chrono.getTime());
        System.out.println("---------SIMULATION ENDED---------");
        flushSimulationData();
    }

    synchronized void suspend(){
        threadPool.forEach(Worker::suspendThread);
    }

    synchronized void resume(){
        if(!isStarted)
            start(nBodies, nIter, nThreads);
        else
            threadPool.forEach(Worker::resumeThread);
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

    private void flushSimulationData() {
        vt = iter = 0;
        bodies.clear();
        threadPool.clear();
    }

    private void brokenBarrier(){
        vt += dt;
        iter++;
        if(iter == nIter) {
            stop();
            view.reset();
        } else
            view.display(bodies, vt, iter);
    }

    private void initBarriers(){
        forcesCalculated = new CyclicBarrier(nThreads);
        // Runnable executed on broken barrier
        positionsUpdated = new CyclicBarrier(nThreads, this::brokenBarrier);
    }

    private void initWorkerThreads(){
        List<BodyCouple> bodyCouples = new LinkedList<>();
        for(Body b1 : bodies)
            for(Body b2 : bodies)
                if(b1 != b2)
                    bodyCouples.add(new BodyCouple(b1, b2));

        int coupleDecomposition = bodyCouples.size() / nThreads;
        int bodiesDecomposition = bodies.size() / nThreads;

        for(int i = 0; i < nThreads; i++){
            List<BodyCouple> workerCouples = new LinkedList<>();
            List<Body> workerBodies = new LinkedList<>();

            if(i < (nThreads-1)){
                workerCouples = bodyCouples.subList(coupleDecomposition*i, coupleDecomposition*(i+1));
                workerBodies = bodies.subList(bodiesDecomposition*i, bodiesDecomposition*(i+1));
            } else {
                workerCouples = bodyCouples.subList(coupleDecomposition*i, bodyCouples.size());
                workerBodies = bodies.subList(bodiesDecomposition*i, bodies.size());
            }

            threadPool.add(new Worker(workerCouples, workerBodies, dt, forcesCalculated, positionsUpdated));
            System.out.println("Initialized worker thread "+i+" with "+workerBodies.size()+" couple of bodies");
        }
    }
}
