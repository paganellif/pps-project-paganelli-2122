package concurrent;

import common.Body;
import common.VisualizerPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

public class SimulationView extends JFrame{
    private final VisualizerPanel viewPanel;
    private final JTextField numBodies= new JTextField("100", 5);
    private final JTextField numSteps = new JTextField("60000", 5);
    private final JTextField numThreads = new JTextField(String.valueOf(Runtime.getRuntime().availableProcessors()), 2);
    private final JButton startButton = new JButton("Start");
    private final JButton pauseButton = new JButton("Pause");
    private final JButton resumeButton = new JButton("Resume");
    private final JButton stopButton = new JButton("Stop");

    public SimulationView(int width, int height, Simulation simulation){
        this.viewPanel = new VisualizerPanel(width, height);

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("N Bodies"));
        controlPanel.add(numBodies);
        controlPanel.add(new JLabel("N Steps"));
        controlPanel.add(numSteps);
        controlPanel.add(new JLabel("N Actors"));
        controlPanel.add(numThreads);
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resumeButton);
        controlPanel.add(stopButton);

        // START BUTTON HANDLER
        startButton.setEnabled(true);
        startButton.addActionListener(ev -> {
            if((numBodies.getText() != null) && (!numBodies.getText().equals("")) &&
                    (numSteps.getText() != null) && (!numSteps.getText().equals("")) &&
                    (numThreads.getText() != null) && (!numThreads.getText().equals(""))){
                int bodies = Integer.parseInt(numBodies.getText());
                int steps = Integer.parseInt(numSteps.getText());
                int threads = Integer.parseInt(numThreads.getText());
                simulation.start(bodies, steps, threads);
                startButton.setEnabled(false);
                resumeButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
            }
        });

        // RESUME BUTTON HANDLER
        resumeButton.setEnabled(false);
        resumeButton.addActionListener(ev -> {
            simulation.resume();
            startButton.setEnabled(false);
            resumeButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        });

        // PAUSE BUTTON HANDLER
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(ev -> {
            simulation.suspend();
            startButton.setEnabled(false);
            resumeButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
        });

        // STOP BUTTON HANDLER
        stopButton.setEnabled(false);
        stopButton.addActionListener(ev -> {
            simulation.stop();
            startButton.setEnabled(true);
            resumeButton.setEnabled(false);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
        });

        JPanel cp = new JPanel();
        LayoutManager lm = new BorderLayout();
        cp.setLayout(lm);
        cp.add(BorderLayout.NORTH, controlPanel);
        cp.add(BorderLayout.CENTER, viewPanel);
        setContentPane(cp);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(-1);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(-1);
            }
        });

        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void reset() {
        SwingUtilities.invokeLater(() -> {
            startButton.setEnabled(true);
            resumeButton.setEnabled(false);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
            display(new LinkedList<>(), 0.0, 0);
        });
    }

    public void display(List<Body> bodies, double vt, long iter) {
        try {
            SwingUtilities.invokeLater(() -> viewPanel.display(bodies, vt, iter));
        } catch (Exception ignored){ }
    }
}
