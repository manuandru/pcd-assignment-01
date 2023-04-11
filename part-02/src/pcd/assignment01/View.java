package pcd.assignment01;


import pcd.assignment01.coordination.ModelObserver;
import pcd.assignment01.stats.StatisticForView;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.*;

class View extends JFrame implements ModelObserver {

    private final Controller controller;
    private JTextField directoryInput;
    private final JTextField intervalsInput;
    private final JTextField maxIntervalInput;
    private final JTextField extensionInput;
    private final JTextArea distributions;
    private final JTextArea longestFiles;
    private final JButton startButton;
    private final JButton stopButton;


    public View(Controller controller) {
        super("File statistics");

        this.controller = controller;

        setSize(1200, 600);
        setResizable(false);

        var topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);

        // Input definition
        var inputPanel = new JPanel(new GridLayout(4, 2, 10, 0));
        topPanel.add(inputPanel);

        var chooseDirButton = new JButton("Choose directory");
        inputPanel.add(chooseDirButton);
        chooseDirButton.addActionListener(e -> {
            var fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            var returnVal = fc.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                directoryInput.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });
        directoryInput = new JTextField("./", 20);
        directoryInput.setEnabled(false);
        inputPanel.add(directoryInput);

        inputPanel.add(new JLabel("Extensions (comma separated, empty => all files)"));
        extensionInput = new JTextField("java,c", 5);
        inputPanel.add(extensionInput);

        inputPanel.add(new JLabel("Intervals:"));
        intervalsInput = new JTextField("5", 5);
        inputPanel.add(intervalsInput);

        inputPanel.add(new JLabel("Max Interval:"));
        maxIntervalInput = new JTextField("1000", 10);
        inputPanel.add(maxIntervalInput);

        // Buttons definition
        var buttonsPanel = new JPanel();
        topPanel.add(buttonsPanel);
        startButton = new JButton("Avvia");
        startButton.addActionListener(e -> startAction());
        buttonsPanel.add(startButton);

        stopButton = new JButton("Ferma");
        stopButton.addActionListener(e -> stopAction());
        stopButton.setEnabled(false);
        buttonsPanel.add(stopButton);

        // Center Area -- files and distributions
        JPanel centerPanel = new JPanel();
        add(centerPanel, BorderLayout.CENTER);

        distributions = new JTextArea(25,20);
        distributions.setEnabled(false);
        centerPanel.add(new JScrollPane(distributions));

        longestFiles = new JTextArea(25,40);
        longestFiles.setEnabled(false);
        centerPanel.add(new JScrollPane(longestFiles));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(-1);
            }
        });
    }

    private void startAction() {

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        distributions.setText("");
        longestFiles.setText("");

        var dir = this.directoryInput.getText();
        var nInterval = Integer.parseInt(this.intervalsInput.getText());
        var maxInterval = Integer.parseInt(this.maxIntervalInput.getText());
        var extensions = Set.of(this.extensionInput.getText().split(","));
        if (this.extensionInput.getText().equals("")) {
            extensions = Set.of();
        }

        this.controller.startSearch(dir, nInterval, maxInterval, extensions);
    }

    private void stopAction() {
        this.controller.stopSearch();
    }

    @Override
    public void modelUpdated(StatisticForView stats) {
        try {
            SwingUtilities.invokeLater(() -> {
                distributions.setText(stats.getIntervals());
//                longestFiles.setText(stats.getLongestFiles());
                longestFiles.setText(stats.getLongestFiles(10));
            });
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void requiredActionIsComplete() {
        try {
            SwingUtilities.invokeLater(() -> {
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
            });
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
