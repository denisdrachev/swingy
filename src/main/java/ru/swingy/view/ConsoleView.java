package ru.swingy.view;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.swingy.interfaces.MapObject;
import ru.swingy.model.Artifact;
import ru.swingy.model.Enemy;
import ru.swingy.model.GameMap;
import ru.swingy.model.Hero;
import ru.swingy.utils.Util;
import ru.swingy.view.interfaces.ViewInterface;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConsoleView implements ViewInterface {

    private Scanner scanner = new Scanner(System.in);

    @Value("${game.map.size:5}")
    private Integer gameMapSize;

    @Override
    public void showMap(GameMap gameMap, Integer heroPositionX, Integer heroPositionY) {

        MapObject[][] map = gameMap.getMap();

        Integer N = gameMap.getDimension();

        if (gameMapSize != 0) {
            if (N >= gameMapSize)
                N = gameMapSize;
            else
                N = 5;
        }

        for (int i = -N / 2; i <= N / 2; i++) {
            for (int j = -N / 2; j <= N / 2; j++) {

                if (heroPositionX >= 0 && heroPositionX < map.length
                        && heroPositionY >= 0 && heroPositionY < map.length
                        && heroPositionX + i < map.length && heroPositionX + i >= 0
                        && heroPositionY + j < map.length && heroPositionY + j >= 0) {

                    int i1 = i + heroPositionX;
                    int i2 = j + heroPositionY;

                    if (i == 0 && j == 0) {
                        System.out.print("H ");
                    } else {
                        System.out.print(map[i1][i2] == null ? "_ " : "E ");
                    }
                } else {
                    System.out.print("  ");
//                    ch.setVisible(false);
                }
            }
            System.out.println();
        }
        System.out.println();

    }

    @Override
    public void showHeroInfo(Hero hero) {

        StringBuilder stringBuilder = new StringBuilder();

        System.out.println("Hero info:");
        System.out.println("Name: " + hero.getName());
        System.out.println("Class: " + hero.getClassType());
        System.out.println("Level: " + hero.getLevel());
        System.out.println("Experience: " + hero.getExperience() + "/" + Util.getLevelExperience(hero.getLevel() + 1));
        System.out.println("Attack: " + hero.getAttack());
        System.out.println("Defence: " + hero.getDefence());
        System.out.println("HP: " + hero.getCurrentHp() + "/" + hero.getHp());

        stringBuilder.append("Artifacts: ");
        if (hero.getArtifacts().size() == 0)
            stringBuilder.append("none");
        else
            stringBuilder.append("\n");
        for (Artifact artifact : hero.getArtifacts()) {
            stringBuilder.append("\t")
                    .append(artifact.getType())
                    .append(": ");
            if (artifact.getAttack() > 0)
                stringBuilder.append(" Attack +").append(artifact.getAttack()).append("\n");
            if (artifact.getDefence() > 0)
                stringBuilder.append(" Defence +").append(artifact.getDefence()).append("\n");
            if (artifact.getHp() > 0)
                stringBuilder.append(" HP +").append(artifact.getHp()).append("\n");
        }
        System.out.println(stringBuilder.toString());
        System.out.println();
    }

    public boolean isFightNeeded() {
        System.out.println("Do you want to fight? [y|n]");
        while (true) {
            String reader = reader();
            if (reader.compareToIgnoreCase("y") == 0) {
                System.out.println();
                return true;
            } else if (reader.compareToIgnoreCase("n") == 0) {
                System.out.println();
                return false;
            } else {
                System.out.println("Incorrect answer. Choose [y|n]");
            }
        }
    }

    @Override
    public void showEvent(String eventText) {
        System.out.println(eventText);
    }

    @Override
    public void showFightResult(String eventText) {
        System.out.println();
        System.out.println(eventText);
        System.out.println();
    }

    @Override
    public void luckInfo(String eventText) {
        System.out.println(eventText);
    }

    @Override
    public boolean metTheEnemy(Enemy enemy, Hero hero) {
        System.out.println("You met an enemy:");
//        System.out.println("Your hero: attack: " + hero.getAttack() + " defence: " + hero.getDefence() + " HP: " + hero.getCurrentHp() + "/" + hero.getHp());
        System.out.println("Enemy: attack: " + enemy.getAttack() + " defence: " + enemy.getDefence() + " HP: " + enemy.getHp());

        return isFightNeeded();
    }

    @Override
    public boolean checkNewArtifact(Artifact artifact) {
        if (artifact != null) {
            System.out.println("Enemy drop artifact: " + artifact);
            System.out.println("Take it? [y|n]");
            while (true) {
                String reader = reader();
                if (reader.equals("y")) {
                    return true;
                } else if (reader.equals("n")) {
                    return false;
                } else {
                    System.out.println("Incorrect answer. Choose [y|n]");
                }
            }
        }
        return false;
    }

    public void showHeroList(List<Hero> heroes) {
        for (Hero hero : heroes) {
            System.out.println(hero);
            for (Artifact artifact : hero.getArtifacts()) {
                System.out.println("\t" + artifact);
            }
        }
        System.out.println();
    }

    @Override
    public Hero pickHeroOrCreate(List<Hero> heroes) {

        showEvent("Create new Hero or continue? [create:1][select:2]");
        showEvent("\tEnter 1 if you want to create a hero");
        showEvent("\tEnter 2 if you want to select a hero");
        while (true) {
            String createOrContinue = reader();
            if (createOrContinue.equals("1")) {
                return createHero();
            } else if (createOrContinue.equals("2")) {
                return pickHero(heroes);
            }
            showEvent("Incorrect input. Try again:");
        }

    }

    private Hero createHero() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        while (true) {
            showEvent("Enter hero name:");
            String heroName = reader();

            showEvent("Enter hero class [WARRIOR, ARCHER, WIZARD]:");
            String heroClass = reader();
            if (heroClass != null)
                heroClass = heroClass.toUpperCase();

            Hero newHero = new Hero(heroName, heroClass);
            Set<ConstraintViolation<Hero>> validate = validator.validate(newHero);
            for (ConstraintViolation<Hero> heroConstraintViolation : validate) {
                System.err.println(heroConstraintViolation.getMessage());
            }
            if (validate.size() == 0)
                return newHero;
            showEvent("Incorrect input. Try again:");
        }
    }

    private Hero pickHero(List<Hero> heroes) {
        showEvent("\nHeroes:");
        showHeroList(heroes);

        while (true) {
            showEvent("Select hero number:");
            String heroId = reader();
            for (Hero hero : heroes) {
                if (hero.getId().toString().equals(heroId)) {
                    showEvent("");
                    return hero;
                }
            }
            showEvent("Hero with number '" + heroId + "' not exist. Try again.");
        }
    }

    @Override
    public String move() {
        while (true) {
            System.out.println("Which way are you going? [North:W, East:D, South:S, West:A, Gui:SWAP]");
            String side = reader().toUpperCase();
            if (side.equals("W") || side.equals("D") || side.equals("S") || side.equals("A") || side.equals("SWAP")) {
                System.out.println();
                return side;
            } else {
                System.out.println("Side with name '" + side + "' not exist. Try again.");
            }
        }
    }

    public void init() {
    }

    public String reader() {
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            return s;
        }
        return null;
    }

    @Override
    public void wonMessage(String s) {

    }
}
