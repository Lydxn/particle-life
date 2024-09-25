package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class AttractionMatrixTest {
    private AttractionMatrix attractionMatrix;

    @BeforeEach
    public void runBefore() {
        this.attractionMatrix = new AttractionMatrix(0.7, 0.4, 50);
        this.attractionMatrix.setAttractions(
                new double[][] {
                        {0.1, 0.5, -0.3},
                        {-1, 1, 0.4},
                        {-0.8, 0, 0.2},
                }
        );
    }

    @Test
    public void testConstructor() {
        assertEquals(this.attractionMatrix.getFriction(), 0.7);
        assertEquals(this.attractionMatrix.getBeta(), 0.4);
        assertEquals(this.attractionMatrix.getRange(), 50);
    }

    @Test
    public void testAddType() {
        this.attractionMatrix.addType();
        double[][] result = {
                {0.1, 0.5, -0.3, 0},
                {-1, 1, 0.4, 0},
                {-0.8, 0, 0.2, 0},
                {0, 0, 0, 0},
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(this.attractionMatrix.getAttraction(i, j), result[i][j]);
            }
        }
    }

    @Test
    public void testRemoveType() {
        this.attractionMatrix.removeType(1);
        double[][] result = {
                {0.1, -0.3},
                {-0.8, 0.2},
        };
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(this.attractionMatrix.getAttraction(i, j), result[i][j]);
            }
        }
    }

    @Test
    public void testRemoveTypeIndexZero() {
        this.attractionMatrix.removeType(0);
        double[][] result = {
                {1, 0.4},
                {0, 0.2},
        };
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(this.attractionMatrix.getAttraction(i, j), result[i][j]);
            }
        }
    }

    @Test
    public void testCalcAttractionVectorOutOfRange() {
        Vector pos1 = new Vector(50, 30);
        Vector vel1 = new Vector(-2, 4);
        ParticleType type1 = new ParticleType(Color.RED, 0);
        Particle p1 = new Particle(pos1, vel1, type1);

        Vector pos2 = new Vector(100, -5);
        Vector vel2 = new Vector(5, 0);
        ParticleType type2 = new ParticleType(Color.BLUE, 1);
        Particle p2 = new Particle(pos2, vel2, type2);

        Vector res = attractionMatrix.calcAttractionVector(p1, p2);
        assertEquals(res.getX(), 0);
        assertEquals(res.getY(), 0);
    }

    public void testCalcAttractionVectorInRange() {
        Vector pos1 = new Vector(50, 30);
        Vector vel1 = new Vector(-2, 4);
        ParticleType type1 = new ParticleType(Color.RED, 0);
        Particle p1 = new Particle(pos1, vel1, type1);

        Vector pos2 = new Vector(40, 25);
        Vector vel2 = new Vector(5, 0);
        ParticleType type2 = new ParticleType(Color.BLUE, 1);
        Particle p2 = new Particle(pos2, vel2, type2);

        // Check that the direction in which the particle will move is correct; it's a lot
        // harder to verify the magnitude because it's defined by the implementation.
        Vector res12 = attractionMatrix.calcAttractionVector(p1, p2);
        assertTrue(res12.getX() < 0 && res12.getY() < 0);
        assertEquals(res12.getY() / res12.getX(), 5.0 / -10.0);

        Vector res21 = attractionMatrix.calcAttractionVector(p2, p1);
        assertTrue(res21.getX() > 0 && res21.getY() > 0);
        assertEquals(res21.getY() / res21.getX(), 5.0 / -10.0);
    }
}
