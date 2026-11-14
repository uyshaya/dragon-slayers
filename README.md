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
| | Two interfaces, each implemented by two or more classes | |
| ✓ | Inheritance at least two levels deep | `GameCharacter` → `Hero` → `Warrior` |
| ✓ | All instance fields private | `GameCharacter` |
| ✓ | Method overriding with `@Override` | `getClassName()` in each hero and enemy |
| | Method overloading | |
| | Polymorphic battle loop over a supertype collection | |

### §2 Collections and generics

| Done | Requirement | Where |
|---|---|---|
| | Two collection types from `java.util` | |
| ✓ | Generic class or method | `Party<T extends GameCharacter>` |
| | Sorting with a `Comparator` | |
| | Non-trivial Stream API use | |

### §3 Exception handling

| Done | Requirement | Where |
|---|---|---|
| | Two custom exceptions | |
| | File I/O in try-with-resources or try-catch-finally | |
| | No empty catch blocks | |
| | Failed user actions show a message, not a crash | |

### §4 File input and output

| Done | Requirement | Where |
|---|---|---|
| | Human-readable text saves | |
| | Binary saves with `ObjectOutputStream` | |
| | Corrupted or missing saves handled | |
| | Three save slots | |

### §5 Graphical user interface

| Done | Requirement | Where |
|---|---|---|
| | Swing only | |
| | Three or more screens | |
| | Two or more layout managers | |
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

## AI usage disclosure

I used Claude for the following:

- Summarized the project spec.
- Drafted the README template and later reorganized it to match the spec's required sections.
- Set up `pom.xml` so `mvn clean package` builds a runnable JAR for Java 17.
- Wrote the hero and enemy stat constants in `GameBalance` and suggested their values.
- Reviewed the classes I wrote and pointed out errors.
- Added the Javadoc comments for the classes, interfaces, and methods in `com.dragonslayers.model`.
- Suggested the damage formula (attack, minus half the target's defense, plus a random amount, with a minimum of 1) and the values of its constants in `GameBalance`.
- Suggested the single-target abilities Fireball (ignores defense), Dirty Trick, and Flame Breath (triple damage), and the shared `calculateDamage()` and `rollRandomDamage()` helpers.
- Suggested the hero text save format and writing `toSaveFormat()` once in `Hero`.
- Suggested the `Party` class design, including returning a read-only list from `getMembers()`.

I wrote all other code myself.

## External assets and attribution

No assets yet. Each external asset will be listed here with its source URL, author, and license.

## Known issues

None yet.
