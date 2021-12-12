package pcd.common;

public class BodyCouple {
    private final Body b1;
    private final Body b2;

    public BodyCouple(Body b1, Body b2){
        this.b1 = b1;
        this.b2 = b2;
    }

    public Body getB1() {
        return b1;
    }

    public Body getB2() {
        return b2;
    }
}
