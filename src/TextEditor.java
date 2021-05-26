/*
 * Copyright (c) 2021. Sachin Verma
 * All rights reserved
 *
 */

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextEditor extends JFrame {
    private final File[] file = {null};
    private final JTextArea textArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(textArea);

    private final JMenuItem open = new JMenuItem("Open");
    private final JMenuItem save = new JMenuItem("Save");
    private final JMenuItem exit = new JMenuItem("Exit");
    private final JMenu menu = new JMenu("File");

    private final JMenuItem find = new JMenuItem("Find");
    private final JMenuItem findAndReplace = new JMenuItem("Find and Replace");
    private final JMenu searchMenu = new JMenu("Search");

    private final JMenuItem appearance = new JMenuItem("Appearance");
    private final JMenu settings = new JMenu("Settings");

    private final JMenuBar menuBar = new JMenuBar();

    private final Action openFileChooser = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(getParent());
            try {
                file[0] = new File(fileChooser.getSelectedFile().getPath());
                textArea.setText(readFile(file[0].getPath()));
                textArea.setCaretPosition(0);
                setTitle(file[0].getName());
            } catch (IOException e) {
                textArea.setText("");
                e.printStackTrace();
            }
        }
    };

    private final Action writeToFile = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try (PrintWriter writer = new PrintWriter(file[0])){
                writer.print(textArea.getText());
            } catch (IOException e) {
                System.out.println("File access error!");
                e.printStackTrace();
            }
        }
    };


    public TextEditor() {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    void initComponents() {
        setMargin(textArea, 20, 10, 20, 10);
        textArea.setName("TextArea");
        textArea.setFont(new Font("Serif", Font.PLAIN, 18));

        scrollPane.setName("ScrollPane");

        add(scrollPane, BorderLayout.CENTER);

        initMenuBar();
    }

    void initMenuBar() {
        open.addActionListener(openFileChooser);
        open.setMnemonic(KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        open.setName("MenuOpen");

        save.addActionListener(writeToFile);
        save.setMnemonic(KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        save.setName("MenuSave");

        exit.addActionListener(actionEvent -> System.exit(0));
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_DOWN_MASK));
        exit.setName("MenuExit");

        menu.setMnemonic(KeyEvent.VK_F);
        menu.setName("MenuFile");
        menu.add(open);
        menu.add(save);
        menu.addSeparator();
        menu.add(exit);

        find.setMnemonic(KeyEvent.VK_F);
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        find.addActionListener(actionEvent -> new Find(textArea));

        findAndReplace.setMnemonic(KeyEvent.VK_R);
        findAndReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        findAndReplace.addActionListener(actionEvent -> new FindAndReplace(textArea));

        searchMenu.setMnemonic(KeyEvent.VK_S);
        searchMenu.setName("MenuSearch");
        searchMenu.add(find);
        searchMenu.add(findAndReplace);

        appearance.addActionListener(actionEvent -> new Appearance(textArea));
        appearance.setMnemonic(KeyEvent.VK_A);
        settings.setMnemonic(KeyEvent.VK_T);
        settings.add(appearance);

        menuBar.add(menu);
        menuBar.add(searchMenu);
        menuBar.add(settings);
        setJMenuBar(menuBar);
    }

    public static String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    public static void setMargin(JComponent aComponent, int aTop, int aRight, int aBottom, int aLeft) {
        Border border = aComponent.getBorder();

        Border marginBorder = new EmptyBorder(new Insets(aTop, aLeft,
                aBottom, aRight));
        aComponent.setBorder(border == null ? marginBorder
                : new CompoundBorder(marginBorder, border));
    }
}
