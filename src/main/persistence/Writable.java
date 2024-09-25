package persistence;

import org.json.JSONObject;

// Refactored from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/Writable.java
// Represents an object which can be converted to a JSON object
public interface Writable {
    // EFFECTS: returns the object as a JSON
    public JSONObject toJson();
}
