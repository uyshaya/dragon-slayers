# Dragon Slayers

A turn-based pixel-art RPG made with Java Swing.

- **Course:** Object-Oriented Programming, University of San Jose - Recoletos
- **Instructor:** Engr. Violdan E. Bayocot
- **Submitted:** TBA
- **Team:** Shaniah Uy (GitHub: [@uyshaya](https://github.com/uyshaya)), solo project

## How to build and run

You need JDK 17 or newer, and Maven.

1. Clone the repository.
   ```
   git clone https://github.com/uyshaya/dragon-slayers.git
   cd dragon-slayers
   ```
2. Build the JAR.
   ```
   mvn clean package
   ```
3. Run the game.
   ```
   java -jar target/dragon-slayers.jar
   ```

You can also open the folder in IntelliJ IDEA and run `com.dragonslayers.Main`.

## Controls and gameplay

Coming soon.

## Project layout

```
src/main/java/com/dragonslayers/
├── Main.java      starts the game
├── model/         characters, items, party, battle
├── view/          Swing screens
├── controller/    game flow
├── io/            saving and loading
├── exceptions/    custom exceptions
└── assets/        sprite loading
```

## Feature checklist

Each row is a requirement from Chapter III of the spec.

### §1 Object-oriented architecture

| Done | Requirement | Where |
|---|---|---|
| ✓ | Abstract class | `GameCharacter`, `Hero`, `Enemy` |
| ✓ | Two interfaces, each implemented by two or more classes | `Saveable` (`Hero`, items), `Usable` (`Potion`, `Weapon`, `Armor`) |
| ✓ | Inheritance at least two levels deep | `GameCharacter` → `Hero` → `Warrior` |
| ✓ | All instance fields private | `GameCharacter` |
| ✓ | Method overriding with `@Override` | `getClassName()` in each hero and enemy |
| ✓ | Method overloading | `Inventory.addItem()` |
| ✓ | Polymorphic battle loop over a supertype collection | `BattleEngine.runEnemyTurns()` |

### §2 Collections and generics

| Done | Requirement | Where |
|---|---|---|
| ✓ | Two collection types from `java.util` | `ArrayList` in `Party`, `HashMap` in `Inventory` |
| ✓ | Generic class or method | `Party<T extends GameCharacter>` |
| ✓ | Sorting with a `Comparator` | `Battle` (`BY_SPEED`), `Leaderboard` (`BY_RANK`: wins, then fewest rounds, with `thenComparingInt`) |
| ✓ | Non-trivial Stream API use | `Inventory.getUsableItemNames()` |

### §3 Exception handling

| Done | Requirement | Where |
|---|---|---|
| ✓ | Two custom exceptions | `SaveNotFoundException`, `CorruptedSaveException`, `InvalidActionException` |
| ✓ | File I/O in try-with-resources or try-catch-finally | `BinarySaveManager`, `TextSaveManager` |
| ✓ | No empty catch blocks | save managers rethrow as custom exceptions; `GameController.loadGame()` shows a message |
| ✓ | Failed user actions show a message, not a crash | `BattlePanel` (invalid actions), `CharacterSelectPanel` (bad names), `GameController.loadGame()` (missing or damaged saves) |

### §4 File input and output

| Done | Requirement | Where |
|---|---|---|
| ✓ | Human-readable text saves | `TextSaveManager` (`saves/slot1.txt` to `slot3.txt`), `LeaderboardManager` (`saves/leaderboard.txt`) |
| ✓ | Binary saves with `ObjectOutputStream` | `BinarySaveManager` |
| ✓ | Corrupted or missing saves handled | `SaveNotFoundException`, `CorruptedSaveException` in `load()` of both save managers |
| ✓ | Three save slots | `SaveManager.SLOT_COUNT` |

### §5 Graphical user interface

| Done | Requirement | Where |
|---|---|---|
| ✓ | Swing only | `view` package (`JFrame`, `JPanel`, `JButton`, `JOptionPane`) |
| ✓ | Three or more screens | `MainMenuPanel`, `CharacterSelectPanel`, `BattlePanel`, `InventoryPanel`, `EndPanel`, switched with `CardLayout` |
| ✓ | Two or more layout managers | `CardLayout` in `GameFrame`; `GridBagLayout`, `BorderLayout`, `GridLayout`, `FlowLayout` in `MainMenuPanel` |
| ✓ | Inner class, anonymous class, and lambda event handlers | inner `ActionButtonListener` in `BattlePanel`, anonymous class in `CharacterSelectPanel`, lambdas in `MainMenuPanel` |
| ✓ | Custom `paintComponent` with `Graphics2D` | `BattleCanvas` draws the arena, sprites, and health bars; `BackgroundPanel` draws the background picture behind every screen |
| ✓ | Nearest-neighbour sprite scaling | `VALUE_INTERPOLATION_NEAREST_NEIGHBOR` in `BattleCanvas`, `Image.SCALE_REPLICATE` in `SpriteLoader.getIcon()` |

### §6 Code quality

| Done | Requirement | Where |
|---|---|---|
| | Javadoc on all classes and public methods | |
| | Naming conventions | |
| | No class over about 300 lines | |
| ✓ | No magic numbers | `GameBalance` |
| ✓ | Logical packages | `model`, `view`, `controller`, `io`, `exceptions`, `assets` |

### Bonus

| Done | Bonus | Where |
|---|---|---|
| ✓ | Animation with `javax.swing.Timer` (+3) | `BattleCanvas.playProjectile()`: when a hero or enemy attacks or uses an ability, an effect sprite flies to the target, one after another; living characters bob up and down while idle |
| ✓ | Persistent achievements across save files (+2) | `Leaderboard` and `LeaderboardManager`: each dragon win is recorded for the party name in `saves/leaderboard.txt`, separate from the save slots; the main menu shows the top three |

## Who did what

**Shaniah Uy (GitHub: [@uyshaya](https://github.com/uyshaya))** - solo project.

- Wrote the character hierarchy in `com.dragonslayers.model`: the abstract `GameCharacter` with its stats, level, and defending flag (`defend()`, `stopDefending()`), and the abstract `Hero` and `Enemy` classes.
- Wrote the hero classes `Warrior`, `Mage`, `Archer`, and `Cleric`, and the enemy classes `Goblin`, `Skeleton`, and `DragonBoss`, each with a name-only constructor and its own `getClassName()`.
- Created the `GameBalance` constants class and the `Attackable`, `Healable`, `Saveable`, and `Usable` interfaces.
- Set up the project structure and the `Main` entry point.
- Wrote the combat methods in `GameCharacter`: `takeDamage()`, `heal()`, `isKnockedOut()`, `attack()`, and the `calculateDamage()` and `rollRandomDamage()` helpers.
- Wrote `toSaveFormat()` in `Hero`.
- Wrote `useAbility()` for each hero and enemy (Power Strike, Fireball, Double Shot, Heal, Dirty Trick, Bone Guard, and Flame Breath), with each ability's value as a `private static final` constant in its own class.
- Wrote the generic `Party<T extends GameCharacter>` class with `addMember()`, `getAliveMembers()`, `isDefeated()`, and a read-only `getMembers()`.
- Wrote `takeTurn()` and `shouldUseAbility()` in `Enemy`, and the every-third-turn Flame Breath in `DragonBoss`.
- Wrote the `Battle` class with the combat log, round counter, win checks, and turn order sorted by speed with a `Comparator`.
- Wrote `BattleEngine` with the hero actions (attack, ability, defend, and `heroUseItem()`) and the enemy phase.
- Wrote the item hierarchy (`Item`, `Potion`, `Equipment`, `Weapon`, `Armor`, `KeyItem`) and `equipWeapon()`/`equipArmor()` in `Hero`, which replace the old bonus instead of stacking it.
- Wrote `Inventory` with two `HashMap`s (items and counts by name), the overloaded `addItem()`, `useItem()`, and the stream query `getUsableItemNames()`.
- Wrote the custom exceptions `SaveNotFoundException`, `CorruptedSaveException`, and `InvalidActionException`, and made `Inventory.useItem()`, `Weapon.useOn()`, and `Armor.useOn()` throw `InvalidActionException` for missing items, unusable items, and non-hero targets.
- Wrote `GameState` (hero party, inventory, and encounter number) with two constructors, one for a new game and one for loading, and made `GameCharacter`, `Item`, `Party`, `Inventory`, and `GameState` implement `Serializable`.
- Wrote the `SaveManager` interface and `BinarySaveManager`, which saves and loads a `GameState` in 3 slots with try-with-resources and turns missing or damaged files into `SaveNotFoundException` and `CorruptedSaveException`.
- Wrote `TextSaveManager`, which saves the encounter, heroes with their equipped weapons and armor, and items with counts as `key=value` lines, and `createHero()`/`createItem()` to rebuild them, plus `restore()` in `GameCharacter` to set a loaded hero's level and HP with a range check.
- Wrote `GameFrame` (the window, with a `CardLayout`, `addScreen()`, `showScreen()`, and the screen-name constants) and `GameController` (`start()`, and `loadGame()` with error dialogs), and changed `Main` to start the controller.
- Added `getAbilityName()` to every character and used it in the combat log lines in `BattleEngine` and `Enemy.takeTurn()`, and wrote `HeroFactory`, which creates a hero from its class name and is now used by `TextSaveManager`.
- Started `CharacterSelectPanel` (the layout, the class cards read from `HeroFactory`, the anonymous class listener on each Choose button, and the party label built with a stream).
- Wrote `EncounterFactory` with five encounters of increasing difficulty ending with the boss, added hero turn tracking to `BattleEngine` (`getCurrentHero()`, `startRound()`, `endHeroTurn()`), and added `abilityTargetsAllies()` to `Hero`, overridden in `Cleric`.
- Started `BattlePanel` (the inner class `ActionButtonListener`, the generic method `chooseTarget()`, and `startBattle()`) and added `startEncounter()` to `GameController`.
- Wrote `InventoryPanel` (the `BorderLayout` with the title, Party, and Inventory areas, and `showState()`) and added `showInventory()` to `GameController`, so the screen appears after each win and after loading a save.
- Designed the leaderboard: a party name, wins counted per party name, and the top three parties shown, ranked by wins and then the fastest win.
- Wrote `LeaderboardEntry` (party name, wins, and best rounds, with two constructors and `recordWin()`) and started `Leaderboard` (the `BY_RANK` comparator and `recordWin()`).
- Designed the ability use limit: each hero can use its ability only a set number of times per encounter, so that Attack is still worth choosing.
- Designed click-to-target: Attack and Ability targets are chosen by clicking the enemy or ally on the battle screen instead of a dialog, with Esc to cancel.

## AI usage disclosure

I used Claude for the following:

- Summarized the project spec.
- Drafted the README template and later reorganized it to match the spec's required sections.
- Set up `pom.xml` so `mvn clean package` builds a runnable JAR for Java 17.
- Wrote the hero and enemy stat constants in `GameBalance` and suggested their values.
- Reviewed the classes I wrote and pointed out errors.
- Added Javadoc comments to most classes, interfaces, and methods.
- Suggested the damage formula (attack, minus half the target's defense, plus a random amount, with a minimum of 1) and the values of its constants in `GameBalance`.
- Suggested the single-target abilities Fireball (ignores defense), Dirty Trick, and Flame Breath (triple damage), and the shared `calculateDamage()` and `rollRandomDamage()` helpers.
- Suggested the hero text save format and writing `toSaveFormat()` once in `Hero`.
- Suggested the `Party`, `Battle`, `BattleEngine`, and `Inventory` class designs, including returning a read-only list from `getMembers()`, sorting each side's turn order with a stream and a `Comparator`, ending a defend at the start of the character's next turn, and storing items and counts in two maps.
- Suggested the enemy turn design: a random alive target, a 1 in 3 ability chance, and the boss using Flame Breath every third turn by overriding `shouldUseAbility()`.
- Suggested a simpler version of the spec's item hierarchy (no `Consumable` or `Antidote`), equipping through `useOn()`, and storing equipped items in `Hero`.
- Suggested the three custom exceptions, which ones are checked or unchecked, and where to throw `InvalidActionException`.
- Suggested the `GameState` design and which classes need `Serializable` for binary saves.
- Suggested the `SaveManager` interface and the `BinarySaveManager` save and load code, including the multi-catch that wraps read errors in `CorruptedSaveException`.
- Suggested the `TextSaveManager` file format, the `restore()` method, and the parsing approach, then helped fix the `load()` loop and renamed its variables to make it easier to read, as I requested.
- Suggested the `GameFrame` and `GameController` designs, and wrote `MainMenuPanel` (the title, the three buttons with lambda listeners, and the save slot dialog) at my request.
- Suggested `getAbilityName()` and moving hero creation into `HeroFactory`, and pointed out that the enemy log line said "uses" even on normal attacks.
- Finished `CharacterSelectPanel` at my request (the name checks with warning messages, the Clear, Back, and Start buttons, and the card layout), and added `showMainMenu()`, `newGame()`, and `startGame()` to `GameController`.
- Suggested `EncounterFactory`, the hero turn tracking in `BattleEngine`, and `abilityTargetsAllies()`, pointed out that `endHeroTurn()` restarted the round after every action, and added the Javadoc for these.
- Finished `BattlePanel` at my request (the layout, the ability and item actions, the status and log display, and the Javadoc), and added `battleEnded()` and the starting potions to `GameController`.
- Suggested the steps for `InventoryPanel` and its `GameController` hooks, and reviewed my code for each step.
- Finished `refresh()` and `describe()` in `InventoryPanel` at my request (the hero text, the null checks for equipment, and the inventory stream), and fixed the Party scroll pane.
- Wrote the remaining GUI work at my request, after I finished the object-oriented parts of the project: the Use Item, Save Game, and Next Battle buttons in `InventoryPanel`, `saveGame()` and `continueGame()` in `GameController`, the Binary or Text choice when loading (`loadGame()` picks the matching `SaveManager`), `EndPanel` for the Victory and Game Over screens, `BattleCanvas` (the `paintComponent` drawing with the sprites, health bars, faded knocked-out characters, and the frame on the current hero), `SpriteLoader` with its `HashMap` cache, the sprites on the main menu and the character cards, and the mirroring that makes enemies face the heroes.
- Found the two CC0 sprite packs (Kenney's Tiny Dungeon and Clint Bellanger's Tiny Creatures) at my request, picked a tile for each hero and enemy, and copied them into `assets/sprites/`.
- Wrote the attack and ability animations for the heroes and enemies and the idle bob animation at my request, including the turn tracking they need in `Enemy` (`getLastTarget()`, `usedAbilityLastTurn()`, `abilityTargetsSelf()`) and `BattleEngine` (`getEnemiesThatActed()`).
- Wrote the screen and battle backgrounds at my request (`BackgroundPanel`, an abstract panel that every screen extends, and the battle arena drawn by `BattleCanvas`), using Kenney's Tiny Dungeon sample scene and tiles.
- Wrote the encounter rewards at my request: `createReward()` in `EncounterFactory` (Cloth Armor, an Iron Sword, Padded Armor, Leather Armor, and the Dragon Key key item for defeating the dragon), the step in `GameController.battleEnded()` that adds them to the inventory, the Save Game button on the Victory screen, and the shared `chooseSlot()` dialog in `BackgroundPanel`.
- Refined my leaderboard idea by suggesting rounds instead of real time for the fastest win, and keeping every party while showing only the top three. Fixed `Leaderboard` at my request (`equals` instead of `==`, the first-win case with `Optional`, `getAllParties()`, and the Javadoc).
- Wrote the rest of the leaderboard feature at my request: the party name and round total in `GameState` and the text save, `LeaderboardManager`, `recordVictory()`, `showLeaderboard()`, and the finished-save message in `GameController`, the Leaderboard button, and the party name prompt with the shared `askForName()` check.
- Wrote the ability use limit at my request (3 uses per encounter, `ABILITY_USES_PER_ENCOUNTER` in `GameBalance`): the counter and its methods in `Hero`, `checkAbilityUses()` in `BattleEngine`, which throws `InvalidActionException` when no uses are left, the reset in `startEncounter()`, and the uses shown on each hero's battle plate.
- Wrote the click-to-target feature at my request: the target choice in `BattlePanel` (`pendingAction`, `chooseClickedTarget()`, `getValidTargets()`, an anonymous `MouseAdapter`, and the Esc key binding) and, in `BattleCanvas`, `getCharacterAt()`, `getPlateBounds()`, the red and green target outlines, and the hand cursor.

I wrote all other code myself.

## External assets and attribution

All sprites come from two free pixel-art packs. Both are licensed CC0 (public domain), so they are free to use. Crediting is not required, but they are credited here.

| Pack | Author | Source | License |
|---|---|---|---|
| Tiny Dungeon (1.0) | Kenney (kenney.nl) | https://opengameart.org/content/tiny-dungeon | CC0 1.0 |
| Tiny Creatures (1.0) | Clint Bellanger (clintbellanger.net) | https://opengameart.org/content/tiny-creatures | CC0 1.0 |

Each file in `assets/sprites/` is one 16x16 tile from a pack, renamed for the game:

| File | Pack | Tile |
|---|---|---|
| `warrior.png` | Tiny Dungeon | `Tiles/tile_0097.png` |
| `mage.png` | Tiny Dungeon | `Tiles/tile_0084.png` |
| `archer.png` | Tiny Dungeon | `Tiles/tile_0112.png` |
| `cleric.png` | Tiny Dungeon | `Tiles/tile_0099.png` |
| `goblin.png` | Tiny Creatures | `Tiles/tile_0011.png` |
| `skeleton.png` | Tiny Creatures | `Tiles/tile_0002.png` |
| `dragon_boss.png` | Tiny Creatures | `Tiles/tile_0034.png` |
| `fireball.png` | Tiny Creatures | `Tiles/tile_0056.png` |
| `heal.png` | Tiny Creatures | `Tiles/tile_0090.png` |
| `power_strike.png` | Tiny Dungeon | `Tiles/tile_0104.png`, rotated to point right |
| `double_shot.png` | Tiny Dungeon | `Tiles/tile_0131.png`, rotated to point right |
| `attack.png` | Tiny Dungeon | `Tiles/tile_0103.png`, rotated to point right |
| `bone_guard.png` | Tiny Dungeon | `Tiles/tile_0102.png` |
| `dirty_trick.png` | Tiny Creatures | `Tiles/tile_0058.png` |
| `flame_breath.png` | Tiny Creatures | `Tiles/tile_0004.png` |
| `background.png` | Tiny Dungeon | `Sample.png`, a 480x306 area cropped from x 380, y 0 |
| `battle_background.png` | Tiny Dungeon | 9x8 tiles laid out as an arena: `tile_0040` (wall), `tile_0050` (wall shadow), `tile_0048`, `tile_0049`, `tile_0051` (sand floor) |

The Tiny Creatures sprites were cut from the pack's `Tilemap/tilemap_packed.png` instead of the single tile files, because the single files have a black background and the sheet has a transparent one. The pixels are the same. The dagger, sword, and arrow were rotated 90 degrees so they point toward the enemies. The game mirrors the enemy sprites in code so they face the heroes. Those files are not changed. The screen background is a cropped part of Kenney's sample scene, without the license banner at the bottom.

## Known issues

- Hero and item names cannot contain `,` or `=`, because the text save uses them as separators.
