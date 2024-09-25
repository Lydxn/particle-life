package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class ParticleTest {
    private Particle p1;
    private Particle p2;

    @BeforeEach
    public void runBefore() {
        Vector pos = new Vector(50, 30);
        Vector vel = new Vector(-2, 4);
        ParticleType type = new ParticleType(Color.RED, 11);
        this.p1 = new Particle(pos, type);
        this.p2 = new Particle(pos, vel, type);
    }

    @Test
    public void testConstructor() {
        assertEquals(this.p1.getVel().getX(), 0);
        assertEquals(this.p1.getVel().getY(), 0);

        assertEquals(this.p2.getPos().getX(), 50);
        assertEquals(this.p2.getPos().getY(), 30);
        assertEquals(this.p2.getVel().getX(), -2);
        assertEquals(this.p2.getVel().getY(), 4);
        assertEquals(this.p2.getType().getColor(), Color.RED);
        assertEquals(this.p2.getType().getID(), 11);
    }

    @Test
    public void testToString() {
        assertEquals(this.p2.toString(), "Particle[(50.0,30.0), (-2.0,4.0), 11]");
    }
}
