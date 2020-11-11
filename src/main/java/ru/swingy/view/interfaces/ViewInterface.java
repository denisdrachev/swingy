package ru.swingy.view.interfaces;

import ru.swingy.model.Artifact;
import ru.swingy.model.Enemy;
import ru.swingy.model.GameMap;
import ru.swingy.model.Hero;

import java.util.List;

public interface ViewInterface {

    void showMap(GameMap gameMap, Integer heroPositionX, Integer heroPositionY);

    void showEvent(String eventText);

    void showHeroInfo(Hero hero);

    Hero pickHeroOrCreate(List<Hero> heroes);

    String move();

    void showFightResult(String eventText);

    void luckInfo(String eventText);

    boolean metTheEnemy(Enemy enemy, Hero hero);

    boolean checkNewArtifact(Artifact artifact);

    void wonMessage(String s);
}
