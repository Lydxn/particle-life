package persistence;

import model.Event;
import model.EventLog;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

// Refactored from:
//   https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonWriter.java
// Represents a manager class that can read from JSON files
public class JsonReader {
    private String filename;
    private Scanner scanner;

    // EFFECTS: instantiates a JSON reader from a source file
    public JsonReader(String filename) throws IOException {
        this.filename = filename;
        this.scanner = new Scanner(new File(filename));
    }

    // MODIFIES: this
    // EFFECTS: writes a JSON object to the file
    public JSONObject read() throws IOException {
        String source = this.scanner.useDelimiter("\\A").next();
        EventLog.getInstance().logEvent(new Event("Loaded data from " + this.filename));
        return new JSONObject(source);
    }

    // MODIFIES: this
    // EFFECTS: closes the reader
    public void close() throws IOException {
        this.scanner.close();
    }
}
