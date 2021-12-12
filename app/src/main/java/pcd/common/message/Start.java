package pcd.common.message;

public class Start implements Message {
    private int nBodies, nWorkers;
    private long nIter;

    public Start(int nBodies, long nIter, int nWorkers){
        this.nBodies = nBodies;
        this.nIter = nIter;
        this.nWorkers = nWorkers;
    }

    public int getnBodies() {
        return nBodies;
    }

    public int getnWorkers() {
        return nWorkers;
    }

    public long getnIter() {
        return nIter;
    }
}
