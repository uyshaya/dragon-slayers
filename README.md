# Dragon Slayers

A turn-based pixel-art RPG made with Java Swing.

Object-Oriented Programming · Engr. Violdan E. Bayocot · University of San Jose - Recoletos

**Author:** Shaniah Uy · **Submitted:** TBA

## Run it

You need JDK 17 or newer, and Maven.

```
git clone <repo-url>
cd dragon-slayers
mvn clean package
java -jar target/dragon-slayers.jar
```

Or open the folder in IntelliJ IDEA and run `com.dragonslayers.Main`.

## How to play

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

## Requirements checklist

| Requirement | Where |
|---|---|
| Abstract class | |
| Two interfaces, two implementers each | |
| Inheritance three levels deep | |
| Private fields, overriding, overloading | |
| Polymorphic battle loop | |
| Two collection types | |
| Generic class or method | |
| Comparator sort and Stream API | |
| Two custom exceptions | |
| Safe file I/O, no empty catch blocks | |
| Text and binary saves, three slots | |
| Corrupted or missing saves handled | |
| Three or more screens (CardLayout) | |
| Two or more layout managers | |
| Inner class, anonymous class, and lambda handlers | |
| Custom `paintComponent` drawing | |
| Javadoc, naming, small classes, no magic numbers | |

## Credits

**Who did what:** Solo project by Shaniah Uy.

**Art and assets:** To be added.

**AI use:** Claude helped summarize the spec, fill in the README, and review the starter files. Claude also updated the `pom.xml` setup so `mvn clean package` builds a runnable JAR.

## Known issues

None yet.
