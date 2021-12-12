package pcd.reactive;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import pcd.reactive.entry.Entry;
import pcd.reactive.entry.TempErrorEntry;
import pcd.reactive.entry.TempSensorEntry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

public class SimulationView extends JFrame {
    private final VisualizerPanel tempHistory;
    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JTextField minTemp;
    private final JTextField avgTemp;
    private final JTextField maxTemp;
    private final JTextField thresholdTime;
    private final JTextField thresholdTemp;
    private ThermalManager tm;
    private final List<TempStream> tempStreams = new LinkedList<>();
    private final Flowable<Long> time;
    private final Flowable<Double> temp;
    private Disposable disposableTemp;
    private Disposable disposableError;

    public SimulationView(int width, int height) {
        this.tempHistory = new VisualizerPanel(width, (height / 2) - 20, new JTextArea(10, 10));

        tempStreams.add(new TempStream(20, 40, 0.1, 500));
        tempStreams.add(new TempStream(20, 40, 0.1, 500));
        tempStreams.add(new TempStream(20, 40, 0.1, 500));

        setTitle("Reactive Temperature Monitor");
        setSize(width, height);
        setResizable(false);

        JPanel controlPanel = new JPanel();
        controlPanel.setSize(width, 40);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(new JLabel("Threshold Time: [ms]"));
        thresholdTime = new JTextField("2000", 5);
        controlPanel.add(thresholdTime);
        controlPanel.add(new JLabel("Threshold Temp: [C°]"));
        thresholdTemp = new JTextField("100", 5);
        controlPanel.add(thresholdTemp);
        controlPanel.add(new JLabel("Min Temp: [C°]"));
        this.minTemp = new JTextField("0", 5);
        this.minTemp.setEditable(false);
        controlPanel.add(minTemp);
        controlPanel.add(new JLabel("Avg Temp: [C°]"));
        this.avgTemp = new JTextField("0", 5);
        this.avgTemp.setEditable(false);
        controlPanel.add(avgTemp);
        controlPanel.add(new JLabel("Max Temp: [C°]"));
        this.maxTemp = new JTextField("0", 5);
        this.maxTemp.setEditable(false);
        controlPanel.add(maxTemp);

        this.temp = Flowable.create(emitter -> {
            emitter.onNext(Double.parseDouble(thresholdTemp.getText()));

            this.thresholdTemp.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { emit(); }

                @Override
                public void removeUpdate(DocumentEvent e) { emit(); }

                @Override
                public void changedUpdate(DocumentEvent e) { emit(); }

                private void emit() {
                    emitter.onNext(Double.parseDouble(thresholdTemp.getText()));
                }
            });
        }, BackpressureStrategy.LATEST);

        this.time = Flowable.create(emitter -> {
            emitter.onNext(Long.parseLong(thresholdTime.getText()));

            this.thresholdTime.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { emit(); }

                @Override
                public void removeUpdate(DocumentEvent e) { emit(); }

                @Override
                public void changedUpdate(DocumentEvent e) { emit(); }

                private void emit() {
                    emitter.onNext(Long.parseLong(thresholdTime.getText()));
                }
            });
        }, BackpressureStrategy.LATEST);

        // START BUTTON HANDLER
        startButton.setEnabled(true);
        startButton.addActionListener(ev -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            this.tm = new ThermalManager(this.tempStreams, this.temp, this.time);
            //tm.log();
            this.tempHistory.flush();
            this.disposableTemp = tm.toTempEntry().subscribe(this::addEntry);
            this.disposableError = tm.toErrorEntry().subscribe(this::addEntry);
        });

        // STOP BUTTON HANDLER
        stopButton.setEnabled(false);
        stopButton.addActionListener(ev -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            this.disposableTemp.dispose();
            this.disposableError.dispose();
        });

        JPanel cp = new JPanel();
        LayoutManager lm = new BorderLayout();
        cp.setLayout(lm);
        cp.add(BorderLayout.NORTH, controlPanel);
        cp.add(BorderLayout.CENTER, tempHistory);
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

    void addEntry(final Entry entry){
        if(entry instanceof TempSensorEntry){
            tempHistory.display(entry.toString());
            this.minTemp.setText(String.format("%.2f", ((TempSensorEntry) entry).getTempMin()));
            this.avgTemp.setText(String.format("%.2f", ((TempSensorEntry) entry).getTempAvg()));
            this.maxTemp.setText(String.format("%.2f", ((TempSensorEntry) entry).getTempMax()));
        } else if(entry instanceof TempErrorEntry){
            try {
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame();
                    JDialog dialog = new JDialog(frame, "WARNING", true);
                    dialog.setLayout(new FlowLayout());
                    JButton ok = new JButton("OK");
                    ok.addActionListener((ev) -> {
                        dialog.setVisible(false);
                    });
                    dialog.add(new JLabel(((TempErrorEntry) entry).toMessage()));
                    dialog.add(ok);
                    dialog.setSize(700, 100);
                    dialog.setResizable(false);
                    dialog.setVisible(true);
                });
            } catch (Exception ignored) { }
        }
    }

    private static class VisualizerPanel extends JScrollPane {
        private final JTextArea textArea;
    
        public VisualizerPanel(int width, int heigth, JTextArea textArea) {
            super(textArea);
            this.textArea = textArea;
            setSize(width, heigth - 40);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }

        public void display(String values) {
            try {
              SwingUtilities.invokeLater(() -> textArea.append(values+'\n'));
            } catch (Exception ignored) { }
        }

        public void flush() {
            try {
                SwingUtilities.invokeLater(() -> textArea.setText(""));
            } catch (Exception ignored) { }
        }
    }
}
