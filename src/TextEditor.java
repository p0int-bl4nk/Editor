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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

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

    private final File configFile = new File(".config");
    private int fontSize;
    private String fontFamily;
    private Color fontColor;
    private Color backgroundColor;
    private int width;
    private int height;
    private int x;
    private int y;


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
        getUserConfig();
        setBounds(x, y, width, height);

        initComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                writeUserConfig();
            }
        });
        setVisible(true);
    }

    private void writeUserConfig() {
        try (PrintWriter writer = new PrintWriter(configFile)) {
            writer.println("Size " + textArea.getFont().getSize());
            writer.println("Family " + textArea.getFont().getFamily());
            writer.println("Color " + textArea.getForeground().getRed() + " " + textArea.getForeground().getGreen()
                    + " " + textArea.getForeground().getBlue());
            writer.println("BackgroundColor " + textArea.getBackground().getRed() + " " +
                    textArea.getBackground().getGreen() + " " + textArea.getBackground().getBlue());
            writer.println("Width " + this.getWidth());
            writer.println("Height " + this.getHeight());
            writer.println("X " + this.getX());
            writer.println("Y " + this.getY());
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Error writing user configuration file!");
            fileNotFoundException.printStackTrace();
        }
    }

    private void getUserConfig() {
        try (Scanner scanner = new Scanner(configFile)) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String setting = input.substring(0, input.indexOf(" ") + 1);
                String fromFile = input.replace(setting, "");
                //System.out.println(fromFile + " " + setting);
                int r;
                int g;
                int b;
                switch (setting.trim()) {
                    case "Size":
                        fontSize = Integer.parseInt(fromFile);
                        break;
                    case "Family":
                        fontFamily = fromFile;
                        break;
                    case "Color":
                        r = Integer.parseInt(fromFile.split(" ")[0]);
                        g = Integer.parseInt(fromFile.split(" ")[1]);
                        b = Integer.parseInt(fromFile.split(" ")[2]);
                        fontColor = new Color(r, g, b);
                        break;
                    case "BackgroundColor":
                        r = Integer.parseInt(fromFile.split(" ")[0]);
                        g = Integer.parseInt(fromFile.split(" ")[1]);
                        b = Integer.parseInt(fromFile.split(" ")[2]);
                        backgroundColor = new Color(r, g, b);
                        break;
                    case "Width":
                        width = Integer.parseInt(fromFile);
                    case "Height":
                        height = Integer.parseInt(fromFile);
                        break;
                    case "X":
                        x = Integer.parseInt(fromFile);
                        break;
                    case "Y":
                        y = Integer.parseInt(fromFile);
                        break;
                    default:
                        System.out.println("IllegalChoice!");
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            fontSize = 18;
            fontColor = Color.BLACK;
            fontFamily = "Serif";
            backgroundColor = Color.white;
            width = 1000;
            height = 500;
            x = 460;
            y = 290;
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setMargin(textArea, 20, 10, 20, 10);
        textArea.setName("TextArea");
        textArea.setFont(new Font(fontFamily, Font.PLAIN, fontSize));
        textArea.setForeground(fontColor);
        textArea.setBackground(backgroundColor);

        scrollPane.setName("ScrollPane");

        add(scrollPane, BorderLayout.CENTER);

        initMenuBar();
    }

    private void initMenuBar() {
        open.addActionListener(openFileChooser);
        open.setMnemonic(KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        open.setName("MenuOpen");

        save.addActionListener(writeToFile);
        save.setMnemonic(KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        save.setName("MenuSave");

        exit.addActionListener(actionEvent -> {
            writeUserConfig();
            System.exit(0);
        });
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
