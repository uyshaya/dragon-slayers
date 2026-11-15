package com.dragonslayers.io;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.exceptions.SaveNotFoundException;
import com.dragonslayers.model.GameState;

import java.io.IOException;

/**
 * Saves and loads a game state in one of the save slots.
 */
public interface SaveManager {

    /** The first slot number. */
    int FIRST_SLOT = 1;

    /** How many save slots there are. */
    int SLOT_COUNT = 3;

    /** The folder the save files are written to. */
    String SAVE_FOLDER = "saves";

    /**
     * Saves the game state to a slot, replacing any save already there.
     *
     * @param state the game state to save
     * @param slot  the slot number, from {@link #FIRST_SLOT} to {@link #SLOT_COUNT}
     * @throws IOException if the file cannot be written
     */
    void save(GameState state, int slot) throws IOException;

    /**
     * Loads the game state from a slot.
     *
     * @param slot the slot number, from {@link #FIRST_SLOT} to {@link #SLOT_COUNT}
     * @return the loaded game state
     * @throws SaveNotFoundException  if the slot has no save
     * @throws CorruptedSaveException if the save cannot be read
     */
    GameState load(int slot) throws SaveNotFoundException, CorruptedSaveException;
}
