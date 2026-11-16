package com.dragonslayers.io;

import com.dragonslayers.exceptions.CorruptedSaveException;
import com.dragonslayers.exceptions.InvalidActionException;
import com.dragonslayers.exceptions.SaveNotFoundException;
import com.dragonslayers.model.*;

import java.io.*;

/**
 * Saves the game state as a plain text file that people can read and edit.
 * <p>Each line is {@code key=value}, for example {@code Hero=Warrior,Aria,1,95}.</p>
 */
public class TextSaveManager implements SaveManager {

    private static final String FILE_EXTENSION = ".txt";

    private static final String ENCOUNTER_KEY = "Encounter";

    private static final String HERO_KEY = "Hero";

    private static final String ITEM_KEY = "Item";

    private static final String EQUIPPED_KEY = "Equipped";

    /**
     * Creates a text save manager.
     */
    public TextSaveManager() {
    }

    /**
     * Writes the game state to the slot's {@code .txt} file.
     * <p>Writes the encounter, one line per hero followed by its equipped items, and one line per item with its count.</p>
     *
     * @param state the game state to save
     * @param slot  the slot number
     * @throws IOException if the file cannot be written
     */
    @Override
    public void save(GameState state, int slot) throws IOException {
        File file = getFile(slot);
        file.getParentFile().mkdirs(); // create saves/ if it doesn't exist
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println(ENCOUNTER_KEY + "=" + state.getEncounter());
            for (Hero hero : state.getHeroes().getMembers()) {
                out.println(hero.toSaveFormat());
                if (hero.getWeapon() != null) {
                    out.println(EQUIPPED_KEY + "=Weapon," + hero.getWeapon().getName() + "," + hero.getWeapon().getBonus());
                }
                if (hero.getArmor() != null) {
                    out.println(EQUIPPED_KEY + "=Armor," + hero.getArmor().getName() + "," + hero.getArmor().getBonus());
                }
            }
            for (Item item : state.getInventory().getItems().values()) {
                out.println(item.toSaveFormat() + "," + state.getInventory().getCount(item.getName()));
            }
        }
    }

    /**
     * Reads the game state from the slot's {@code .txt} file.
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
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            Party<Hero> heroes = new Party<>();
            Inventory inventory = new Inventory();
            int encounter = GameState.FIRST_ENCOUNTER;
            String line;
            Hero lastHero = null;

            while ((line = in.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] keyAndValue = line.split("=", 2);
                String key = keyAndValue[0];
                String value = keyAndValue[1];
                switch (key) {
                    case ENCOUNTER_KEY -> encounter = Integer.parseInt(value);
                    case HERO_KEY -> {
                        String[] heroDetails = value.split(",");
                        Hero hero = HeroFactory.create(heroDetails[0], heroDetails[1]);
                        int level = Integer.parseInt(heroDetails[2]);
                        int hp = Integer.parseInt(heroDetails[3]);
                        hero.restore(level, hp);
                        heroes.addMember(hero);
                        lastHero = hero;
                    }
                    case ITEM_KEY -> {
                        String[] itemDetails = value.split(",");
                        int count = Integer.parseInt(itemDetails[itemDetails.length - 1]);
                        inventory.addItem(createItem(itemDetails), count);
                    }
                    case EQUIPPED_KEY -> {
                        String[] equipmentDetails = value.split(",");
                        Item item = createItem(equipmentDetails);
                        if (lastHero == null || !(item instanceof Equipment)) {
                            throw new CorruptedSaveException("Invalid equipment line: " + line);
                        }
                        ((Equipment) item).useOn(lastHero);
                    }
                    default -> throw new CorruptedSaveException("Unknown line: " + line);
                }
            }

            if (heroes.getMembers().isEmpty()) {
                throw new CorruptedSaveException("Save in slot " + slot + " has no heroes");
            }

            return new GameState(heroes, inventory, encounter);
        } catch (IOException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
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

    /**
     * Creates an item from the details of an item line.
     *
     * @param itemDetails the type, name, and any bonus, for example {@code Weapon, Iron Sword, 5}
     * @return the new item
     * @throws CorruptedSaveException if the item type is unknown
     */
    private Item createItem(String[] itemDetails) throws CorruptedSaveException {
        String type = itemDetails[0];
        String name = itemDetails[1];
        return switch (type) {
            case "Potion" -> new Potion(name);
            case "Weapon" -> new Weapon(name, Integer.parseInt(itemDetails[2]));
            case "Armor" -> new Armor(name, Integer.parseInt(itemDetails[2]));
            case "KeyItem" -> new KeyItem(name);
            default -> throw new CorruptedSaveException("Unknown item: " + type);
        };
    }
}
