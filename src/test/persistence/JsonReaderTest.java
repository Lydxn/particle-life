package persistence;

import model.AttractionMatrix;
import model.Particle;
import model.ParticleType;
import model.Simulation;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Refactored from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonReaderTest.java
public class JsonReaderTest {
    @Test
    public void testFileNotFound() {
        try {
            JsonReader jsonReader = new JsonReader("./data/noSuchFile.json");
            fail("Should throw IOException");
        } catch (IOException e) {
            // fallthrough
        }
    }

    @Test
    public void testEmptySimulation() {
        try {
            JsonReader jsonReader = new JsonReader("./data/emptyExample.json");
            Simulation simulation = Simulation.fromJson(jsonReader.read());
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
            JsonReader jsonReader = new JsonReader("./data/typicalExample.json");
            Simulation simulation = Simulation.fromJson(jsonReader.read());
            jsonReader.close();
            assertEquals(simulation.getWidth(), 1280);
            assertEquals(simulation.getHeight(), 720);
            List<Particle> particles = simulation.getParticles();
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
            List<ParticleType> particleTypes = simulation.getParticleTypes();
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
