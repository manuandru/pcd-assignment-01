package pcd.assignment01;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

class View extends JFrame {

    private Controller controller;
    private JTextField directoryInput;
    private JTextField intervalsInput;
    private JTextField maxIntervalInput;
    JTextArea distributions;
    JTextArea longestFiles;


    public View(Controller controller) {
        super("File statistics");

        this.controller = controller;

        setSize(800, 600);
        setResizable(false);

        var topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);

        // Input definition
        var inputPanel = new JPanel(new GridLayout(3, 2, 10, 0));
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

        inputPanel.add(new JLabel("Intervals:"));
        intervalsInput = new JTextField("5", 10);
        inputPanel.add(intervalsInput);

        inputPanel.add(new JLabel("Max Interval:"));
        maxIntervalInput = new JTextField("1000", 10);
        inputPanel.add(maxIntervalInput);

        // Buttons definition
        var buttonsPanel = new JPanel();
        topPanel.add(buttonsPanel);
        JButton startButton = new JButton("Avvia");
        startButton.addActionListener(e -> startAction());
        buttonsPanel.add(startButton);

        JButton stopButton = new JButton("Ferma");
        stopButton.addActionListener(e -> stopAction());
        buttonsPanel.add(stopButton);

        // Center Area -- files and distributions
        JPanel centerPanel = new JPanel();
        add(centerPanel, BorderLayout.CENTER);

        distributions = new JTextArea(28,30);
        distributions.setEnabled(false);
        centerPanel.add(distributions);

        longestFiles = new JTextArea(28,30);
        longestFiles.setEnabled(false);
        centerPanel.add(longestFiles);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(-1);
            }
        });
    }

    private void startAction() {
        this.controller.startSearch();
    }

    private void stopAction() {
        this.controller.stopSearch();
    }

//    @Override
//    public void modelUpdated(MyModel model) {
//        try {
//            System.out.println("[View] model updated => updating the view");
//            SwingUtilities.invokeLater(() -> {
//                state.setText("state: " + model.getState());
//            });
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
}
