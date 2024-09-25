package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents the attraction rules for the simulation
public class AttractionMatrix implements Writable {
    double[][] attractions;

    private double friction;
    private double beta;
    private double range;

    // EFFECTS: Instantiates an empty attraction matrix, with a defined friction, beta, and range.
    public AttractionMatrix(double friction, double beta, double range) {
        this.attractions = new double[0][0];

        this.friction = friction;
        this.beta = beta;
        this.range = range;
    }

    // MODIFIES: this
    // EFFECTS: Adds a particle type to the end of the attraction matrix.
    public void addType() {
        int n = this.attractions.length + 1;
        double[][] newAttractions = new double[n][n];
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                newAttractions[i][j] = this.attractions[i][j];
            }
        }
        this.attractions = newAttractions;
    }

    // REQUIRES: 0 <= index < getAttractions().size()
    // MODIFIES: this
    // EFFECTS: Removes a particle type at the specified index of the attraction matrix.
    public void removeType(int index) {
        int n = this.attractions.length - 1;
        double[][] newAttractions = new double[n][n];
        for (int i = 0; i < n; i++) {
            int skipRow = i >= index ? 1 : 0;
            for (int j = 0; j < n; j++) {
                int skipCol = j >= index ? 1 : 0;
                newAttractions[i][j] = this.attractions[i + skipRow][j + skipCol];
            }
        }
        this.attractions = newAttractions;
    }

    // EFFECTS: Returns the vector at which particle p acts due to particle q.
    public Vector calcAttractionVector(Particle p, Particle q) {
        Vector delta = q.getPos().sub(p.getPos());
        double dist = delta.mag();
        // dist > 0 avoids / by 0; dist <= this.range skips a particle that exerts a zero force.
        if (dist > 0 && dist <= this.range) {
            double attraction = this.getAttraction(p.getType().getID(), q.getType().getID());
            double force = this.calcForce(dist / this.range, attraction);
            return delta.mul(force / dist);
        } else {
            return new Vector();
        }
    }

    // EFFECTS: converts an AttractionMatrix to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray attractionsArray = new JSONArray();
        for (double[] attraction : this.attractions) {
            attractionsArray.put(new JSONArray(attraction));
        }
        json.put("attractions", attractionsArray);
        json.put("friction", this.friction);
        json.put("beta", this.beta);
        json.put("range", this.range);
        return json;
    }

    // EFFECTS: converts a JSON object to an AttractionMatrix
    public static AttractionMatrix fromJson(JSONObject json) {
        double friction = json.getDouble("friction");
        double beta = json.getDouble("beta");
        double range = json.getDouble("range");
        AttractionMatrix attractionMatrix = new AttractionMatrix(friction, beta, range);
        JSONArray attractionsArray = json.getJSONArray("attractions");
        double[][] attractions = new double[attractionsArray.length()][attractionsArray.length()];
        for (int i = 0; i < attractions.length; i++) {
            JSONArray row = attractionsArray.getJSONArray(i);
            for (int j = 0; j < row.length(); j++) {
                attractions[i][j] = row.getDouble(j);
            }
        }
        attractionMatrix.setAttractions(attractions);
        return attractionMatrix;
    }

    public double getAttraction(int i, int j) {
        return this.attractions[i][j];
    }

    public double[][] getAttractions() {
        return this.attractions;
    }

    public double getFriction() {
        return this.friction;
    }

    public double getBeta() {
        return this.beta;
    }

    public double getRange() {
        return this.range;
    }

    // REQUIRES: Must have some number of rows and columns
    public void setAttractions(double[][] attractions) {
        this.attractions = attractions;
    }

    public void setAttraction(int i, int j, double attraction) {
        this.attractions[i][j] = attraction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setRange(double range) {
        this.range = range;
    }

    // EFFECTS: Calculates the attraction force by a particle with a given attraction value and
    //          that is d units away.
    private double calcForce(double d, double attraction) {
        // Formula taken from https://www.youtube.com/watch?v=scvuli-zcRc
        if (d < this.beta) {
            return d / this.beta - 1;
        } else {
            return attraction * (1 - Math.abs(2 * d - 1 - this.beta) / (1 - this.beta));
        }
    }
}
