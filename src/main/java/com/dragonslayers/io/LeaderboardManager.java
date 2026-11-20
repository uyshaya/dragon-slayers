package com.dragonslayers.io;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.model.Leaderboard;
import com.dragonslayers.model.LeaderboardEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Saves and loads the leaderboard in {@code saves/leaderboard.txt}.
 * <p>The file is separate from the save slots, so wins are kept even when a slot is overwritten.
 * Each line holds one party: {@code name,wins,bestRounds}.</p>
 */
public class LeaderboardManager {

    private static final String FILE_NAME = "leaderboard.txt";

    private static final String SEPARATOR = ",";

    private static final int NAME_PART = 0;

    private static final int WINS_PART = 1;

    private static final int ROUNDS_PART = 2;

    /**
     * Creates a leaderboard manager.
     */
    public LeaderboardManager() {
    }

    /**
     * Writes every party on the leaderboard to the file, replacing the old file.
     *
     * @param leaderboard the leaderboard to save
     * @throws IOException if the file cannot be written
     */
    public void save(Leaderboard leaderboard) throws IOException {
        File file = getFile();
        file.getParentFile().mkdirs(); // create saves/ if it doesn't exist
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (LeaderboardEntry entry : leaderboard.getAllParties()) {
                out.println(entry.getPartyName() + SEPARATOR + entry.getWins() + SEPARATOR + entry.getBestRounds());
            }
        }
    }

    /**
     * Reads the leaderboard from the file. If there is no file yet, nobody has won, so the board is empty.
     *
     * @return the loaded leaderboard
     * @throws CorruptedSaveException if the file cannot be read or a line is damaged
     */
    public Leaderboard load() throws CorruptedSaveException {
        Leaderboard leaderboard = new Leaderboard();
        File file = getFile();
        if (!file.exists()) {
            return leaderboard;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(SEPARATOR);
                int wins = Integer.parseInt(parts[WINS_PART]);
                int bestRounds = Integer.parseInt(parts[ROUNDS_PART]);
                leaderboard.addEntry(new LeaderboardEntry(parts[NAME_PART], wins, bestRounds));
            }
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new CorruptedSaveException("The leaderboard file is damaged", e);
        }
        return leaderboard;
    }

    /**
     * Returns the leaderboard file inside the save folder.
     *
     * @return the file
     */
    private File getFile() {
        return new File(SaveManager.SAVE_FOLDER, FILE_NAME);
    }
}
