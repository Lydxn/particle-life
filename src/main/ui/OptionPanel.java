package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

// Options menu of the GUI application
public class OptionPanel extends JPanel {
    private GraphicsPanel gp;
    private JPanel particleInfoPanel;
    private JSlider frictionSlider;
    private JSlider betaSlider;
    private JSlider rangeSlider;

    // EFFECTS: Instantiates the options panel with buttons
    public OptionPanel(GraphicsPanel gp) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.gp = gp;

        particleInfoPanel = new JPanel();
        particleInfoPanel.setLayout(new BoxLayout(particleInfoPanel, BoxLayout.Y_AXIS));

        resetOptionPanel();
    }

    // MODIFIES: this
    // EFFECTS: resets all the options in the panel according to the simulation
    private void resetOptionPanel() {
        removeAll();

        resetParticleInfoPanel();

        add(new JButton(new ResetAction()));
        add(new JButton(new AddParticleTypeAction()));
        add(new JButton(new SaveSimulationAction()));
        add(new JButton(new LoadSimulationAction()));

        AttractionMatrix attractionMatrix = gp.getSimulation().getAttractionMatrix();
        frictionSlider = new JSlider(0, 100, (int) (attractionMatrix.getFriction() * 100));
        frictionSlider.addChangeListener(new FrictionListener());
        add(frictionSlider);

        betaSlider = new JSlider(0, 100, (int) (attractionMatrix.getBeta() * 100));
        betaSlider.addChangeListener(new BetaListener());
        add(betaSlider);

        rangeSlider = new JSlider(0, 100, (int) attractionMatrix.getRange());
        rangeSlider.addChangeListener(new RangeListener());
        add(rangeSlider);

        add(particleInfoPanel);
    }

    // MODIFIES: this
    // EFFECTS: resets all the particle info (a.k.a. ParticleTypePanel)
    private void resetParticleInfoPanel() {
        particleInfoPanel.removeAll();
        for (ParticleType type : this.gp.getSimulation().getParticleTypes()) {
            particleInfoPanel.add(new ParticleTypePanel(type, particleInfoPanel, gp));
        }
    }

    // Action class that implements the reset simulation behaviour
    private class ResetAction extends AbstractAction {
        // EFFECTS: Instantiates the reset simulation action
        public ResetAction() {
            super("Reset Simulation");
        }

        // MODIFIES: this
        // EFFECTS: resets the simulation with 500 random particles when reset button is pressed
        @Override
        public void actionPerformed(ActionEvent ae) {
            Simulation simulation = gp.getSimulation();
            simulation.generateRandomWorld(500);
            resetParticleInfoPanel();
        }
    }

    // Action class that implements the add particle type behaviour
    private class AddParticleTypeAction extends AbstractAction {
        // EFFECTS: Instantiates the add particle type action
        public AddParticleTypeAction() {
            super("Add Particle Type");
        }

        // MODIFIES: this
        // EFFECTS: adds a particle type when the add particle type button is pressed
        @Override
        public void actionPerformed(ActionEvent ae) {
            Simulation simulation = gp.getSimulation();
            ParticleType type = simulation.addParticleType();
            type.setColor(new Color((int) (Math.random() * 0x1000000)));

            int numTypes = simulation.getParticleTypes().size();
            for (int i = 0; i < numTypes; i++) {
                double attraction1 = Math.random() * 2 - 1;
                double attraction2 = Math.random() * 2 - 1;
                simulation.getAttractionMatrix().setAttraction(i, numTypes - 1, attraction1);
                simulation.getAttractionMatrix().setAttraction(numTypes - 1, i, attraction2);
            }

            for (int i = 0; i < 100; i++) {
                Vector pos = new Vector(
                        Math.random() * simulation.getWidth(),
                        Math.random() * simulation.getHeight()
                );
                Vector vel = new Vector();
                simulation.addParticle(new Particle(pos, vel, type));
            }

            particleInfoPanel.add(new ParticleTypePanel(type, particleInfoPanel, gp));
        }
    }

    // Action class that implements the save simulation behaviour
    private class SaveSimulationAction extends AbstractAction {
        // EFFECTS: Instantiates the save simulation action
        public SaveSimulationAction() {
            super("Save Simulation");
        }

        // MODIFIES: this
        // EFFECTS: saves the current simulation to a file when the save simulation button is pressed
        @Override
        public void actionPerformed(ActionEvent ae) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    JsonWriter writer = new JsonWriter(filename);
                    writer.write(gp.getSimulation().toJson());
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error: Unable to save simulation to " + filename);
                }
            }
        }
    }

    // Action class that implements the load simulation behaviour
    private class LoadSimulationAction extends AbstractAction {
        // EFFECTS: Instantiates the load simulation action
        public LoadSimulationAction() {
            super("Load Simulation");
        }

        // MODIFIES: this
        // EFFECTS: loads the current simulation from a file when the load simulation button is pressed
        @Override
        public void actionPerformed(ActionEvent ae) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    JsonReader reader = new JsonReader(filename);
                    gp.setSimulation(Simulation.fromJson(reader.read()));
                    resetOptionPanel();
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error: Unable to load simulation from " + filename);
                }
            }
        }
    }

    // Action class that implements modifying the simulation friction parameter
    private class FrictionListener implements ChangeListener {
        // MODIFIES: this
        // EFFECTS: changes the simulation friction parameter when the slider is adjusted
        @Override
        public void stateChanged(ChangeEvent e) {
            double friction = frictionSlider.getValue() / 100.0;
            gp.getSimulation().getAttractionMatrix().setFriction(friction);
        }
    }

    // Action class that implements modifying the simulation beta parameter
    private class BetaListener implements ChangeListener {
        // MODIFIES: this
        // EFFECTS: changes the simulation beta parameter when the slider is adjusted
        @Override
        public void stateChanged(ChangeEvent e) {
            double beta = betaSlider.getValue() / 100.0;
            gp.getSimulation().getAttractionMatrix().setBeta(beta);
        }
    }

    // Action class that implements modifying the simulation range parameter
    private class RangeListener implements ChangeListener {
        // MODIFIES: this
        // EFFECTS: changes the simulation range parameter when the slider is adjusted
        @Override
        public void stateChanged(ChangeEvent e) {
            double range = rangeSlider.getValue();
            gp.getSimulation().getAttractionMatrix().setRange(range);
        }
    }
}
