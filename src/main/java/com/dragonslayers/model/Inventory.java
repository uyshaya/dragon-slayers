package com.dragonslayers.model;

import com.dragonslayers.exceptions.InvalidActionException;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Inventory of items.
 */
public class Inventory implements Serializable {

    private static final int DEFAULT_COUNT = 1;

    private final Map<String, Item> items = new HashMap<>();

    private final Map<String, Integer> counts = new HashMap<>();

    /**
     * Creates an empty inventory.
     */
    public Inventory() {
    }

    /**
     * Returns every item, keyed by name.
     * <p>The map is read-only. Use {@link #addItem(Item)} to add items.</p>
     *
     * @return the items
     */
    public Map<String, Item> getItems() {
        return Collections.unmodifiableMap(items);
    }

    /**
     * Returns how many of an item the party has.
     *
     * @param name the item's name
     * @return the count, or 0 if the party has none
     */
    public int getCount(String name) {
        return counts.getOrDefault(name, 0);
    }

    /**
     * Adds several of an item.
     *
     * @param item  the item to add
     * @param count how many to add
     */
    public void addItem(Item item, int count) {
        items.putIfAbsent(item.getName(), item);
        counts.put(item.getName(), getCount(item.getName()) + count);
    }

    /**
     * Adds one of an item.
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        addItem(item, DEFAULT_COUNT);
    }

    /**
     * Uses one of an item on the target, then removes it.
     *
     * @param name   the item's name
     * @param target the character to use it on
     * @throws InvalidActionException if the item is missing or not usable
     */
    public void useItem(String name, GameCharacter target) {
        final Item item = items.get(name);
        if (item == null) {
            throw new InvalidActionException("No " + name + " in the inventory");
        }

        if (item instanceof Usable usable) {
            usable.useOn(target);
            removeOne(name);
        } else {
            throw new InvalidActionException(name + " cannot be used");
        }
    }

    /**
     * Removes one of an item. Removes the item fully when none are left.
     *
     * @param name the item's name
     */
    private void removeOne(String name) {
        int newCount = getCount(name) - 1;
        if (newCount > 0) {
            counts.put(name, newCount);
        } else {
            counts.remove(name);
            items.remove(name);
        }
    }

    /**
     * Returns the names of the usable items, sorted A to Z.
     *
     * @return the usable item names
     */
    public List<String> getUsableItemNames() {
        return items.values().stream()
                .filter(item -> item instanceof Usable)
                .map(Item::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
