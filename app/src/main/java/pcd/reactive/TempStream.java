package pcd.reactive;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import static java.lang.Thread.sleep;

public class TempStream {
    private final TempSensor tempSensor;
    private final Flowable<Double> tempStream;

    public TempStream(double min, double max, double spikeFreq, int freq){
        this.tempSensor = new TempSensor(min, max, spikeFreq);
        this.tempStream = Flowable.create(emitter -> {
            new Thread(() -> {
                while(true){
                    emitter.onNext(tempSensor.getCurrentValue());
                    try {
                        sleep(freq);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }, BackpressureStrategy.LATEST);
    }

    public Flowable<Double> toFlowable(){
        return tempStream;
    }
}
