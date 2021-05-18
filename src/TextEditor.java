package editor;

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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    final File[] file = {null};
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);

    JMenuItem open = new JMenuItem("Open");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem exit = new JMenuItem("Exit");
    JMenu menu = new JMenu("File");

    JMenuItem startSearch = new JMenuItem("Start search");
    JMenuItem previousMatch = new JMenuItem("Previous match");
    JMenuItem nextMatch = new JMenuItem("Next match");
    JMenuItem useRegex = new JMenuItem("Use regex");
    JMenu searchMenu = new JMenu("Search");

    JMenuBar menuBar = new JMenuBar();

    ImageIcon openImage = new ImageIcon("/home/stellarloony/Pictures/openIcon.png", "Open");
    ImageIcon saveImage = new ImageIcon("/home/stellarloony/Pictures/saveIcon.png", "Save");
    ImageIcon searchIcon = new ImageIcon("/home/stellarloony/Pictures/searchIcon.png", "Search");
    ImageIcon nextIcon = new ImageIcon("/home/stellarloony/Pictures/nextIcon.png", "Next");
    ImageIcon prevIcon = new ImageIcon("/home/stellarloony/Pictures/prevIcon.png", "Previous");

    JButton saveButton = new JButton(saveImage);
    JButton openButton = new JButton(openImage);
    JTextField searchString = new JTextField();
    JCheckBox checkBox = new JCheckBox("Use regex");
    JButton searchButton = new JButton(searchIcon);
    JButton nextButton = new JButton(nextIcon);
    JButton prevButton = new JButton(prevIcon);

    JToolBar toolBar = new JToolBar();


    Map<Integer, Integer> foundText;
    Object[] startIndex;
    Object[] length;
    int index;

    Action openFileChooser = new AbstractAction() {
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

    Action writeToFile = new AbstractAction() {
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

    Action search = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            foundText = new LinkedHashMap<>();
            index = 0;
            Thread t = new Thread(() -> new Search(searchString.getText(), textArea.getText(),
                    checkBox.isSelected(), foundText).doSearch());
            t.start();
            try {
                t.join();
                startIndex = foundText.keySet().toArray();
                length =  foundText.values().toArray();
                selectString((int) startIndex[index], (int) length[index]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Action next = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            index = ++index > startIndex.length - 1 ? 0 : index;
            selectString((int) startIndex[index], (int) length[index]);
        }
    };

    Action previous = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            index = --index < 0 ? startIndex.length - 1 : index;
            selectString((int) startIndex[index], (int) length[index]);
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
        Font font = new Font("Courier", Font.PLAIN, 16);
        textArea.setFont(font);

        setMargin(scrollPane, 10, 0, 10, 0);
        scrollPane.setName("ScrollPane");

        add(scrollPane, BorderLayout.CENTER);

        initToolBar();
        initMenuBar();
    }

    void initMenuBar() {
        open.addActionListener(openFileChooser);
        open.setMnemonic(KeyEvent.VK_O);
        open.setName("MenuOpen");

        save.addActionListener(writeToFile);
        save.setMnemonic(KeyEvent.VK_S);
        save.setName("MenuSave");

        exit.addActionListener(actionEvent -> System.exit(0));
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setName("MenuExit");

        menu.setMnemonic(KeyEvent.VK_F);
        menu.setName("MenuFile");
        menu.add(open);
        menu.add(save);
        menu.addSeparator();
        menu.add(exit);

        startSearch.setMnemonic(KeyEvent.VK_S);
        startSearch.addActionListener(search);
        startSearch.setName("MenuStartSearch");

        previousMatch.setMnemonic(KeyEvent.VK_P);
        previousMatch.addActionListener(previous);
        previousMatch.setName("MenuPreviousMatch");

        nextMatch.setMnemonic(KeyEvent.VK_N);
        nextMatch.addActionListener(next);
        nextMatch.setName("MenuNextMatch");

        useRegex.setMnemonic(KeyEvent.VK_R);
        useRegex.addActionListener(actionEvent -> checkBox.setSelected(true));
        useRegex.setName("MenuUseRegExp");

        searchMenu.setMnemonic(KeyEvent.VK_S);
        searchMenu.setName("MenuSearch");
        searchMenu.add(startSearch);
        searchMenu.add(previousMatch);
        searchMenu.add(nextMatch);
        searchMenu.add(useRegex);

        menuBar.add(menu);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);
    }

    void initToolBar() {

        saveButton.addActionListener(writeToFile);
        saveButton.setToolTipText("Save");
        saveButton.setName("SaveButton");

        openButton.addActionListener(openFileChooser);
        openButton.setToolTipText("Open");
        openButton.setName("OpenButton");

        searchString.setToolTipText("Search field");
        searchString.setName("SearchField");

        checkBox.setName("UseRegExCheckbox");

        searchButton.addActionListener(search);
        searchButton.setToolTipText("Search");
        searchButton.setName("StartSearchButton");

        nextButton.addActionListener(next);
        nextButton.setToolTipText("Next match");
        nextButton.setName("NextMatchButton");

        prevButton.addActionListener(previous);
        prevButton.setToolTipText("Previous Match");
        prevButton.setName("PreviousMatchButton");

        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.addSeparator();
        toolBar.add(searchString);
        toolBar.add(searchButton);
        toolBar.add(prevButton);
        toolBar.add(nextButton);
        toolBar.add(checkBox);

        add(toolBar, BorderLayout.NORTH);
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

    public void selectString(int iStart, int iLength) {
        System.out.println(iStart + " -> " + iLength);
        textArea.setCaretPosition(iStart + iLength);
        textArea.select(iStart, iStart + iLength);
        textArea.grabFocus();
    }

}

class Search {
    String regex;
    String fileText;
    boolean useRegex;
    Map<Integer, Integer> foundText;

    public Search(String regex, String fileText, boolean useRegex, Map<Integer, Integer> foundText) {
        this.regex = regex;
        this.fileText = fileText;
        this.useRegex = useRegex;
        this.foundText = foundText;
    }

    void doSearch() {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileText);

        while (matcher.find()) {
            foundText.put(matcher.start(), matcher.group().length());
        }
    }
}
