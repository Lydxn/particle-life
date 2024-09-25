package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    private Simulation simulation;

    @BeforeEach
    public void runBefore() {
        this.simulation = new Simulation(800, 600);
    }

    @Test
    public void testConstructor() {
        assertEquals(this.simulation.getWidth(), 800);
        assertEquals(this.simulation.getHeight(), 600);
        assertTrue(this.simulation.getParticles().isEmpty());
        assertTrue(this.simulation.getParticleTypes().isEmpty());
    }

    @Test
    public void generateRandomWorld() {
        this.simulation.generateRandomWorld(100);

        List<ParticleType> particleTypes = this.simulation.getParticleTypes();
        assertEquals(particleTypes.size(), 3);
        assertEquals(particleTypes.get(0).getColor(), Color.RED);
        assertEquals(particleTypes.get(0).getID(), 0);
        assertEquals(particleTypes.get(1).getColor(), Color.BLUE);
        assertEquals(particleTypes.get(1).getID(), 1);
        assertEquals(particleTypes.get(2).getColor(), Color.GREEN);
        assertEquals(particleTypes.get(2).getID(), 2);

        List<Particle> particles = this.simulation.getParticles();
        assertEquals(particles.size(), 100);
        for (Particle p : particles) {
            Vector pos = p.getPos();
            assertTrue(pos.getX() >= 0 && pos.getX() < 800 && pos.getY() >= 0 && pos.getY() < 600);
            Vector vel = p.getVel();
            assertTrue(vel.getX() == 0 && vel.getY() == 0);
            ParticleType type = p.getType();
            assertTrue(particleTypes.contains(type));
        }

        AttractionMatrix attractionMatrix = this.simulation.getAttractionMatrix();
        double[][] attractions = attractionMatrix.getAttractions();
        assertTrue(attractions.length == 3 && attractions[0].length == 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(-1 <= attractions[i][j] && attractions[i][j] <= 1);
            }
        }
    }

    @Test
    public void testUpdateOneParticle() {
        Vector pos1 = new Vector(50, 30);
        Vector vel1 = new Vector(-2, 4);
        ParticleType type1 = new ParticleType(Color.RED, 0);
        Particle p1 = new Particle(pos1, vel1, type1);

        this.simulation.getAttractionMatrix().setFriction(1.0);
        this.simulation.addParticle(p1);
        this.simulation.update();

        // There's only one particle, so it shouldn't move at all.
        Particle res1 = this.simulation.getParticles().get(0);
        assertTrue(res1.getPos().getX() == 48 && res1.getPos().getY() == 34);
        assertTrue(res1.getVel().getX() == -2 && res1.getVel().getY() == 4);
    }

    @Test
    public void testUpdateMultipleParticles() {
        for (int iter = 0; iter < 1000; iter++) {
            this.simulation.generateRandomWorld(3);
            this.simulation.getAttractionMatrix().setFriction(1.0);

            AttractionMatrix attractionMatrix = this.simulation.getAttractionMatrix();
            attractionMatrix.setRange(iter % 10 * 500);
            attractionMatrix.setFriction(iter / 10 % 10 * 0.1);
            attractionMatrix.setBeta(iter / 100 * 0.1);

            Particle p1 = this.simulation.getParticles().get(0);
            Particle p2 = this.simulation.getParticles().get(1);
            Particle p3 = this.simulation.getParticles().get(2);

            this.simulation.update();

            Particle exp1 = this.simulation.getParticles().get(0);
            Particle exp2 = this.simulation.getParticles().get(1);
            Particle exp3 = this.simulation.getParticles().get(2);

            Vector vel1 = p1.getVel()
                    .add(attractionMatrix.calcAttractionVector(p1, p2))
                    .add(attractionMatrix.calcAttractionVector(p1, p3))
                    .mul(attractionMatrix.getFriction());
            Vector vel2 = p2.getVel()
                    .add(attractionMatrix.calcAttractionVector(p2, p1))
                    .add(attractionMatrix.calcAttractionVector(p2, p3))
                    .mul(attractionMatrix.getFriction());
            Vector vel3 = p3.getVel()
                    .add(attractionMatrix.calcAttractionVector(p3, p1))
                    .add(attractionMatrix.calcAttractionVector(p3, p2))
                    .mul(attractionMatrix.getFriction());

            Vector pos1 = p1.getPos().add(vel1);
            Vector pos2 = p2.getPos().add(vel2);
            Vector pos3 = p3.getPos().add(vel3);

            // Check that velocities are updated correctly
            assertTrue(exp1.getVel().getX() == vel1.getX() && exp1.getVel().getY() == vel1.getY());
            assertTrue(exp2.getVel().getX() == vel2.getX() && exp2.getVel().getY() == vel2.getY());
            assertTrue(exp3.getVel().getX() == vel3.getX() && exp3.getVel().getY() == vel3.getY());

            this.simulation.update();
            exp1 = this.simulation.getParticles().get(0);
            exp2 = this.simulation.getParticles().get(1);
            exp3 = this.simulation.getParticles().get(2);

            // Check that positions are updated the following iteration
            assertTrue(exp1.getPos().getX() == pos1.getX() && exp1.getPos().getY() == pos1.getY());
            assertTrue(exp2.getPos().getX() == pos2.getX() && exp2.getPos().getY() == pos2.getY());
            assertTrue(exp3.getPos().getX() == pos3.getX() && exp3.getPos().getY() == pos3.getY());
        }
    }

    @Test
    public void testAddParticle() {
        Vector pos1 = new Vector(50, 30);
        Vector vel1 = new Vector(-2, 4);
        ParticleType type1 = new ParticleType(Color.RED, 0);
        Particle p1 = new Particle(pos1, vel1, type1);

        Vector pos2 = new Vector(40, 25);
        Vector vel2 = new Vector(5, 0);
        ParticleType type2 = new ParticleType(Color.BLUE, 1);
        Particle p2 = new Particle(pos2, vel2, type2);

        this.simulation.addParticle(p1);
        assertEquals(this.simulation.getParticles().size(), 1);
        assertEquals(this.simulation.getParticles().get(0), p1);
        this.simulation.addParticle(p2);
        assertEquals(this.simulation.getParticles().size(), 2);
        assertEquals(this.simulation.getParticles().get(1), p2);
    }

    @Test
    public void testAddParticleType() {
        this.simulation.addParticleType();
        assertEquals(this.simulation.getParticleTypes().size(), 1);
        assertEquals(this.simulation.getParticleTypes().get(0).getID(), 0);
        assertEquals(this.simulation.getAttractionMatrix().getAttractions().length, 1);

        this.simulation.addParticleType();
        assertEquals(this.simulation.getParticleTypes().size(), 2);
        assertEquals(this.simulation.getParticleTypes().get(1).getID(), 1);
        assertEquals(this.simulation.getAttractionMatrix().getAttractions().length, 2);

        this.simulation.addParticleType();
        assertEquals(this.simulation.getParticleTypes().size(), 3);
        assertEquals(this.simulation.getParticleTypes().get(2).getID(), 2);
        assertEquals(this.simulation.getAttractionMatrix().getAttractions().length, 3);
    }

    @Test
    public void testRemoveParticleType() {
        this.simulation.generateRandomWorld(100);

        ParticleType type1 = this.simulation.getParticleTypes().get(0);
        ParticleType type2 = this.simulation.getParticleTypes().get(1);
        ParticleType type3 = this.simulation.getParticleTypes().get(2);

        int countType1 = 0, countType2 = 0, countType3 = 0;
        for (Particle p : this.simulation.getParticles()) {
            if (p.getType() == type1) {
                ++countType1;
            } else if (p.getType() == type2) {
                ++countType2;
            } else {
                ++countType3;
            }
        }

        this.simulation.removeParticleType(1);
        assertEquals(this.simulation.getParticleTypes().size(), 2);
        assertEquals(this.simulation.getParticleTypes().get(0).getColor(), Color.RED);
        assertEquals(this.simulation.getParticleTypes().get(0).getID(), 0);
        assertEquals(this.simulation.getParticleTypes().get(1).getColor(), Color.GREEN);
        assertEquals(this.simulation.getParticleTypes().get(1).getID(), 1);
        assertEquals(this.simulation.getAttractionMatrix().getAttractions().length, 2);
        assertEquals(this.simulation.getParticles().size(), 100 - countType2);

        this.simulation.removeParticleType(0);
        assertEquals(this.simulation.getParticleTypes().size(), 1);
        assertEquals(this.simulation.getParticleTypes().get(0).getColor(), Color.GREEN);
        assertEquals(this.simulation.getParticleTypes().get(0).getID(), 0);
        assertEquals(this.simulation.getAttractionMatrix().getAttractions().length, 1);
        assertEquals(this.simulation.getParticles().size(), 100 - countType2 - countType1);

        this.simulation.removeParticleType(0);
        assertEquals(this.simulation.getParticleTypes().size(), 0);
        assertEquals(this.simulation.getAttractionMatrix().getAttractions().length, 0);
        assertEquals(this.simulation.getParticles().size(), 100 - countType2 - countType1 - countType3);
    }
}
