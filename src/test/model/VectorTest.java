package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
    private Vector v0;
    private Vector v1;
    private Vector v2;

    @BeforeEach
    public void runBefore() {
        this.v0 = new Vector();
        this.v1 = new Vector(3, 4);
        this.v2 = new Vector(-4, 7);
    }

    @Test
    public void testConstructor() {
        assertEquals(this.v0.getX(), 0);
        assertEquals(this.v0.getY(), 0);
        assertEquals(this.v1.getX(), 3);
        assertEquals(this.v1.getY(), 4);
        assertEquals(this.v2.getX(), -4);
        assertEquals(this.v2.getY(), 7);
    }

    @Test
    public void testAdd() {
        assertEquals(this.v0.add(this.v1).getX(), 3);
        assertEquals(this.v0.add(this.v1).getY(), 4);
        assertEquals(this.v1.add(this.v2).getX(), -1);
        assertEquals(this.v1.add(this.v2).getY(), 11);
        assertEquals(this.v2.add(this.v0).getX(), -4);
        assertEquals(this.v2.add(this.v0).getY(), 7);
    }

    @Test
    public void testSub() {
        assertEquals(this.v0.sub(this.v1).getX(), -3);
        assertEquals(this.v0.sub(this.v1).getY(), -4);
        assertEquals(this.v1.sub(this.v2).getX(), 7);
        assertEquals(this.v1.sub(this.v2).getY(), -3);
        assertEquals(this.v2.sub(this.v0).getX(), -4);
        assertEquals(this.v2.sub(this.v0).getY(), 7);
    }

    @Test
    public void testMul() {
        assertEquals(this.v0.mul(2).getX(), 0);
        assertEquals(this.v0.mul(2).getY(), 0);
        assertEquals(this.v1.mul(-4).getX(), -12);
        assertEquals(this.v1.mul(-4).getY(), -16);
        assertEquals(this.v2.mul(3).getX(), -12);
        assertEquals(this.v2.mul(3).getY(), 21);
    }

    @Test
    public void testMag() {
        assertEquals(this.v0.mag(), 0);
        assertEquals(this.v1.mag(), 5);
        assertEquals(this.v2.mag(), Math.sqrt((-4) * (-4) + 7 * 7));
    }
}
