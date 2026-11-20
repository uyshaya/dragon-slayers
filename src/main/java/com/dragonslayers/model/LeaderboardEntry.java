package com.dragonslayers.model;

/**
 * One line of the leaderboard: a party name, how many times it defeated the dragon,
 * and its best (lowest) number of battle rounds for a win.
 */
public class LeaderboardEntry {

    private static final int FIRST_WIN = 1;

    private final String partyName;

    private int wins;

    private int bestRounds;

    /**
     * Creates an entry with the given values. Used when loading the leaderboard file.
     *
     * @param partyName  the party's name
     * @param wins       how many times the party won
     * @param bestRounds the party's lowest round total for a win
     */
    public LeaderboardEntry(String partyName, int wins, int bestRounds) {
        this.partyName = partyName;
        this.wins = wins;
        this.bestRounds = bestRounds;
    }

    /**
     * Creates the entry for a party's first win.
     *
     * @param partyName the party's name
     * @param rounds    the battle rounds the win took
     */
    public LeaderboardEntry(String partyName, int rounds) {
        this(partyName, FIRST_WIN, rounds);
    }

    /**
     * Records another win: one more win, and the lower of the old and new round totals.
     * Package-private, so only {@link Leaderboard} can change an entry.
     *
     * @param rounds the battle rounds the new win took
     */
    void recordWin(int rounds) {
        this.wins++;
        this.bestRounds = Math.min(bestRounds, rounds);
    }

    /**
     * Returns the party's name.
     *
     * @return the party name
     */
    public String getPartyName() {
        return partyName;
    }

    /**
     * Returns how many times the party defeated the dragon.
     *
     * @return the number of wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns the party's lowest round total for a win.
     *
     * @return the best rounds
     */
    public int getBestRounds() {
        return bestRounds;
    }
}
