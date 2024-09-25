package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Represents a simulation which encapsulates the rules of the virtual world.
public class Simulation implements Writable {
    private double width;
    private double height;

    private List<Particle> particles;
    private List<ParticleType> particleTypes;
    private AttractionMatrix attractionMatrix;

    // REQUIRES: width, height > 0
    // EFFECTS: creates a simulation having the specified width and height, with no particles or
    //          particle types, and an attraction matrix with predefined parameters.
    public Simulation(double width, double height) {
        this.width = width;
        this.height = height;

        init();
    }

    // REQUIRES: numParticles >= 0
    // MODIFIES: this
    // EFFECTS: Randomly fills the simulation with the specified number of particles, three
    //          distinct particle types (RED, BLUE, and GREEN), and the corresponding attraction matrix.
    public void generateRandomWorld(int numParticles) {
        init();

        // Create three particle types and initialize their colors.
        this.addParticleType();
        this.addParticleType();
        this.addParticleType();
        this.particleTypes.get(0).setColor(Color.RED);
        this.particleTypes.get(1).setColor(Color.BLUE);
        this.particleTypes.get(2).setColor(Color.GREEN);

        // Place each of the specified number of particles randomly within the simulation's bounds.
        for (int i = 0; i < numParticles; i++) {
            Vector pos = new Vector(Math.random() * this.width, Math.random() * this.height);
            ParticleType type = this.particleTypes.get((int) (Math.random() * 3));
            this.particles.add(new Particle(pos, type));
        }

        // Define each particle's attraction rules (randomly)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double attraction = Math.random() * 2 - 1;
                this.attractionMatrix.setAttraction(i, j, attraction);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the simulation by one frame, according to each particle's attraction to one
    //          another.
    public void update() {
        List<Particle> newParticles = new ArrayList<>();
        for (Particle p : this.particles) {
            Vector newVel = p.getVel();
            for (Particle q : this.particles) {
                Vector attractionVector = this.attractionMatrix.calcAttractionVector(p, q);
                newVel = newVel.add(attractionVector);
            }
            newVel = newVel.mul(this.attractionMatrix.getFriction());
            newParticles.add(new Particle(p.getPos().add(p.getVel()), newVel, p.getType()));
        }
        this.particles = newParticles;
    }

    // MODIFIES: this
    // EFFECTS: Adds a particle to the simulation.
    public void addParticle(Particle particle) {
        this.particles.add(particle);
        EventLog.getInstance().logEvent(new Event("Added a particle"));
    }

    // MODIFIES: this
    // EFFECTS: Adds a particle type to the simulation (initially has color white), and updates the
    //          corresponding attractionRules table.
    public ParticleType addParticleType() {
        ParticleType type = new ParticleType(Color.WHITE, this.particleTypes.size());
        this.particleTypes.add(type);
        this.attractionMatrix.addType();
        EventLog.getInstance().logEvent(new Event("Added a particle type"));
        return type;
    }

    // REQUIRES: 0 <= index < getParticleTypes().size()
    // MODIFIES: this
    // EFFECTS: Removes a particle type from the simulation, updates the particle ids and attraction matrix.
    public void removeParticleType(int index) {
        // Remove all particles that are associated with the removed type
        ParticleType removedType = this.particleTypes.remove(index);
        for (int i = this.particles.size() - 1; i >= 0; i--) {
            if (this.particles.get(i).getType().getID() == removedType.getID()) {
                this.particles.remove(i);
            }
        }

        this.attractionMatrix.removeType(index);

        // Redistribute particle type ids
        int id = 0;
        for (ParticleType type : this.particleTypes) {
            for (Particle particle : particles) {
                if (particle.getType().getID() == type.getID()) {
                    particle.getType().setID(id);
                }
            }
            type.setID(id++);
        }

        EventLog.getInstance().logEvent(new Event("Removed a particle type"));
    }

    // EFFECTS: converts a Simulation to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("width", this.width);
        json.put("height", this.height);
        JSONArray particlesArray = new JSONArray();
        for (Particle particle : this.particles) {
            particlesArray.put(particle.toJson());
        }
        JSONArray particleTypesArray = new JSONArray();
        for (ParticleType particleType : this.particleTypes) {
            particleTypesArray.put(particleType.toJson());
        }
        json.put("particles", particlesArray);
        json.put("particleTypes", particleTypesArray);
        json.put("attractionMatrix", this.attractionMatrix.toJson());
        return json;
    }

    // EFFECTS: converts a JSON object to a Simulation
    public static Simulation fromJson(JSONObject json) {
        double width = json.getDouble("width");
        double height = json.getDouble("height");
        Simulation simulation = new Simulation(width, height);
        List<Particle> particles = new ArrayList<Particle>();
        for (Object particle : json.getJSONArray("particles")) {
            particles.add(Particle.fromJson((JSONObject) particle));
        }
        List<ParticleType> particleTypes = new ArrayList<ParticleType>();
        for (Object particleType : json.getJSONArray("particleTypes")) {
            particleTypes.add(ParticleType.fromJson((JSONObject) particleType));
        }
        AttractionMatrix attractionMatrix = AttractionMatrix.fromJson(
                json.getJSONObject("attractionMatrix"));
        simulation.setParticles(particles);
        simulation.setParticleTypes(particleTypes);
        simulation.setAttractionMatrix(attractionMatrix);
        return simulation;
    }

    // MODIFIES: this
    // EFFECTS: Initializes the member variables
    private void init() {
        this.particles = new ArrayList<>();
        this.particleTypes = new ArrayList<>();
        this.attractionMatrix = new AttractionMatrix(0.7, 0.4, 50);
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public List<Particle> getParticles() {
        return this.particles;
    }

    public List<ParticleType> getParticleTypes() {
        return this.particleTypes;
    }

    public AttractionMatrix getAttractionMatrix() {
        return this.attractionMatrix;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public void setParticleTypes(List<ParticleType> particleTypes) {
        this.particleTypes = particleTypes;
    }

    public void setAttractionMatrix(AttractionMatrix attractionMatrix) {
        this.attractionMatrix = attractionMatrix;
    }
}
