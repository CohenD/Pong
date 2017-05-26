package pong2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class PongApp extends JFrame {

    PongApp() {
        PongPanel p = new PongPanel();
        JMenuBar menuBar = new JMenuBar();

        JMenuItem beginMenuItemUndo = new JMenuItem("Begin Game");
        menuBar.add(beginMenuItemUndo);
        beginMenuItemUndo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                p.reset();
            }
        });

        JMenuItem autoMenuItemUndo = new JMenuItem("Toggle Auto Game");
        menuBar.add(autoMenuItemUndo);
        autoMenuItemUndo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                p.autoModeToggle();
            }
        });

        add(menuBar, BorderLayout.NORTH);
        add(autoMenuItemUndo, BorderLayout.SOUTH);
        add(p);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
