package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class ParticleTypeTest {
    private ParticleType particleType;

    @BeforeEach
    public void runBefore() {
        this.particleType = new ParticleType(Color.BLUE, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(this.particleType.getColor(), Color.BLUE);
        assertEquals(this.particleType.getID(), 3);
    }

    @Test
    public void testToString() {
        assertEquals(this.particleType.toString(), "ParticleType[" + Color.BLUE + ", 3]");
    }
}
