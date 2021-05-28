/*
 * Copyright (c) 2021. Sachin Verma
 * All rights reserved
 *
 */

import javax.swing.*;
import java.awt.*;

public class Appearance extends JFrame{
    private JComboBox<String> fontFamilyComboBox;
    private JTextPane theQuickBrownFoxTextPane;
    private JComboBox<Integer> fontSizeComboBox;
    private JButton OKButton;
    private JButton cancelButton;
    private JPanel contentPane;
    private JButton previewButton;
    private JButton chooseColorFontButton;
    private JButton chooseColorBackgroundButton;
    private JTextPane fontColorTextPane;
    private JTextPane backgroundColorTextPane;
    private Color fontColor;
    private Color backgroundColor;

    public Appearance(JTextArea textArea) {
        super("Appearance");
        setSize(760, 340);
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addFontFamilies();
        addFontSizes();

        fontColor = textArea.getForeground();
        backgroundColor = textArea.getBackground();

        fontColorTextPane.setBackground(fontColor);
        backgroundColorTextPane.setBackground(backgroundColor);

        theQuickBrownFoxTextPane.setBackground(backgroundColor);
        theQuickBrownFoxTextPane.setForeground(fontColor);
        theQuickBrownFoxTextPane.setFont(textArea.getFont());

        fontSizeComboBox.setSelectedItem(textArea.getFont().getSize());
        fontFamilyComboBox.setSelectedItem(textArea.getFont().getFamily());

        OKButton.addActionListener(actionEvent -> {
            textArea.setFont(new Font(String.valueOf(fontFamilyComboBox.getSelectedItem()),
                    Font.PLAIN, Integer.parseInt(String.valueOf(fontSizeComboBox.getSelectedItem()))));
            textArea.setBackground(backgroundColor);
            textArea.setForeground(fontColor);
            dispose();
        });

        previewButton.addActionListener(actionEvent -> {
            //System.out.println(getX() + " " + getY());
            theQuickBrownFoxTextPane.setFont(new Font(String.valueOf(fontFamilyComboBox.getSelectedItem()),
                    Font.PLAIN, Integer.parseInt(String.valueOf(fontSizeComboBox.getSelectedItem()))));
            theQuickBrownFoxTextPane.setForeground(fontColor);
            theQuickBrownFoxTextPane.setBackground(backgroundColor);

        });
        cancelButton.addActionListener(actionEvent -> dispose());

        chooseColorFontButton.addActionListener(actionEvent -> {
            fontColor = JColorChooser.showDialog(this,
                    "Choose a font color", fontColor);
            fontColorTextPane.setBackground(fontColor);
        });
        chooseColorBackgroundButton.addActionListener(actionEvent -> {
            backgroundColor = JColorChooser.showDialog(this,
                    "Choose a background color", backgroundColor);
            backgroundColorTextPane.setBackground(backgroundColor);
        });

        setVisible(true);
    }

    private void addFontFamilies() {
        for (String family : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            fontFamilyComboBox.addItem(family);
        }
    }

    private void addFontSizes() {
        for (int i = 10; i <= 60; i += 2) {
            fontSizeComboBox.addItem(i);
        }
    }
}
