package pcd.common;

import java.util.LinkedList;
import java.util.List;

public class Body {
    private final Position position;
    private final Velocity velocity;
    private final double radius;
    private final double mass;
    private static final double UNIVERSAL_GRAVITY = 6.674 * Math.pow(10, -11);
    private final List<Force> forces = new LinkedList<>();

    public Body(Position position, Velocity velocity, double radius, double mass){
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.mass = mass;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public double getDistance(Body body){
        double dx = position.getX() - body.getPosition().getX();
        double dy = position.getY() - body.getPosition().getY();
        double minDist = radius + body.getRadius();
        double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        return (distance < minDist) ? minDist : distance;
    }

    public void addforce(Force force){
        this.forces.add(force);
    }

    public Force addForce(Body body){
        double dx = (body.getPosition().getX() - position.getX())/Math.abs(body.getPosition().getX() - position.getX());
        double dy = (body.getPosition().getY() - position.getY())/Math.abs(body.getPosition().getY() - position.getY());

        double dp = getDistance(body);
        double x = UNIVERSAL_GRAVITY*mass*body.getMass()/dp * dx;
        double y = UNIVERSAL_GRAVITY*mass*body.getMass()/dp * dy;
        Force force = new Force(x,y);
        forces.add(force);
        return force;
    }

    public void updatePosition(double dt){
        double forceX = 0, forceY = 0;
        for(Force force : forces) {
            forceX += force.getX();
            forceY += force.getY();
        }

        forces.clear();

        double vx0 = velocity.getX() + (forceX / mass * dt);
        double vy0 = velocity.getY() + (forceY / mass * dt);

        position.setX(position.getX() + (velocity.getX() * dt) + (0.5 * forceX / mass * Math.pow(dt, 2)));
        position.setY(position.getY() + (velocity.getY() * dt) + (0.5 * forceY / mass * Math.pow(dt, 2)));
        velocity.setX(vx0);
        velocity.setY(vy0);
    }

    @Override
    public String toString() {
        return "Body{" +
                "position=" + position +
                ", velocity=" + velocity +
                ", radius=" + radius +
                ", mass=" + mass +
                ", forces=" + forces +
                '}';
    }
}
