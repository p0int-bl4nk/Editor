/*
 * Copyright (c) 2021. Sachin Verma
 * All rights reserved
 *
 */

import javax.swing.*;

public class Find extends JFrame {
    private JPanel panel1;
    private JButton nextButton;
    private JButton findButton;
    private JButton previousButton;
    private JTextField searchTextField;
    private JLabel numberOfOccurrences;
    private Search find;
    public Find(JTextArea textArea) {
        super("Find");
        setContentPane(panel1);
        setSize(300, 155);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        findButton.addActionListener(actionEvent -> {
            find = new Search(searchTextField.getText(), textArea);
            updateNumberOfOccurrences();
        });

        nextButton.addActionListener(actionEvent -> {
            find.nextOccurrence();
            updateNumberOfOccurrences();
        });

        previousButton.addActionListener(actionEvent -> {
            find.previousOccurrence();
            updateNumberOfOccurrences();
        });
    }
    void updateNumberOfOccurrences() {
        int index = find.getIndex() + 1;
        int total = find.getTotalOccurrences();
        numberOfOccurrences.setText("Number of occurrences: " + index + "/" + total);
    }
}
