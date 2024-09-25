package ui;

import model.Particle;
import model.ParticleType;
import model.Simulation;
import model.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// Graphics portion of the GUI application
public class GraphicsPanel extends JPanel {
    private Simulation simulation;

    // EFFECTS: Instantiates the graphics panel with a width and a height
    public GraphicsPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        addMouseListener(new GraphicsListener());

        this.simulation = new Simulation(width, height);
        this.simulation.generateRandomWorld(500);
    }

    // EFFECTS: override paintComponent() which is called every tick
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawParticles(g);
    }

    // EFFECTS: draws the particles on the screen
    public void drawParticles(Graphics g) {
        for (Particle particle : this.simulation.getParticles()) {
            g.setColor(particle.getType().getColor());
            Vector pos = particle.getPos();
            g.fillOval((int) pos.getX() - 2, (int) pos.getY() - 2,4, 4);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the simulation
    public void update() {
        this.simulation.update();
    }

    public Simulation getSimulation() {
        return this.simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    // MouseListener class to add and delete particles (left and right click, respectively)
    private class GraphicsListener extends MouseAdapter {
        // MODIFIES: this
        // EFFECTS: add/delete particles when mouse is pressed
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                // left click -> add particle
                List<ParticleType> particleTypes = simulation.getParticleTypes();
                ParticleType type = particleTypes.get((int) (Math.random() * particleTypes.size()));
                Vector pos = new Vector(e.getX(), e.getY());
                Vector vel = new Vector();
                simulation.addParticle(new Particle(pos, vel, type));
            } else {
                // right click -> remove particle
                List<Particle> particles = simulation.getParticles();
                for (int i = particles.size() - 1; i >= 0; i--) {
                    Vector pos = particles.get(i).getPos();
                    double deltaX = pos.getX() - e.getX();
                    double deltaY = pos.getY() - e.getY();
                    double dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    if (dist <= 30) {
                        particles.remove(i);
                    }
                }
            }
        }
    }
}
