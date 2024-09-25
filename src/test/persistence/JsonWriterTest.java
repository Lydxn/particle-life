package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Refactored from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonWriterTest.java
public class JsonWriterTest {
    @Test
    public void testFileNotFound() {
        try {
            JsonWriter jsonWriter = new JsonWriter("./data/null\0byte");
            fail("Should throw IOException");
        } catch (IOException e) {
            // fallthrough
        }
    }

    @Test
    public void testEmptySimulation() {
        try {
            Simulation simulation = new Simulation(800, 600);
            JsonWriter jsonWriter = new JsonWriter("./data/emptyExample.json");
            jsonWriter.write(simulation.toJson());
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/emptyExample.json");
            simulation = Simulation.fromJson(jsonReader.read());
            jsonReader.close();
            assertEquals(simulation.getWidth(), 800);
            assertEquals(simulation.getHeight(), 600);
            assertEquals(simulation.getParticles().size(), 0);
            assertEquals(simulation.getParticleTypes().size(), 0);
            AttractionMatrix attractionMatrix = simulation.getAttractionMatrix();
            assertEquals(attractionMatrix.getAttractions().length, 0);
            assertEquals(attractionMatrix.getFriction(), 0.7);
            assertEquals(attractionMatrix.getBeta(), 0.4);
            assertEquals(attractionMatrix.getRange(), 50);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testTypicalSimulation() {
        try {
            Simulation simulation = new Simulation(1280, 720);
            simulation.getAttractionMatrix().setFriction(0.9);
            simulation.getAttractionMatrix().setBeta(0.3);
            simulation.getAttractionMatrix().setRange(100);
            simulation.addParticleType();
            simulation.addParticleType();
            simulation.addParticleType();
            ParticleType pt1 = simulation.getParticleTypes().get(0);
            ParticleType pt2 = simulation.getParticleTypes().get(1);
            ParticleType pt3 = simulation.getParticleTypes().get(2);
            pt1.setColor(new Color(4141, true));
            pt2.setColor(new Color(0, true));
            pt3.setColor(new Color(-1, true));
            simulation.addParticle(new Particle(new Vector(100, 200), new Vector(1, 2), pt1));
            simulation.addParticle(new Particle(new Vector(0, 350), new Vector(-4, 3), pt3));

            JsonWriter jsonWriter = new JsonWriter("./data/typicalExample.json");
            jsonWriter.write(simulation.toJson());
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/typicalExample.json");
            simulation = Simulation.fromJson(jsonReader.read());
            jsonReader.close();
            assertEquals(simulation.getWidth(), 1280);
            assertEquals(simulation.getHeight(), 720);
            java.util.List<Particle> particles = simulation.getParticles();
            assertEquals(particles.size(), 2);
            assertEquals(particles.get(0).getPos().getX(), 100);
            assertEquals(particles.get(0).getPos().getY(), 200);
            assertEquals(particles.get(0).getVel().getX(), 1);
            assertEquals(particles.get(0).getVel().getY(), 2);
            assertEquals(particles.get(0).getType().getID(), 0);
            assertEquals(particles.get(1).getPos().getX(), 0);
            assertEquals(particles.get(1).getPos().getY(), 350);
            assertEquals(particles.get(1).getVel().getX(), -4);
            assertEquals(particles.get(1).getVel().getY(), 3);
            assertEquals(particles.get(1).getType().getID(), 2);
            java.util.List<ParticleType> particleTypes = simulation.getParticleTypes();
            assertEquals(particleTypes.size(), 3);
            assertEquals(particleTypes.get(0).getColor().getRGB(), 4141);
            assertEquals(particleTypes.get(0).getID(), 0);
            assertEquals(particleTypes.get(1).getColor().getRGB(), 0);
            assertEquals(particleTypes.get(1).getID(), 1);
            assertEquals(particleTypes.get(2).getColor().getRGB(), -1);
            assertEquals(particleTypes.get(2).getID(), 2);
            AttractionMatrix attractionMatrix = simulation.getAttractionMatrix();
            assertEquals(attractionMatrix.getAttractions().length, 3);
            assertEquals(attractionMatrix.getFriction(), 0.9);
            assertEquals(attractionMatrix.getBeta(), 0.3);
            assertEquals(attractionMatrix.getRange(), 100);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}
