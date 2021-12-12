package pcd.reactive.entry;

public class TempErrorEntry extends EntryImpl{
    private final double temp;

    public TempErrorEntry(double temp) {
        this.temp = temp;
    }

    public double getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return super.toString()+"|TEMP_THRESHOLD: "+temp;
    }

    public String toMessage(){
        return "!!!WARNING!!!"+'\n'+"The current average temperature has exceeded the threshold! ["+String.format("%.2f",temp)+" C°]";
    }
}
