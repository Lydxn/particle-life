package ui;

import model.ParticleType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// ParticleType interface of the GUI application
public class ParticleTypePanel extends JPanel {
    private ParticleType type;
    private JPanel particleInfoPanel;
    private GraphicsPanel gp;

    // EFFECTS: Instantiates a particle type panel given a type
    public ParticleTypePanel(ParticleType type, JPanel particleInfoPanel, GraphicsPanel gp) {
        this.type = type;
        this.particleInfoPanel = particleInfoPanel;
        this.gp = gp;

        add(new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.setColor(type.getColor());
                g.fillOval(0, 0, 10, 10);
            }
        });
        add(new JButton(new DeleteParticleTypeAction()));
    }

    // Action class to delete a particle type
    private class DeleteParticleTypeAction extends AbstractAction {
        // EFFECTS: Instantiates the delete particle type action
        public DeleteParticleTypeAction() {
            super("Delete Particle Type?");
        }

        // MODIFIES: this
        // EFFECTS: deletes the given particle type when the delete particle type button is pressed
        @Override
        public void actionPerformed(ActionEvent ae) {
            particleInfoPanel.remove(type.getID());
            gp.getSimulation().removeParticleType(type.getID());
        }
    }
}
