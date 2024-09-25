package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a particle having a position, velocity, and type that determines its
// attractive properties.
public class Particle implements Writable {
    private Vector pos;
    private Vector vel;
    private ParticleType type;

    // EFFECTS: instantiates a particle with a position vector, velocity vector, and type
    public Particle(Vector pos, Vector vel, ParticleType type) {
        this.pos = pos;
        this.vel = vel;
        this.type = type;
    }

    // EFFECTS: instantiates a particle with an x-y coordinate, no velocity, and a type
    public Particle(Vector pos, ParticleType type) {
        this(pos, new Vector(0, 0), type);
    }

    // EFFECTS: converts a Particle to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("pos", pos.toJson());
        json.put("vel", vel.toJson());
        json.put("type", type.toJson());
        return json;
    }

    // EFFECTS: converts a JSON object to a Particle
    public static Particle fromJson(JSONObject json) {
        Vector pos = Vector.fromJson(json.getJSONObject("pos"));
        Vector vel = Vector.fromJson(json.getJSONObject("vel"));
        ParticleType type = ParticleType.fromJson(json.getJSONObject("type"));
        return new Particle(pos, vel, type);
    }

    @Override
    public String toString() {
        return "Particle[" + this.pos + ", " + this.vel + ", " + this.type.getID() + "]";
    }

    public Vector getPos() {
        return this.pos;
    }

    public Vector getVel() {
        return this.vel;
    }

    public ParticleType getType() {
        return this.type;
    }
}
