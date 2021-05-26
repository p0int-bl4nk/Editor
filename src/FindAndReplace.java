import javax.swing.*;
import java.util.*;

public class FindAndReplace extends JFrame {
    private JTextField searchTextField;
    private JTextField replaceTextField;
    private JButton findButton;
    private JButton replaceButton;
    private JButton replaceAllButton;
    private JLabel numberOfOccurrences;
    private JPanel contentPane;

    Search find;
    Deque<String> stack = new ArrayDeque<>();

    public FindAndReplace(JTextArea textArea) {
        super("Find and Replace");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        findButton.addActionListener(actionEvent -> {
            if (stack.size() > 0 && stack.peekLast().equals(searchTextField.getText())) {
                find.nextOccurrence();
            } else {
                if (stack.size() > 0) {
                    stack.pollLast();
                }
                stack.offerLast(searchTextField.getText());
                find = new Search(searchTextField.getText(), textArea);
                updateNumberOfOccurrences();
            }
        });
        replaceButton.addActionListener(actionEvent -> {
            find.replace(replaceTextField.getText());
            updateNumberOfOccurrences();
        });

        replaceAllButton.addActionListener(actionEvent -> {
            find.replaceAllOccurrences(replaceTextField.getText());
            updateNumberOfOccurrences();
        });
    }

    void updateNumberOfOccurrences() {
        int index = find.getIndex() + 1;
        int total = find.getTotalOccurrences();
        numberOfOccurrences.setText("Number of occurrences: " + index + "/" + total);
    }

}
