package pcd.common;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class VisualizerPanel extends JPanel {
    private final int width;
    private final int heigth;
    private double vt = 0.0;
    private long iter = 0;
    private List<Body> bodies = new LinkedList<>();

    public VisualizerPanel(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
        setSize(width, heigth-40);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0,0,getWidth(),getHeight());

        bodies.forEach(body -> {
            double rad = body.getRadius();
            double dx = width/2.0 - 10;
            double dy = heigth/2.0 - 20;
            int x0 = (int) (dx + body.getPosition().getX() * dx);
            int y0 = (int) (dy + body.getPosition().getY() * dy);

            g2.drawOval(x0, y0, (int) (rad*dx*2), (int) (rad*dy*2));
        });

        g2.drawString("Bodies: "+bodies.size()+" - vt: "+
                String.format("%.2f", vt)+" - iter: "+iter,2,20);
    }

    public void display(List<Body> bodies, double vt, long iter) {
        this.bodies = bodies;
        this.vt = vt;
        this.iter = iter;
        repaint();
    }
}
