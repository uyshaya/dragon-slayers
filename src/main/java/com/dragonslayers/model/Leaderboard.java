package com.dragonslayers.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Every party that defeated the dragon, ranked, with the top three shown on the leaderboard screen.
 * <p>Parties are ranked by wins, most first. When wins are equal, the party with fewer rounds ranks higher.
 * Every party is remembered, so a new party's wins keep adding up until it can reach the top three.</p>
 */
public class Leaderboard {

    private static final int SHOWN_PARTIES = 3;

    private static final Comparator<LeaderboardEntry> BY_RANK =
            Comparator.comparingInt(LeaderboardEntry::getWins).reversed()
                    .thenComparingInt(LeaderboardEntry::getBestRounds);

    private final List<LeaderboardEntry> parties = new ArrayList<>();

    /**
     * Creates an empty leaderboard.
     */
    public Leaderboard() {
    }

    /**
     * Records a win for a party. A party already on the board gets one more win;
     * a new party joins with one win. Then the parties are sorted by rank.
     *
     * @param partyName the winning party's name
     * @param rounds    the battle rounds the win took
     */
    public void recordWin(String partyName, int rounds) {
        Optional<LeaderboardEntry> existing = parties.stream()
                .filter(leaderboardEntry -> leaderboardEntry.getPartyName().equals(partyName))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().recordWin(rounds);
        } else {
            parties.add(new LeaderboardEntry(partyName, rounds));
        }
        parties.sort(BY_RANK);
    }
    /**
     * Adds an entry as it is, for example one read from the leaderboard file.
     *
     * @param entry the entry to add
     */
    public void addEntry(LeaderboardEntry entry) {
        parties.add(entry);
        parties.sort(BY_RANK);
    }

    /**
     * Returns the three best parties, best first, for the leaderboard screen.
     *
     * @return up to three entries, as a read-only copy
     */
    public List<LeaderboardEntry> getTopThreeParties() {
        return List.copyOf(parties.subList(0, Math.min(SHOWN_PARTIES, parties.size())));
    }

    /**
     * Returns every party, best first, for saving the leaderboard file.
     *
     * @return all entries, read-only
     */
    public List<LeaderboardEntry> getAllParties() {
        return Collections.unmodifiableList(parties);
    }
}
