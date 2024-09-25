package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a vector in 2-dimensional space that can perform mathematical operations
public class Vector implements Writable {
    private double dx;
    private double dy;

    // EFFECTS: instantiates a 2D vector with an x,y coordinate
    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    // EFFECTS: instantiates a zero-length vector
    public Vector() {
        this(0, 0);
    }

    // EFFECTS: returns the point-wise addition of two vectors
    public Vector add(Vector v) {
        return new Vector(this.dx + v.dx, this.dy + v.dy);
    }

    // EFFECTS: returns the point-wise subtraction of two vectors
    public Vector sub(Vector v) {
        return new Vector(this.dx - v.dx, this.dy - v.dy);
    }

    // EFFECTS: returns the product of a vector by a constant
    public Vector mul(double mult) {
        return new Vector(this.dx * mult, this.dy * mult);
    }

    // EFFECTS: returns the magnitude (length) of a vector
    public double mag() {
        return Math.sqrt(this.dx * this.dx + this.dy * this.dy);
    }

    // EFFECTS: converts a Vector to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("dx", this.dx);
        json.put("dy", this.dy);
        return json;
    }

    // EFFECTS: converts a JSON object to a Vector
    public static Vector fromJson(JSONObject json) {
        double dx = json.getDouble("dx");
        double dy = json.getDouble("dy");
        return new Vector(dx, dy);
    }

    @Override
    public String toString() {
        return "(" + this.dx + "," + this.dy + ")";
    }

    public double getX() {
        return this.dx;
    }

    public double getY() {
        return this.dy;
    }
}
