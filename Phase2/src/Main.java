import ui.ChessFrame; // stub

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessFrame frame = new ChessFrame(); // still needs to be implemented
            frame.setVisible(true);
        });
    }
}

