package ui;

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// ParticleLife GUI application / Game state
public class GameGUI extends JFrame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    private GraphicsPanel gp;
    private OptionPanel op;

    // EFFECTS: Instantiates the Particle Life GUI application
    public GameGUI() {
        super("Particle Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Print event logs after quitting the program
                for (Event event : EventLog.getInstance()) {
                    System.out.println("LOG: " + event.getDescription());
                }
            }
        });

        gp = new GraphicsPanel(GAME_WIDTH, GAME_HEIGHT);
        op = new OptionPanel(gp);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(gp, BorderLayout.EAST);
        panel.add(op, BorderLayout.WEST);

        add(panel);

        pack();
        setVisible(true);

        start();
    }

    // EFFECTS: Runs the game loop
    private void start() {
        new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                revalidate();
                gp.repaint();
                gp.update();
            }
        }).start();
    }
}
