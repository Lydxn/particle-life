package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;

// Represents a particle type having a color and an id. Each particle type has a different set of
// rules governing its attraction with other types of particles (if particles are atoms, this is
// analogous to "elements" on the Periodic Table).
public class ParticleType implements Writable {
    private Color color;
    private int id;

    // EFFECTS: instantiates a particle type with a color and an id
    public ParticleType(Color color, int id) {
        this.color = color;
        this.id = id;
    }

    // EFFECTS: converts a ParticleType to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("color", this.color.getRGB());
        json.put("id", this.id);
        return json;
    }

    // EFFECTS: converts a JSON object to a ParticleType
    public static ParticleType fromJson(JSONObject json) {
        Color color = new Color(json.getInt("color"), true);
        int id = json.getInt("id");
        return new ParticleType(color, id);
    }

    @Override
    public String toString() {
        return "ParticleType[" + this.color + ", " + this.id + "]";
    }

    public Color getColor() {
        return this.color;
    }

    public int getID() {
        return this.id;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setID(int id) {
        this.id = id;
    }
}
