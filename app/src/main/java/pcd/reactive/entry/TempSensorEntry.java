package pcd.reactive.entry;

public class TempSensorEntry extends EntryImpl{
    private final double tempAvg;
    private final double tempMin;
    private final double tempMax;

    public TempSensorEntry(double tempAvg, double tempMin, double tempMax){
        this.tempAvg = tempAvg;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public double getTempAvg() {
        return tempAvg;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    @Override
    public String toString() {
        return super.toString()
                +"|TEMP_MIN: "+String.format("%.2f", tempMin)
                +"|TEMP_AVG: "+String.format("%.2f", tempAvg)
                +"|TEMP_MAX: "+String.format("%.2f", tempMax);
    }
}
