package persistence;

import model.Event;
import model.EventLog;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Refactored from:
//   https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonWriter.java
// Represents a manager class that can write to JSON files
public class JsonWriter {
    private String filename;
    private PrintWriter writer;

    // EFFECTS: instantiates a JSON writer from a destination file
    public JsonWriter(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.writer = new PrintWriter(new File(filename));
    }

    // MODIFIES: this
    // EFFECTS: writes a JSON object to the file
    public void write(JSONObject json) {
        this.writer.print(json);
        EventLog.getInstance().logEvent(new Event("Wrote data to " + this.filename));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        this.writer.close();
    }
}
