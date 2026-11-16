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
| ✓ | Sorting with a `Comparator` | `Battle` (`BY_SPEED`) |
| ✓ | Non-trivial Stream API use | `Inventory.getUsableItemNames()` |

### §3 Exception handling

| Done | Requirement | Where |
|---|---|---|
| ✓ | Two custom exceptions | `SaveNotFoundException`, `CorruptedSaveException`, `InvalidActionException` |
| ✓ | File I/O in try-with-resources or try-catch-finally | `BinarySaveManager`, `TextSaveManager` |
| ✓ | No empty catch blocks | save managers rethrow as custom exceptions; `GameController.loadGame()` shows a message |
| | Failed user actions show a message, not a crash | |

### §4 File input and output

| Done | Requirement | Where |
|---|---|---|
| ✓ | Human-readable text saves | `TextSaveManager` (`saves/slot1.txt` to `slot3.txt`) |
| ✓ | Binary saves with `ObjectOutputStream` | `BinarySaveManager` |
| ✓ | Corrupted or missing saves handled | `SaveNotFoundException`, `CorruptedSaveException` in `load()` of both save managers |
| ✓ | Three save slots | `SaveManager.SLOT_COUNT` |

### §5 Graphical user interface

| Done | Requirement | Where |
|---|---|---|
| ✓ | Swing only | `view` package (`JFrame`, `JPanel`, `JButton`, `JOptionPane`) |
| | Three or more screens | |
| ✓ | Two or more layout managers | `CardLayout` in `GameFrame`; `BorderLayout`, `GridLayout`, `FlowLayout` in `MainMenuPanel` |
| | Inner class, anonymous class, and lambda event handlers | |
| | Custom `paintComponent` with `Graphics2D` | |
| | Nearest-neighbour sprite scaling | |

### §6 Code quality

| Done | Requirement | Where |
|---|---|---|
| | Javadoc on all classes and public methods | |
| | Naming conventions | |
| | No class over about 300 lines | |
| ✓ | No magic numbers | `GameBalance` |
| ✓ | Logical packages | `model`, `view`, `controller`, `io`, `exceptions`, `assets` |

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

I wrote all other code myself.

## External assets and attribution

No assets yet. Each external asset will be listed here with its source URL, author, and license.

## Known issues

- Hero and item names cannot contain `,` or `=`, because the text save uses them as separators.
