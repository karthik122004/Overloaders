package ui;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

public class MoveHistoryPanel extends JPanel {
    private ArrayList<String> moveHistory;
    private DefaultListModel<String> listModel;
    private JList<String> moveList;

    /**
     * Constructor - Creates history panel structure
     */
    public MoveHistoryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 600));

        moveHistory = new ArrayList<>();
        listModel = new DefaultListModel<>();
        moveList = new JList<>(listModel);

        JScrollPane scrollPane = new JScrollPane(moveList);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds a move to the history (not connected yet)
     * @param move The move string to add
     */
    public void addMove(String move) {
        moveHistory.add(move);
        listModel.addElement(move);
    }

    /**
     * Clears all move history
     */
    public void clearHistory() {
        moveHistory.clear();
        listModel.clear();
    }
}
