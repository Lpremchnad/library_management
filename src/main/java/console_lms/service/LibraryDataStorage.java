package console_lms.service;

import console_lms.models.LibraryData;

import java.io.*;

/**
 * Handles saving and loading the LibraryData via Java Object Serialization.
 */
public class LibraryDataStorage {

    private static final String DATA_FILE = "library_data.dat";

    /**
     * Saves entire library state to disk.
     */
    public void save(LibraryData data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }

    /**
     * Loads library state from disk. Creates a new one if it doesn't exist.
     */
    public LibraryData load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new LibraryData(); // fresh start
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (LibraryData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load data, starting fresh. Reason: " + e.getMessage());
            return new LibraryData();
        }
    }
}
