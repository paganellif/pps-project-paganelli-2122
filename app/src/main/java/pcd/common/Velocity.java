package pcd.common;

public class Velocity {
    private double x;
    private double y;

    public Velocity(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double module() {
        return (x*x) + (y*y);
    }

    @Override
    public String toString() {
        return "Velocity{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
