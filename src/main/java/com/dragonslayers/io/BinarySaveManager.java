package com.dragonslayers.io;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.exceptions.InvalidActionException;
import com.dragonslayers.exceptions.SaveNotFoundException;
import com.dragonslayers.model.GameState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Saves the full game state as a binary file with {@link ObjectOutputStream}.
 */
public class BinarySaveManager implements SaveManager {

    private static final String FILE_EXTENSION = ".dat";

    /**
     * Creates a binary save manager.
     */
    public BinarySaveManager() {
    }

    /**
     * Writes the game state to the slot's {@code .dat} file.
     *
     * @param state the game state to save
     * @param slot  the slot number
     * @throws IOException if the file cannot be written
     */
    @Override
    public void save(GameState state, int slot) throws IOException {
        File file = getFile(slot);
        file.getParentFile().mkdirs(); // create saves/ if it doesn't exist
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(state);
        }
    }

    /**
     * Reads the game state from the slot's {@code .dat} file.
     *
     * @param slot the slot number
     * @return the loaded game state
     * @throws SaveNotFoundException  if the slot has no save
     * @throws CorruptedSaveException if the file is damaged or holds the wrong data
     */
    @Override
    public GameState load(int slot) throws SaveNotFoundException, CorruptedSaveException {
        File file = getFile(slot);
        if (!file.exists()) {
            throw new SaveNotFoundException("Slot " + slot + " is empty");
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new CorruptedSaveException("Save in slot " + slot + " is damaged", e);
        }
    }

    /**
     * Returns the save file for a slot.
     *
     * @param slot the slot number
     * @return the file
     * @throws InvalidActionException if the slot number is out of range
     */
    private File getFile(int slot) {
        if (slot < FIRST_SLOT || slot > SLOT_COUNT) {
            throw new InvalidActionException("Slot must be " + FIRST_SLOT + " to " + SLOT_COUNT);
        }
        return new File(SAVE_FOLDER, "slot" + slot + FILE_EXTENSION);
    }
}
