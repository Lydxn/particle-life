package ui;

import model.Particle;
import model.ParticleType;
import model.Simulation;
import model.Vector;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// ParticleLife terminal application / Game state
public class GameTerminal {
    private static final String JSON_STORE = "./data/simulation.json";
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    private Simulation simulation;
    private Scanner scanner;

    // EFFECTS: runs the simulation
    public GameTerminal() {
        this.simulation = new Simulation(GAME_WIDTH, GAME_HEIGHT);
        runCommandLineGUI();
    }

    // MODIFIES: this
    // EFFECTS: runs the command line GUI application
    @SuppressWarnings("methodlength")
    private void runCommandLineGUI() {
        this.scanner = new Scanner(System.in);

        printWelcome();

        while (true) {
            printMenu();

            int choice = Integer.parseInt(this.scanner.nextLine());
            switch (choice) {
                case 1:
                    addParticleGUI();
                    break;
                case 2:
                    addParticleTypeGUI();
                    break;
                case 3:
                    removeParticleTypeGUI();
                    break;
                case 4:
                    viewEverythingGUI();
                    break;
                case 5:
                    generateRandomWorldGUI();
                    break;
                case 6:
                    updateParticlesGUI();
                    break;
                case 7:
                    modifyConstantsGUI();
                    break;
                case 8:
                    saveFileGUI();
                    break;
                case 9:
                    loadFileGUI();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Error: Invalid option!");
                    break;
            }
            System.out.println();
        }
    }

    // EFFECTS: Prints the welcome message
    private void printWelcome() {
        System.out.println("Welcome to Particle Life Command Line GUI!");
    }

    // EFFECTS: Prints the menu
    private void printMenu() {
        System.out.println("-------- Menu --------");
        System.out.println("1. Add Particle");
        System.out.println("2. Add Particle Type");
        System.out.println("3. Remove Particle Type");
        System.out.println("4. View Everything");
        System.out.println("5. Generate Random World");
        System.out.println("6. Update Particles");
        System.out.println("7. Modify Constants");
        System.out.println("8. Save Simulation");
        System.out.println("9. Load Simulation");
        System.out.println("0. Exit");
        System.out.print("Choose a number between 0 and 9: ");
    }

    // MODIFIES: this
    // EFFECTS: Runs the Add Particle menu option
    private void addParticleGUI() {
        if (this.simulation.getParticleTypes().isEmpty()) {
            System.out.println("Error: You must add a particle type first!");
            return;
        }

        List<ParticleType> particleTypes = this.simulation.getParticleTypes();

        Vector pos = new Vector(Math.random() * GAME_WIDTH, Math.random() * GAME_HEIGHT);
        Vector vel = new Vector();
        ParticleType type = particleTypes.get((int) (Math.random() * particleTypes.size()));
        Particle particle = new Particle(pos, vel, type);

        this.simulation.addParticle(particle);
        System.out.println("Success: Particle added!");
    }

    // MODIFIES: this
    // EFFECTS: Runs the Add Particle Type menu option
    private void addParticleTypeGUI() {
        this.simulation.addParticleType();
        System.out.println("Success: Particle type added!");
    }

    // MODIFIES: this
    // EFFECTS: Runs the Remove Particle Type menu option
    private void removeParticleTypeGUI() {
        int size = this.simulation.getParticleTypes().size();
        System.out.print("Which particle type to delete (0-" + (size - 1) + ")? ");
        int index = Integer.parseInt(this.scanner.nextLine());
        if (!(0 <= index && index < size)) {
            System.out.println("Error: index out of range!");
            return;
        }

        this.simulation.removeParticleType(index);
        System.out.println("Success: Particle type removed!");
    }

    // EFFECTS: Runs the View Everything menu option
    private void viewEverythingGUI() {
        System.out.println("Particle Types: " + this.simulation.getParticleTypes());
        System.out.println("Particles: " + this.simulation.getParticles());
    }

    // MODIFIES: this
    // EFFECTS: Runs the Generate Random World menu option
    private void generateRandomWorldGUI() {
        System.out.print("How many particles would you like to add? ");
        int numParticles = Integer.parseInt(this.scanner.nextLine());

        this.simulation.generateRandomWorld(numParticles);
        System.out.println("Success: Added " + numParticles + " particle(s)!");
    }

    // MODIFIES: this
    // EFFECTS: Runs the Update Particles menu option
    private void updateParticlesGUI() {
        this.simulation.update();
        System.out.println("Success: Updated particles by one frame.");
    }

    // MODIFIES: this
    // EFFECTS: Runs the Modify Constants menu option
    private void modifyConstantsGUI() {
        System.out.print("Which constant would you like to modify ([f]riction/[b]eta/[r]ange)? ");
        String choice = this.scanner.nextLine();
        if (!(choice.equals("f") || choice.equals("b") || choice.equals("r"))) {
            System.out.println("Error: Invalid option!");
            return;
        }

        System.out.print("What is it's new value? ");
        double newValue = Double.parseDouble(this.scanner.nextLine());

        if (choice.equals("f")) {
            this.simulation.getAttractionMatrix().setFriction(newValue);
        } else if (choice.equals("b")) {
            this.simulation.getAttractionMatrix().setBeta(newValue);
        } else {
            this.simulation.getAttractionMatrix().setRange(newValue);
        }
        System.out.println("Success: Modified constant");
    }

    // MODIFIES: this
    // EFFECTS: Saves the current simulation state to the local storage
    private void saveFileGUI() {
        try {
            JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
            jsonWriter.write(this.simulation.toJson());
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to write to file " + JSON_STORE);
        }
        System.out.println("Success: Simulation saved to " + JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: Loads the current simulation state from the local storage
    private void loadFileGUI() {
        try {
            JsonReader jsonReader = new JsonReader(JSON_STORE);
            this.simulation = Simulation.fromJson(jsonReader.read());
            jsonReader.close();
        } catch (IOException e) {
            System.out.println("Error: Unable to read from file " + JSON_STORE);
        }
        System.out.println("Success: Simulation loaded from " + JSON_STORE);
    }
}
