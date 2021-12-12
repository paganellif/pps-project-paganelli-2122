package pcd.common;

public class Chrono {
    private boolean isRunning = false;
    private long startTime = 0;

    public Chrono(boolean isRunning, long startTime){
        this.isRunning = isRunning;
        this.startTime = startTime;
    }

    public void start(){
        if(!this.isRunning)
            startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

    public void stop(){
        startTime = getTime();
        isRunning = false;
    }

    public boolean isRunning(){
        return this.isRunning;
    }

    public long getTime(){
        return isRunning ? (System.currentTimeMillis() - startTime) : startTime;
    }
}
