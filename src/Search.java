/*
 * Copyright (c) 2021. Sachin Verma
 * All rights reserved
 *
 */

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Search {
    private final String regex;
    private final JTextArea textArea;
    private Matcher matcher;
    private int totalOccurrences;

    private List<Object> startIndex = new ArrayList<>();
    private List<Object> length = new ArrayList<>();
    private int index = 0;

    public Search(String regex, JTextArea textArea) {
        this.regex = regex;
        this.textArea = textArea;

        findAllOccurrences();
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    public int getIndex() {
        return index;
    }

    void findAllOccurrences() {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(textArea.getText());

        while (matcher.find()) {
            startIndex.add(matcher.start());
            length.add(matcher.group().length());
            selectString((int) startIndex.get(index), (int) length.get(index));
        }
        setTotalOccurrences();
    }

    void nextOccurrence() {
        index = ++index > startIndex.size() - 1 ? 0 : index;
        if (startIndex.size() > 0) {
            selectString((int) startIndex.get(index), (int) length.get(index));
        }
    }

    void  previousOccurrence() {
        index = --index < 0 ? startIndex.size() - 1 : index;
        if (startIndex.size() > 0) {
            selectString((int) startIndex.get(index), (int) length.get(index));
        }
    }

    void replace(String replacement) {
        textArea.replaceRange(replacement, (int) startIndex.get(index), (int) startIndex.get(index) + (int) length.get(index));
        startIndex = new ArrayList<>();
        length = new ArrayList<>();
        findAllOccurrences();
    }

    void replaceAllOccurrences(String replacement) {
        textArea.setText(matcher.replaceAll(replacement));
        startIndex.clear();
        length.clear();
        setTotalOccurrences();
        textArea.setCaretPosition(0);
    }

    public void setTotalOccurrences() {
        totalOccurrences = startIndex.size() ;
    }

    public void selectString(int iStart, int iLength) {
        textArea.setCaretPosition(iStart + iLength);
        textArea.select(iStart, iStart + iLength);
        textArea.grabFocus();
    }
}