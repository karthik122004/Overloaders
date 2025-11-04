package ui;

import javax.swing.*;
import java.awt.*;

public class ChessBoardPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(10,10,512,512);
        final int width = 400;
        final int length = 400;
        frame.setSize(width,length);
        JLabel label = new JLabel();
        JPanel panel = new JPanel(){
            public void paintComponent(Graphics g){
                boolean white = true;
                for(int y =0; y<8;y++){
                    for(int x =0; x<8;x++){
                        if(white){
                            g.setColor(Color.WHITE);
                        }
                        else{
                            g.setColor(Color.BLACK);
                        }
                        g.fillRect(x*64, y*64, 64, 64);
                        white = !white;
                    }
                    white = !white;
                }
            }
        };
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }




}
