package com.dragonslayers.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A group of characters that fight together.
 * <p>Used as {@code Party<Hero>} for the player and {@code Party<Enemy>} for the enemies.</p>
 *
 * @param <T> the type of character in this party
 */
public class Party<T extends GameCharacter> {

    private final List<T> members = new ArrayList<>();

    /**
     * Adds a character to this party.
     *
     * @param member the character to add
     */
    public void addMember(T member) {
        members.add(member);
    }

    /**
     * Returns the members who are not knocked out.
     *
     * @return the alive members
     */
    public List<T> getAliveMembers() {
        return getMembers()
                .stream()
                .filter(member -> !member.isKnockedOut())
                .toList();
    }

    /**
     * Checks if every member is knocked out.
     *
     * @return true if no member is alive
     */
    public boolean isDefeated() {
        return getAliveMembers().isEmpty();
    }

    /**
     * Returns all members, including knocked-out ones.
     * <p>The list is read-only. Use {@link #addMember(GameCharacter)} to add members.</p>
     *
     * @return the members
     */
    public List<T> getMembers() {
        return Collections.unmodifiableList(members);
    }
}
