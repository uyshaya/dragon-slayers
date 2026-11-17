package com.dragonslayers.view;

import com.dragonslayers.assets.SpriteLoader;
import com.dragonslayers.controller.GameController;
import com.dragonslayers.model.Hero;
import com.dragonslayers.model.HeroFactory;
import com.dragonslayers.model.Party;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The screen where the player picks and names three heroes.
 * <p>Each class card shows the class stats and signature ability.</p>
 */
public class CharacterSelectPanel extends JPanel {

    private static final int PARTY_SIZE = 3;

    private static final int CARD_GAP = 10;

    private static final int PADDING = 20;

    private static final float TITLE_FONT_SIZE = 28f;

    private static final int CARD_SPRITE_SCALE = 6;

    private final List<Hero> chosenHeroes = new ArrayList<>();

    private final JLabel partyLabel = new JLabel();

    private final JButton startButton = new JButton("Start");

    /**
     * Creates the character select screen.
     *
     * @param controller the controller that handles going back and starting the game
     */
    public CharacterSelectPanel(GameController controller) {
        setLayout(new BorderLayout(CARD_GAP, CARD_GAP));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        JLabel title = new JLabel("Choose " + PARTY_SIZE + " heroes", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, HeroFactory.HERO_CLASSES.size(), CARD_GAP, CARD_GAP));
        for (String className : HeroFactory.HERO_CLASSES) {
            cards.add(createCard(className));
        }
        add(cards, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear");
        JButton backButton = new JButton("Back");
        clearButton.addActionListener(e -> reset());
        backButton.addActionListener(e -> controller.showMainMenu());
        startButton.addActionListener(e -> controller.startGame(createParty()));

        JPanel bottom = new JPanel();
        bottom.add(partyLabel);
        bottom.add(clearButton);
        bottom.add(backButton);
        bottom.add(startButton);
        add(bottom, BorderLayout.SOUTH);

        updatePartyLabel();
    }

    /**
     * Clears the chosen heroes. Called when a new game starts.
     */
    public void reset() {
        chosenHeroes.clear();
        updatePartyLabel();
    }

    /**
     * Creates the card for one hero class, with its stats, ability, sprite, and a Choose button.
     *
     * @param className the hero class, for example {@code Warrior}
     * @return the card panel
     */
    private JPanel createCard(String className) {
        Hero sample = HeroFactory.create(className, className); // only used to read its stats
        JPanel stats = new JPanel(new GridLayout(0, 1)); // 0 rows = as many as needed
        stats.add(new JLabel("HP: " + sample.getMaxHp()));
        stats.add(new JLabel("Attack: " + sample.getAttack()));
        stats.add(new JLabel("Defense: " + sample.getDefense()));
        stats.add(new JLabel("Speed: " + sample.getSpeed()));
        stats.add(new JLabel("Ability: " + sample.getAbilityName()));

        JButton chooseButton = new JButton("Choose");
        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseHero(className);
            }
        });

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createTitledBorder(sample.getClassName()));
        card.add(stats, BorderLayout.NORTH);
        card.add(new JLabel(SpriteLoader.getIcon(className, CARD_SPRITE_SCALE)), BorderLayout.CENTER);
        card.add(chooseButton, BorderLayout.SOUTH);
        return card;
    }

    /**
     * Asks for a name and adds a hero of the given class to the party.
     * Shows a message instead if the party is full or the name is not allowed.
     *
     * @param className the hero class to add
     */
    private void chooseHero(String className) {
        if (chosenHeroes.size() >= PARTY_SIZE) {
            showError("Your party is full.");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Name your " + className + ":");
        if (name == null) {
            return; // the player pressed Cancel
        }
        name = name.trim();
        if (name.isEmpty()) {
            showError("Name cannot be empty.");
            return;
        }
        if (name.contains(",") || name.contains("=")) {
            showError("Name cannot contain , or =");
            return;
        }

        chosenHeroes.add(HeroFactory.create(className, name));
        updatePartyLabel();
    }

    /**
     * Shows the chosen heroes and enables Start only when the party is full.
     */
    private void updatePartyLabel() {
        String names = chosenHeroes.stream()
                .map(hero -> hero.getName() + " (" + hero.getClassName() + ")")
                .collect(Collectors.joining(", "));
        partyLabel.setText("Party: " + names);
        startButton.setEnabled(chosenHeroes.size() == PARTY_SIZE);
    }

    /**
     * Puts the chosen heroes into a new party.
     *
     * @return the hero party
     */
    private Party<Hero> createParty() {
        Party<Hero> party = new Party<>();
        for (Hero hero : chosenHeroes) {
            party.addMember(hero);
        }
        return party;
    }

    /**
     * Shows an error message to the player.
     *
     * @param message the message to show
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Cannot choose hero", JOptionPane.WARNING_MESSAGE);
    }
}
