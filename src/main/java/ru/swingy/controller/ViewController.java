package ru.swingy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.swingy.interfaces.MapObject;
import ru.swingy.model.Artifact;
import ru.swingy.model.Enemy;
import ru.swingy.model.GameMap;
import ru.swingy.model.Hero;
import ru.swingy.service.ArtifactService;
import ru.swingy.service.GameMapService;
import ru.swingy.service.HeroService;
import ru.swingy.view.ConsoleView;
import ru.swingy.view.JFrameView;
import ru.swingy.view.interfaces.ViewInterface;

import javax.swing.*;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ViewController {

    private final JFrameView jFrameView;
    private final ConsoleView consoleView;
    private ViewInterface viewInterface;

    private final HeroService heroService;
    private final GameMapService gameMapService;
    private final ArtifactService artifactService;

    private GameMap gameMap;
    private Hero hero;
    private Random random = new Random();

    public void initJFrame(JFrame jFrame) {
        jFrameView.init(jFrame);
    }

    public void initConsole() {
        consoleView.init();
    }

    public void activeJFrame() {
        jFrameView.setVisibleFrame(true);
        viewInterface = jFrameView;
    }

    public void activeConsole() {
        jFrameView.setVisibleFrame(false);
        viewInterface = consoleView;
    }

    public void start() {
        while (true) {
            choseOrCreateHero();
            while (true) {
                showHeroInfo();
                showMap();
                boolean isGameOver = move();
                if (isGameOver)
                    break;
            }
        }
    }

    private void showHeroInfo() {
        viewInterface.showHeroInfo(hero);
    }

    private boolean move() {
        String moveSide = viewInterface.move();
        if (moveSide.equals("SWAP")) {
            if (viewInterface instanceof JFrameView) {
                activeConsole();
            } else {
                activeJFrame();
            }
            return false;
        }
        boolean isGameOver = moveHeroToPosition(moveSide);
        if (isGameOver)
            return true;
        boolean isHeroAlive = fightIfNeeded();
        if (!isHeroAlive)
            return true;
        return false;
    }

    private boolean fightIfNeeded() {
        MapObject[][] map = gameMap.getMap();
        if (map[hero.getPositionX()][hero.getPositionY()] != null) {
            Enemy enemy = (Enemy) map[hero.getPositionX()][hero.getPositionY()];

            boolean isFightNeeded = viewInterface.metTheEnemy(enemy, hero);

            if (!(!isFightNeeded && isYouAreLucky())) {
                fight(enemy);
                if (hero.getCurrentHp() <= 0)
                    return false;
            }
        }
        return true;
    }

    private boolean fight(Enemy enemy) {
        viewInterface.showEvent("FIGHT!");
        while (hero.getCurrentHp() > 0 && enemy.getHp() > 0) {
            Integer damageForEnemy = enemy.setDamage(hero.getAttack());
            viewInterface.showEvent("The hero dealt damage: " + damageForEnemy + ". Enemy HP: " + enemy.getHp());
            if (enemy.getHp() <= 0) {
                viewInterface.showFightResult("Congratulations! You won the battle!");
                artifactPart();
                heroService.gainExperienceAfterWinning(hero);
                gameMapService.clearMapField(gameMap, hero.getPositionX(), hero.getPositionY());
                return true;
            }
            Integer damageForHero = hero.setDamage(enemy.getAttack());
            viewInterface.showEvent("The enemy dealt damage: " + damageForHero + ". Hero HP: " + hero.getCurrentHp());
            if (hero.getCurrentHp() <= 0) {
                viewInterface.showFightResult("Wow! You are DEAD!");
                return false;
            }
        }
        return true;
    }

    private void artifactPart() {
        Artifact newArtifact = artifactService.tryGetArtifact(hero.getLevel());
        boolean needSetArtifact = viewInterface.checkNewArtifact(newArtifact);

        if (needSetArtifact) {

            for (Artifact art : hero.getArtifacts()) {

                if (art.getType().equals(newArtifact.getType())) {

                    hero.setAttack(hero.getAttack() - art.getAttack());
                    hero.setDefence(hero.getDefence() - art.getDefence());
                    hero.setHp(hero.getHp() - art.getHp());

                    art.setAttack(newArtifact.getAttack());
                    art.setDefence(newArtifact.getDefence());
                    art.setHp(newArtifact.getHp());

                    hero.setAttack(hero.getAttack() + newArtifact.getAttack());
                    hero.setDefence(hero.getDefence() + newArtifact.getDefence());
                    hero.setHp(hero.getHp() + newArtifact.getHp());
                    return;
                }
            }
            hero.getArtifacts().add(newArtifact);

            hero.setAttack(hero.getAttack() + newArtifact.getAttack());
            hero.setDefence(hero.getDefence() + newArtifact.getDefence());
            hero.setHp(hero.getHp() + newArtifact.getHp());
            viewInterface.showEvent("Artifact added!");
        }
        viewInterface.showEvent("");
    }

    private boolean isYouAreLucky() {
        viewInterface.showEvent("Oh! Test your luck...");
        int luck = random.nextInt(2);
        if (luck == 1) {
            viewInterface.luckInfo("You were lucky!\n");
            gameMapService.clearMapField(gameMap, hero.getPositionX(), hero.getPositionY());
            return true;
        } else {
            viewInterface.luckInfo("You were unlucky");
            return false;
        }
    }

    private boolean moveHeroToPosition(String moveSide) {
        switch (moveSide) {
            case "W":
                hero.incPositionX(-1);
                break;
            case "D":
                hero.incPositionY(1);
                break;
            case "S":
                hero.incPositionX(1);
                break;
            case "A":
                hero.incPositionY(-1);
                break;
        }
        if (hero.getPositionX() >= gameMap.getDimension()
                || hero.getPositionY() >= gameMap.getDimension()
                || hero.getPositionX() < 0
                || hero.getPositionY() < 0) {
            viewInterface.wonMessage("You won! Congratulations!");
            heroService.saveHero(hero);
            return true;
        }
        return false;
    }

    private void showMap() {
        viewInterface.showMap(gameMap, hero.getPositionX(), hero.getPositionY());
    }

    private void choseOrCreateHero() {
        List<Hero> allHeroes = heroService.getAllHeroes();
        hero = viewInterface.pickHeroOrCreate(allHeroes);
        if (hero != null) {
            gameMap = gameMapService.createGameMap(hero.getLevel());
            hero.setPositionX(gameMap.getDimension() / 2);
            hero.setPositionY(gameMap.getDimension() / 2);
            hero.setCurrentHp(hero.getHp());
        }
    }

}
