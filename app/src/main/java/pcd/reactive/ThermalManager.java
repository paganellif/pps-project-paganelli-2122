package pcd.reactive;

import pcd.common.Chrono;
import io.reactivex.rxjava3.core.Flowable;
import pcd.reactive.entry.TempErrorEntry;
import pcd.reactive.entry.TempSensorEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ThermalManager {
    private final List<Flowable<Double>> streams;
    private final double maxVariation = 10;
    private final Flowable<Double> thresholdTemp;
    private final Flowable<Long> thresholdTime;
    private final Chrono chrono;

    public ThermalManager(final List<TempStream> tempStreams,
                          final Flowable<Double> thresholdTemp,
                          final Flowable<Long> thresholdTime){
        this.thresholdTemp = thresholdTemp;
        this.thresholdTime = thresholdTime;
        this.chrono = new Chrono(false, 0);
        this.streams = tempStreams.stream()
                .map(TempStream::toFlowable)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Flowable<Double> rawTemps(){ return Flowable.merge(streams); }

    public Flowable<Double> temps(){
        return this.rawTemps().scan((a,b) -> (Math.abs(b - a) < maxVariation) ? b : a);
    }

    public Flowable<Double> averageTemps(){
        return this.temps()
                .scan(Double::sum)
                .zipWith(Flowable.rangeLong(1, Long.MAX_VALUE), (a,b) -> a/b);
    }

    public Flowable<Double> minTemps() {
        return this.temps().scan(Double::min);
    }

    public Flowable<Double> maxTemps() {
        return this.temps().scan(Double::max);
    }

    public Flowable<TempSensorEntry> toTempEntry() {
        return Flowable.zip(this.minTemps(), this.averageTemps(), this.maxTemps(),
                (a, b, c) -> new TempSensorEntry(b,a,c));
    }

    public Flowable<TempErrorEntry> toErrorEntry() {
        return this.averageTemps()
                .withLatestFrom(thresholdTemp, (avgTemp, thrTemp) -> {
                    double delta = avgTemp - thrTemp;
                    if(delta > 0.0)
                        this.chrono.start();
                    else
                        this.chrono.stop();
                    return delta;
                })
                .filter(delta -> delta > 0.0)
                .withLatestFrom(thresholdTime, (delta, thrTime) -> {
                    if(this.chrono.isRunning() && (this.chrono.getTime() > thrTime)){
                        this.chrono.stop();
                        return new TempErrorEntry(delta);
                    } else
                        return new TempErrorEntry(Double.NaN);
                })
                .filter(tempErrorEntry -> !Double.isNaN(tempErrorEntry.getTemp()));
    }


    public void log() {
        this.rawTemps().subscribe(a -> System.out.println("RAW: "+a));
        this.temps().subscribe(a -> System.out.println("TEMPS: "+a));
        this.averageTemps().subscribe(a -> System.out.println("AVG: "+a));
        this.minTemps().subscribe(a -> System.out.println("MIN: "+a));
        this.maxTemps().subscribe(a -> System.out.println("MAX: "+a));
        this.toTempEntry().subscribe(System.out::println);
    }
}
